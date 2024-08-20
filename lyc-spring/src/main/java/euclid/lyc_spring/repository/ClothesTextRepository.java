package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.clothes.ClothesText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesTextRepository extends JpaRepository<ClothesText, Long> {
}
