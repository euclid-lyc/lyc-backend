package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PostingHandler;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import euclid.lyc_spring.domain.posting.Image;
import euclid.lyc_spring.domain.posting.ImageUrl;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.request.ImageRequestDTO.*;
import euclid.lyc_spring.dto.request.PostingRequestDTO.*;
import euclid.lyc_spring.dto.response.PostingDTO.*;
import euclid.lyc_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostingService {

    private final MemberRepository memberRepository;
    private final PostingRepository postingRepository;
    private final SavedPostingRepository savedPostingRepository;
    private final LikedPostingRepository likedPostingRepository;
    private final ImageRepository imageRepository;
    private final ImageUrlRepository imageUrlRepository;

    /**
     * GET API
     */

    public PostingImageListDTO getAllMemberCoordies(Long memberId) {

        // Authorization
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PostingImageDTO> postingImageDTOList = postingRepository.findByFromMember_Id(memberId).stream()
                .map(PostingImageDTO::toDTO)
                .toList();

        return PostingImageListDTO.builder()
                .memberId(memberId)
                .imageList(postingImageDTOList)
                .build();
    }

    public PostingImageListDTO getAllMemberReviews(Long memberId) {

        // Authorization
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 내가 아닌 from_member 로부터 리뷰를 받음
        List<PostingImageDTO> postingImageDTOList = postingRepository.findByToMember_Id(memberId).stream()
                .filter(toPosting -> !memberId.equals(toPosting.getFromMember().getId()))
                .map(PostingImageDTO::toDTO)
                .toList();

        return PostingImageListDTO.builder()
                .memberId(memberId)
                .imageList(postingImageDTOList)
                .build();
    }


    public PostingImageListDTO getAllSavedCoordies(Long memberId) {

        // Authorization
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        authorizeSavedCoordie(member);

        List<PostingImageDTO> savedPostingList = savedPostingRepository.findAllByMemberId(memberId).stream()
                .map(savedPosting -> PostingImageDTO.toDTO(savedPosting.getPosting()))
                .collect(Collectors.toList());

        return PostingImageListDTO.builder()
                .memberId(memberId)
                .imageList(savedPostingList)
                .build();
    }


    public PostingViewDTO getSavedCoordie(Long memberId, Long postingId, Long savedPostingId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        savedPostingRepository.findById(savedPostingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.SAVED_POSTING_NOT_FOUND));

        return PostingViewDTO.toDTO(posting);
    }

    public PostingViewDTO getPosting(Long memberId, Long postingId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        return PostingViewDTO.toDTO(posting);
    }

    public ClickDTO getIsClickedLike(Long memberId, Long postingId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        List<LikedPosting> likedPostingList = likedPostingRepository.findAllByMember_Id(memberId).stream()
                .filter(likedPosting -> likedPosting.getPosting().getId().equals(postingId))
                .toList();

        if (likedPostingList.isEmpty()) {
            return ClickDTO.builder()
                    .memberId(memberId)
                    .postingId(postingId)
                    .isClicked(false)
                    .build();
        } else {
            return ClickDTO.builder()
                    .memberId(memberId)
                    .postingId(postingId)
                    .isClicked(true)
                    .build();
        }
    }

    public ClickDTO getIsClickedSave(Long memberId, Long postingId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        List<SavedPosting> savedPostingList = savedPostingRepository.findAllByMemberId(memberId).stream()
                .filter(savedPosting -> savedPosting.getPosting().getId().equals(postingId))
                .toList();

        if (savedPostingList.isEmpty()) {
            return ClickDTO.builder()
                    .memberId(memberId)
                    .postingId(postingId)
                    .isClicked(false)
                    .build();
        } else {
            return ClickDTO.builder()
                    .memberId(memberId)
                    .postingId(postingId)
                    .isClicked(true)
                    .build();
        }
    }

    public RecentPostingListDTO getRecentPostings() {

        // Authorization
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }

        List<RecentPostingDTO> postingImageDTOList = postingRepository.findAll().stream()
                .sorted(Comparator.comparing(Posting::getCreatedAt).reversed())
                .map(RecentPostingDTO::toDTO)
                .limit(10)
                .toList();

        return new RecentPostingListDTO(postingImageDTOList);
    }

    /**
     * POST API
     */

    @Transactional
    public PostingViewDTO createPosting(Long memberId, PostingSaveDTO postingSaveDTO) {

        // Authorization
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        authorizeWriter(writer);

        Member fromMember = memberRepository.findById(postingSaveDTO.getFromMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member toMember = memberRepository.findById(postingSaveDTO.getToMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Posting posting = Posting.builder()
                .minTemp(postingSaveDTO.getMinTemp())
                .maxTemp(postingSaveDTO.getMaxTemp())
                .style(postingSaveDTO.getStyle())
                .likes(0L)
                .content(postingSaveDTO.getContent())
                .fromMember(fromMember)
                .toMember(toMember)
                .writer(writer)
                .build();

        fromMember.addFromPosting(posting);
        toMember.addToPosting(posting);
        writer.addPosting(posting);

        posting = postingRepository.save(posting);

        createImage(posting, postingSaveDTO);

        return PostingViewDTO.toDTO(posting);
    }

    private void createImage(Posting posting, PostingSaveDTO postingSaveDTO) {

        postingSaveDTO.getImageList()
                .forEach(imageSaveDTO -> {
                    Image image = Image.builder()
                            .image(imageSaveDTO.getImage())
                            .posting(posting)
                            .build();
                    posting.addImage(image);
                    imageRepository.save(image);
                    createImageUrl(image, imageSaveDTO);
                });

    }

    private void createImageUrl(Image image, ImageSaveDTO imageSaveDTO) {
        imageSaveDTO.getImageUrlList()
                .forEach(link -> {
                    ImageUrl imageUrl = ImageUrl.builder()
                            .link(imageSaveDTO.getImage())
                            .image(image)
                            .build();
                    image.addImageUrl(imageUrl);
                    imageUrlRepository.save(imageUrl);
                });
    }

    public PostingViewDTO likePosting(Long memberId, Long postingId){
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (getIsClickedLike(memberId, postingId).getIsClicked())
            throw new PostingHandler(ErrorStatus.POSTING_ALREADY_LIKED);
        LikedPosting likedPosting = new LikedPosting(member, posting);

        likedPostingRepository.save(likedPosting);

        posting.reloadLikes(posting.getLikes()+1);
        postingRepository.save(posting);

        return PostingViewDTO.toDTO(posting);
    }

    public PostingViewDTO savedPosting(Long memberId, Long postingId) {
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if(savedPostingRepository.existsByMemberIdAndPostingId(memberId, postingId)){
            throw new PostingHandler(ErrorStatus.POSTING_ALREADY_SAVED);
        }

        SavedPosting savedPosting = new SavedPosting(member, posting);
        savedPostingRepository.save(savedPosting);

        return PostingViewDTO.toDTO(posting);
    }




    /**
     * DELETE API
     */

    @Transactional
    public PostingIdDTO deletePosting(Long memberId, Long postingId) {

        // Authorization
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        authorizeWriter(member);

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        if (!member.equals(posting.getWriter())) {
            throw new PostingHandler(ErrorStatus.POSTING_NOT_FOUND);
        }

        posting.getImageList().forEach(image -> {
            imageUrlRepository.deleteAll(image.getImageUrlList());
            imageRepository.delete(image);
        });

        member.removePosting(posting);
        postingRepository.deleteById(postingId);

        return new PostingIdDTO(postingId);
    }

    public SavedPostingIdDTO deleteSavedPosting(Long memberId, Long postingId, Long savedPostingId) {

        // Authorization (저장한 회원)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        authorizeWriter(member);

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        SavedPosting savedPosting = savedPostingRepository.findById(savedPostingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.SAVED_POSTING_NOT_FOUND));

        if (!member.equals(savedPosting.getMember())) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
        if (!posting.equals(savedPosting.getPosting())) {
            throw new PostingHandler(ErrorStatus.POSTING_NOT_FOUND);
        }

        member.removeSavedPosting(savedPosting);
        posting.removeSavedPosting(savedPosting);
        savedPostingRepository.deleteById(savedPostingId);

        return new SavedPostingIdDTO(postingId, savedPostingId);
    }

    public PostingViewDTO unlikePosting(Long memberId, Long postingId) {
        List<LikedPosting> likedPostings = likedPostingRepository.findByMember_IdAndPosting_Id(memberId, postingId);

        if (likedPostings.isEmpty()) {
            throw new PostingHandler(ErrorStatus.POSTING_NOT_LIKED);
        }

        likedPostings.forEach(likedPostingRepository::delete);

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        posting.reloadLikes(posting.getLikes()-1);
        postingRepository.save(posting);

        return PostingViewDTO.toDTO(posting);
    }

/* ---------------------------------------- 인증/인가 ---------------------------------------- */

    private void authorizeWriter(Member member) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // 인증 정보가 존재하면 loginId 확인
            String loginId = (String) authentication.getPrincipal();
            if (!loginId.equals(member.getLoginId())) {
                // 글쓴이 혹은 게시글을 저장한 회원과 로그인한 회원이 일치하지 않으면 오류
                throw new JwtHandler(ErrorStatus.JWT_UNAUTHORIZED);
            }
        } else {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }
    }

    private void authorizeSavedCoordie(Member member) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // 인증 정보가 존재하면 loginId 확인
            String loginId = (String) authentication.getPrincipal();
            if (!loginId.equals(member.getLoginId())) {
                // 게시글을 저장한 회원과 로그인한 회원이 일치하지 않으면 저장한 코디 공개 여부 확인
                if (!member.getIsPublic()) {
                    throw new PostingHandler(ErrorStatus.SAVED_POSTING_CANNOT_ACCESS);
                }
            }
        } else {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }
    }
}