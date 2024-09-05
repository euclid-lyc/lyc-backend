package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.chat.Message;
import euclid.lyc_spring.domain.chat.QMessage;
import euclid.lyc_spring.domain.mapping.QMemberChat;
import euclid.lyc_spring.repository.querydsl.MessageRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Message> findMessagesByChatId(Long chatId, Integer pageSize, LocalDateTime cursorDateTime) {

        QMemberChat memberChat = QMemberChat.memberChat;
        QMessage message = QMessage.message;

        return queryFactory
                .selectFrom(message)
                .join(memberChat).on(message.memberChat.id.eq(memberChat.id))
                .where(memberChat.chat.id.eq(chatId)
                        .and(message.createdAt.before(cursorDateTime)))
                .orderBy(message.createdAt.asc(), message.id.asc())
                .limit(pageSize)
                .fetch();
    }
}
