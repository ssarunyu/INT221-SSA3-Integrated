package int221.sit.taskboard.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden - Authorization header is missing for GET request");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Access is denied");
        }
    }
}
