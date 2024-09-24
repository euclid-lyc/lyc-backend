package euclid.lyc_spring.service.posting;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.CommissionHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PostingHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.enums.CommissionStatus;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import euclid.lyc_spring.domain.posting.Image;
import euclid.lyc_spring.domain.posting.ImageUrl;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.request.PostingRequestDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.repository.*;
import euclid.lyc_spring.service.s3.S3ImageService;
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
    private final CommissionRepository commissionRepository;

    private final S3ImageService s3ImageService;

/*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    @Override
    public PostingDTO.PostingViewDTO createPosting(PostingRequestDTO.PostingSaveDTO postingSaveDTO, Long commissionId) {

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

        // 리뷰의 경우 연동 필요
        if (commissionId != null) {
            Commission commission = commissionRepository.findById(commissionId)
                    .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

            System.out.println("director : " + commission.getDirector().getId());
            System.out.println("member : " + commission.getMember().getId());

            // 아직 종료되지 않은 의뢰거나 이미 리뷰가 작성된 경우 새로운 리뷰를 작성할 수 없음
            if (!commission.getStatus().equals(CommissionStatus.TERMINATED) || commission.getReview() != null) {
                throw new PostingHandler(ErrorStatus.COMMISSION_NOT_TERMINATED);
            }

            if (!fromMember.equals(commission.getDirector())) {
                throw new PostingHandler(ErrorStatus.REVIEW_FROM_MEMBER_NOT_MATCHED);
            }

            if (!toMember.equals(commission.getMember())) {
                throw new PostingHandler(ErrorStatus.REVIEW_TO_MEMBER_NOT_MATCHED);
            }

            commission.setReview(posting);
            commission = commissionRepository.save(commission);
            posting.setCommission(commission); // 연관관계 매핑

        }

        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

    @Override
    public PostingDTO.PostingViewDTO createPostingImage(Long postingId, List<List<String>> links, List<String> images) {
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> {
                    images.forEach(s3ImageService::deleteImageFromS3);
                    return new PostingHandler(ErrorStatus.POSTING_NOT_FOUND);
                });

        createImage(posting, links, images);
        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

    private void createImage(Posting posting, List<List<String>> links, List<String> images) {
        for (int i=0; i<images.size(); i++) {
            Image image = Image.builder()
                    .image(images.get(i))
                    .posting(posting)
                    .build();
            posting.addImage(image);
            imageRepository.save(image);

            links.get(i).forEach(link -> {
                createLink(image, link);
            });
        }
    }

    private void createLink(Image image, String link) {
        ImageUrl imageUrl = ImageUrl.builder()
                .link(link)
                .image(image)
                .build();
        image.addImageUrl(imageUrl);
        imageUrlRepository.save(imageUrl);
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
            s3ImageService.deleteImageFromS3(image.getImage());
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

        // 인기도 증가
        Member uploader = posting.getFromMember();
        uploader.reloadPopularity(uploader.getPopularity()+1);

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

        // 인기도 하락(인기도작 금지)
        Member uploader = posting.getFromMember();
        if(uploader.getPopularity() != 0L)
            uploader.reloadPopularity(uploader.getPopularity()-1);

        posting.reloadLikes(posting.getLikes() - 1);
        postingRepository.save(posting);

        return PostingDTO.PostingViewDTO.toDTO(posting);
    }

/*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/

/*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

}