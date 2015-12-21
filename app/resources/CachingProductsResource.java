package resources;


import models.Product;
import models.Purchase;
import play.cache.CacheApi;
import play.cache.NamedCache;
import play.libs.F.Promise;

import javax.inject.Inject;

public class CachingProductsResource implements ProductsResource {

    private final WSProductsResource wsProductsResource;
    private final CacheApi cacheApi;

    @Inject
    private CachingProductsResource(WSProductsResource wsProductsResource, @NamedCache("products-cache") CacheApi cacheApi) {
        this.wsProductsResource = wsProductsResource;
        this.cacheApi = cacheApi;
    }

    @Override
    public Promise<Product> getProduct(Product.Id id) {
        return cacheApi.getOrElse("getProduct" + id.getId(), () -> wsProductsResource.getProduct(id));
    }
}
