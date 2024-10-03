package euclid.lyc_spring.config;

import euclid.lyc_spring.domain.chat.Message;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.repository.MemberChatRepository;
import euclid.lyc_spring.repository.MessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

//    @Bean
//    public CommandLineRunner loadData(MessageRepository messageRepository, MemberChatRepository memberChatRepository) {
//        return args -> {
//            // MemberChat 객체를 먼저 생성해야 합니다.
//            // 이를 위한 더미 데이터 생성 예제입니다.
//            MemberChat memberChat1 = memberChatRepository.findById(1L).orElseThrow(); // 적절한 데이터를 세팅해 주세요.
//            MemberChat memberChat2 = memberChatRepository.findById(2L).orElseThrow();
//
//            // 더미 데이터 생성
//            Message message1 = Message.builder()
//                    .content("안녕하세요")
//                    .isText(true)
//                    .isChecked(false)
//                    .memberChat(memberChat1)
//                    .build();
//
//            Message message2 = Message.builder()
//                    .content("안녕못해요")
//                    .isText(true)
//                    .isChecked(true)
//                    .memberChat(memberChat2)
//                    .build();
//
//            // 저장
//            messageRepository.save(message1);
//            messageRepository.save(message2);
//        };
//    }
}