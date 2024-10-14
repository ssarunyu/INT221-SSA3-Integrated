package int221.sit.taskboard.entities.itbkk_db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "collaborators")
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collabId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserList owner;

    @ManyToOne
    @JoinColumn(name = "boardId", nullable = false)
    private Boards board;

    @Column(name = "added_on")
    private ZonedDateTime addedOn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_right", nullable = false)
    private AccessRight accessRight = AccessRight.READ;

    public enum AccessRight {
        READ,
        WRITE
    }
}
