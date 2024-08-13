package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Product;
import com.pdp.ecommerce.model.dto.ProductCreateDto;
import com.pdp.ecommerce.model.dto.SearchDto;
import com.pdp.ecommerce.model.projection.CategoryProductProjection;
import com.pdp.ecommerce.model.projection.RatingAndReviewsProjection;
import com.pdp.ecommerce.service.ProductService;
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

import java.util.UUID;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final S3Service s3Service;

    @Operation(
            summary = "Search for products.",
            description = "This endpoint searches for products based on the provided keyword and gender. Returns a list of matching products if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Search operation successful. Returns a list of matching products.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Product.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation errors. Check the provided search criteria.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred during the search operation.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/search")
    public HttpEntity<?> findProduct(@RequestBody SearchDto searchDto) {
        return productService.findByNameAndGender(searchDto);
    }

    @Operation(
            summary = " Recommended products",
            description = "This endpoint returns recommended 4 products based on the user's random favourite product if user doesn't have favourite product it will return random 4 products."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Random products successful. Returns a list of matching recommended products.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Product.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation errors. Check the provided search criteria.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred during the search operation.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/recommendation")
    public HttpEntity<?> getRecommendation() {
        return productService.recommendProducts();
    }

    @Operation(
            summary = "Retrieve paged products by category",
            description = "Fetches a paginated list of products based on the specified category. Use both `page` and `categoryName` parameters to navigate through the results, starting from 1. If the category name is not found or no products exist for the given category, the response will be an empty list."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fetches a paginated list of products based on the specified category. You must provide both the `page` parameter to navigate through the results, starting from 1, and the `categoryName` to filter the products. An empty list will be returned if no products match the category or if the category does not exist.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Product.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters, such as an invalid page number or missing category name.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while fetching products.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/category-products")
    public HttpEntity<?> getPagedProductsByCategory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam String categoryName) {
        return productService.getPagedProductsByCategory(page, categoryName);
    }

    @Operation(
            summary = "Filter products based on category and criteria.",
            description = """
                    Retrieves a list of products filtered by the specified criteria. The `categoryId` is required to determine the category of products to filter. The `filterBy` parameter specifies the filtering criteria with the following options:
                    - `newly_added`: Filters products by their addition date, showing the newest products first.
                    - `best_rating`: Filters products based on their rating, showing the highest-rated products first.
                    - Any other value: Filters products based on the category ID, returning products in the specified category without additional sorting.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of filtered products.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Product.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters. Ensure `categoryId` is a valid UUID and `filterBy` is correctly specified. Valid `filterBy` values are 'newly_added', 'best_rating', or other valid filter criteria.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while filtering products.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/filter")
    public HttpEntity<?> getFilteredProducts(@RequestParam UUID categoryId, @RequestParam String filterBy) {
        return productService.filterBy(categoryId, filterBy);
    }

    @Operation(
            summary = "Retrieve detailed information about a product",
            description = "Fetches detailed information about a product using its unique identifier. The `id` parameter is required and should be a valid UUID. If the product is found, the details of the product will be returned. If the product is not found or an error occurs, an appropriate error message will be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the product details.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = Product.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid product ID format. Ensure the `id` parameter is a valid UUID.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product not found with the provided ID.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the product details.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("{id}")
    public HttpEntity<?> getProduct(@PathVariable UUID id) {
        return productService.getDetailedProductById(id);
    }

    @Operation(
            summary = "Retrieve  products ratings and reviews grouping by user",
            description = "Fetches the ratings and reviews for a specific product identified by its `productId`. The `productId` parameter is required and should be a valid UUID. This endpoint returns the product's aggregated rating and user reviews. If the product is found, the details will be provided. If there is an error, an appropriate error message will be returned."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the product's ratings and reviews grouping by user.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = RatingAndReviewsProjection.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid product ID format. Ensure the `productId` parameter is a valid UUID.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product not found with the provided ID.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the product's ratings and reviews.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("rating-review")
    public HttpEntity<?> getRatingReview(@RequestParam UUID productId) {
        return productService.getRatingAndReviews(productId);
    }

    @Operation(
            summary = "Get product description",
            description = "Retrieves the description of a specific product based on the provided `productId`. The `productId` should be a valid UUID representing the product. The response will be a string containing the product's description."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the product description.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Product description."
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid product ID format. Ensure the `productId` parameter is a valid UUID.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Product not found with the provided ID.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the product description.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("description")
    public HttpEntity<?> getProductDescription(@RequestParam UUID productId) {
        return productService.getProductDescription(productId);
    }

    @Operation(
            summary = "Add a new product",
            description = "Creates a new product based on the provided details. The `ProductCreateDto` contains the necessary information for the product, such as name, description, and price. Optionally, an image file can be uploaded for the product. If an image is provided, it will be uploaded to the S3 storage, and the URL will be used for the product's image. The endpoint returns a response indicating the success of the product creation process."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product successfully created. Includes the product details and the URL of the uploaded image if provided.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Product successfully created."
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data. Ensure that the `ProductCreateDto` contains all required fields and that the image file, if provided, is correctly formatted.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while creating the product or uploading the image.", content = @Content(mediaType = "application/json"))
    })

    @PostMapping
    public HttpEntity<?> addProduct(@RequestParam MultipartFile image, ProductCreateDto productCreateDto) {
        String imageUrl = s3Service.uploadFile(image);
        return productService.createProduct(productCreateDto, imageUrl);
    }

    @Operation(
            summary = "Retrieve all products with their categories",
            description = "Fetches a list of all products along with their associated categories. This endpoint returns detailed information about each product, including its category. The response will be a list of products, where each product includes category details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the list of all products with their categories.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = CategoryProductProjection.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No products found. Ensure that the database contains products with associated categories.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving the products.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("findAll")
    public HttpEntity<?> findAllProducts() {
        return productService.findAllWithCategory();
    }
}
