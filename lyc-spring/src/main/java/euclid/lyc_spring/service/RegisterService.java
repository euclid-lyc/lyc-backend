package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.domain.info.*;
import euclid.lyc_spring.dto.request.InfoRequestDTO.*;
import euclid.lyc_spring.dto.request.MemberRequestDTO.*;
import euclid.lyc_spring.dto.request.RegisterDTO.*;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {

    private final MemberRepository memberRepository;
    private final InfoRepository infoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InfoStyleRepository infoStyleRepository;
    private final InfoFitRepository infoFitRepository;
    private final InfoMaterialRepository infoMaterialRepository;
    private final InfoBodyTypeRepository infoBodyTypeRepository;


    public RegisterService(MemberRepository memberRepository, InfoRepository infoRepository, InfoStyleRepository infoStyleRepository,
                           InfoFitRepository infoFitRepository, InfoMaterialRepository infoMaterialRepository, InfoBodyTypeRepository infoBodyTypeRepository) {
        this.memberRepository = memberRepository;
        this.infoRepository = infoRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.infoStyleRepository = infoStyleRepository;
        this.infoFitRepository = infoFitRepository;
        this.infoMaterialRepository = infoMaterialRepository;
        this.infoBodyTypeRepository = infoBodyTypeRepository;
    }

    @Transactional
    public MemberDTO.MemberInfoDTO join(RegisterMemberDTO registerMemberDTO) {

        MemberInfoDTO memberInfoDTO = registerMemberDTO.getMemberInfo();
        BasicInfoDTO basicInfoDTO = registerMemberDTO.getBasicInfo();

        String image = memberInfoDTO.getProfileImage();

        if(memberInfoDTO.getProfileImage().isEmpty())
            image = "default url";

        if (memberRepository.findByEmail(memberInfoDTO.getEmail()).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXIST);
        }

        Member member = Member.builder()
                .name(memberInfoDTO.getName())
                .loginId(memberInfoDTO.getLoginId())
                .loginPw(bCryptPasswordEncoder.encode(memberInfoDTO.getLoginPw()))
                .email(memberInfoDTO.getEmail())
                .nickname(memberInfoDTO.getNickname())
                .introduction(memberInfoDTO.getIntroduction())
                .profileImage(image)
                .role(Role.MEMBER)
                .build();

        member = memberRepository.save(member);

        createInfo(member, basicInfoDTO);

        return MemberDTO.MemberInfoDTO.toDTO(member);

    }

    private void createInfo(Member member, BasicInfoDTO infoDto) {

        Info info = Info.builder()
                .member(member)
                .height(infoDto.getHeight())
                .weight(infoDto.getWeight())
                .topSize(infoDto.getTopSize())
                .bottomSize(infoDto.getBottomSize())
                .postalCode(infoDto.getPostalCode())
                .address(infoDto.getAddress())
                .detailAddress(infoDto.getDetailAddress())
                .text(infoDto.getText())
                .build();

        member.setInfo(info);
        info = infoRepository.save(info);

        createInfoStyle(info, infoDto.getInfoStyle());
        createInfoFit(info, infoDto.getInfoFit());
        createInfoMaterial(info, infoDto.getInfoMaterial());
        createInfoBodyType(info, infoDto.getInfoBodyType());

    }

    private void createInfoStyle(Info info, InfoStyleListDTO infoStyleListDTO) {

        infoStyleListDTO.getPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .style(style)
                            .isPrefer(true)
                            .build();
                    info.addInfoStyle(infoStyle);
                    infoStyleRepository.save(infoStyle);
                });

        infoStyleListDTO.getNonPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .style(style)
                            .isPrefer(false)
                            .build();
                    info.addInfoStyle(infoStyle);
                    infoStyleRepository.save(infoStyle);
                });
    }

    private void createInfoFit(Info info, InfoFitListDTO infoFitListDTO) {

        infoFitListDTO.getPreferredFitList()
                .forEach(style -> {
                    InfoFit infoFit = InfoFit.builder()
                            .fit(style)
                            .isPrefer(true)
                            .build();
                    info.addInfoFit(infoFit);
                    infoFitRepository.save(infoFit);
                });

        infoFitListDTO.getNonPreferredFitList()
                .forEach(style -> {
                    InfoFit infoFit = InfoFit.builder()
                            .fit(style)
                            .isPrefer(false)
                            .build();
                    info.addInfoFit(infoFit);
                    infoFitRepository.save(infoFit);
                });
    }

    private void createInfoMaterial(Info info, InfoMaterialListDTO infoMaterialListDTO) {

        infoMaterialListDTO.getPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .material(material)
                            .isPrefer(true)
                            .build();
                    info.addInfoMaterial(infoMaterial);
                    infoMaterialRepository.save(infoMaterial);
                });

        infoMaterialListDTO.getNonPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .material(material)
                            .isPrefer(false)
                            .build();
                    info.addInfoMaterial(infoMaterial);
                    infoMaterialRepository.save(infoMaterial);
                });
    }

    private void createInfoBodyType(Info info, InfoBodyTypeListDTO infoBodyTypeListDTO) {

        infoBodyTypeListDTO.getGoodBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(true)
                            .build();
                    info.addInfoBodyType(infoBodyType);
                    infoBodyTypeRepository.save(infoBodyType);
                });

        infoBodyTypeListDTO.getBadBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .bodyType(bodyType)
                            .isGood(false)
                            .build();
                    info.addInfoBodyType(infoBodyType);
                    infoBodyTypeRepository.save(infoBodyType);
                });
    }
}
