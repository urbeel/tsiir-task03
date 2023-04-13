package by.urbel.task03.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Order implements Comparable<Order> {
    private Long id;
    private User user;
    private OrderStatus status;
    private List<Item> items;
    private Timestamp orderTime;
    private long totalPrice;

    @Override
    public int compareTo(Order order) {
        return id.compareTo(order.getId());
    }
}
