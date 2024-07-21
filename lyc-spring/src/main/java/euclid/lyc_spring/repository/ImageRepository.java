package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.posting.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}