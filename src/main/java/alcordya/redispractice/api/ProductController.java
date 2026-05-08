package alcordya.redispractice.api;

import alcordya.redispractice.domain.ProductService;
import alcordya.redispractice.domain.db.ProductEntity;
import alcordya.redispractice.domain.service.CacheMode;
import alcordya.redispractice.domain.service.DbProductService;
import alcordya.redispractice.domain.service.ManualCachingProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final DbProductService dbProductService;;
    private final ManualCachingProductService manualCachingProductService;;
    private final ProductDtoMapper productDtoMapper;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductCreateRequest request,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode) {
        log.info("Creating product with cacheMode={}", cacheMode);

        ProductService service = resolveProductService(cacheMode);
        ProductEntity entity = service.create(request);
        ProductDto dto = productDtoMapper.toDto(entity);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(
            @PathVariable Long id,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode) {
        log.info("Getting product with id={} with cache mode={}", id, cacheMode);


        ProductService service = resolveProductService(cacheMode);
        ProductEntity entity = service.getById(id);
        ProductDto dto = productDtoMapper.toDto(entity);

        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode) {
        log.info("Updating product id={} with cacheMode={}", id, cacheMode);

        ProductService service = resolveProductService(cacheMode);
        ProductEntity product = service.update(id, request);
        ProductDto dto = productDtoMapper.toDto(product);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(
            @PathVariable Long id,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode) {
        log.info("Deleting product id={} with cacheMode={}", id, cacheMode);

        ProductService service = resolveProductService(cacheMode);
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    private ProductService resolveProductService(CacheMode cacheMode) {
        return switch (cacheMode) {
            case MANUAL -> manualCachingProductService;
            case NONE_CACHE -> dbProductService;
            case null -> throw new IllegalArgumentException("Invalid cacheMode");
        };
    }
}
