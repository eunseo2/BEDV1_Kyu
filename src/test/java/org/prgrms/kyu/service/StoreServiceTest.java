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
import org.prgrms.kyu.dto.StoreCreateRequest;
import org.prgrms.kyu.dto.StoreFindResponse;
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
  StoreService storeService;

  @Mock
  StoreRepository storeRepository;


  @Mock
  UserService userService;

  @Mock
  UserRepository userRepository;

  Long fakeUserId =  1L;
  long fakeStoreId = 1L;
  User saveUser;
  Store saveStore;

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


    StoreCreateRequest storeCreateRequest = new StoreCreateRequest(
        "맘스터치",
        "01011112222",
        "맘스터치입니다.",
        "Seoul");
    saveStore = storeCreateRequest.convertToStore(saveUser);

    given(userRepository.save(any())).willReturn(saveUser);
    given(storeRepository.save(any())).willReturn(saveStore);

    //when
    userRepository.save(saveUser);
    storeService.save(storeCreateRequest,fakeUserId);

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
    StoreFindResponse storeFindResponse = new StoreFindResponse(saveStore);
    given(storeRepository.findById(fakeStoreId)).willReturn(Optional.ofNullable(saveStore));
    doReturn(storeFindResponse).when(storeService).findById(fakeStoreId);

    //when
    StoreFindResponse findStore = storeService.findById(fakeStoreId);

    //then
    Optional<Store> store = storeRepository.findById(fakeStoreId);
    assertThat(findStore,allOf(
        notNullValue(),
        samePropertyValuesAs(new StoreFindResponse(store.get()))));
  }



  @Test
  @DisplayName("모든 음식점을 찾을 수 있다.")
  public void findAllTest() throws AuthenticationException {
    //given

    Long fakeStoreId = 2L;
    StoreCreateRequest storeCreateRequest = new StoreCreateRequest(
        "맘스터치",
        "01011112222",
        "맘스터치입니다.",
        "Seoul");

    Store saveStore2 = storeCreateRequest.convertToStore(saveUser);
    given(storeRepository.save(any())).willReturn(saveStore2);
    storeService.save(storeCreateRequest,fakeUserId);


    StoreFindResponse storeFindResponse = new StoreFindResponse(saveStore);
    StoreFindResponse storeFindResponse2 = new StoreFindResponse(saveStore2);
    doReturn(List.of(storeFindResponse, storeFindResponse2)).when(storeService).findAll();
    given(storeRepository.findAll()).willReturn(List.of(saveStore, saveStore2));

    //when
    List<StoreFindResponse> all = storeService.findAll();

    //then
    List<StoreFindResponse> findAll = storeRepository.findAll().stream()
        .map((StoreFindResponse::new))
        .collect(Collectors.toList());

    assertThat(all,allOf(
        notNullValue(),
        hasSize(2)));
  }

}
