package org.prgrms.kyu.controller.api;

import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.ApiResponse;
import org.prgrms.kyu.commons.S3Uploader;
import org.prgrms.kyu.dto.FoodRequest;
import org.prgrms.kyu.dto.FoodResponse;
import org.prgrms.kyu.service.FoodService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class FoodRestController {

    private final FoodService foodService;
    private final S3Uploader s3Uploader;

    @PostMapping("/stores/{storeId}/foods")
    public ApiResponse<Long> create(@RequestBody FoodRequest request, @PathVariable Long storeId) {
        return ApiResponse.ok(foodService.save(request, storeId));
    }

    @GetMapping("/stores/{storeId}/foods")
    public ApiResponse<List<FoodResponse>> getFoodList(@PathVariable Long storeId) {
        return ApiResponse.ok(foodService.getFoodList(storeId));
    }

    @PostMapping("/images")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }
}
