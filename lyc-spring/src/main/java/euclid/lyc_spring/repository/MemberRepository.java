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

    Optional<Member> findByEmail(String email);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findAllByInactiveBefore(LocalDateTime maxInactive);
    boolean existsByIdAndInactiveIsNotNull(Long memberId);
    Optional<Member> findByLoginIdAndRole(String loginId, Role role);
    Optional<Member> findByNameAndPhone(String name, String phone);
    Optional<Member> findByNameAndEmail(String name, String email);
    Optional<Member> findByNameAndLoginIdAndPhone(String name, String loginId, String phone);
    Optional<Member> findByNameAndLoginIdAndEmail(String name, String loginId, String email);
}