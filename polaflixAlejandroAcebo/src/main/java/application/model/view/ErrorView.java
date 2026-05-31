package application.model.view;

import java.time.LocalDateTime;

public record ErrorView(
        LocalDateTime timestamp,
        int status,
        String error,
        String code,
        String message,
        String path) {
}
