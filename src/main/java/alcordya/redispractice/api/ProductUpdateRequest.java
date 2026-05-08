package alcordya.redispractice.api;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        BigDecimal price,
        String description
) {
}
