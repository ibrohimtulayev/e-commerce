package com.pdp.ecommerce.controller;

import com.pdp.ecommerce.entity.Category;
import com.pdp.ecommerce.model.dto.ProductCreateDto;
import com.pdp.ecommerce.model.projection.CategoryProjection;
import com.pdp.ecommerce.service.CategoryService;
import com.pdp.ecommerce.service.aws.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final S3Service s3Service;

    @Operation(
            summary = "Retrieve 4 random categories",
            description = "Fetches a random selection of four categories from the available list. This endpoint is useful for displaying a diverse set of categories to users, potentially for featured sections or to encourage exploration of different product types."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved four random categories.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = CategoryProjection.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving random categories.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/random")
    public HttpEntity<?> getRandomCategories() {
        return ResponseEntity.ok(categoryService.getRandomCategories());
    }

    @Operation(
            summary = "Retrieve all categories",
            description = "Fetches a list of all categories available in the system, including both parent categories and subcategories. This endpoint is useful for displaying the complete category structure, allowing users to view and navigate through all available categories."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all categories.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = CategoryProjection.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving categories.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/all")
    public HttpEntity<?> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(
            summary = "Retrieve all subcategories",
            description = "Fetches a list of all subcategories available in the system. This endpoint returns categories that are nested under parent categories, providing detailed information about subcategories. This is useful for users who want to explore or filter products by specific subcategories."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all subcategories.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = Category.class
                                    )
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while retrieving subcategories.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("subcategories")
    public HttpEntity<?> getSubCategories() {
        return categoryService.getAllSubcategories();
    }

    @Operation(
            summary = "Create a new category",
            description = "Creates a new category in the system. This endpoint allows users to provide details for the new category, including an optional image. The image will be uploaded and associated with the new category if provided. The endpoint returns the created category with its details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created the new category.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "Category successfully created!"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input provided. Ensure the category details are correctly formatted and the image is in a valid format.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized access. Ensure the user is authenticated and has the necessary permissions.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Token has expired. Please authenticate again to access this resource.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error occurred while creating the category.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public HttpEntity<?> createCategory(@RequestParam(required = false) MultipartFile image, ProductCreateDto.CategoryDto categoryDto) {
        String imageUrl = s3Service.uploadFile(image);
        return categoryService.createCategory(imageUrl, categoryDto);
    }

}
