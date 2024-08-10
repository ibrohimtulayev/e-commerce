
### README for E-commerce Application

---

## Project Overview

This project is an e-commerce application that allows users to browse products, add them to their cart or wishlist, make purchases, and manage orders. The application is built using Spring Boot and incorporates various features such as user authentication, product management, discounts, and more.

### Key Features:

1. **User Authentication and Authorization:**
   - User registration, login, and confirmation via email.
   - JWT-based authentication to secure endpoints.

2. **Product Management:**
   - Create, update, and delete products.
   - Filter and search for products by category, rating, or other criteria.
   - Upload product images to AWS S3.

3. **Shopping Cart and Wishlist:**
   - Add and remove products from the shopping cart.
   - View and manage the wishlist.

4. **Order Management:**
   - Place orders with various products.
   - Track delivery status and manage order history.

5. **Discounts:**
   - Create and manage discount events.
   - Apply discounts to products in the shopping cart.

6. **Category Management:**
   - Create categories and subcategories.
   - Display products based on category hierarchy.

### Technology Stack:

- **Backend:** Spring Boot, Java
- **Database:** PostgreSQL
- **Security:** JWT (JSON Web Tokens)
- **File Storage:** AWS S3 for image uploads
- **API Documentation:** Swagger/OpenAPI
- **Validation:** Jakarta Validation (Bean Validation API)

---

## Endpoints Overview

### 1. **Authentication Controller (`AuthController`)**
   - `POST /api/auth/register`: Register a new user.
   - `POST /api/auth/confirmation`: Confirm user registration via email code.
   - `POST /api/auth/login`: User login and token generation.

### 2. **Product Controller (`ProductController`)**
   - `POST /api/product/search`: Search for products by keyword and gender.
   - `GET /api/product/recommendation`: Get recommended products.
   - `GET /api/product/category-products`: Retrieve paged products by category.
   - `GET /api/product/filter`: Filter products based on category and criteria.
   - `GET /api/product/{id}`: Retrieve detailed information about a product.
   - `GET /api/product/rating-review`: Get rating and reviews for a product.
   - `POST /api/product`: Add a new product with an optional image.

### 3. **Category Controller (`CategoryController`)**
   - `GET /api/category/random`: Get random categories.
   - `GET /api/category/all`: Get all parent categories.
   - `GET /api/category/subcategories`: Get all subcategories.
   - `POST /api/category`: Create a new category with an optional image.

### 4. **Cart Controller (`CartController`)**
   - `GET /api/cart`: View all items in the cart.

### 5. **Basket Product Controller (`BasketProductController`)**
   - `POST /api/basket-product/add`: Add a product to the basket.
   - `DELETE /api/basket-product/remove`: Remove a product from the basket.

### 6. **Discount Controller (`DiscountController`)**
   - `GET /api/discount`: Get current discount events.
   - `POST /api/discount`: Create a new discount event with an optional image.
   - `GET /api/discount/current`: Get the current active discount.

### 7. **Order Controller (`OrderController`)**
   - `GET /api/order/delivery`: View order delivery information.
   - `POST /api/order/make`: Place a new order.

### 8. **User Controller (`UserController`)**
   - `GET /api/user/searchHistory`: Get user's search history.
   - `GET /api/user/wishlist`: View user's wishlist.
   - `POST /api/user/wishlist/add`: Add a product to the wishlist.
   - `DELETE /api/user/wishlist/{id}`: Remove a product from the wishlist.

---

## Getting Started

### Prerequisites

- **Java 17**
- **Spring Boot 3.x**
- **PostgreSQL 14**
- **AWS S3** account for file storage.

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/ecommerce-app.git
   cd ecommerce-app
   ```

2. **Configure the application:**
   - Update the `application.properties` or `application.yml` with your database and AWS S3 credentials.

3. **Build and run the application:**
   ```bash
   ./mvnw clean install
   java -jar target/ecommerce-app.jar
   ```

4. **Access the application:**
   - The application runs on `http://localhost:8080`.
   - Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

### Deployment

- The application can be deployed on any server supporting Java, such as AWS EC2, DigitalOcean, or a similar service.
- Make sure to configure the environment variables for database and S3 access before deploying.

---

## Contribution

Feel free to fork this repository, create feature branches, and submit pull requests. Contributions, whether by fixing bugs, improving documentation, or adding features, are highly appreciated.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Contact

For further inquiries or support, please contact:

- **Name:** Your Name
- **Email:** youremail@example.com

---

This README provides a clear and concise guide to understanding, setting up, and contributing to the e-commerce application.
