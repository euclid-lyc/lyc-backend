package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.clothes.Clothes;

import java.time.LocalDateTime;
import java.util.List;

public interface ClothesRepositoryCustom {

    List<Clothes> findClothesByMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);
}
