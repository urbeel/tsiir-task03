package by.urbel.task03.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductCategory implements Comparable<ProductCategory> {
    private Long id;
    private String name;

    public ProductCategory(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(ProductCategory category) {
        return id.compareTo(category.getId());
    }
}
