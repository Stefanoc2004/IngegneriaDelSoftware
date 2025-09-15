package it.unicam.cs.ids.filieraagricola.test.model;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.ProductPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ProductPackage}.
 *
 * <p>This test class verifies construction, defensive copying,
 * equality/hashCode contract and textual representation of the
 * {@code ProductPackage} domain model.</p>
 *
 * <p>Tests use a concrete {@link Product} fixture rather than mocking
 * to keep model tests simple and deterministic.</p>
 */
@DisplayName("ProductPackage Model Tests")
class ProductPackageTest {

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product(
                "p-id-1",
                "Tomatoes",
                "vegetables",
                "Fresh local tomatoes",
                "organic",
                "EU-Organic",
                LocalDate.now().minusDays(2),
                "producer-1"
        );
    }

    /**
     * Verifies default constructor generates a non-null id and an empty products list.
     */
    @Test
    @DisplayName("default constructor generates id and empty products list")
    void defaultConstructor_generatesIdAndEmptyProducts() {
        ProductPackage pkg = new ProductPackage();

        assertAll("default-construction",
                () -> assertNotNull(pkg.getId(), "id must be generated"),
                () -> assertNotNull(pkg.getProducts(), "products list must not be null"),
                () -> assertEquals(0, pkg.getProducts().size(), "products list must be empty")
        );
    }

    /**
     * Verifies constructor with name and products assigns name and copies products list.
     */
    @Test
    @DisplayName("constructor with name and products sets fields and copies list")
    void constructorWithNameAndProducts_assignsValues() {
        List<Product> products = new ArrayList<>();
        products.add(testProduct);

        ProductPackage pkg = new ProductPackage("My Pack", products);

        assertAll("values",
                () -> assertNotNull(pkg.getId(), "id must be generated"),
                () -> assertEquals("My Pack", pkg.getName(), "name should match"),
                () -> assertNotSame(products, pkg.getProducts(), "internal list must be a defensive copy"),
                () -> assertEquals(1, pkg.getProducts().size(), "product count must be preserved")
        );
    }

    /**
     * Verifies copy constructor copies fields and produces independent list object.
     */
    @Test
    @DisplayName("copy constructor creates a copy with same id/name and defensive list copy")
    void copyConstructor_copiesFields() {
        List<Product> originalList = new ArrayList<>();
        originalList.add(testProduct);
        ProductPackage original = new ProductPackage("P", originalList);

        ProductPackage copy = new ProductPackage(original);

        assertAll("copy",
                () -> assertEquals(original.getId(), copy.getId(), "ids must be equal"),
                () -> assertEquals(original.getName(), copy.getName(), "names must be equal"),
                () -> assertNotSame(original.getProducts(), copy.getProducts(), "product lists must be different objects"),
                () -> assertEquals(original.getProducts().size(), copy.getProducts().size(), "list sizes must match")
        );
    }

    /**
     * Verifies setProducts handles null by creating an empty list.
     */
    @Test
    @DisplayName("setProducts with null produces empty internal list")
    void setProducts_withNull_resultsEmptyList() {
        ProductPackage pkg = new ProductPackage("x", Collections.singletonList(testProduct));
        pkg.setProducts(null);

        assertAll("setProducts-null",
                () -> assertNotNull(pkg.getProducts(), "products list must not be null after set"),
                () -> assertEquals(0, pkg.getProducts().size(), "products list must be empty after setting null")
        );
    }

    /**
     * Verifies equals and hashCode are based on id only.
     */
    @Test
    @DisplayName("equals and hashCode use id identity")
    void equalsAndHashCode_basedOnId() {
        String fixedId = "fixed-id-123";
        ProductPackage a = new ProductPackage(fixedId, "n", null);
        ProductPackage b = new ProductPackage(fixedId, "other-name", null);

        assertAll("identity-equality",
                () -> assertEquals(a, b, "objects with same id must be equal"),
                () -> assertEquals(a.hashCode(), b.hashCode(), "hashCodes must match for equal objects")
        );
    }

    /**
     * Verifies toString contains productsCount information for logging/debugging.
     */
    @Test
    @DisplayName("toString contains productsCount information")
    void toString_containsProductsCount() {
        ProductPackage pkg = new ProductPackage("name", Collections.emptyList());
        String s = pkg.toString();

        assertTrue(s.contains("productsCount"), "toString should contain productsCount");
    }

    @Nested
    @DisplayName("Defensive copy and mutation behavior")
    class DefensiveCopyTests {

        /**
         * Verifies that mutating the list passed to constructor does not affect internal list.
         */
        @Test
        @DisplayName("mutating source list after construction does not change internal list")
        void mutatingSourceList_doesNotAffectInternalList() {
            List<Product> products = new ArrayList<>();
            products.add(testProduct);
            ProductPackage pkg = new ProductPackage("pack", products);

            // mutate source
            products.clear();

            assertEquals(1, pkg.getProducts().size(), "internal list must remain unchanged after external mutation");
        }

        /**
         * Verifies that modifying the returned list from getter affects internal state
         * only if the getter returns internal reference (current class returns internal list).
         *
         * <p>Test documents current behavior. If later changed to defensive copy in getter,
         * this test should be updated accordingly.</p>
         */
        @Test
        @DisplayName("modifying list returned by getter mutates internal list (document current behavior)")
        void modifyingReturnedList_mutatesInternalList_documentBehavior() {
            ProductPackage pkg = new ProductPackage("pack2", Collections.singletonList(testProduct));
            List<Product> returned = pkg.getProducts();
            returned.add(new Product(
                    "p-id-2",
                    "Peppers",
                    "vegetables",
                    "Sweet peppers",
                    "conventional",
                    null,
                    LocalDate.now().minusDays(1),
                    "producer-2"
            ));

            assertEquals(2, pkg.getProducts().size(), "current getter returns internal list so mutation is reflected");
        }
    }
}
