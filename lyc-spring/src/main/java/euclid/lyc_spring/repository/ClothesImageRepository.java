package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.clothes.ClothesImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothesImageRepository extends JpaRepository<ClothesImage, Long> {

    Optional<ClothesImage> findByClothesId(Long clothesId);
}
