package org.prgrms.kyu.controller;


import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.prgrms.kyu.service.StoreService;
import org.prgrms.kyu.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;


    @GetMapping("/stores/{storeId}")
    public String findById(@PathVariable("storeId") Long storeId, Model model,  Authentication authentication) throws NotFoundException {
        model.addAttribute("userInfo",
                userService.getUser(((UserDetails) authentication.getPrincipal()).getUsername()));
        model.addAttribute("store", storeService.findById(storeId));
        return "store/detail-view";
    }
}
