package org.prgrms.kyu.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.prgrms.kyu.dto.*;
import org.prgrms.kyu.entity.User;
import org.prgrms.kyu.repository.UserRepository;
import org.prgrms.kyu.service.OrderService;
import org.prgrms.kyu.service.SecurityService;
import org.prgrms.kyu.service.StoreService;
import org.prgrms.kyu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.naming.AuthenticationException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderRestController.class)
@AutoConfigureRestDocs
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @MockBean
    private StoreService storeService;

    @MockBean
    SecurityService securityService;

    Long userId;
    JoinRequest form;

    @BeforeEach
    public void userSetUp() throws AuthenticationException {
        this.userId = 1L;

        form = new JoinRequest(
                "test1@test.com",
                "1234",
                "user",
                "nick",
                "Seoul",
                "STORE_OWNER");

        given(userService.join(ArgumentMatchers.any(JoinRequest.class))).willReturn(1L);
        userService.join(form);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void save() throws Exception {
        Long userId = 1L;
        Long storeId = 1L;

        OrderRequest orderRequest = OrderRequest.builder()
                .address("test address")
                .phoneNumber("010-1234-5678")
                .orderFood(
                        OrderFoodRequest.builder()
                                .foodId(1L)
                                .quantity(1)
                                .price(1)
                                .build()
                )
                .build();

        given(this.userService.getUser(ArgumentMatchers.anyString())).willReturn(new UserInfo(new User(this.form)));
        given(this.securityService.isAuthenticated()).willReturn(true);
        given(orderService.save(ArgumentMatchers.any(OrderRequest.class), eq(userId), eq(storeId))).willReturn(1L);


        //then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/stores/{storeId}/orders", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-save",
                        requestFields(
                                fieldWithPath("address").type(JsonFieldType.STRING).description("address"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("phoneNumber"),
                                fieldWithPath("orderFood").type(JsonFieldType.OBJECT).description("oderFood"),
                                fieldWithPath("orderFood.foodId").type(JsonFieldType.NUMBER).description("orderFood.foodId"),
                                fieldWithPath("orderFood.quantity").type(JsonFieldType.NUMBER).description("orderFood.quantity"),
                                fieldWithPath("orderFood.price").type(JsonFieldType.NUMBER).description("orderFood.price")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("StatusCode"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("serverDateTime"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER).description("orderId")
                        )

                ));
    }
}
