package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PostingHandler;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.response.PostingDTO.*;
import euclid.lyc_spring.repository.LikedPostingRepository;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.PostingRepository;
import euclid.lyc_spring.repository.SavedPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final MemberRepository memberRepository;
    private final PostingRepository postingRepository;
    private final SavedPostingRepository savedPostingRepository;
    private final LikedPostingRepository likedPostingRepository;


    public PostingImageListDTO getAllMemberCoordies(Long memberId) {

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

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PostingImageDTO> savedPostingList = savedPostingRepository.findAllByMember_Id(memberId).stream()
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

        List<SavedPosting> savedPostingList = savedPostingRepository.findAllByMember_Id(memberId).stream()
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
}
