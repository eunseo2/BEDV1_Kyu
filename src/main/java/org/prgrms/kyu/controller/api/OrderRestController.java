package org.prgrms.kyu.controller.api;

import javassist.NotFoundException;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.ApiResponse;
import org.prgrms.kyu.dto.OrderFoodRequest;
import org.prgrms.kyu.dto.OrderRequest;
import org.prgrms.kyu.service.OrderService;
import org.prgrms.kyu.service.SecurityService;
import org.prgrms.kyu.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderRestController {

  private final OrderService orderService;
  private final SecurityService securityService;
  private final UserService userService;

  @PostMapping("stores/{storeId}/orders")
  public ApiResponse<Long> saveOrder(@RequestBody OrderRequest orderRequest,@PathVariable Long storeId, Authentication authentication)
      throws AuthenticationException, NotFoundException {
    if (!securityService.isAuthenticated()) throw new AuthenticationException();
    return ApiResponse.ok(orderService.save(orderRequest,
        userService.getUser(authentication.getName()).getId(),storeId));
  }

}
