package org.prgrms.kyu.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.prgrms.kyu.dto.JoinRequest;
import org.prgrms.kyu.dto.StoreRequest;
import org.prgrms.kyu.dto.StoreResponse;
import org.prgrms.kyu.dto.UserInfo;
import org.prgrms.kyu.entity.User;
import org.prgrms.kyu.repository.UserRepository;
import org.prgrms.kyu.service.SecurityService;
import org.prgrms.kyu.service.StoreService;
import org.prgrms.kyu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(StoreRestController.class)
class StoreRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private StoreService storeService;

  @MockBean
  private UserService userService;

  @MockBean
  UserRepository userRepository;

  @MockBean
  SecurityService securityService;

  Long userId;
  JoinRequest form;
  StoreRequest storeRequest;
  StoreRequest storeRequest2;

  @BeforeEach
  public void userSetUp(){
    //given
    this.userId = 1L;
    form = new JoinRequest(
        "test1@test.com",
        "1234",
        "user",
        "nick",
        "Seoul",
        "STORE_OWNER");

    storeRequest =
        new StoreRequest(
            "momstouch",
            "01011112222",
            "i am momstouch.",
            "https://kyu-nation.s3.ap-northeast-2.amazonaws.com/static/1ec58c2b-b52e-4ac4-a44a-5a2795a3b879%ED%9B%84%EB%9D%BC%EC%9D%B4%EB%93%9C.jpg",
            "Seoul");

    storeRequest2 =
        new StoreRequest(
            "momstouch2",
            "01011113333",
            "i am momstouch2.",
            "https://kyu-nation.s3.ap-northeast-2.amazonaws.com/static/1ec58c2b-b52e-4ac4-a44a-5a2795a3b879%ED%9B%84%EB%9D%BC%EC%9D%B4%EB%93%9C.jpg",
            "Seoul");

    given(userService.join(ArgumentMatchers.any(JoinRequest.class))).willReturn(1L);
    userService.join(form);
  }



  @Test
  @DisplayName("음식점을 생성할 수 있다.")
  @WithMockUser(roles = "STORE_OWNER")
  public void saveStoreTest() throws Exception {
    //given //when
    given(this.userService.getUser(ArgumentMatchers.anyString())).willReturn(new UserInfo(new User(this.form)));
    given(this.securityService.isAuthenticated()).willReturn(true);
    given(this.storeService.save(ArgumentMatchers.any(StoreRequest.class), eq(this.userId))).willReturn(1L);

    //then
    mockMvc.perform(post("/api/v1/stores")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(storeRequest)))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("store-save",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                fieldWithPath("telephone").type(JsonFieldType.STRING).description("telephone"),
                fieldWithPath("description").type(JsonFieldType.STRING).description("description"),
                fieldWithPath("image").type(JsonFieldType.STRING).description("image"),
                fieldWithPath("location").type(JsonFieldType.STRING).description("location")
            ),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                fieldWithPath("data").type(JsonFieldType.NUMBER).description("게시글 id"),
                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("응답시간")
            )
        ));
  }

  @Test
  @DisplayName("모든 음식점을 찾을 수 있다.")
  public void getAllStoreTest() throws Exception {
    //given
    given(storeService.save(ArgumentMatchers.any(StoreRequest.class), eq(this.userId))).willReturn(1L);
    Long storeId1 = storeService.save(storeRequest, this.userId);
    given(storeService.save(ArgumentMatchers.any(StoreRequest.class), eq(this.userId))).willReturn(2L);
    Long storeId2 = storeService.save(storeRequest2, this.userId);

    List<StoreResponse> list = List.of(
        new StoreResponse(
            storeId1,
            storeRequest.getName(),
            storeRequest.getTelephone(),
            storeRequest.getDescription(),
            storeRequest.getImage(),
            storeRequest.getLocation()
        ),
        new StoreResponse(
            storeId2,
            storeRequest2.getName(),
            storeRequest2.getTelephone(),
            storeRequest2.getDescription(),
            storeRequest.getImage(),
            storeRequest2.getLocation()
        ));


    //when
    given(storeService.findAll()).willReturn(list);

    //then
    mockMvc.perform(get("/api/v1/stores")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("store-find-all",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("응답시간"),

                fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("데이터"),
                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("id"),
                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("name"),
                fieldWithPath("data[].telephone").type(JsonFieldType.STRING).description("telephone"),
                fieldWithPath("data[].description").type(JsonFieldType.STRING).description("description"),
                fieldWithPath("data[].image").type(JsonFieldType.STRING).description("image"),
                fieldWithPath("data[].location").type(JsonFieldType.STRING).description("location")
            )
        ));
  }



  @Test
  @DisplayName("id로 가게 정보를 검색할 수 있다.")
  @WithMockUser(roles = "STORE_OWNER")
  public void getOneStoreTest() throws Exception {
    //given
    given(storeService.save(ArgumentMatchers.any(StoreRequest.class), eq(this.userId))).willReturn(1L);
    Long storeId = storeService.save(storeRequest, this.userId);

    StoreResponse storeResponse = new StoreResponse(
        storeId,
        storeRequest.getName(),
        storeRequest.getTelephone(),
        storeRequest.getDescription(),
        storeRequest.getImage(),
        storeRequest.getLocation()
    );

    //when
    given(storeService.findById(ArgumentMatchers.any(Long.class))).willReturn(storeResponse);

    //then
    mockMvc.perform(get("/api/v1/stores/{id}",storeId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("store-find-by-id",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("응답시간"),

                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("id"),
                fieldWithPath("data.name").type(JsonFieldType.STRING).description("name"),
                fieldWithPath("data.telephone").type(JsonFieldType.STRING).description("telephone"),
                fieldWithPath("data.description").type(JsonFieldType.STRING).description("description"),
                fieldWithPath("data.image").type(JsonFieldType.STRING).description("image"),
                fieldWithPath("data.location").type(JsonFieldType.STRING).description("location")
            )
        ));
  }

}
