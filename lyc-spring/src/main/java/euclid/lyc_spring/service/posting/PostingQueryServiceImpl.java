package euclid.lyc_spring.service.posting;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PostingHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.dto.response.WeatherDTO;
import euclid.lyc_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostingQueryServiceImpl implements PostingQueryService {

    private final MemberRepository memberRepository;
    private final BlockMemberRepository blockMemberRepository;
    private final PostingRepository postingRepository;
    private final SavedPostingRepository savedPostingRepository;
    private final LikedPostingRepository likedPostingRepository;
    private final CommissionRepository commissionRepository;

/*-------------------------------------------------- 피드 --------------------------------------------------*/

    @Override
    public PostingDTO.RecentPostingListDTO getRecentPostings() {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository.findAll().stream()
                .sorted(Comparator.comparing(Posting::getCreatedAt).reversed())
                .filter(posting ->
                        !blockMemberRepository.existsByMemberIdAndBlockMemberId(loginMember.getId(), posting.getWriter().getId()))
                .map(PostingDTO.PostingImageDTO::toDTO)
                .limit(10)
                .toList();

        return new PostingDTO.RecentPostingListDTO(postingImageDTOList);
    }

    @Override
    public PostingDTO.RecommendedPostingListDTO getPostingsForMember(Integer pageSize, Long cursorScore, Long cursorId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        InfoResponseDTO.AllInfoDTO memberInfo = InfoResponseDTO.AllInfoDTO.toDTO(member.getInfo());

        List<PostingDTO.RecommendedPostingDTO> postings = postingRepository.findPostingsForMember(memberInfo, pageSize, cursorScore, cursorId)
                .stream()
                .map(postingScoreDTO -> {
                    Posting posting = postingRepository.findById(postingScoreDTO.getPostingId())
                            .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));
                    return PostingDTO.RecommendedPostingDTO.toDTO(posting, postingScoreDTO.getTotalScore());
                })
                .toList();

        return PostingDTO.RecommendedPostingListDTO.toDTO(postings);
    }

    @Override
    public PostingDTO.RecentPostingListDTO getPostingsAccordingToWeather(WeatherDTO weatherDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository.findPostingsByWeather(weatherDTO.getTemp_min(), weatherDTO.getTemp_max())
                .stream()
                .map(PostingDTO.PostingImageDTO::toDTO)
                .toList();

        return new PostingDTO.RecentPostingListDTO(postingImageDTOList);
    }


    /*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    @Override
    public PostingDTO.PostingViewDTO getPosting(Long postingId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

    @Override
    public PostingDTO.PostingImageListDTO getAllSavedPostings(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // member와 writer가 같지 않고 저장한 코디가 비공개라면 오류 반환
        if (!member.equals(writer) && !writer.getIsPublic()) {
            throw new PostingHandler(ErrorStatus.SAVED_POSTING_CANNOT_ACCESS);
        }

        // writer가 차단된 회원인지 확인
        if (blockMemberRepository.existsByMemberIdAndBlockMemberId(member.getId(), writer.getId())) {
            throw new MemberHandler(ErrorStatus.BLOCKED_MEMBER);
        }

        List<PostingDTO.PostingImageDTO> savedPostingList = savedPostingRepository
                .findSavedPostingsByMemberId(memberId, pageSize, cursorDateTime).stream()
                .map(savedPosting -> PostingDTO.PostingImageDTO.toDTO(savedPosting.getPosting()))
                .collect(Collectors.toList());

        return PostingDTO.PostingImageListDTO.builder()
                .imageList(savedPostingList)
                .build();
    }

    @Override
    public Boolean getPostingSaveStatus(Long postingId) {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        if (savedPostingRepository.existsByMemberIdAndPostingId(member.getId(), postingId)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean getPostingLikeStatus(Long postingId) {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        if (likedPostingRepository.existsByMemberIdAndPostingId(member.getId(), postingId)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/

    @Override
    public PostingDTO.PostingImageListDTO getAllMemberCoordies(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // writer가 차단된 회원인지 확인
        if (blockMemberRepository.existsByMemberIdAndBlockMemberId(member.getId(), writer.getId())) {
            throw new MemberHandler(ErrorStatus.BLOCKED_MEMBER);
        }

        List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository
                .findCoordiesByFromMemberId(writer.getId(), pageSize, cursorDateTime).stream()
                .map(PostingDTO.PostingImageDTO::toDTO)
                .toList();

        return PostingDTO.PostingImageListDTO.builder()
                .imageList(postingImageDTOList)
                .build();
    }

    /*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

    @Override
    public PostingDTO.PostingImageListDTO getAllMemberReviews(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // writer가 차단된 회원인지 확인
        if (blockMemberRepository.existsByMemberIdAndBlockMemberId(member.getId(), writer.getId())) {
            throw new MemberHandler(ErrorStatus.BLOCKED_MEMBER);
        }

        List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository
                .findReviewsByToMemberId(memberId, pageSize, cursorDateTime).stream()
                .map(PostingDTO.PostingImageDTO::toDTO)
                .toList();
        
        return PostingDTO.PostingImageListDTO.builder()
                .imageList(postingImageDTOList)
                .build();
    }

    @Override
    public CommissionDTO.TerminatedCommissionListDTO getReviewsAvailableForSubmission(Integer pageSize, LocalDateTime cursorDateTime, Long cursorId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Commission> commissions = commissionRepository.findUnreviewedCommissions(pageSize, cursorDateTime, cursorId);
        return CommissionDTO.TerminatedCommissionListDTO.toDTO(commissions);
    }
}
