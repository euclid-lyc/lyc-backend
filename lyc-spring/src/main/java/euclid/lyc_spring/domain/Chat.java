package euclid.lyc_spring.domain;

import euclid.lyc_spring.domain.mapping.MemberChat;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Getter
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @CreatedDate
    @Column
    private Date createdAt;

    @LastModifiedDate
    @Column
    private Date updatedAt;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<MemberChat> memberChatList;
}
