package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.clothes.ClothesText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothesTextRepository extends JpaRepository<ClothesText, Long> {

    Optional<ClothesText> findByClothesId(Long clothesId);
}
