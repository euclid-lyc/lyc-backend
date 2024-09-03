package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.domain.chat.Message;
import euclid.lyc_spring.domain.chat.QMessage;
import euclid.lyc_spring.domain.mapping.QMemberChat;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.repository.querydsl.MemberChatRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class MemberChatRepositoryCustomImpl implements MemberChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatResponseDTO.LatestMessageDTO> findAllChatsByMemberId(Long memberId, Pageable pageable) {

        QMemberChat memberChat = QMemberChat.memberChat;
        QMessage message = QMessage.message;

        // 회원이 속한 채팅방의 아이디 목록을 찾는 서브쿼리
        SubQueryExpression<Long> chatIdSubQuery = findChatIdsByMemberId(memberId);

        return queryFactory
                .select(Projections.constructor(ChatResponseDTO.LatestMessageDTO.class,
                        memberChat.chat.id,
                        message.createdAt.max()))
                .from(message)
                .join(memberChat).on(memberChat.id.eq(message.memberChat.id))
                .where(memberChat.chat.id.in(chatIdSubQuery))
                .groupBy(memberChat.chat.id)
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private SubQueryExpression<Long> findChatIdsByMemberId(Long memberId) {
        QMemberChat memberChat = QMemberChat.memberChat;
        return JPAExpressions
                .select(memberChat.chat.id)
                .from(memberChat)
                .where(memberChat.member.id.eq(memberId));
    }

    @Override
    public List<ChatResponseDTO.MemberPreviewDTO> findPartnerByChatAndMemberId(Long chatId, Long memberId) {

        QMemberChat memberChat = QMemberChat.memberChat;
        QMember member = QMember.member;

        return queryFactory
                .select(Projections.constructor(ChatResponseDTO.MemberPreviewDTO.class,
                                member.nickname,
                                member.loginId,
                                member.profileImage))
                .from(member)
                .join(memberChat).on(member.id.eq(memberChat.member.id))
                .where(memberChat.chat.id.eq(chatId)
                        .and(memberChat.member.id.ne(memberId)))
                .fetch();
    }

    @Override
    public List<Message> findImageMessagesSortedByCreatedAt(Long chatId, Pageable pageable, LocalDateTime cursorDateTime) {

        QMemberChat memberChat = QMemberChat.memberChat;
        QMessage message = QMessage.message;

        return queryFactory
                .selectFrom(message)
                .join(memberChat).on(memberChat.id.eq(message.memberChat.id))
                .where(memberChat.chat.id.eq(chatId)
                        .and(message.isText.eq(false))
                        .and(message.createdAt.before(cursorDateTime)))
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
