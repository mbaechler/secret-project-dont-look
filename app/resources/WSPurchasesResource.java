package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Product;
import models.Purchase;
import modules.ExamModule;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class WSPurchasesResource implements PurchasesResource {

    private final WSClient ws;
    private final String rootUrl;
    private final ObjectMapper mapper;

    @Inject
    private WSPurchasesResource(WSClient ws, @Named(ExamModule.BACKEND_URL) String rootUrl, ExamModule.CustomizedObjectMapper objectMapper) {
        this.ws = ws;
        this.rootUrl = rootUrl;
        this.mapper = objectMapper;
    }

    @Override
    public Promise<List<Purchase>> listRecentPurchasesForUser(String username) {
        WSRequest purchasesForUser = ws.url(rootUrl + "/purchases/by_user/" + username).setQueryParameter("limit", "5");
        return toPurchase(purchasesForUser.get());
    }

    @Override
    public Promise<List<Purchase>> listSimilarPurchasesFor(Product.Id productId) {
        return toPurchase(ws.url(rootUrl + "/purchases/by_product/" + productId.getId()).get());
    }

    private Promise<List<Purchase>> toPurchase(Promise<WSResponse> purchasesForUser) {
        return purchasesForUser
            .map(WSResponse::getBodyAsStream)
            .map(stream -> mapper.readValue(stream, Purchases.class))
            .map(x -> x.purchases);
    }

    private static class Purchases {
        public List<Purchase> purchases;
    }

}
