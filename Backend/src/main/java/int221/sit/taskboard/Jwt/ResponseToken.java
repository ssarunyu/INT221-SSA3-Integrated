package int221.sit.taskboard.Jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseToken {
    private final String access_token;
    private final String refresh_token;
}

