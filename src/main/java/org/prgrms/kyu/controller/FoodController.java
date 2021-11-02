package org.prgrms.kyu.controller;

import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.commons.S3Uploader;
import org.prgrms.kyu.dto.FoodRequest;
import org.prgrms.kyu.service.FoodService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final SecurityService securityService;
    private final UserService userService;
    private final S3Uploader s3Uploader;

    @PostMapping("/stores/{storeId}/new-food")
    public String newFoodPage(@PathVariable("storeId") Long storeId, Model model) {
        model.addAttribute("foodForm", new FoodRequest());
        return "food/new-food";
    }

    @PostMapping("/stores/{storeId}/foods")
    public String newFood(@PathVariable("storeId") Long storeId, @ModelAttribute("foodForm") FoodRequest request) {
        foodService.save(request, storeId);
        return "redirect:/stores/" + storeId;
    }

    @GetMapping("/stores/{storeId}/foods")
    public String getFoodList(Model model, @PathVariable Long storeId, Authentication authentication) {
        if (securityService.isAuthenticated()) {
            model.addAttribute("userInfo",
                    userService.getUser(((UserDetails) authentication.getPrincipal()).getUsername()));
        }
        model.addAttribute("storeId", storeId);
        return "/food/food-list";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

}
