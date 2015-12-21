package services;

import models.Purchase;
import play.libs.F.Promise;
import resources.PurchasesResource;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PurchasesService {

    private final PurchasesResource purchasesResource;

    @Inject
    private PurchasesService(PurchasesResource purchasesResource) {
        this.purchasesResource = purchasesResource;
    }

    public Promise<List<Set<String>>> listRecentBuyers(List<Purchase> products) {
        Stream<Promise<List<Purchase>>> purchases = products.stream()
            .map(Purchase::getProductId)
            .map(purchasesResource::listSimilarPurchasesFor);
        return Promise.sequence(purchases::iterator)
            .map(this::extractBuyersLists);
    }

    private List<Set<String>> extractBuyersLists(List<List<Purchase>> purchases) {
        Stream<Set<String>> listStream = purchases.stream().map(this::extractBuyers);
        return listStream.collect(Collectors.toList());
    }

    private Set<String> extractBuyers(List<Purchase> purchases) {
        return purchases.stream().map(Purchase::getUsername).collect(Collectors.toSet());
    }


    public Promise<List<Purchase>> listRecentPurchasesForUser(String username) {
        return purchasesResource.listRecentPurchasesForUser(username);
    }
}
