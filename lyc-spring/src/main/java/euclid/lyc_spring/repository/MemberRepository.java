package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByInactiveBefore(LocalDateTime maxInactive);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByLoginIdAndRole(String loginId, Role role);
    Optional<Member> findByNameAndEmail(String name, String email);
    Optional<Member> findByNameAndLoginIdAndEmail(String name, String loginId, String email);

    boolean existsByIdAndInactiveIsNotNull(Long memberId);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
}