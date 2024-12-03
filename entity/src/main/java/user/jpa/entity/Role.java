package user.jpa.entity;


import auth.AUTH_ROLE;
import auth.PAIR_ROLE;
import jakarta.persistence.*;
import user.jpa.generator.RoleIdGenerator;
import user.jpa.plugs.RoleId;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
@Setter
@Entity
@Table(name="ROLE", schema = "AUTH")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @EmbeddedId
    @GeneratedValue(generator = "roleIdGenerator")
    @GenericGenerator(name = "roleIdGenerator", type = RoleIdGenerator.class)
    private RoleId id;

    @Column(name="SERVICE_ROLE_NAME")
    private String serviceRoleName;

    @Column(name="CONTACT_ROLE_NAME")
    private String contactRoleName;

    @Column(name="DESC")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "role")
    private List<AssignRole> assignRoles = new ArrayList<>();

    @Transient
    private PAIR_ROLE roleInfo;

    @PrePersist
    public void prePersist() {
        this.serviceRoleName = this.roleInfo.getService().getRoleName();
        this.contactRoleName = this.roleInfo.getContact().getRoleName();
    }

    public static Role getAddEntity(AUTH_ROLE.SERVICE serviceRole, AUTH_ROLE.CONTACT contactRole, String description) {
        return Role.builder()
                .roleInfo(PAIR_ROLE.builder()
                        .service(serviceRole)
                        .contact(contactRole)
                        .build())
                .description(Optional.ofNullable(description).orElse(""))
                .build();
    }
}