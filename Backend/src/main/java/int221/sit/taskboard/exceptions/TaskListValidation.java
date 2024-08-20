//package int221.sit.taskboard.exceptions;
//
//import int221.sit.taskboard.DTO.TaskAndStatusInt;
//
//public class TaskListValidation {
//    public static void validateTaskDataLength(TaskAndStatusInt newTaskListDto) {
//        StringBuilder errorMessages = new StringBuilder();
//        boolean hasError = false;
//
//        if (newTaskListDto.getTitle() != null && newTaskListDto.getTitle().length() > 100) {
//            errorMessages.append("title size must be between 0 and 100. ");
//            hasError = true;
//        }
//        if (newTaskListDto.getDescription() != null && newTaskListDto.getDescription().length() > 500) {
//            errorMessages.append("description size must be between 0 and 500. ");
//            hasError = true;
//        }
//        if (newTaskListDto.getAssignees() != null && newTaskListDto.getAssignees().length() > 30) {
//            errorMessages.append("assignees size must be between 0 and 30. ");
//            hasError = true;
//        }
//
//        if (hasError) {
//            throw new BadRequestException(errorMessages.toString().trim());
//        }
//    }
//}
