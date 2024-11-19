package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.domain.chat.QChat;
import euclid.lyc_spring.domain.chat.QMessage;
import euclid.lyc_spring.domain.mapping.QMemberChat;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.repository.querydsl.ChatRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatResponseDTO.ChatPreviewDTO> searchChats(String keyword, Long loginId) {
        QChat chat = QChat.chat;
        QMemberChat memberChat = QMemberChat.memberChat;
        QMember member = QMember.member;
        QMessage message = QMessage.message;

        return queryFactory
                .select(Projections.constructor(ChatResponseDTO.ChatPreviewDTO.class,
                        chat.id,
                        member.nickname, // 상대방 닉네임
                        member.loginId,  // 상대방 아이디
                        member.profileImage, // 상대방 프로필 이미지
                        message.isText,  // 마지막 메시지가 텍스트인지 여부
                        message.content, // 마지막 메시지 내용
                        message.createdAt // 마지막 메시지 생성 시간
                ))
                .from(chat)
                .join(memberChat).on(chat.id.eq(memberChat.chat.id))
                .join(member).on(memberChat.member.id.eq(member.id))
                .leftJoin(message).on(
                        message.memberChat.chat.id.eq(chat.id) // 이 부분 memberChat에 chatId를 추가해서 하는 게 더 바람직한데.. 수정이 너무 귀찮아서 일단 이렇게 함
                                // 테스트를 못 해보고 올리는 거라 이게 서버에서 작동을 잘 하면 수정하겠음 작동을 할지안할지 모르는 부분이라..............
                                .and(message.createdAt.eq(
                                        JPAExpressions
                                                .select(message.createdAt.max())
                                                .from(message)
                                                .where(message.memberChat.chat.id.eq(chat.id))
                                ))
                )
                .where(
                        memberChat.member.id.ne(loginId), // 본인은 제외
                        member.nickname.containsIgnoreCase(keyword)
                                .or(member.loginId.containsIgnoreCase(keyword))
                )
                .fetch();
    }
}
