package euclid.lyc_spring.service;

import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<TodayDirectorDTO> getTodayDirectorList() {
        return memberRepository.findAll().stream()
                .sorted(Comparator.comparing(Member::getFollower).reversed())
                .map(TodayDirectorDTO::toDTO)
                .limit(10)
                .toList();

    }
}
