package resources;

import models.Product;
import models.Purchase;
import play.libs.F.Promise;

import java.util.List;


public interface PurchasesResource {

    Promise<List<Purchase>> listRecentPurchasesForUser(String username);

    Promise<List<Purchase>> listSimilarPurchasesFor(Product.Id productId);
}
