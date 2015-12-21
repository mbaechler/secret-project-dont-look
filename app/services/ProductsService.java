package services;

import models.Product;
import models.Purchase;
import play.libs.F.Promise;
import resources.ProductsResource;

import javax.inject.Inject;
import java.util.List;

public class ProductsService {

    private final ProductsResource productsResource;

    @Inject
    private ProductsService(ProductsResource productsResource) {
        this.productsResource = productsResource;
    }

    public Promise<List<Product>> getProducts(List<Purchase> purchases) {
        return Promise.sequence(
            purchases.stream()
                .map(Purchase::getProductId)
                .map(productsResource::getProduct)::iterator);
    }

}
