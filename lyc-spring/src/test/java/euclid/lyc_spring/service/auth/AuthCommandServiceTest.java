package euclid.lyc_spring.service.auth;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.request.InfoRequestDTO;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.request.RegisterDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthCommandServiceTest {

    @InjectMocks
    private AuthCommandServiceImpl authCommandService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private InfoRepository infoRepository;
    @Mock
    private InfoStyleRepository infoStyleRepository;
    @Mock
    private InfoFitRepository infoFitRepository;
    @Mock
    private InfoMaterialRepository infoMaterialRepository;
    @Mock
    private InfoBodyTypeRepository infoBodyTypeRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private InfoRequestDTO.InfoStyleListDTO infoStyleListDTO;
    @Mock
    private InfoRequestDTO.InfoFitListDTO infoFitListDTO;
    @Mock
    private InfoRequestDTO.InfoMaterialListDTO infoMaterialListDTO;
    @Mock
    private InfoRequestDTO.InfoBodyTypeListDTO infoBodyTypeListDTO;
    @Mock
    private InfoRequestDTO.BasicInfoDTO basicInfoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void join() {
//
//        MemberRequestDTO.MemberDTO memberDTO = new MemberRequestDTO.MemberDTO("string", "string", "string",
//                "string", "string", "string", "string", "string");
//        RegisterDTO.RegisterMemberDTO registerDTO = new RegisterDTO.RegisterMemberDTO(memberDTO, basicInfoDTO);
//
//        when(memberRepository.findByEmail(registerDTO.getMember().getEmail())).thenReturn(Optional.empty());
//        when(bCryptPasswordEncoder.encode(registerDTO.getMember().getLoginPw())).thenReturn("비밀번호 정상적");
//        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        MemberDTO.MemberInfoDTO result = authCommandService.join(registerDTO);
//        assertNotNull(result);
//        assertEquals("string", result.getLoginId());
//        assertEquals("string", result.getNickname());
//        verify(memberRepository, times(1)).save(any(Member.class));
//    }
}