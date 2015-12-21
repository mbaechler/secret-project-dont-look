package resources;

import models.Product;
import models.Purchase;
import play.cache.CacheApi;
import play.cache.NamedCache;
import play.libs.F.Promise;

import javax.inject.Inject;
import java.util.List;

public class CachingPurchasesResource implements PurchasesResource {

    private final WSPurchasesResource wsPurchasesService;
    private final CacheApi cacheApi;

    @Inject
    private CachingPurchasesResource(WSPurchasesResource wsPurchasesService, @NamedCache("purchases-cache") CacheApi cacheApi) {
        this.wsPurchasesService = wsPurchasesService;
        this.cacheApi = cacheApi;
    }

    @Override
    public Promise<List<Purchase>> listRecentPurchasesForUser(String username) {
        return cacheApi.getOrElse(
            "listRecentPurchasesForUser" + username,
            () -> wsPurchasesService.listRecentPurchasesForUser(username));
    }

    @Override
    public Promise<List<Purchase>> listSimilarPurchasesFor(Product.Id productId) {
        return cacheApi.getOrElse(
            "listSimilarPurchasesFor" + productId.getId(),
            () -> wsPurchasesService.listSimilarPurchasesFor(productId));
    }


}
