package application.model.view;

import java.util.List;

public record CatalogoView(
        String inicial,
        List<SerieCatalogoView> series) {
}
