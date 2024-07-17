package euclid.lyc_spring.domain;

import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.domain.commission.Commission;
import euclid.lyc_spring.domain.info.Info;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import euclid.lyc_spring.domain.posting.Posting;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String loginId;

    @Column(length = 30, nullable = false)
    private String loginPw;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 10, nullable = false)
    private String nickname;

    @Column(columnDefinition = "text")
    private String introduction;

    @Column(columnDefinition = "text")
    private String profileImage;

    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    private Short stampNo;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime inactive;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Integer point;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long follower;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long following;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Info info;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private PushSet pushSet;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notificationList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberChat> memberChatList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PointUsage> pointUsageList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<LikedPosting> likedPostingList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SavedPosting> savedPostingList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Clothes> clothesList;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> followerList;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follow> followingList;

    @OneToMany(mappedBy = "blockMember", cascade = CascadeType.ALL)
    private List<BlockMember> blockMemberList;

    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL)
    private List<Commission> directorList;

    @OneToMany(mappedBy = "fromMember", cascade = CascadeType.ALL)
    private List<Posting> coordieList;

    @OneToMany(mappedBy = "toMember", cascade = CascadeType.ALL)
    private List<Posting> postingList;


}
