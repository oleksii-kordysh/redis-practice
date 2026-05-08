package alcordya.redispractice.domain;

import alcordya.redispractice.api.ProductCreateRequest;
import alcordya.redispractice.api.ProductUpdateRequest;
import alcordya.redispractice.domain.db.ProductEntity;

public interface ProductService {

    ProductEntity create(ProductCreateRequest request);

    ProductEntity update(Long id, ProductUpdateRequest request);

    ProductEntity getById(Long id);

    void delete(Long id);
}
