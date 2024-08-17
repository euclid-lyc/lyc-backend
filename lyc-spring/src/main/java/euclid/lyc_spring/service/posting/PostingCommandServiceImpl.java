package euclid.lyc_spring.service.posting;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PostingHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import euclid.lyc_spring.domain.posting.Image;
import euclid.lyc_spring.domain.posting.ImageUrl;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.request.ImageRequestDTO;
import euclid.lyc_spring.dto.request.PostingRequestDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostingCommandServiceImpl implements PostingCommandService {

    private final MemberRepository memberRepository;
    private final PostingRepository postingRepository;
    private final SavedPostingRepository savedPostingRepository;
    private final LikedPostingRepository likedPostingRepository;
    private final ImageRepository imageRepository;
    private final ImageUrlRepository imageUrlRepository;

/*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    @Override
    public PostingDTO.PostingViewDTO createPosting(PostingRequestDTO.PostingSaveDTO postingSaveDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member writer = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
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

        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

    private void createImage(Posting posting, PostingRequestDTO.PostingSaveDTO postingSaveDTO) {

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

    private void createImageUrl(Image image, ImageRequestDTO.ImageSaveDTO imageSaveDTO) {
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

    @Override
    public PostingDTO.PostingIdDTO deletePosting(Long postingId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        // 로그인한 회원이 게시글 작성자인지 확인
        postingRepository.findByIdAndWriter(postingId, member)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.WRITER_ONLY_ALLOWED));

        posting.getImageList().forEach(image -> {
            imageUrlRepository.deleteAll(image.getImageUrlList());
            imageRepository.delete(image);
        });

        member.removePosting(posting);
        postingRepository.deleteById(postingId);

        return new PostingDTO.PostingIdDTO(postingId);
    }

    @Override
    public PostingDTO.PostingViewDTO savePosting(Long postingId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        // 이미 저장된 게시글은 다시 저장 불가
        if (savedPostingRepository.existsByMemberIdAndPostingId(member.getId(), postingId)) {
            throw new PostingHandler(ErrorStatus.POSTING_ALREADY_SAVED);
        }
        // 자신이 작성한 게시글은 저장 불가
        if (posting.getWriter().equals(member)) {
            throw new PostingHandler(ErrorStatus.POSTING_CANNOT_SAVED_BY_WRITER);
        }

        SavedPosting savedPosting = new SavedPosting(member, posting);
        savedPostingRepository.save(savedPosting);

        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

    @Override
    public PostingDTO.SavedPostingIdDTO deleteSavedPosting(Long postingId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        // 로그인한 회원이 저장하지 않은 게시글은 삭제할 수 없음
        SavedPosting savedPosting = savedPostingRepository.findByMemberIdAndPostingId(member.getId(), postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.SAVED_POSTING_NOT_FOUND));

        member.removeSavedPosting(savedPosting);
        posting.removeSavedPosting(savedPosting);
        savedPostingRepository.delete(savedPosting);

        return new PostingDTO.SavedPostingIdDTO(postingId, savedPosting.getId());
    }

    @Override
    public PostingDTO.PostingViewDTO likePosting(Long postingId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        if (getIsClickedLike(member.getId(), postingId))
            throw new PostingHandler(ErrorStatus.POSTING_ALREADY_LIKED);
        LikedPosting likedPosting = new LikedPosting(member, posting);

        likedPostingRepository.save(likedPosting);

        posting.reloadLikes(posting.getLikes() + 1);
        postingRepository.save(posting);

        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

    private boolean getIsClickedLike(Long memberId, Long postingId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        List<LikedPosting> likedPostingList = likedPostingRepository.findAllByMember_Id(memberId).stream()
                .filter(likedPosting -> likedPosting.getPosting().getId().equals(postingId))
                .toList();

        return !likedPostingList.isEmpty();
    }

    public PostingDTO.PostingViewDTO dislikePosting(Long postingId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<LikedPosting> likedPostings = likedPostingRepository.findByMemberIdAndPostingId(member.getId(), postingId);

        if (likedPostings.isEmpty()) {
            throw new PostingHandler(ErrorStatus.POSTING_NOT_LIKED);
        }

        likedPostingRepository.deleteAll(likedPostings);

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingHandler(ErrorStatus.POSTING_NOT_FOUND));

        posting.reloadLikes(posting.getLikes() - 1);
        postingRepository.save(posting);

        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

/*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/

/*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

}