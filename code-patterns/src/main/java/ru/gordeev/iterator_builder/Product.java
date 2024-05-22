package ru.gordeev.iterator_builder;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class Product {

    private final int id;

    private final String title;

    private final String description;

    private final double cost;

    private final int weight;

    private final int width;

    private final int length;

    private final int height;


    public static Builder builder() {
        return new Builder();
    }

    private Product(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.cost = builder.cost;
        this.weight = builder.weight;
        this.width = builder.width;
        this.length = builder.length;
        this.height = builder.height;
    }


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Builder {

        private int id;

        private String title;

        private String description;

        private double cost;

        private int weight;

        private int width;

        private int length;

        private int height;


        public Product build() {
            return new Product(this);
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder cost(double cost) {
            this.cost = cost;
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }
    }
}
