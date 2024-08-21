package euclid.lyc_spring.service;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.dto.request.InfoRequestDTO.*;
import euclid.lyc_spring.dto.request.MemberRequestDTO.*;
import euclid.lyc_spring.dto.request.RegisterDTO.*;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterServiceTest {

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
    @InjectMocks
    private RegisterService registerService;
    @Mock
    private InfoStyleListDTO infoStyleListDTO;
    @Mock
    private InfoFitListDTO infoFitListDTO;
    @Mock
    private InfoMaterialListDTO infoMaterialListDTO;
    @Mock
    private InfoBodyTypeListDTO infoBodyTypeListDTO;
    @Mock
    private BasicInfoDTO basicInfoDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        MemberInfoDTO memberInfoDTO = new MemberInfoDTO("string", "string", "string",
                "string", "string", "string", "string", "string");
        RegisterMemberDTO registerDTO = new RegisterMemberDTO(memberInfoDTO, basicInfoDTO);

        when(memberRepository.findByEmail(registerDTO.getMemberInfo().getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(registerDTO.getMemberInfo().getLoginPw())).thenReturn("비밀번호 정상적");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MemberDTO.MemberInfoDTO result = registerService.join(registerDTO);
        assertNotNull(result);
        assertEquals("string", result.getLoginId());
        assertEquals("string", result.getNickname());
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}