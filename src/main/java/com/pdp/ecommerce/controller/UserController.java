package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.model.projection.ProductProjection;
import com.pdp.ecommerce.model.projection.UserOrderProjection;
import com.pdp.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Retrieve user search history",
            description = "This endpoint retrieves the search history of the currently logged-in user. The search history includes a list of all search queries made by the user. The response will include the details of each search query, such as the search keywords and timestamps of when the searches were performed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the user's search history.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = String.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the search history.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/searchHistory")
    public HttpEntity<?> getSearchHistory() {
        return userService.getUserSearchHistory();
    }

    @Operation(
            summary = "Retrieve user wishlist.",
            description = "This endpoint retrieves the wishlist of the currently logged-in user. The wishlist includes a list of products that the user has saved for future reference or purchase. Each item in the wishlist will include product details such as name, description, and price."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the user's wishlist.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = ProductProjection.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the wishlist.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/wishlist")
    public HttpEntity<?> getUserWishlist() {
        return userService.getWishlist();
    }

    @Operation(
            summary = "Add a product to the user's wishlist",
            description = "This endpoint allows the user to add a product to their wishlist. The `productId` parameter specifies the product to be added. If the product is successfully added to the wishlist, a confirmation response will be returned. If the product is already in the wishlist, the response will indicate that the item is already present."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully added the product to the user's wishlist.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Product added to wishlist successfully."
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters. Ensure the `productId` is a valid UUID.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product not found. The product with the provided ID does not exist.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while adding the product to the wishlist.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("wishlist/add")
    public HttpEntity<?> addToWishlist(@RequestParam UUID productId) {
        return userService.addToWishlist(productId);
    }

    @Operation(
            summary = "Clear the user's wishlist",
            description = "This endpoint allows the user to clear all items from their wishlist. Once the request is processed, the wishlist will be empty. If the operation is successful, a confirmation message will be returned. If there is a conflict such as an expired token or any other issue, appropriate error responses will be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully cleared all items from the user's wishlist.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Wishlist cleared successfully."
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflict error. The request could not be processed due to a conflict such as an expired token or other similar issues.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while clearing the wishlist.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("wishlist/clear")
    public HttpEntity<?> clearWishlist() {
        return userService.clearWishlist();
    }

    @Operation(
            summary = "Remove a product from the user's wishlist",
            description = "This endpoint allows the user to remove a specific product from their wishlist. You need to provide the `productId` of the product to be removed. If the product is successfully removed, a confirmation message will be returned. If the product is not found in the wishlist, or if there are any conflicts or errors, appropriate error responses will be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully removed the product from the user's wishlist.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Product removed from wishlist successfully."
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad request. Ensure the `productId` is a valid UUID and the request is correctly formatted.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product not found. The specified `productId` does not exist in the user's wishlist.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflict error. The request could not be processed due to a conflict such as an expired token or other similar issues.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while removing the product from the wishlist.", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("wishlist/{productId}")
    public HttpEntity<?> removeFromWishlist(@PathVariable UUID productId) {
        return userService.removeFavouriteProduct(productId);
    }

    @Operation(
            summary = "Change the user's address",
            description = "Allows the user to update their address using latitude and longitude coordinates. Provide `lat` (latitude) and `lon` (longitude) as request parameters to set the new address. A successful update will return a confirmation message. If there are issues with the coordinates or other errors, appropriate error responses will be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address successfully updated.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Address updated successfully."
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters. Ensure `lat` and `lon` are valid numeric values and within acceptable ranges.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated to change the address.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflict error. The request could not be processed due to a conflict such as an expired token or other similar issues.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while updating the address.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/change-address")
    public HttpEntity<?> changeAddress(@RequestParam Double lat, @RequestParam Double lon) {
        return userService.changeAddress(lat, lon);
    }

    @Operation(
            summary = "Retrieve the user's orders",
            description = "Fetches a list of orders placed by the authenticated user. This endpoint returns detailed information about each order, including order status, date, and items. The response will provide a comprehensive overview of all orders associated with the user account. If there are issues retrieving the orders or the user is not authenticated, appropriate error responses will be returned."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of user orders.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            description = "List of orders placed by the user",
                                            implementation = UserOrderProjection.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated to retrieve their orders.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No orders found for the user. The user may not have placed any orders yet.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflict error. The request could not be processed due to a conflict such as an expired token or other similar issues.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the user's orders.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/orders")
    public HttpEntity<?> getUserOrders() {
        return userService.getOrders();
    }
}

