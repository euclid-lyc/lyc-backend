package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.repository.querydsl.ClothesRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long>, ClothesRepositoryCustom {
    List<Clothes> findByMember(Member member);
    Optional<Clothes> findByIdAndMember(Long clothesId, Member member);
}
