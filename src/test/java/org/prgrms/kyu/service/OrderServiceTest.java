package org.prgrms.kyu.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import javassist.NotFoundException;
import javax.naming.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.kyu.dto.FoodRequest;
import org.prgrms.kyu.dto.JoinRequest;
import org.prgrms.kyu.dto.OrderFoodRequest;
import org.prgrms.kyu.dto.OrderRequest;
import org.prgrms.kyu.dto.StoreResponse;
import org.prgrms.kyu.entity.Food;
import org.prgrms.kyu.entity.Order;
import org.prgrms.kyu.entity.Store;
import org.prgrms.kyu.entity.User;
import org.prgrms.kyu.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private UserService userService;

  @Mock
  private StoreService storeService;

  @Mock
  private OrderFoodService orderFoodService;

  private Long fakeUserId =  1L;
  private Long fakeStoreId = 1L;
  private Long fakeFoodId = 1L;
  private Long fakeOrderFoodId = 1L;
  private User saveUser;
  private Store saveStore;
  private Food saveFood;
  private Order saveOrder;

  @Test
  public void orderSaveTest() throws AuthenticationException, NotFoundException {
    JoinRequest joinRequest = new JoinRequest(
        "test1@test.com",
        "1234",
        "user",
        "nick",
        "Seoul",
        "CUSTOMER");
    saveUser = new User(joinRequest);

    saveStore = Store.builder()
        .id(fakeStoreId)
        .name("맘스터치")
        .description("맘스터치입니다.")
        .location("Seoul")
        .telephone("01011112222")
        .build();

    FoodRequest foodRequest = FoodRequest.builder()
        .name("싸이버거")
        .description("싸이버거 입니다")
        .price(1000)
        .build();
    saveFood = FoodRequest.convertToFood(foodRequest);
    saveFood.update(saveStore);
    StoreResponse storeResponse = new StoreResponse(saveStore);

    OrderRequest orderRequest = new OrderRequest(
        "배달해주세요",
        "01011223344",
        new OrderFoodRequest(fakeFoodId,2,1000)
    );

    saveOrder = orderRequest.convertToOrder(saveUser,saveStore);

    given(userService.findById(any())).willReturn(saveUser);
    doReturn(storeResponse).when(storeService).findById(fakeStoreId);
    given(orderRepository.save(any())).willReturn(saveOrder);
    given(orderFoodService.save(any(),any())).willReturn(fakeOrderFoodId);

    //when
    Long save = orderService.save(orderRequest, fakeUserId, fakeStoreId);

    //then
  }
}
