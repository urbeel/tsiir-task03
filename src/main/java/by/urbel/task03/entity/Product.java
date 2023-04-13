package by.urbel.task03.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product implements Comparable<Product> {
    private Long id;
    private String name;
    private ProductCategory category;
    private String description;
    private long price;
    private int quantity;
    private String photoUrl;

    @Override
    public int compareTo(Product product) {
        return id.compareTo(product.getId());
    }
}
