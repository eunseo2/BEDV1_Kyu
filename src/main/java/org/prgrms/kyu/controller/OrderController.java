package org.prgrms.kyu.controller;

import javassist.NotFoundException;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.ApiResponse;
import org.prgrms.kyu.dto.OrderFoodRequest;
import org.prgrms.kyu.dto.OrderRequest;
import org.prgrms.kyu.dto.StoreRequest;
import org.prgrms.kyu.dto.UserInfo;
import org.prgrms.kyu.entity.UserType;
import org.prgrms.kyu.service.OrderService;
import org.prgrms.kyu.service.SecurityService;
import org.prgrms.kyu.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;
  private final SecurityService securityService;
  private final UserService userService;

  @GetMapping("/stores/{storeId}/orders")
  public String newOrder(Model model, @PathVariable Long storeId, Authentication authentication) {
    if (!securityService.isAuthenticated()) return "redirect:/";
    UserType userType = userService.getUserType(
        ((UserDetails) authentication.getPrincipal()).getUsername());
    if(userType.equals(UserType.CUSTOMER)){
      UserInfo user = userService.getUser(
          ((UserDetails) authentication.getPrincipal()).getUsername());
      model.addAttribute("userInfo",user);
      model.addAttribute("storeId", storeId);
      model.addAttribute("orderForm", new OrderRequest());
      model.addAttribute("foodId",1);
      model.addAttribute("quantity",1);
      model.addAttribute("price",10000);
      return "/order/new-order";
    }else if(userType.equals(UserType.STORE_OWNER)){
      return "redirect:/";
    }
    return "/user/loginForm";
  }

  @PostMapping("/stores/{storeId}/orders")
  public String saveOrder(@PathVariable Long storeId, @ModelAttribute("orderForm") OrderRequest orderRequest,
      Authentication authentication)
      throws AuthenticationException, NotFoundException {
    if (!securityService.isAuthenticated()) throw new AuthenticationException();
    final UserInfo userInfo = userService.getUser(authentication.getName());
    orderService.save(orderRequest, userInfo.getId(), storeId);
    return "redirect:/";
  }

  @PostMapping("stores/{storeId}/orderFood")
  public String getFoodInfo(@PathVariable Long storeId, @RequestBody OrderFoodRequest orderFoodRequest,
      Authentication authentication, Model model)
      throws AuthenticationException {
    if (!securityService.isAuthenticated()) return "redirect:/";
    UserType userType = userService.getUserType(
        ((UserDetails) authentication.getPrincipal()).getUsername());
    if(userType.equals(UserType.CUSTOMER)){
      UserInfo user = userService.getUser(
          ((UserDetails) authentication.getPrincipal()).getUsername());
      model.addAttribute("userInfo",user);
      model.addAttribute("storeId", storeId);
      model.addAttribute("orderForm", new OrderRequest());
      model.addAttribute("orderFoods", orderFoodRequest);
      return "/order/new-order";
    }else if(userType.equals(UserType.STORE_OWNER)){
      return "redirect:/";
    }
    return "/user/loginForm";
  }

}
