package euclid.lyc_spring.service.posting;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PostingHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.response.PostingDTO;
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
    private final PostingRepository postingRepository;
    private final SavedPostingRepository savedPostingRepository;
    private final LikedPostingRepository likedPostingRepository;
    private final ImageRepository imageRepository;
    private final ImageUrlRepository imageUrlRepository;

/*-------------------------------------------------- 피드 --------------------------------------------------*/

    @Override
    public PostingDTO.RecentPostingListDTO getRecentPostings() {

        List<PostingDTO.RecentPostingDTO> postingImageDTOList = postingRepository.findAll().stream()
                .sorted(Comparator.comparing(Posting::getCreatedAt).reversed())
                .map(PostingDTO.RecentPostingDTO::toDTO)
                .limit(10)
                .toList();

        return new PostingDTO.RecentPostingListDTO(postingImageDTOList);
    }

/*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    @Override
    public PostingDTO.PostingViewDTO getPosting(Long postingId) {

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

        List<PostingDTO.PostingImageDTO> savedPostingList = savedPostingRepository
                .findSavedPostingsByMemberId(memberId, pageSize, cursorDateTime).stream()
                .map(savedPosting -> PostingDTO.PostingImageDTO.toDTO(savedPosting.getPosting()))
                .collect(Collectors.toList());

        return PostingDTO.PostingImageListDTO.builder()
                .memberId(memberId)
                .imageList(savedPostingList)
                .build();
    }

/*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/

    @Override
    public PostingDTO.PostingImageListDTO getAllMemberCoordies(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository
                .findCoordiesByFromMemberId(member.getId(), pageSize, cursorDateTime).stream()
                .map(PostingDTO.PostingImageDTO::toDTO)
                .toList();

        return PostingDTO.PostingImageListDTO.builder()
                .memberId(member.getId())
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

        //// 내가 아닌 from_member 로부터 리뷰를 받음
        //List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository.findByToMemberId(memberId).stream()
        //        .filter(toPosting -> !memberId.equals(toPosting.getFromMember().getId()))
        //        .map(PostingDTO.PostingImageDTO::toDTO)
        //        .toList();

        List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository
                .findReviewsByToMemberId(memberId, pageSize, cursorDateTime).stream()
                .map(PostingDTO.PostingImageDTO::toDTO)
                .toList();
        return PostingDTO.PostingImageListDTO.builder()
                .memberId(memberId)
                .imageList(postingImageDTOList)
                .build();
    }
}
