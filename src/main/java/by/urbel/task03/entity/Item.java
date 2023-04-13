package by.urbel.task03.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Item {
    private Product product;
    private int quantity;
}
