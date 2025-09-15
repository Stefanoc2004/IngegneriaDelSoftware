package it.unicam.cs.ids.filieraagricola.test.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.service.ProductService;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ProductService} class.
 *
 * <p>This test class verifies the business logic and validation rules of the ProductService,
 * including product creation, validation, categorization (organic, certified, fresh),
 * searching, updating, and deletion operations. Tests ensure proper exception handling
 * and data normalization.</p>
 *
 * @author Agricultural Platform Team
 * @version 1.0
 */
@DisplayName("ProductService Tests")
class ProductServiceTest {

    private ProductService productService;
    private LocalDate validProductionDate;
    private LocalDate futureDate;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        validProductionDate = LocalDate.now().minusDays(10);
        futureDate = LocalDate.now().plusDays(1);
    }

    @Nested
    @DisplayName("Product Creation Tests")
    class ProductCreationTests {

        @Test
        @DisplayName("createProduct with valid data should create and return product")
        void createProduct_withValidData_createsProduct() {
            // Arrange
            String name = "  Organic Tomatoes  ";
            String category = "  VEGETABLES  ";
            String description = "Fresh tomatoes";
            String cultivationMethod = "  ORGANIC  ";
            String certifications = "EU-Organic";
            String producerId = "producer-123";

            // Act
            Product product = productService.createProduct(
                    name, category, description, cultivationMethod,
                    certifications, validProductionDate, producerId
            );

            // Assert
            assertAll("Created product",
                    () -> assertNotNull(product.getId(), "Product should have generated ID"),
                    () -> assertEquals("Organic Tomatoes", product.getName(), "Name should be trimmed"),
                    () -> assertEquals("vegetables", product.getCategory(), "Category should be normalized to lowercase"),
                    () -> assertEquals("Fresh tomatoes", product.getDescription()),
                    () -> assertEquals("organic", product.getCultivationMethod(), "Method should be normalized"),
                    () -> assertEquals("EU-Organic", product.getCertifications()),
                    () -> assertEquals(validProductionDate, product.getProductionDate()),
                    () -> assertEquals("producer-123", product.getProducerId())
            );
        }

        @Test
        @DisplayName("createProduct with null certifications should set empty string")
        void createProduct_withNullCertifications_setsEmptyString() {
            // Act
            Product product = productService.createProduct(
                    "Product", "category", "description", "method",
                    null, validProductionDate, "producer-123"
            );

            // Assert
            assertEquals("", product.getCertifications(),
                    "Null certifications should be converted to empty string");
        }

        @Test
        @DisplayName("createProduct should add product to repository")
        void createProduct_addsToRepository() {
            // Arrange
            int initialCount = productService.getProductCount();

            // Act
            productService.createProduct(
                    "Test Product", "category", "description", "method",
                    "certs", validProductionDate, "producer-123"
            );

            // Assert
            assertEquals(initialCount + 1, productService.getProductCount(),
                    "Product count should increase by 1");
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("validateProductData with invalid name should throw ValidationException")
        void validateProductData_withInvalidName_throwsValidationException(String name) {
            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> productService.validateProductData(
                            name, "category", "description", "method",
                            validProductionDate, "producer-123"
                    )
            );

            assertEquals("Product name cannot be null or empty", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("validateProductData with invalid category should throw ValidationException")
        void validateProductData_withInvalidCategory_throwsValidationException(String category) {
            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> productService.validateProductData(
                            "name", category, "description", "method",
                            validProductionDate, "producer-123"
                    )
            );

            assertEquals("Product category cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("validateProductData with null production date should throw ValidationException")
        void validateProductData_withNullProductionDate_throwsValidationException() {
            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> productService.validateProductData(
                            "name", "category", "description", "method",
                            null, "producer-123"
                    )
            );

            assertEquals("Production date cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("validateProductData with future production date should throw ValidationException")
        void validateProductData_withFutureDate_throwsValidationException() {
            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> productService.validateProductData(
                            "name", "category", "description", "method",
                            futureDate, "producer-123"
                    )
            );

            assertEquals("Production date cannot be in the future", exception.getMessage());
        }

        @Test
        @DisplayName("validateProductData with all valid data should not throw exception")
        void validateProductData_withValidData_doesNotThrow() {
            // Act & Assert
            assertDoesNotThrow(
                    () -> productService.validateProductData(
                            "name", "category", "description", "method",
                            validProductionDate, "producer-123"
                    )
            );
        }
    }

    @Nested
    @DisplayName("Product Classification Tests")
    class ProductClassificationTests {

        @ParameterizedTest
        @CsvSource({
                "organic, true",
                "ORGANIC, true",
                "biologico, true",
                "BIOLOGICO, true",
                "organic farming, true",
                "metodo biologico, true",
                "conventional, false",
                "hydroponic, false",
                "'', false"
        })
        @DisplayName("isOrganic should correctly identify organic products")
        void isOrganic_withVariousMethods_returnsCorrectResult(String method, boolean expected) {
            // Arrange
            Product product = new Product();
            product.setCultivationMethod(method);

            // Act
            boolean result = productService.isOrganic(product);

            // Assert
            assertEquals(expected, result,
                    String.format("Method '%s' should be organic=%s", method, expected));
        }

        @Test
        @DisplayName("isOrganic with null product should return false")
        void isOrganic_withNullProduct_returnsFalse() {
            // Act & Assert
            assertFalse(productService.isOrganic(null));
        }

        @Test
        @DisplayName("isOrganic with null cultivation method should return false")
        void isOrganic_withNullMethod_returnsFalse() {
            // Arrange
            Product product = new Product();
            product.setCultivationMethod(null);

            // Act & Assert
            assertFalse(productService.isOrganic(product));
        }

        @ParameterizedTest
        @CsvSource({
                "EU-Organic, true",
                "ISO-22000, true",
                "'  ', false",
                "'', false"
        })
        @DisplayName("hasCertifications should correctly identify certified products")
        void hasCertifications_withVariousCerts_returnsCorrectResult(String certs, boolean expected) {
            // Arrange
            Product product = new Product();
            product.setCertifications(certs);

            // Act
            boolean result = productService.hasCertifications(product);

            // Assert
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("hasCertifications with null product should return false")
        void hasCertifications_withNullProduct_returnsFalse() {
            // Act & Assert
            assertFalse(productService.hasCertifications(null));
        }

        @Test
        @DisplayName("isFresh should return true for products within 30 days")
        void isFresh_withRecentDate_returnsTrue() {
            // Arrange
            Product product = new Product();
            product.setProductionDate(LocalDate.now().minusDays(15));

            // Act & Assert
            assertTrue(productService.isFresh(product));
        }

        @Test
        @DisplayName("isFresh should return false for products older than 30 days")
        void isFresh_withOldDate_returnsFalse() {
            // Arrange
            Product product = new Product();
            product.setProductionDate(LocalDate.now().minusDays(31));

            // Act & Assert
            assertFalse(productService.isFresh(product));
        }

        @Test
        @DisplayName("isFresh with null product or date should return false")
        void isFresh_withNullProductOrDate_returnsFalse() {
            // Arrange
            Product productWithNullDate = new Product();
            productWithNullDate.setProductionDate(null);

            // Act & Assert
            assertAll("Null handling",
                    () -> assertFalse(productService.isFresh(null), "Null product should return false"),
                    () -> assertFalse(productService.isFresh(productWithNullDate), "Null date should return false")
            );
        }
    }

    @Nested
    @DisplayName("Product Search Tests")
    class ProductSearchTests {

        private Product organicTomato;
        private Product conventionalCarrot;
        private Product certifiedPotato;

        @BeforeEach
        void setUpProducts() {
            // Create organic tomato
            organicTomato = productService.createProduct(
                    "Organic Tomato", "vegetables", "Fresh organic tomato",
                    "organic", "EU-Organic", LocalDate.now().minusDays(5), "producer-1"
            );

            // Create conventional carrot
            conventionalCarrot = productService.createProduct(
                    "Carrot", "vegetables", "Fresh carrot",
                    "conventional", "", LocalDate.now().minusDays(40), "producer-2"
            );

            // Create certified potato
            certifiedPotato = productService.createProduct(
                    "Potato", "vegetables", "Certified potato",
                    "conventional", "ISO-22000", LocalDate.now().minusDays(10), "producer-3"
            );
        }

        @Test
        @DisplayName("findById with existing ID should return product")
        void findById_withExistingId_returnsProduct() {
            // Act
            Product found = productService.findById(organicTomato.getId());

            // Assert
            assertEquals(organicTomato, found);
        }

        @Test
        @DisplayName("findById with non-existing ID should throw NotFoundException")
        void findById_withNonExistingId_throwsNotFoundException() {
            // Act & Assert
            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> productService.findById("non-existing-id")
            );

            assertEquals("Product not found with ID: non-existing-id", exception.getMessage());
        }

        @Test
        @DisplayName("findByCategory should return products in category")
        void findByCategory_withExistingCategory_returnsProducts() {
            // Act
            List<Product> vegetables = productService.findByCategory("VEGETABLES");

            // Assert
            assertEquals(3, vegetables.size(), "Should find all vegetable products");
            assertTrue(vegetables.contains(organicTomato));
            assertTrue(vegetables.contains(conventionalCarrot));
            assertTrue(vegetables.contains(certifiedPotato));
        }

        @Test
        @DisplayName("findByCategory with non-existing category should return empty list")
        void findByCategory_withNonExistingCategory_returnsEmptyList() {
            // Act
            List<Product> fruits = productService.findByCategory("fruits");

            // Assert
            assertTrue(fruits.isEmpty());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("findByCategory with null/empty category should return empty list")
        void findByCategory_withNullOrEmptyCategory_returnsEmptyList(String category) {
            // Act
            List<Product> result = productService.findByCategory(category);

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("findOrganicProducts should return only organic products")
        void findOrganicProducts_returnsOnlyOrganicProducts() {
            // Act
            List<Product> organicProducts = productService.findOrganicProducts();

            // Assert
            assertEquals(1, organicProducts.size());
            assertTrue(organicProducts.contains(organicTomato));
        }

        @Test
        @DisplayName("findCertifiedProducts should return only certified products")
        void findCertifiedProducts_returnsOnlyCertifiedProducts() {
            // Act
            List<Product> certifiedProducts = productService.findCertifiedProducts();

            // Assert
            assertEquals(2, certifiedProducts.size());
            assertTrue(certifiedProducts.contains(organicTomato));
            assertTrue(certifiedProducts.contains(certifiedPotato));
        }

        @Test
        @DisplayName("findFreshProducts should return only fresh products")
        void findFreshProducts_returnsOnlyFreshProducts() {
            // Act
            List<Product> freshProducts = productService.findFreshProducts();

            // Assert
            assertEquals(2, freshProducts.size());
            assertTrue(freshProducts.contains(organicTomato));
            assertTrue(freshProducts.contains(certifiedPotato));
            assertFalse(freshProducts.contains(conventionalCarrot));
        }

        @Test
        @DisplayName("getAllProducts should return unmodifiable list")
        void getAllProducts_returnsUnmodifiableList() {
            // Act
            List<Product> products = productService.getAllProducts();

            // Assert
            assertEquals(3, products.size());
            assertThrows(UnsupportedOperationException.class,
                    () -> products.add(new Product()),
                    "List should be unmodifiable"
            );
        }
    }

    @Nested
    @DisplayName("Product Update and Delete Tests")
    class UpdateDeleteTests {

        private Product existingProduct;

        @BeforeEach
        void setUpProduct() {
            existingProduct = productService.createProduct(
                    "Original Name", "original", "Original description",
                    "original", "Original cert", validProductionDate, "original-producer"
            );
        }

        @Test
        @DisplayName("updateProduct with valid data should update all fields")
        void updateProduct_withValidData_updatesAllFields() {
            // Arrange
            Product updatedData = new Product();
            updatedData.setName("  Updated Name  ");
            updatedData.setCategory("  UPDATED  ");
            updatedData.setDescription("Updated description");
            updatedData.setCultivationMethod("  UPDATED METHOD  ");
            updatedData.setCertifications("Updated cert");
            updatedData.setProductionDate(LocalDate.now().minusDays(2));
            updatedData.setProducerId("updated-producer");

            // Act
            Product result = productService.updateProduct(existingProduct.getId(), updatedData);

            // Assert
            assertAll("Updated product",
                    () -> assertEquals(existingProduct.getId(), result.getId(), "ID should not change"),
                    () -> assertEquals("Updated Name", result.getName(), "Name should be normalized"),
                    () -> assertEquals("updated", result.getCategory(), "Category should be normalized"),
                    () -> assertEquals("Updated description", result.getDescription()),
                    () -> assertEquals("updated method", result.getCultivationMethod(), "Method should be normalized"),
                    () -> assertEquals("Updated cert", result.getCertifications()),
                    () -> assertEquals(LocalDate.now().minusDays(2), result.getProductionDate()),
                    () -> assertEquals("updated-producer", result.getProducerId())
            );
        }

        @Test
        @DisplayName("updateProduct with non-existing ID should throw NotFoundException")
        void updateProduct_withNonExistingId_throwsNotFoundException() {
            // Arrange
            Product updatedData = new Product();
            updatedData.setName("Name");
            updatedData.setCategory("category");
            updatedData.setDescription("description");
            updatedData.setCultivationMethod("method");
            updatedData.setProductionDate(validProductionDate);
            updatedData.setProducerId("producer");

            // Act & Assert
            assertThrows(NotFoundException.class,
                    () -> productService.updateProduct("non-existing-id", updatedData)
            );
        }

        @Test
        @DisplayName("updateProduct with invalid data should throw ValidationException")
        void updateProduct_withInvalidData_throwsValidationException() {
            // Arrange
            Product updatedData = new Product();
            updatedData.setName(null); // Invalid: null name
            updatedData.setCategory("category");
            updatedData.setDescription("description");
            updatedData.setCultivationMethod("method");
            updatedData.setProductionDate(validProductionDate);
            updatedData.setProducerId("producer");

            // Act & Assert
            assertThrows(ValidationException.class,
                    () -> productService.updateProduct(existingProduct.getId(), updatedData)
            );
        }

        @Test
        @DisplayName("deleteProduct with existing ID should remove product")
        void deleteProduct_withExistingId_removesProduct() {
            // Arrange
            int initialCount = productService.getProductCount();

            // Act
            productService.deleteProduct(existingProduct.getId());

            // Assert
            assertEquals(initialCount - 1, productService.getProductCount());
            assertThrows(NotFoundException.class,
                    () -> productService.findById(existingProduct.getId())
            );
        }

        @Test
        @DisplayName("deleteProduct with non-existing ID should throw NotFoundException")
        void deleteProduct_withNonExistingId_throwsNotFoundException() {
            // Act & Assert
            assertThrows(NotFoundException.class,
                    () -> productService.deleteProduct("non-existing-id")
            );
        }
    }

    @Nested
    @DisplayName("Product Count Tests")
    class ProductCountTests {

        @Test
        @DisplayName("getProductCount should return correct count")
        void getProductCount_returnsCorrectCount() {
            // Arrange
            assertEquals(0, productService.getProductCount(), "Initial count should be 0");

            // Act - Add products
            productService.createProduct(
                    "Product 1", "category", "desc", "method",
                    "cert", validProductionDate, "producer"
            );
            productService.createProduct(
                    "Product 2", "category", "desc", "method",
                    "cert", validProductionDate, "producer"
            );

            // Assert
            assertEquals(2, productService.getProductCount());
        }
    }
}