package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
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

        BooleanBuilder whereClause = new BooleanBuilder()
                .and(memberChat.chat.id.eq(chatId));

        if (cursorDateTime != null) {
            whereClause.and(message.createdAt.before(cursorDateTime));
        }

        return queryFactory
                .selectFrom(message)
                .join(memberChat).on(message.memberChat.id.eq(memberChat.id))
                .where(whereClause)
                .orderBy(message.createdAt.desc(), message.id.asc())
                .limit(pageSize)
                .fetch();
    }
}
