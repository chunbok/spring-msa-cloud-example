package yoonate.rab.jpa.plugs;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false, of = {"serviceRole", "contactRole"})
public class RoleId implements Serializable {

    @Column(name="SERVICE_ROLE", nullable = false, unique = true)
    private String serviceRole;

    @Column(name="CONTACT_ROLE", nullable = false, unique = true)
    private String contactRole;
}
