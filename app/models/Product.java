package models;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = Product.Builder.class)
public class Product {

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private Id id;
        private String face;
        private int price;
        private int size;

        private Builder() {
        }

        public Builder id(Id id) {
            this.id = id;
            return this;
        }

        public Builder face(String face) {
            this.face = face;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Product build() {
            return new Product(id, face, price, size);
        }
    }

    public static Id id(int id) {
        return new Id(id);
    }

    public static class Id {
        private final int id;

        private Id(int id) {
            this.id = id;
        }

        @JsonValue
        public int getId() {
            return id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Id) {
                return ((Id)obj).id == id;
            }
            return false;
        }
    }

    private final Id id;
    private final String face;
    private final int price;
    private final int size;

    private Product(Id id, String face, int price, int size) {
        this.id = id;
        this.face = face;
        this.price = price;
        this.size = size;
    }

    public Id getId() {
        return id;
    }

    public String getFace() {
        return face;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

}
