package org.prgrms.kyu.controller.api;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.ApiResponse;
import org.prgrms.kyu.dto.StoreRequest;
import org.prgrms.kyu.dto.StoreResponse;
import org.prgrms.kyu.service.SecurityService;
import org.prgrms.kyu.service.StoreService;
import org.prgrms.kyu.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StoreRestController {

  private final StoreService storeService;
  private final SecurityService securityService;
  private final UserService userService;

  @GetMapping("/stores")
  public ApiResponse<List<StoreResponse>> getAllStore(){
    return ApiResponse.ok(storeService.findAll());
  }

  @GetMapping("/stores/{id}")
  public ApiResponse<StoreResponse> getOneStore(@PathVariable Long id)
      throws NotFoundException {
    return ApiResponse.ok(storeService.findById(id));
  }

  @PostMapping("/stores")
  public ApiResponse<Long> saveStore(@RequestBody StoreRequest storeRequest, Authentication authentication)
      throws AuthenticationException {
    if (!securityService.isAuthenticated()) throw new AuthenticationException();
    return ApiResponse.ok(storeService.save(storeRequest,
            userService.getUser(authentication.getName()).getId()));
  }



}
