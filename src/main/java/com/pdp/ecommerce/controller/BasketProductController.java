package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.service.BasketProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/basket-product")
public class BasketProductController {
    private final BasketProductService basketProductService;


    @Operation(
            summary = "Add a product to the basket",
            description = "Adds a specified amount of a product to the basket. This endpoint allows users to add a product to their shopping basket by providing the product details ID and the quantity they wish to add. The operation will create or update the `BasketProduct` entry with the given amount."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully added the product to the basket.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Basket product successfully added!"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input provided. Ensure the productDetailsId and amount are correctly formatted and valid.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product with the specified ID not found. Verify the productDetailsId provided.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while adding the product to the basket.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/add")
    public HttpEntity<?> addBasketProduct(@RequestParam UUID productDetailsId, @RequestParam Integer amount) {
        return basketProductService.add(productDetailsId,amount);
    }

    @Operation(
            summary = "Remove a product from the basket",
            description = "Removes a product from the basket using its unique identifier. This endpoint allows users to delete a specific product from their basket by providing the `id` of the `BasketProduct`. The operation will remove the entry corresponding to the given ID from the basket."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully removed the product from the basket.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Basket product successfully removed!"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided. Ensure the ID is correctly formatted and valid.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product with the specified ID not found in the basket. Verify the ID provided.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while removing the product from the basket.", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/remove")
    public HttpEntity<?> removeBasketProduct(@RequestParam UUID id) {
        return basketProductService.remove(id);

    }
}
