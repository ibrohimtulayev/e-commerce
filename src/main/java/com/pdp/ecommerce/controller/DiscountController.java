package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Discount;
import com.pdp.ecommerce.model.dto.DiscountDto;
import com.pdp.ecommerce.service.DiscountService;
import com.pdp.ecommerce.service.aws.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/discount")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
    private final S3Service s3Service;

    @Operation(
            summary = "Retrieve discount event information",
            description = "Fetches the current discount event details available for users. This endpoint provides information related to any ongoing discounts or promotional events. It's useful for users to view and take advantage of available discounts before making a purchase."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the discount event information.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Discount.class
                                    )
                            )

                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving discount event information.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public HttpEntity<?> getDiscount() {
        return discountService.getDiscountEvent();
    }

    @Operation(
            summary = "Create a new discount event with an image",
            description = "Creates a new discount event with a mandatory image upload. This endpoint requires users to provide an image along with the details of the discount event, such as the title, description, and discount percentage. The image will be uploaded and associated with the discount event, which can then be used in promotions and displayed to users."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created the discount event.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Discount event successfully created!"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input provided. Ensure that the image and discount details are correctly formatted.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while creating the discount event.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public HttpEntity<?> createDiscountEvent(@RequestParam MultipartFile image, DiscountDto discountDto) {
        String imageUrl = s3Service.uploadFile(image);
        return discountService.createDiscount(imageUrl, discountDto);
    }

    @Operation(
            summary = "Retrieve the current active discount",
            description = "Fetches the details of the currently active discount event. This endpoint returns information about the single discount event that is currently available, including its title, description, discount percentage, and any associated terms. It is useful for users who want to take advantage of ongoing promotions."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the current active discount.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DiscountDto.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the current discount.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("current")
    public HttpEntity<?> getCurrentDiscount() {
        return discountService.findCurrentDiscount();
    }
}
