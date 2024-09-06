//package int221.sit.taskboard.entities;
//
//import int221.sit.taskboard.DTO.SharedBoardId;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "shared_boards")
//public class SharedBoard {
//    @EmbeddedId
//    private SharedBoardId id;
//
//    @ManyToOne
//    @JoinColumn(name = "ownerId", insertable = false, updatable = false)
//    private Users owner;
//
//    @ManyToOne
//    @JoinColumn(name = "boardId", insertable = false, updatable = false)
//    private Boards board;
//}
