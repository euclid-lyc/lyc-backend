package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.domain.clothes.QClothes;
import euclid.lyc_spring.repository.querydsl.ClothesRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ClothesRepositoryCustomImpl implements ClothesRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Clothes> findClothesByMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {
        QClothes clothes = QClothes.clothes;
        return queryFactory.selectFrom(clothes)
                .where(clothes.member.id.eq(memberId)
                        .and(clothes.createdAt.before(cursorDateTime)))
                .orderBy(clothes.createdAt.desc())
                .limit(pageSize)
                .fetch();
    }
}
