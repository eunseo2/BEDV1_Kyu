package org.prgrms.kyu.dto;

import lombok.*;
import org.prgrms.kyu.entity.Food;
import org.prgrms.kyu.entity.Order;
import org.prgrms.kyu.entity.OrderFood;
import org.prgrms.kyu.entity.OrderStatus;
import org.prgrms.kyu.entity.Store;
import org.prgrms.kyu.entity.User;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderRequest {

  String address;
  String phoneNumber;
  OrderFoodRequest orderFood;

  public Order convertToOrder(User customer, Store store) {
    return Order.builder()
        .address(this.address)
        .orderStatus(OrderStatus.PAYMENT_CONFIRMED)
        .phoneNumber(this.phoneNumber)
        .totalAmount(this.orderFood.getQuantity() * this.orderFood.getPrice())
        .customer(customer)
        .store(store)
        .build();
  }
}
