package org.prgrms.kyu.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.prgrms.kyu.dto.FoodRequest;
import org.prgrms.kyu.dto.FoodResponse;
import org.prgrms.kyu.entity.Food;
import org.prgrms.kyu.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FoodRestController.class)
@AutoConfigureRestDocs
class FoodRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FoodService foodService;

    @Test
    public void save() throws Exception {
        //Given
        FoodRequest request = FoodRequest.builder()
                .name("test name")
                .description("test description")
                .price(1000)
                .build();

        given(foodService.save(ArgumentMatchers.any(FoodRequest.class),eq(1L)))
                .willReturn(1L);

        //When //Then
        mockMvc.perform(post("/api/v1/stores/{storeId}/foods", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("food-save",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("foodName"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("foodDescription"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("foodPrice")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("StatusCode"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("serverDateTime"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("food Id")
                        )

                ));
    }

    @Test
    public void getFoodList() throws Exception {
        //Given
        FoodRequest request = FoodRequest.builder()
                .name("test name")
                .description("test description")
                .price(1000)
                .build();
        given(foodService.getFoodList(1L))
                .willReturn(List.of(new FoodResponse(new Food(request.getName(), request.getDescription(), request.getPrice(), "image link"))));

        //When //Then
        mockMvc.perform(get("/api/v1/stores/{storeId}/foods", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("food-save",
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("StatusCode"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("serverDateTime"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("List<FoodResponse>"),
                                fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("Food Name"),
                                fieldWithPath("data.[].description").type(JsonFieldType.STRING).description("Food Description"),
                                fieldWithPath("data.[].price").type(JsonFieldType.NUMBER).description("Food Price"),
                                fieldWithPath("data.[].image").type(JsonFieldType.STRING).description("Food Image Link")
                        )
                ));
    }
}
