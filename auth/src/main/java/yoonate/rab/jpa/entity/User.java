package yoonate.rab.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table( name = "USER", schema = "AUTH")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql="UPDATE USER SET ACTIVATED = FALSE WHERE NO = ?")
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

    @NotNull
    @Column(name ="ACTIVATED", nullable = false)
    private boolean activated;

    @OneToMany(mappedBy = "info.userNo")
//    @JoinColumn(name = "USER_NO", referencedColumnName = "NO", insertable = false, updatable = false)
    private List<AssignRole> assignRoles;

}