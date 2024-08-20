package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.posting.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
}