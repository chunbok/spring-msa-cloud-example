package jpa.entity;

import jakarta.persistence.*;
import jpa.dto.PairRoleInfo;
import jpa.generator.AssignRoleIdGenerator;
import jpa.plugs.AssignRoleId;
import jpa.plugs.RoleId;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name="USER_ROLE_INFO", schema = "AUTH")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AssignRole {

    @EmbeddedId
    @GeneratedValue(generator = "assignRoleIdGenerator")
    @GenericGenerator(name = "assignRoleIdGenerator", type = AssignRoleIdGenerator.class)
    private AssignRoleId info;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userNo")
    @JoinColumn(name="USER_NO", referencedColumnName = "NO")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumns({
            @JoinColumn(name="SERVICE_ROLE", referencedColumnName = "SERVICE_ROLE"),
            @JoinColumn(name="CONTACT_ROLE", referencedColumnName = "CONTACT_ROLE")
    })
    private Role role;

    public static AssignRole getSave(int userNo, PairRoleInfo role) {
        return AssignRole.builder()
            .info(AssignRoleId.builder()
                .userNo(userNo)
                .roleId(
                    RoleId.builder()
                        .serviceRole(role.getService().name())
                        .contactRole(role.getContact().name())
                        .build()
                ).build()
            )
            .build();
    }
}
