package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.PostingDTO.*;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final MemberRepository memberRepository;
    private final PostingRepository postingRepository;

    public PostingImageListDTO getMemberCoordies(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PostingImageDTO> postingImageDTOList = postingRepository.findByFromMember_Id(memberId).stream()
                .map(PostingImageDTO::toDTO)
                .toList();

        return PostingImageListDTO.builder()
                .memberId(memberId)
                .imageList(postingImageDTOList)
                .build();
    }
}
