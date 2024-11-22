package jpa.entity;

import auth.PAIR_ROLE;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table( name = "USER", schema = "AUTH")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE USER SET ACTIVATED = FALSE WHERE NO = ?")
@SQLRestriction("ACTIVATED = TRUE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NO", nullable = false)
    private Integer no;

    @Size(max = 50)
    @NotNull
    @Column(name = "ID", nullable = false, length = 50)
    private String id;

    @Size(max = 130)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 130)
    private String password;

    @Builder.Default
    @NotNull
    @Column(name ="ACTIVATED", nullable = false)
    private boolean activated = true;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AssignRole> assignRoles = new ArrayList<>();

    public void addAssignRole(PAIR_ROLE info, String description) {
        this.assignRoles.add(
                AssignRole.builder().user(this).role(
                        Role.builder().roleInfo(info).description(description).build()
                ).build()
        );
    }
}