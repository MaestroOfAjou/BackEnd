package Moa.Web.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class MemberTag {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name ="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name ="tag_id")
    private Tag tag;

    private int tagAmount;

}
