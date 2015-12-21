package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.ZonedDateTime;

@JsonDeserialize(builder = Purchase.Builder.class)
public class Purchase {

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        
        private int id;
        private String username;
        private Product.Id productId;
        private ZonedDateTime date;

        private Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder productId(Product.Id productId) {
            this.productId = productId;
            return this;
        }

        public Builder date(ZonedDateTime date) {
            this.date = date;
            return this;
        }

        public Purchase build() {
            return new Purchase(id, username, productId, date);
        }
    }
    
    private final int id;
    private final String username;
    private final Product.Id productId;
    private final ZonedDateTime date;

    private Purchase(int id, String username, Product.Id productId, ZonedDateTime date) {
        this.id = id;
        this.username = username;
        this.productId = productId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Product.Id getProductId() {
        return productId;
    }

    public ZonedDateTime getDate() {
        return date;
    }
}
