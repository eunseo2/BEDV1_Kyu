package org.prgrms.kyu.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javassist.NotFoundException;
import javax.naming.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.prgrms.kyu.dto.JoinRequest;
import org.prgrms.kyu.dto.StoreRequest;
import org.prgrms.kyu.dto.StoreResponse;
import org.prgrms.kyu.entity.Store;
import org.prgrms.kyu.entity.User;
import org.prgrms.kyu.repository.StoreRepository;
import org.prgrms.kyu.repository.UserRepository;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

  @Spy
  @InjectMocks
  private StoreService storeService;

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  private Long fakeUserId =  1L;
  private long fakeStoreId = 1L;
  private User saveUser;
  private Store saveStore;

  @BeforeEach
  public void setUp() throws AuthenticationException {
    //given
    JoinRequest joinRequest = new JoinRequest(
        "test1@test.com",
        "1234",
        "user",
        "nick",
        "Seoul",
        "STORE_OWNER");
    saveUser = new User(joinRequest);


    StoreRequest storeRequest = new StoreRequest(
        "맘스터치",
        "01011112222",
        "맘스터치입니다.",
        "https://kyu-nation.s3.ap-northeast-2.amazonaws.com/static/1ec58c2b-b52e-4ac4-a44a-5a2795a3b879%ED%9B%84%EB%9D%BC%EC%9D%B4%EB%93%9C.jpg",
        "Seoul");
    saveStore = storeRequest.convertToStore(saveUser);

    given(userRepository.save(any())).willReturn(saveUser);
    given(storeRepository.save(any())).willReturn(saveStore);

    //when
    userRepository.save(saveUser);
    storeService.save(storeRequest,fakeUserId);

  }


  @Test
  @DisplayName("음식점을 생성할 수 있다.")
  public void storeSaveTest() {
    //given
    ReflectionTestUtils.setField(saveUser, "id", fakeUserId);
    ReflectionTestUtils.setField(saveStore, "id", fakeStoreId);
    given(storeRepository.findById(fakeStoreId)).willReturn(Optional.ofNullable(saveStore));

    //when //then
    Optional<Store> findStore = storeRepository.findById(fakeStoreId);

    assertThat(findStore.get(),allOf(notNullValue(),samePropertyValuesAs(saveStore)));
  }



  @Test
  @DisplayName("id로 음식점을 찾을 수 있다.")
  public void findByIdTest() throws NotFoundException {
    //given
    StoreResponse storeResponse = new StoreResponse(saveStore);
    given(storeRepository.findById(fakeStoreId)).willReturn(Optional.ofNullable(saveStore));
    doReturn(storeResponse).when(storeService).findById(fakeStoreId);

    //when
    StoreResponse findStore = storeService.findById(fakeStoreId);

    //then
    Optional<Store> store = storeRepository.findById(fakeStoreId);
    assertThat(findStore,allOf(
        notNullValue(),
        samePropertyValuesAs(new StoreResponse(store.get()))));
  }



  @Test
  @DisplayName("모든 음식점을 찾을 수 있다.")
  public void findAllTest() throws AuthenticationException {
    //given

    Long fakeStoreId = 2L;
    StoreRequest storeCreateRequest = new StoreRequest(
        "맘스터치",
        "01011112222",
        "맘스터치입니다.",
        "https://kyu-nation.s3.ap-northeast-2.amazonaws.com/static/1ec58c2b-b52e-4ac4-a44a-5a2795a3b879%ED%9B%84%EB%9D%BC%EC%9D%B4%EB%93%9C.jpg",
        "Seoul");

    Store saveStore2 = storeCreateRequest.convertToStore(saveUser);
    given(storeRepository.save(any())).willReturn(saveStore2);
    storeService.save(storeCreateRequest,fakeUserId);


    StoreResponse storeFindResponse = new StoreResponse(saveStore);
    StoreResponse storeFindResponse2 = new StoreResponse(saveStore2);
    doReturn(List.of(storeFindResponse, storeFindResponse2)).when(storeService).findAll();
    given(storeRepository.findAll()).willReturn(List.of(saveStore, saveStore2));

    //when
    List<StoreResponse> all = storeService.findAll();

    //then
    List<StoreResponse> findAll = storeRepository.findAll().stream()
        .map((StoreResponse::new))
        .collect(Collectors.toList());

    assertThat(all,allOf(
        notNullValue(),
        hasSize(2)));
  }

}
