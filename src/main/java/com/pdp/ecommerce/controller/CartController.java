package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.model.projection.ProductDetailsProjection;
import com.pdp.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cart")
public class CartController {
    private final CartService cartService;

    @Operation(
            summary = "Retrieve all items in the cart",
            description = "Fetches a list of all items currently in the user's cart. This endpoint provides detailed information about the products in the cart, including quantities and other relevant details. It is useful for displaying the contents of the cart to the user for review or checkout."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all items in the cart.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = ProductDetailsProjection.class
                                    )
                            )

                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving cart items.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public HttpEntity<?> cart() {
        return cartService.showAll();
    }
}
