package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.MemberDTO;

import java.util.List;

public interface FollowRepositoryCustom {

    List<MemberDTO.FollowerCountDTO> findPopularDirectors(Integer pageSize, Long followerCount);
}
