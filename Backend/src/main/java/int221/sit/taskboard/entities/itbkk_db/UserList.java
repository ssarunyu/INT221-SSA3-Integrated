package int221.sit.taskboard.entities.itbkk_db;

import int221.sit.taskboard.entities.itbkk_shared.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users_store")
public class UserList {
    @Id
    @Column(name = "oid")
    private String userListId;

    @Size(max = 50)
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Users.Role role;

    @OneToMany(mappedBy = "owner")
    private List<SharedBoard> sharedBoards;

    public enum Role {
        LECTURER, STAFF, STUDENT
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

}
