package int221.sit.taskboard.users_account;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @Column(name = "oid")
    private String userId;

    @Size(max = 50)
    @Column(name = "username")
    private String username;

    @Size(max = 14)
    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private enum Role {
        LECTURER, STAFF, STUDENT
    }
}
