package yoonate.rab.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import yoonate.rab.jpa.plugs.AssignRoleColumns;
import yoonate.rab.user.dto.PairRoleInfo;

@Getter
@Setter
@Entity
@Table(name="USER_ROLE_INFO", schema = "AUTH")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignRole {

    @EmbeddedId
    private AssignRoleColumns info;

    @ManyToOne
    @JoinColumn(name="USER_NO", referencedColumnName = "NO", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="SERVICE_ROLE", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name="CONTACT_ROLE", nullable = false, insertable = false, updatable = false)
    })
    private Role role;

    public static AssignRole save(int userNo, PairRoleInfo role) {
        return AssignRole.builder()
            .info(AssignRoleColumns.builder()
                .userNo(userNo)
                .serviceRole(role.getService().name())
                .contactRole(role.getContact().name())
                .build()
            ).build();
    }
}
