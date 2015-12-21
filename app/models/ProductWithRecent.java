package models;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ProductWithRecent {

    public static Builder from(Product product) {
        return new Builder(product);
    }

    public static class Builder {
        private Product product;
        private List<String> recent;

        private Builder(Product product) {
            this.product = product;
            recent = Collections.emptyList();
        }

        public Builder recent(Set<String> recent) {
            this.recent = ImmutableList.copyOf(recent);
            return this;
        }

        public ProductWithRecent build() {
            return new ProductWithRecent(product, recent);
        }
    }

    private final Product product;
    private final List<String> recent;

    private ProductWithRecent(Product product, List<String> recent) {
        this.product = product;
        this.recent = recent;
    }

    public Product.Id getId() {
        return product.getId();
    }

    public String getFace() {
        return product.getFace();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public int getSize() {
        return product.getSize();
    }

    public List<String> getRecent() {
        return recent;
    }
}
