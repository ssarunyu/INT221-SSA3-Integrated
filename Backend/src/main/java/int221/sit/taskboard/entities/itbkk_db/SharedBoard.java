package int221.sit.taskboard.entities.itbkk_db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shared_boards")
public class SharedBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sharedId;

    @ManyToOne
    @JoinColumn(name = "ownerOid")
    private UserList owner;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Boards board;
}
