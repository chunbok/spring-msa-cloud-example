package yoonate.rab.jpa.plugs;

import jakarta.persistence.*;
import lombok.*;
import yoonate.rab.jpa.entity.Role;
import yoonate.rab.jpa.entity.User;

import java.io.Serializable;

@Embeddable
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleColumns implements Serializable {

    @Column(name="USER_NO", nullable = false, unique = true)
//    @JoinColumn(name="USER_NO", referencedColumnName = "USER_NO")
    private int userNo;

    @Column(name="SERVICE_ROLE", nullable = false, unique = true)
//    @JoinColumn(name="SERVICE_ROLE", referencedColumnName = "SERVICE_ROLE_NAME")
    private String serviceRole;

    @Column(name="CONTACT_ROLE", nullable = false, unique = true)
//    @JoinColumn(name="CONTACT_ROLE", referencedColumnName = "CONTACT_ROLE")
    private String contactRole;
}
