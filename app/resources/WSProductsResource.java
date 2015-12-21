package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Product;
import models.Purchase;
import modules.ExamModule;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import javax.inject.Named;

public class WSProductsResource implements ProductsResource {

    private final WSClient ws;
    private final String rootUrl;
    private final ObjectMapper mapper;

    @Inject
    private WSProductsResource(WSClient ws, @Named(ExamModule.BACKEND_URL) String rootUrl, ExamModule.CustomizedObjectMapper objectMapper) {
        this.ws = ws;
        this.rootUrl = rootUrl;
        this.mapper = objectMapper;
    }

    @Override
    public Promise<Product> getProduct(Product.Id id) {
        return ws.url(rootUrl + "/products/" + id.getId())
            .get()
            .map(WSResponse::asJson)
            .map(json -> json.get("product"))
            .map(x -> mapper.readValue(x.toString(), Product.class));
    }
}
