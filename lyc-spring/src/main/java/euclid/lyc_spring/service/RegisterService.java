package euclid.lyc_spring.service;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.info.Info;
import euclid.lyc_spring.domain.info.InfoStyle;
import euclid.lyc_spring.dto.InfoDTO;
import euclid.lyc_spring.dto.InfoStyleListDTO;
import euclid.lyc_spring.dto.MemberDTO;
import euclid.lyc_spring.repository.InfoRepository;
import euclid.lyc_spring.repository.InfoStyleRepository;
import euclid.lyc_spring.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final MemberRepository memberRepository;
    private final InfoRepository infoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InfoStyleRepository infoStyleRepository;

    public RegisterService(MemberRepository memberRepository, InfoRepository infoRepository, InfoStyleRepository infoStyleRepository) {
        this.memberRepository = memberRepository;
        this.infoRepository = infoRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.infoStyleRepository = infoStyleRepository;
    }

    public Boolean join(MemberDTO memberdto, InfoDTO.BasicInfoDTO infodto, InfoStyleListDTO infoStyleListDTO) {
        String email = memberdto.getEmail();
        String image = memberdto.getProfileImage();
        if(image==null)
            image = "기본 프로필 url";

        System.out.println(email);

        Boolean isExist = memberRepository.existsByEmail(email);
        if (isExist) {
            return false;
        }

        Member member = new Member(memberdto.getName(), memberdto.getLoginId(), bCryptPasswordEncoder.encode(memberdto.getLoginPw()), memberdto.getEmail(),
                memberdto.getNickname(), memberdto.getIntroduction(), image);
        memberRepository.save(member);

        Info info = new Info(member, infodto.getHeight(), infodto.getWeight(), infodto.getTopSize(), infodto.getBottomSize(),
                infodto.getPostalCode(), infodto.getAddress(), infodto.getDetailAddress(), infodto.getText());
        infoRepository.save(info);

        for(int i = 0; i<infoStyleListDTO.getInfoStyleList().size(); i++){
            InfoStyle infoStyle = new InfoStyle(info, infoStyleListDTO.getInfoStyleList().get(i), infoStyleListDTO.getIsPreferInfoStyle().get(i));
            infoStyleRepository.save(infoStyle);
            System.out.println(infoStyleListDTO.getInfoStyleList().get(i));
        }
        return true;

    }
}
