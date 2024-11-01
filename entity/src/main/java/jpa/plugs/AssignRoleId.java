package jpa.plugs;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleId implements Serializable {

    @Column(name="USER_NO", nullable = false, unique = true)
    private Integer userNo;

    private RoleId roleId;

}
