package euclid.lyc_spring.service.social;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.BlockMember;
import euclid.lyc_spring.domain.Follow;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.Report;
import euclid.lyc_spring.domain.info.*;
import euclid.lyc_spring.dto.request.InfoRequestDTO;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialCommandServiceImpl implements SocialCommandService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final BlockMemberRepository blockMemberRepository;
    private final ReportRepository reportRepository;

    private final InfoRepository infoRepository;
    private final InfoStyleRepository infoStyleRepository;
    private final InfoFitRepository infoFitRepository;
    private final InfoMaterialRepository infoMaterialRepository;
    private final InfoBodyTypeRepository infoBodyTypeRepository;

/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberInfoDTO followMember(Long memberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member following = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (Objects.equals(loginMember.getId(), memberId)){
            throw new MemberHandler(ErrorStatus.FORBIDDEN);
        } else if (followRepository.findByFollowerIdAndFollowingId(loginMember.getId(),memberId).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_FOLLOWING);
        } else if (blockMemberRepository.findByMemberIdAndBlockedMemberId(loginMember.getId(), memberId).isPresent()) {
            throw new MemberHandler(ErrorStatus.BLOCKING_MEMBER);
        } else if (blockMemberRepository.findByMemberIdAndBlockedMemberId(memberId, loginMember.getId()).isPresent()) {
            throw new MemberHandler(ErrorStatus.BLOCKED_MEMBER);
        }

        Member follower = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Follow follow = new Follow(follower,following);

        following.reloadFollower(following.getFollower()+1);
        follower.reloadFollowing(follower.getFollowing()+1);

        // 인기도 증가
        following.reloadPopularity(following.getPopularity()+1);

        followRepository.save(follow);

        return MemberDTO.MemberInfoDTO.toDTO(following);
    }

    @Override
    public MemberDTO.MemberInfoDTO unfollowMember(Long memberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (Objects.equals(loginMember.getId(), memberId)){
            throw new MemberHandler(ErrorStatus.FORBIDDEN);
        } else if (followRepository.findByFollowerIdAndFollowingId(loginMember.getId(),memberId).isEmpty()) {
            throw new MemberHandler(ErrorStatus.NOT_FOLLOWING);
        }
        Follow follow = followRepository.findByFollowerIdAndFollowingId(loginMember.getId(),memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.NOT_FOLLOWING));

        Member followingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        followingMember.reloadFollower(followingMember.getFollower()-1);
        member.reloadFollowing(member.getFollowing()-1);

        // 언팔하면 인기도 하락(인기도작 금지)
        if(followingMember.getPopularity() != 0L)
            followingMember.reloadPopularity(followingMember.getPopularity()-1);

        followRepository.delete(follow);
        return MemberDTO.MemberInfoDTO.toDTO(followingMember);
    }

/*-------------------------------------------------- 인기 디렉터 --------------------------------------------------*/

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

    @Override
    public InfoResponseDTO.AllInfoDTO updateStyleInfo(InfoRequestDTO.StyleInfoDTO styleInfoDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Info info = infoRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_INFO_NOT_FOUND));

        info = updateInfo(info, styleInfoDTO);
        member.setInfo(info);

        return InfoResponseDTO.AllInfoDTO.toDTO(info);
    }

    private Info updateInfo(Info info, InfoRequestDTO.StyleInfoDTO styleInfoDTO) {
        info.updateInfo(styleInfoDTO);
        info = infoRepository.save(info);

        updateInfoStyle(info, styleInfoDTO);
        updateInfoFit(info, styleInfoDTO);
        updateInfoMaterial(info, styleInfoDTO);
        updateInfoBodyType(info, styleInfoDTO);

        return info;
    }

    private void updateInfoStyle(Info info, InfoRequestDTO.StyleInfoDTO styleInfoDTO) {

        // 기존 InfoStyle 삭제
        infoStyleRepository.findAllByInfoId(info.getId())
                .forEach(infoStyle -> {
                    info.deleteInfoStyle(infoStyle);
                    infoStyleRepository.delete(infoStyle);
                });

        // 새로운 InfoStyle 추가
        styleInfoDTO.getPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .info(info)
                            .style(style)
                            .isPrefer(true)
                            .build();
                    infoStyle = infoStyleRepository.save(infoStyle);
                    info.addInfoStyle(infoStyle);
                });

        styleInfoDTO.getNonPreferredStyleList()
                .forEach(style -> {
                    InfoStyle infoStyle = InfoStyle.builder()
                            .info(info)
                            .style(style)
                            .isPrefer(false)
                            .build();
                    infoStyle = infoStyleRepository.save(infoStyle);
                    info.addInfoStyle(infoStyle);
                });
    }

    private void updateInfoFit(Info info, InfoRequestDTO.StyleInfoDTO styleInfoDTO) {

        // 기존 InfoFit 삭제
        infoFitRepository.findAllByInfoId(info.getId())
                .forEach(infoFit -> {
                    info.deleteInfoFit(infoFit);
                    infoFitRepository.delete(infoFit);
                });

        // 새로운 InfoFit 추가
        styleInfoDTO.getPreferredFitList()
                .forEach(fit -> {
                    InfoFit infoFit = InfoFit.builder()
                            .info(info)
                            .fit(fit)
                            .isPrefer(true)
                            .build();
                    infoFit = infoFitRepository.save(infoFit);
                    info.addInfoFit(infoFit);
                });
        styleInfoDTO.getNonPreferredFitList()
                .forEach(fit -> {
                    InfoFit infoFit = InfoFit.builder()
                            .info(info)
                            .fit(fit)
                            .isPrefer(false)
                            .build();
                    infoFit = infoFitRepository.save(infoFit);
                    info.addInfoFit(infoFit);
                });
    }

    private void updateInfoMaterial(Info info, InfoRequestDTO.StyleInfoDTO styleInfoDTO) {

        // 기존 InfoMaterial 삭제
        infoMaterialRepository.findAllByInfoId(info.getId())
                .forEach(infoMaterial -> {
                    info.deleteInfoMaterial(infoMaterial);
                    infoMaterialRepository.delete(infoMaterial);
                });

        // 새로운 InfoMaterial 추가
        styleInfoDTO.getPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .info(info)
                            .material(material)
                            .isPrefer(true)
                            .build();
                    infoMaterial = infoMaterialRepository.save(infoMaterial);
                    info.addInfoMaterial(infoMaterial);
                });
        styleInfoDTO.getNonPreferredMaterialList()
                .forEach(material -> {
                    InfoMaterial infoMaterial = InfoMaterial.builder()
                            .info(info)
                            .material(material)
                            .isPrefer(false)
                            .build();
                    infoMaterial = infoMaterialRepository.save(infoMaterial);
                    info.addInfoMaterial(infoMaterial);
                });
    }

    private void updateInfoBodyType(Info info, InfoRequestDTO.StyleInfoDTO styleInfoDTO) {
        // 기존 InfoBodyType 삭제
        infoBodyTypeRepository.findAllByInfoId(info.getId())
                .forEach(infoBodyType -> {
                    info.deleteInfoBodyType(infoBodyType);
                    infoBodyTypeRepository.delete(infoBodyType);
                });


        // 새로운 InfoMaterial 추가
        styleInfoDTO.getGoodBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .info(info)
                            .bodyType(bodyType)
                            .isGood(true)
                            .build();
                    infoBodyType = infoBodyTypeRepository.save(infoBodyType);
                    info.addInfoBodyType(infoBodyType);
                });
        styleInfoDTO.getBadBodyTypeList()
                .forEach(bodyType -> {
                    InfoBodyType infoBodyType = InfoBodyType.builder()
                            .info(info)
                            .bodyType(bodyType)
                            .isGood(false)
                            .build();
                    infoBodyType = infoBodyTypeRepository.save(infoBodyType);
                    info.addInfoBodyType(infoBodyType);
                });
    }

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberInfoDTO blockMember(Long blockMemberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Long memberId = member.getId();

        if (Objects.equals(memberId, blockMemberId)){
            throw new MemberHandler(ErrorStatus.FORBIDDEN);
        } else if (blockMemberRepository.findByMemberIdAndBlockedMemberId(memberId, blockMemberId).isPresent()) {
            throw new MemberHandler(ErrorStatus.BLOCKING_MEMBER);
        }

        Member blockMember = memberRepository.findById(blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 차단 관계 생성
        BlockMember blockMemberRelation = new BlockMember(member, blockMember);
        blockMemberRepository.save(blockMemberRelation);

        // 팔로우 관계 삭제
        if(followRepository.existsByFollowerIdAndFollowingId(memberId, blockMemberId)){
            Follow follow = followRepository.findByFollowerIdAndFollowingId(memberId, blockMemberId).orElseThrow();
            followRepository.delete(follow);
            member.reloadFollowing(member.getFollowing()-1);
            blockMember.reloadFollower(blockMember.getFollower()-1);
        }
        if(followRepository.findByFollowerIdAndFollowingId(blockMemberId, memberId).isPresent()){
            Follow follow = followRepository.findByFollowerIdAndFollowingId(blockMemberId, memberId).orElseThrow();
            followRepository.delete(follow);
            blockMember.reloadFollowing(blockMember.getFollowing()-1);
            member.reloadFollower(member.getFollower()-1);
        }

        return MemberDTO.MemberInfoDTO.toDTO(blockMember);
    }

    @Override
    public MemberDTO.MemberInfoDTO unblockMember(Long blockMemberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Long memberId = member.getId();

        if (blockMemberRepository.findByMemberIdAndBlockedMemberId(memberId, blockMemberId).isEmpty()) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_BLOCKING);
        }

        Member blockMember = memberRepository.findById(blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        BlockMember blockMemberRelation = blockMemberRepository.findByMemberIdAndBlockedMemberId(memberId, blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_BLOCKING));

        blockMemberRepository.delete(blockMemberRelation);

        return MemberDTO.MemberInfoDTO.toDTO(blockMember);
    }

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberProfileDTO reportMember(Long reportedMemberId, MemberRequestDTO.ReportDTO reportDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member reportedMember = memberRepository.findById(reportedMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Report report = Report.builder()
                .abuse(reportDTO.getAbuse())
                .obsceneContent(reportDTO.getObsceneContent())
                .privacy(reportDTO.getPrivacy())
                .spam(reportDTO.getSpam())
                .infringement(reportDTO.getInfringement())
                .description(reportDTO.getDescription())
                .member(reportedMember)
                .build();

        report = reportRepository.save(report);
        reportedMember.addReport(report);

        return MemberDTO.MemberProfileDTO.toDTO(reportedMember);
    }
}
