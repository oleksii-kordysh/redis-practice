package alcordya.redispractice.domain.service;

import alcordya.redispractice.api.ProductCreateRequest;
import alcordya.redispractice.api.ProductUpdateRequest;
import alcordya.redispractice.domain.ProductService;
import alcordya.redispractice.domain.db.ProductEntity;
import alcordya.redispractice.domain.db.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbProductService implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductEntity create(ProductCreateRequest request) {
        log.info("Create product request: {}", request.name());
        ProductEntity product = ProductEntity.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();
        return productRepository.save(product);
    }

    @Override
    public ProductEntity update(Long id, ProductUpdateRequest request) {
        log.info("Updating product by id: {}", id);

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found: " + id));

        if (request.price() != null) {
            product.setPrice(request.price());
        }

        if (request.description() != null) {
            product.setDescription(request.description());
        }

        return productRepository.save(product);
    }

    @Override
    public ProductEntity getById(Long id) {
        log.info("Getting product by id: {}", id);

        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found: " + id));
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting product by id: {}", id);

        productRepository.deleteById(id);

    }
}
