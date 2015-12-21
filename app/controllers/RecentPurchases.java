package controllers;

import com.codepoetics.protonpack.StreamUtils;
import expections.UserNotFoundException;
import models.Product;
import models.ProductWithRecent;
import models.Purchase;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.F.Tuple;
import play.libs.Json;
import play.mvc.Result;
import resources.UsersResource;
import services.ProductsService;
import services.PurchasesService;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static play.mvc.Results.internalServerError;
import static play.mvc.Results.ok;
import static play.mvc.Results.status;

public class RecentPurchases {

    private final PurchasesService purchasesService;
    private final ProductsService productsService;
    private final UsersResource usersResource;

    @Inject
    private RecentPurchases(PurchasesService purchasesService, ProductsService productsService,
                            UsersResource usersResource) {
        this.purchasesService = purchasesService;
        this.productsService = productsService;
        this.usersResource = usersResource;
    }

    public Promise<Result> forUser(String username) {
        return usersResource.getUser(username)
            .<Result>flatMap(user -> recentsForUser(user.getUsername()).map(x -> ok(Json.toJson(x))))
            .recover(recover(username));
    }

    private Function<Throwable, Result> recover(String username) {
        return t -> {
            try {
                throw t;
            } catch (UserNotFoundException e) {
                return status(404, String.format("User with username of '%s' was not found", username));
            } catch (Throwable other) {
                return internalServerError();
            }
        };
    }

    private Promise<List<ProductWithRecent>> recentsForUser(String username) {
        Promise<List<Purchase>> purchases = purchasesService.listRecentPurchasesForUser(username);
        Promise<List<Product>> products = purchases.flatMap(productsService::getProducts);
        Promise<List<Set<String>>> listsOfbuyers = purchases.flatMap(purchasesService::listRecentBuyers);

        return products
                .zip(listsOfbuyers)
                .map(this::mergeProductAndBuyers)
                .map(x -> x.sorted(sortByBuyerNumbers()))
                .map(x -> x.collect(Collectors.toList()));
    }

    private Stream<ProductWithRecent> mergeProductAndBuyers(Tuple<List<Product>, List<Set<String>>> tuple) {
        return StreamUtils.zip(
            tuple._1.stream(),
            tuple._2.stream(),
            (product, buyers) -> ProductWithRecent.from(product).recent(buyers).build());
    }

    private Comparator<ProductWithRecent> sortByBuyerNumbers() {
        return Comparator.comparingInt(product -> -product.getRecent().size());
    }
}
