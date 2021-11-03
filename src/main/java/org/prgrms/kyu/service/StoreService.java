package org.prgrms.kyu.service;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.dto.StoreRequest;
import org.prgrms.kyu.dto.StoreResponse;
import org.prgrms.kyu.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

  private final StoreRepository repository;
  private final UserService userService;


  public List<StoreResponse> findAll() {
    return repository
        .findAll()
        .stream()
        .map(StoreResponse::new)
        .collect(Collectors.toList());
  }

  public StoreResponse findById(Long id) throws NotFoundException {
    return repository
        .findById(id)
        .map(StoreResponse::new)
        .orElseThrow(() ->
            new NotFoundException("음식점 정보를 찾을 수 없습니다."));
  }

  public List<StoreResponse> findByUserId(Long userId) {
    return repository
        .findStoresByUserId(userId)
        .stream()
        .map(StoreResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public Long save(StoreRequest storeRequest, Long userId) throws AuthenticationException {
    return repository.save(
            storeRequest.convertToStore(
                userService.findById(userId))).getId();
  }

  @Transactional
  public void deleteAll(){
    repository.deleteAll();
  }



}
