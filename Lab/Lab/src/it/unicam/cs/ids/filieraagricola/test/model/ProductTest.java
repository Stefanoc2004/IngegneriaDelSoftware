package it.unicam.cs.ids.filieraagricola.test.model;

import it.unicam.cs.ids.filieraagricola.model.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Product} class.
 *
 * <p>This test class verifies the behavior of the Product domain model including
 * construction, field access, copying, equality, hashCode, and toString methods.
 * Tests ensure proper handling of null values, UUID generation, and data integrity.</p>
 *
 * @author Agricultural Platform Team
 * @version 1.0
 */
@DisplayName("Product Model Tests")
class ProductTest {

    private Product testProduct;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.now().minusDays(5);
        testProduct = new Product(
                "test-id-123",
                "Organic Tomatoes",
                "vegetables",
                "Fresh organic tomatoes from local farm",
                "organic",
                "EU-Organic, ISO-22000",
                testDate,
                "producer-456"
        );
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should generate UUID and leave fields null")
        void defaultConstructor_generatesUuid_leavesFieldsNull() {
            // Act
            Product product = new Product();

            // Assert
            assertAll("Default constructor",
                    () -> assertNotNull(product.getId(), "ID should be generated"),
                    () -> assertTrue(product.getId().matches(
                                    "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"),
                            "ID should be valid UUID format"),
                    () -> assertNull(product.getName(), "Name should be null"),
                    () -> assertNull(product.getCategory(), "Category should be null"),
                    () -> assertNull(product.getDescription(), "Description should be null"),
                    () -> assertNull(product.getCultivationMethod(), "Cultivation method should be null"),
                    () -> assertNull(product.getCertifications(), "Certifications should be null"),
                    () -> assertNull(product.getProductionDate(), "Production date should be null"),
                    () -> assertNull(product.getProducerId(), "Producer ID should be null")
            );
        }

        @Test
        @DisplayName("Full constructor with valid data should set all fields correctly")
        void fullConstructor_withValidData_setsAllFields() {
            // Arrange
            String expectedId = "custom-id-789";
            String expectedName = "Organic Carrots";
            String expectedCategory = "vegetables";
            String expectedDescription = "Fresh carrots";
            String expectedMethod = "organic";
            String expectedCerts = "EU-Organic";
            LocalDate expectedDate = LocalDate.now().minusDays(2);
            String expectedProducerId = "producer-123";

            // Act
            Product product = new Product(
                    expectedId,
                    expectedName,
                    expectedCategory,
                    expectedDescription,
                    expectedMethod,
                    expectedCerts,
                    expectedDate,
                    expectedProducerId
            );

            // Assert
            assertAll("Full constructor with valid data",
                    () -> assertEquals(expectedId, product.getId()),
                    () -> assertEquals(expectedName, product.getName()),
                    () -> assertEquals(expectedCategory, product.getCategory()),
                    () -> assertEquals(expectedDescription, product.getDescription()),
                    () -> assertEquals(expectedMethod, product.getCultivationMethod()),
                    () -> assertEquals(expectedCerts, product.getCertifications()),
                    () -> assertEquals(expectedDate, product.getProductionDate()),
                    () -> assertEquals(expectedProducerId, product.getProducerId())
            );
        }

        @Test
        @DisplayName("Full constructor with null ID should generate new UUID")
        void fullConstructor_withNullId_generatesUuid() {
            // Act
            Product product = new Product(
                    null,
                    "Test Product",
                    "category",
                    "description",
                    "method",
                    "certs",
                    testDate,
                    "producer-id"
            );

            // Assert
            assertNotNull(product.getId(), "ID should be generated when null");
            assertTrue(product.getId().matches(
                            "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"),
                    "Generated ID should be valid UUID");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Full constructor with empty/blank ID should generate new UUID")
        void fullConstructor_withEmptyOrBlankId_generatesUuid(String id) {
            // Act
            Product product = new Product(
                    id,
                    "Test Product",
                    "category",
                    "description",
                    "method",
                    "certs",
                    testDate,
                    "producer-id"
            );

            // Assert
            assertNotNull(product.getId(), "ID should be generated when empty/blank");
            assertFalse(product.getId().trim().isEmpty(), "Generated ID should not be empty");
        }

        @Test
        @DisplayName("Full constructor with whitespace-padded ID should trim it")
        void fullConstructor_withWhitespacePaddedId_trimsId() {
            // Arrange
            String paddedId = "  test-id-with-spaces  ";

            // Act
            Product product = new Product(
                    paddedId,
                    "Test Product",
                    "category",
                    "description",
                    "method",
                    "certs",
                    testDate,
                    "producer-id"
            );

            // Assert
            assertEquals("test-id-with-spaces", product.getId(),
                    "ID should be trimmed of surrounding whitespace");
        }

        @Test
        @DisplayName("Copy constructor with valid product should create exact copy")
        void copyConstructor_withValidProduct_createsExactCopy() {
            // Act
            Product copy = new Product(testProduct);

            // Assert
            assertAll("Copy constructor",
                    () -> assertEquals(testProduct.getId(), copy.getId()),
                    () -> assertEquals(testProduct.getName(), copy.getName()),
                    () -> assertEquals(testProduct.getCategory(), copy.getCategory()),
                    () -> assertEquals(testProduct.getDescription(), copy.getDescription()),
                    () -> assertEquals(testProduct.getCultivationMethod(), copy.getCultivationMethod()),
                    () -> assertEquals(testProduct.getCertifications(), copy.getCertifications()),
                    () -> assertEquals(testProduct.getProductionDate(), copy.getProductionDate()),
                    () -> assertEquals(testProduct.getProducerId(), copy.getProducerId()),
                    () -> assertNotSame(testProduct, copy, "Copy should be different instance")
            );
        }

        @Test
        @DisplayName("Copy constructor with null should throw NullPointerException")
        void copyConstructor_withNull_throwsNullPointerException() {
            // Act & Assert
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new Product(null),
                    "Should throw NullPointerException for null product"
            );

            assertEquals("Product to copy cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("All setters should update fields correctly")
        void setters_withNewValues_updateFields() {
            // Arrange
            Product product = new Product();
            String newId = "new-id";
            String newName = "New Name";
            String newCategory = "new-category";
            String newDescription = "New description";
            String newMethod = "new-method";
            String newCerts = "New certifications";
            LocalDate newDate = LocalDate.now();
            String newProducerId = "new-producer";

            // Act
            product.setId(newId);
            product.setName(newName);
            product.setCategory(newCategory);
            product.setDescription(newDescription);
            product.setCultivationMethod(newMethod);
            product.setCertifications(newCerts);
            product.setProductionDate(newDate);
            product.setProducerId(newProducerId);

            // Assert
            assertAll("Setters update fields",
                    () -> assertEquals(newId, product.getId()),
                    () -> assertEquals(newName, product.getName()),
                    () -> assertEquals(newCategory, product.getCategory()),
                    () -> assertEquals(newDescription, product.getDescription()),
                    () -> assertEquals(newMethod, product.getCultivationMethod()),
                    () -> assertEquals(newCerts, product.getCertifications()),
                    () -> assertEquals(newDate, product.getProductionDate()),
                    () -> assertEquals(newProducerId, product.getProducerId())
            );
        }

        @Test
        @DisplayName("Setters should accept null values")
        void setters_withNullValues_acceptNull() {
            // Arrange
            Product product = new Product();

            // Act
            product.setId(null);
            product.setName(null);
            product.setCategory(null);
            product.setDescription(null);
            product.setCultivationMethod(null);
            product.setCertifications(null);
            product.setProductionDate(null);
            product.setProducerId(null);

            // Assert
            assertAll("Setters accept null",
                    () -> assertNull(product.getId()),
                    () -> assertNull(product.getName()),
                    () -> assertNull(product.getCategory()),
                    () -> assertNull(product.getDescription()),
                    () -> assertNull(product.getCultivationMethod()),
                    () -> assertNull(product.getCertifications()),
                    () -> assertNull(product.getProductionDate()),
                    () -> assertNull(product.getProducerId())
            );
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("equals should return true for same instance")
        void equals_withSameInstance_returnsTrue() {
            // Assert
            assertEquals(testProduct, testProduct, "Same instance should be equal");
        }

        @Test
        @DisplayName("equals should return true for products with same ID")
        void equals_withSameId_returnsTrue() {
            // Arrange
            Product product1 = new Product();
            product1.setId("same-id");
            product1.setName("Product 1");

            Product product2 = new Product();
            product2.setId("same-id");
            product2.setName("Product 2");

            // Assert
            assertEquals(product1, product2,
                    "Products with same ID should be equal regardless of other fields");
        }

        @Test
        @DisplayName("equals should return false for products with different IDs")
        void equals_withDifferentIds_returnsFalse() {
            // Arrange
            Product product1 = new Product();
            product1.setId("id-1");

            Product product2 = new Product();
            product2.setId("id-2");

            // Assert
            assertNotEquals(product1, product2, "Products with different IDs should not be equal");
        }

        @Test
        @DisplayName("equals should return false when comparing with null")
        void equals_withNull_returnsFalse() {
            // Assert
            assertNotEquals(testProduct, null, "Product should not equal null");
        }

        @Test
        @DisplayName("equals should return false when comparing with different class")
        void equals_withDifferentClass_returnsFalse() {
            // Assert
            assertNotEquals(testProduct, "Not a Product",
                    "Product should not equal object of different class");
        }

        @Test
        @DisplayName("equals should handle null IDs correctly")
        void equals_withNullIds_handlesCorrectly() {
            // Arrange
            Product product1 = new Product();
            product1.setId(null);

            Product product2 = new Product();
            product2.setId(null);

            Product product3 = new Product();
            product3.setId("non-null-id");

            // Assert
            assertAll("Null ID handling",
                    () -> assertNotEquals(product1, product2,
                            "Products with null IDs should not be equal"),
                    () -> assertNotEquals(product1, product3,
                            "Product with null ID should not equal product with non-null ID")
            );
        }

        @Test
        @DisplayName("hashCode should be consistent for same ID")
        void hashCode_withSameId_returnsConsistentValue() {
            // Arrange
            Product product1 = new Product();
            product1.setId("consistent-id");

            Product product2 = new Product();
            product2.setId("consistent-id");

            // Assert
            assertEquals(product1.hashCode(), product2.hashCode(),
                    "Products with same ID should have same hashCode");
        }

        @Test
        @DisplayName("hashCode should return 0 for null ID")
        void hashCode_withNullId_returnsZero() {
            // Arrange
            Product product = new Product();
            product.setId(null);

            // Assert
            assertEquals(0, product.hashCode(), "hashCode should be 0 for null ID");
        }

        @Test
        @DisplayName("equals and hashCode contract should be maintained")
        void equalsHashCode_contract_isMaintained() {
            // Arrange
            Product product1 = new Product();
            product1.setId("contract-id");

            Product product2 = new Product();
            product2.setId("contract-id");

            Product product3 = new Product();
            product3.setId("different-id");

            // Assert equals contract
            assertAll("Equals contract",
                    () -> assertEquals(product1, product1, "Reflexive: x.equals(x) should be true"),
                    () -> assertEquals(product1, product2, "Symmetric: x.equals(y) implies y.equals(x)"),
                    () -> assertEquals(product2, product1, "Symmetric: y.equals(x) implies x.equals(y)"),
                    () -> assertNotEquals(product1, product3, "Different IDs should not be equal")
            );

            // Assert hashCode contract
            if (product1.equals(product2)) {
                assertEquals(product1.hashCode(), product2.hashCode(),
                        "Equal objects must have equal hash codes");
            }
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should include key identifying fields")
        void toString_withPopulatedFields_includesKeyFields() {
            // Act
            String result = testProduct.toString();

            // Assert
            assertAll("toString content",
                    () -> assertTrue(result.contains("Product{"), "Should start with class name"),
                    () -> assertTrue(result.contains("id='test-id-123'"), "Should include ID"),
                    () -> assertTrue(result.contains("name='Organic Tomatoes'"), "Should include name"),
                    () -> assertTrue(result.contains("category='vegetables'"), "Should include category"),
                    () -> assertTrue(result.contains("producerId='producer-456'"), "Should include producer ID"),
                    () -> assertTrue(result.contains("}"), "Should end with closing brace")
            );
        }

        @Test
        @DisplayName("toString should handle null fields gracefully")
        void toString_withNullFields_handlesGracefully() {
            // Arrange
            Product product = new Product();

            // Act
            String result = product.toString();

            // Assert
            assertAll("toString with nulls",
                    () -> assertNotNull(result, "toString should not return null"),
                    () -> assertTrue(result.contains("Product{"), "Should start with class name"),
                    () -> assertTrue(result.contains("id="), "Should include ID field"),
                    () -> assertTrue(result.contains("name='null'"), "Should handle null name"),
                    () -> assertTrue(result.contains("category='null'"), "Should handle null category"),
                    () -> assertTrue(result.contains("producerId='null'"), "Should handle null producer ID")
            );
        }
    }
}