//package int221.sit.taskboard.DTO;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@AllArgsConstructor
//@NoArgsConstructor
//public class SharedBoardId implements Serializable {
//
//    private String ownerId;
//    private String boardId;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        SharedBoardId that = (SharedBoardId) o;
//
//        if (!ownerId.equals(that.ownerId)) return false;
//        return boardId.equals(that.boardId);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = ownerId.hashCode();
//        result = 31 * result + boardId.hashCode();
//        return result;
//    }
//}
