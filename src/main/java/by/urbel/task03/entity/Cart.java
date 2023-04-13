package by.urbel.task03.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Cart {
    private Long id;
    private Long userId;
    private List<Item> items;
    private Long totalPrice;
}
