package org.prgrms.kyu.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.prgrms.kyu.dto.FoodRequest;
import org.prgrms.kyu.dto.FoodResponse;
import org.prgrms.kyu.entity.Food;
import org.prgrms.kyu.entity.Store;
import org.prgrms.kyu.repository.FoodRepository;
import org.prgrms.kyu.repository.StoreRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class FoodServiceTest {
    @InjectMocks
    private FoodService foodService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private StoreRepository storeRepository;

    private Long fakeStoreId;
    private Store store;
    private Food food;
    private FoodRequest foodRequest;

    @BeforeEach
    void setUp() {
        this.fakeStoreId = 1L;

        this.store = Store.builder()
                .id(fakeStoreId)
                .description("test Description")
                .location("test location")
                .telephone("010")
                .build();


        this.foodRequest = FoodRequest.builder()
                .name("test name")
                .description("test description")
                .price(1000)
                .build();

        this.food = FoodRequest.convertToFood(this.foodRequest);
        this.food.update(this.store);

        Long fakeFoodId = 1L;
        ReflectionTestUtils.setField(this.food, "id", fakeFoodId);


        given(this.foodRepository.save(any()))
                .willReturn(this.food);
        given(this.storeRepository.findById(this.fakeStoreId))
                .willReturn(Optional.ofNullable(this.store));
        given(this.foodRepository.findById(fakeFoodId))
                .willReturn(Optional.ofNullable(this.food));
        given(foodRepository.findAllByStoreIdOrderById(this.fakeStoreId))
                .willReturn(List.of(this.food));
    }

    @AfterEach
    void tearDown() {
        foodRepository.deleteAll();
    }

    @Test
    void save() {
        //Given

        //When
        Long newFoodId = foodService.save(this.foodRequest, fakeStoreId);

        //Then
        Food findFood = foodRepository.findById(newFoodId).get();

        assertEquals(food.getId(), findFood.getId());
    }

    @Test
    void getFoodList() {
        //Given

        //When
        final List<FoodResponse> foodList = foodService.getFoodList(fakeStoreId);

        //Then
        Assertions.assertThat(foodList.size()).isEqualTo(1);
    }
}
