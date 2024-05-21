package int221.sit.taskboard.exceptions;

import int221.sit.taskboard.entities.StatusList;

public class StatusListValidation {
    public static void validateTaskDataLength(StatusList statusList){
        StringBuilder errorMessages = new StringBuilder();
        boolean hasError = false;

        if (statusList.getName() != null && statusList.getName().length() > 50) {
            errorMessages.append("name size must be between 0 and 50. ");
            hasError = true;
        }
        if (statusList.getDescription() != null && statusList.getDescription().length() > 200) {
            errorMessages.append("description size must be between 0 and 200. ");
            hasError = true;
        }
        if (hasError) {
            throw new BadRequestException(errorMessages.toString().trim());
        }
    }
}
