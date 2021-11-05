package org.prgrms.kyu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.prgrms.kyu.entity.Food;

@Getter
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String image;

    public FoodResponse(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.description = food.getDescription();
        this.price = food.getPrice();
        this.image = food.getImage();
    }
}
