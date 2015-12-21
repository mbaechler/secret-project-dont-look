package resources;

import models.Product;
import models.Purchase;
import play.libs.F.Promise;

public interface ProductsResource {
    Promise<Product> getProduct(Product.Id id);
}
