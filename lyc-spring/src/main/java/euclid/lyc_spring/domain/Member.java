package euclid.lyc_spring.domain;

import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.enums.Role;
import euclid.lyc_spring.domain.info.Info;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import euclid.lyc_spring.domain.posting.Posting;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String loginPw;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 30, nullable = false)
    private String phone;

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
    @Setter
    private LocalDateTime inactive;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Integer point;

    @Setter
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long follower;

    @Setter
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long following;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Info info;

    @Setter
    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean isPublic; // 저장한 코디 공개 여부

    @Setter
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
    private List<Commission> commissionList;

    @OneToMany(mappedBy = "fromMember", cascade = CascadeType.ALL)
    private List<Posting> fromPostingList;

    @OneToMany(mappedBy = "toMember", cascade = CascadeType.ALL)
    private List<Posting> toPostingList;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Posting> postingList;

    protected Member() {}

    // followerMember
    public Member(Long id){
        this.id = id;
    }

    @Builder
    public Member(String name, String loginId, String loginPw, String email,
                  String phone, String nickname, String introduction,
                  String profileImage, Role role) {
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImage = profileImage;
        this.stampNo = 0;
        this.point = 0;
        this.follower = 0L;
        this.following = 0L;
        this.role = role;
        this.isPublic = true;
        this.notificationList = new ArrayList<>();
        this.attendanceList = new ArrayList<>();
        this.memberChatList = new ArrayList<>();
        this.pointUsageList = new ArrayList<>();
        this.likedPostingList = new ArrayList<>();
        this.savedPostingList = new ArrayList<>();
        this.clothesList = new ArrayList<>();
        this.followerList = new ArrayList<>();
        this.followingList = new ArrayList<>();
        this.blockMemberList = new ArrayList<>();
        this.commissionList = new ArrayList<>();
        this.fromPostingList = new ArrayList<>();
        this.toPostingList = new ArrayList<>();
        this.postingList = new ArrayList<>();
    }


    //=== Add Methods ===//

    public void setInfo(Info info) {
        this.info = info;
        info.setMember(this);
    }

    public void addPushSet(PushSet pushSet) {
        this.pushSet = pushSet;
        pushSet.setMember(this);
    }

    public void addNotification(Notification notification) {
        notificationList.add(notification);
        notification.setMember(this);
    }

    public void addAttendance(Attendance attendance) {
        attendanceList.add(attendance);
        attendance.setMember(this);
    }

    public void addMemberChat(MemberChat memberChat) {
        memberChatList.add(memberChat);
        memberChat.setMember(this);
    }

    public void addPointUsage(PointUsage pointUsage) {
        pointUsageList.add(pointUsage);
        pointUsage.setMember(this);
    }

    public void addLikedPosting(LikedPosting likedPosting) {
        likedPostingList.add(likedPosting);
        likedPosting.setMember(this);
    }

    public void addSavedPosting(SavedPosting savedPosting) {
        savedPostingList.add(savedPosting);
        savedPosting.setMember(this);
    }

    public void addClothes(Clothes clothes) {
        clothesList.add(clothes);
        clothes.setMember(this);
    }

    public void addFollower(Follow follower) {
        followerList.add(follower);
        follower.setFollower(this);
    }

    public void addFollowing(Follow following) {
        followingList.add(following);
        following.setFollower(this);
    }

    public void addBlockMember(BlockMember blockMember) {
        blockMemberList.add(blockMember);
        blockMember.setBlockMember(this);
    }

    public void addCommission(Commission commission) {
        commissionList.add(commission);
        commission.setDirector(this);
    }

    public void addFromPosting(Posting posting) {
        fromPostingList.add(posting);
        posting.setFromMember(this);
    }

    public void addToPosting(Posting posting) {
        toPostingList.add(posting);
        posting.setToMember(this);
    }

    public void addPosting(Posting posting) {
        postingList.add(posting);
        posting.setWriter(this);
    }

    //=== remove Methods ===//

    public void removePosting(Posting posting) {
        fromPostingList.remove(posting);
        toPostingList.remove(posting);
        postingList.remove(posting);
    }

    public void removeSavedPosting(SavedPosting savedPosting) {
        savedPostingList.remove(savedPosting);
    }

    //=== reload Methods ===//

    public void reloadFollowing(Long following) {
        this.following = following;
    }

    public void reloadFollower(Long follower) {
        this.follower = follower;
    }

    public void reloadNickname(String nickname) { this.nickname = nickname; }

    public void reloadIntroduction(String introduction) { this.introduction = introduction; }

    public void reloadLoginId(String loginId) { this.loginId = loginId; }

    public void reloadProfileImage(String profileImage) { this.profileImage = profileImage; }

    public void changeLoginPw(String password, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.loginPw = bCryptPasswordEncoder.encode(password);
    }
}