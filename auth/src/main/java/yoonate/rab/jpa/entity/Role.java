package yoonate.rab.jpa.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import yoonate.rab.jpa.enums.AUTH_ROLE;
import yoonate.rab.jpa.generator.RoleIdGenerator;
import yoonate.rab.jpa.plugs.RoleId;
import yoonate.rab.user.dto.PairRoleInfo;

import java.util.List;
import java.util.Optional;


@Getter
@Setter
@Entity
@Table(name="ROLE", schema = "AUTH")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @EmbeddedId
    @GeneratedValue(generator = "roleIdGenerator")
    @GenericGenerator(name = "roleIdGenerator", type = RoleIdGenerator.class)
    private RoleId id;

    @Column(name="SERVICE_ROLE_NAME", nullable = false)
    private String serviceRoleName;

    @Column(name="CONTACT_ROLE_NAME", nullable = false)
    private String contactRoleName;

    @Column(name="DESC")
    private String description;

    @OneToMany
    @JoinColumns({
        @JoinColumn(name="SERVICE_ROLE", referencedColumnName  = "SERVICE_ROLE_NAME"),
        @JoinColumn(name="CONTACT_ROLE", referencedColumnName = "CONTACT_ROLE_NAME")
    })
    private List<AssignRole> assignRoles;

    @Transient
    private PairRoleInfo roleInfo;

    @PrePersist
    public void prePersist() {
        this.serviceRoleName = this.roleInfo.getService().getRoleName();
        this.contactRoleName = this.roleInfo.getContact().getRoleName();
    }

    public static Role save(AUTH_ROLE.SERVICE serviceRole, AUTH_ROLE.CONTACT contactRole,String description) {
        return Role.builder()
                .roleInfo(PairRoleInfo.builder()
                        .service(serviceRole)
                        .contact(contactRole)
                        .build())
                .description(Optional.ofNullable(description).orElse(""))
                .build();
    }

}
