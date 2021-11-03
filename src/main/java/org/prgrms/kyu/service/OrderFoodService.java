package org.prgrms.kyu.service;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.dto.OrderFoodRequest;
import org.prgrms.kyu.entity.Food;
import org.prgrms.kyu.entity.Order;
import org.prgrms.kyu.entity.OrderFood;
import org.prgrms.kyu.repository.OrderFoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderFoodService {

  private final OrderFoodRepository repository;
  private final FoodService foodService;

  @Transactional
  public Long save(OrderFoodRequest orderFoodRequest, Order order)
      throws NotFoundException {
    Food food = foodService.getById(orderFoodRequest.getFoodId());
    return repository.save(
        orderFoodRequest.convertToOrderFood(
            order, food)).getId();
  }

}
