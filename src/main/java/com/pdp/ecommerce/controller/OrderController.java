package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Address;
import com.pdp.ecommerce.model.dto.OrderDto;
import com.pdp.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Retrieve delivery destination information",
            description = "Fetches the delivery destination details necessary for placing an order. This endpoint returns information related to available delivery options, such as addresses, delivery methods, and any associated fees. This is useful for users to review and select their preferred delivery options before finalizing their order."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the delivery destination information.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Address.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving delivery destination information.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/delivery")
    public HttpEntity<?> orderPage() {
       return orderService.deliveryDestination();
    }

    @Operation(
            summary = "Place a new order",
            description = "Submits a new order based on the provided order details. The `OrderDto` object should include all necessary information such as product IDs, quantities, delivery address, and payment details. This endpoint processes the order and returns a confirmation of the order placement, including an order ID and status. It is used to finalize and place an order in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order successfully placed. Returns the order confirmation details.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Order created successfully!"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid order details provided. Ensure that all required fields in `OrderDto` are correctly specified and valid.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "One or more products in the order were not found or are out of stock.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while placing the order.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("make")
    public HttpEntity<?> makeOrder(@RequestBody OrderDto orderDto) {
        return orderService.make(orderDto);
    }

}
