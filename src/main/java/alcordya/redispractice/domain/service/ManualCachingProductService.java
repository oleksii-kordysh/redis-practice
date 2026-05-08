package alcordya.redispractice.domain.service;

import alcordya.redispractice.api.ProductCreateRequest;
import alcordya.redispractice.api.ProductUpdateRequest;
import alcordya.redispractice.domain.ProductService;
import alcordya.redispractice.domain.db.ProductEntity;
import alcordya.redispractice.domain.db.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class ManualCachingProductService implements ProductService {
    public static final String CACHE_KEY_PREFIX = "product:";
    public static final int CACHE_TTL_MINUTES = 1;

    private final ProductRepository productRepository;
    private final RedisTemplate<String, ProductEntity> productRedisTemplate;

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
        ProductEntity save = productRepository.save(product);

        String cacheKey = CACHE_KEY_PREFIX + id;
        productRedisTemplate.delete(cacheKey);
        log.info("Cache has been invalidated for updated product id={}", id);
        return save;
    }

    @Override
    public ProductEntity getById(Long id) {
        log.info("Getting product by id: {}", id);
        String cacheKey = CACHE_KEY_PREFIX + id;

        ProductEntity cached = productRedisTemplate.opsForValue()
                .get(cacheKey);
        if (cached != null) {
            log.info("Found product with id: {} in cache", id);
            return cached;
        }
        log.info("Not found product with id: {} in cache", id);

        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found: " + id));
        productRedisTemplate.opsForValue()
                .set(cacheKey, entity, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        log.info("Product with id: {} cached", id);
        return entity;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting product by id: {}", id);
        if(!productRepository.existsById(id)) {
            throw new RuntimeException("product not found: " + id);
        }
        productRepository.deleteById(id);

        String cacheKey = CACHE_KEY_PREFIX + id;
        productRedisTemplate.delete(cacheKey);
        log.info("Cache has been invalidated for deleted product id={}", id);
    }
}
