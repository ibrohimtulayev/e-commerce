# E-commerce Application

This project is a fully-featured E-commerce platform developed using Java and Spring Boot. The application provides RESTful APIs for managing users, products, orders, and more, and integrates with Amazon RDS for database management and Amazon S3 for file storage.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Detailed Usage Guide](#detailed-usage-guide)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Configuration](#configuration)
- [License](#license)

## Features
- **User Management**: Allows users to register, log in, and manage their accounts securely.
- **Product Management**: Admins can create, update, and manage products, while users can browse and search products.
- **Order Processing**: Handles the creation and management of orders, including payment and delivery options.
- **Shopping Cart & Basket**: Users can add products to their basket, view and modify their cart before checkout.
- **Discount Management**: Supports the creation and application of discount codes and events.
- **Wishlist**: Users can save products to their wishlist for future purchases.
- **Category Management**: Allows products to be organized into categories and subcategories.
- **File Storage**: Manages file uploads, such as product images, using Amazon S3.
- **Database Management**: Utilizes Amazon RDS for reliable and scalable database operations.
- **Comprehensive API Documentation**: Provides detailed API documentation via Swagger.

## Architecture
The application is built using a microservices-based architecture, where each service is designed to handle specific business logic. The services communicate with each other using REST APIs. The key components include:
- **Auth Service**: Handles user authentication, registration, and account verification.
- **Product Service**: Manages product-related operations, including creation, retrieval, and categorization.
- **Order Service**: Oversees order management, from creation to delivery.
- **Discount Service**: Manages discount codes and promotional events.
- **Category Service**: Organizes products into categories for easier navigation and search.
- **Basket Product Service**: Manages the user's shopping cart or basket, including adding and removing items.
- **S3 Service**: Handles file uploads and storage in Amazon S3 buckets.

## API Endpoints
Below is a comprehensive list of the API endpoints provided by the application:

### Auth Controller
- `POST /api/auth/register`: Register a new user with email and password.
- `POST /api/auth/confirmation`: Confirm user registration with a code sent to their email.
- `POST /api/auth/login`: Authenticate a user and obtain a JWT token.

### Product Controller
- `POST /api/product/search`: Search for products based on various filters such as category, price range, brand, etc.
- `GET /api/product/recommendation`: Retrieve product recommendations tailored to the user.
- `GET /api/product/category-products`: Fetch paginated products under a specific category.
- `POST /api/product`: Add a new product to the catalog (Admin only).
- `GET /api/product/{productId}`: Retrieve details of a specific product by its ID.
- `PUT /api/product/{productId}`: Update an existing product's details (Admin only).
- `DELETE /api/product/{productId}`: Delete a product from the catalog (Admin only).

### Order Controller
- `POST /api/order/make`: Create a new order for the authenticated user.
- `GET /api/order/delivery`: Retrieve available delivery options for placing an order.
- `GET /api/order/user-orders`: Fetch a list of orders placed by the authenticated user.
- `GET /api/order/{orderId}`: Retrieve details of a specific order by its ID.

### Discount Controller
- `GET /api/discount`: Retrieve a list of active discount events.
- `POST /api/discount`: Create a new discount event (Admin only).
- `GET /api/discount/{discountId}`: Retrieve details of a specific discount event by its ID.
- `PUT /api/discount/{discountId}`: Update an existing discount event (Admin only).
- `DELETE /api/discount/{discountId}`: Delete a discount event (Admin only).

### Category Controller
- `GET /api/category`: Retrieve a list of all product categories.
- `POST /api/category`: Add a new product category (Admin only).
- `GET /api/category/{categoryId}`: Retrieve details of a specific category by its ID.
- `PUT /api/category/{categoryId}`: Update an existing product category (Admin only).
- `DELETE /api/category/{categoryId}`: Delete a product category (Admin only).

### Basket Product Controller
- `POST /api/basket-product/add`: Add a product to the user's basket.
- `DELETE /api/basket-product/remove`: Remove a product from the user's basket.
- `GET /api/basket-product`: Retrieve the contents of the user's basket.

### Wishlist Controller
- `POST /api/wishlist/add`: Add a product to the user's wishlist.
- `DELETE /api/wishlist/remove`: Remove a product from the user's wishlist.
- `GET /api/wishlist`: Retrieve the user's wishlist.

### File Upload Controller
- `POST /api/files/upload`: Upload a file to Amazon S3.
- `GET /api/files/{filename}`: Retrieve a file from Amazon S3.

## Detailed Usage Guide

### User Registration and Login
- **Registration**: Users can register by sending a POST request to `/api/auth/register`. They will receive a confirmation code via email, which they must submit to `/api/auth/confirmation` to activate their account.
- **Login**: Once registered, users can log in by sending a POST request to `/api/auth/login`. Upon successful authentication, a JWT token will be returned, which must be included in the Authorization header for subsequent requests.

### Managing Products
- **Adding Products**: Admins can add new products by sending a POST request to `/api/product`.
- **Searching Products**: Users can search for products by sending a POST request to `/api/product/search` with various filters like category, price range, etc.
- **Product Recommendations**: Users can get personalized product recommendations via the `/api/product/recommendation` endpoint.

### Placing Orders
- **Creating an Order**: Users can place an order by sending a POST request to `/api/order/make` with the required details such as product ID, quantity, and payment method.
- **Viewing Orders**: Users can view their order history by sending a GET request to `/api/order/user-orders`.

## Technologies Used
- **Java 17**: The core programming language used for the application.
- **Spring Boot**: A framework for building and deploying Java applications with minimal configuration.
- **Amazon RDS**: A relational database service for PostgreSQL database management.
- **Amazon S3**: A cloud-based object storage service for storing and retrieving files, such as images and documents.
- **Swagger**: A tool for generating interactive API documentation.
- **Jakarta Validation**: Provides a comprehensive validation framework for request data.
- **Maven**: A build automation tool used for managing dependencies and building the project.

## Setup

### Prerequisites
Before setting up the application, ensure that you have the following:
- **Java 17 or Higher**: Ensure Java is installed and `JAVA_HOME` is properly configured.
- **Maven 3.6+**: Install Maven to manage project dependencies and build the application.
- **AWS Account**: An AWS account with access to RDS and S3 services.
- **PostgreSQL Client (Optional)**: For managing the database outside of the application, if necessary.

### Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/ecommerce.git
   cd ecommerce
