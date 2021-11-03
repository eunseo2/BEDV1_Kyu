package org.prgrms.kyu.dto;

import lombok.*;
import org.prgrms.kyu.entity.Food;
import org.prgrms.kyu.entity.Order;
import org.prgrms.kyu.entity.OrderFood;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderFoodRequest {
  Long foodId;
  Integer quantity;
  Integer price;

  public OrderFood convertToOrderFood(Order order,Food food){
    return OrderFood.builder()
        .quantity(this.quantity)
        .orderPrice(this.quantity * this.price)
        .order(order)
        .food(food)
        .build();
  }

}
