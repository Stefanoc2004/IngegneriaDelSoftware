package it.unicam.cs.ids.filieraagricola.test.service;

import it.unicam.cs.ids.filieraagricola.model.Product;
import it.unicam.cs.ids.filieraagricola.model.ProductPackage;
import it.unicam.cs.ids.filieraagricola.service.PackageService;
import it.unicam.cs.ids.filieraagricola.service.exception.NotFoundException;
import it.unicam.cs.ids.filieraagricola.service.exception.ValidationException;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PackageService}.
 *
 * <p>This test suite verifies the public behavior of {@code PackageService}.
 * Tests cover happy paths, validation and not-found error conditions,
 * defensive-copy semantics and collection immutability where applicable.</p>
 *
 * <p>All tests use concrete {@link Product} fixtures and do not depend on external
 * resources. The in-memory repository inside {@code PackageService} makes tests
 * deterministic and fast.</p>
 */
@DisplayName("PackageService Unit Tests")
class PackageServiceTest {

    private PackageService service;

    /**
     * Creates a fresh service instance before each test.
     */
    @BeforeEach
    void setUp() {
        service = new PackageService();
    }

    /**
     * Helper creating a basic product fixture with predictable id.
     *
     * @param id product identifier
     * @return new Product instance
     */
    private Product productFixture(String id) {
        return new Product(
                id,
                "Name-" + id,
                "category",
                "desc for " + id,
                "method",
                "certs",
                LocalDate.now().minusDays(3),
                "producer-" + id
        );
    }

    @Test
    @DisplayName("createPackage with valid inputs returns defensive copy and increases count")
    void createPackage_withValidInputs_returnsCreatedPackage() {
        List<Product> products = new ArrayList<>();
        products.add(productFixture("p1"));

        ProductPackage created = service.createPackage("  Bundle A  ", products);

        assertAll("createPackage",
                () -> assertNotNull(created.getId(), "Created package must have id"),
                () -> assertEquals("Bundle A", created.getName(), "Name must be trimmed/normalized"),
                () -> assertNotSame(products, created.getProducts(), "Returned package must use defensive copy of products"),
                () -> assertEquals(1, service.getPackageCount(), "Repository must contain created package")
        );
    }

    @Test
    @DisplayName("createPackage with null name throws ValidationException")
    void createPackage_withNullName_throwsValidationException() {
        assertThrows(ValidationException.class,
                () -> service.createPackage(null, Collections.emptyList()));
    }

    @Test
    @DisplayName("createPackage with null products throws ValidationException")
    void createPackage_withNullProducts_throwsValidationException() {
        assertThrows(ValidationException.class,
                () -> service.createPackage("Name", null));
    }

    @Test
    @DisplayName("addProductToPackage with null product throws ValidationException")
    void addProductToPackage_withNullProduct_throwsValidationException() {
        ProductPackage pkg = service.createPackage("P", new ArrayList<>());

        assertThrows(ValidationException.class,
                () -> service.addProductToPackage(pkg.getId(), null));
    }

    @Test
    @DisplayName("addProductToPackage with unknown package id throws NotFoundException")
    void addProductToPackage_withUnknownPackage_throwsNotFoundException() {
        Product p = productFixture("pX");
        assertThrows(NotFoundException.class,
                () -> service.addProductToPackage("non-existent-id", p));
    }

    @Test
    @DisplayName("addProductToPackage adds a product and returns updated defensive copy")
    void addProductToPackage_happyPath_updatesPackage() {
        Product p = productFixture("p2");
        ProductPackage pkg = service.createPackage("Pack", new ArrayList<>());

        ProductPackage updated = service.addProductToPackage(pkg.getId(), p);

        assertAll("addProduct",
                () -> assertEquals(1, updated.getProducts().size(), "Updated package must include the new product"),
                () -> assertTrue(service.packageContainsProduct(pkg.getId(), p.getId()), "Service must report product present"),
                () -> assertEquals(1, service.getProductCount(pkg.getId()), "Product count must reflect addition")
        );
    }

    @Test
    @DisplayName("removeProductFromPackage removes existing product and returns updated copy")
    void removeProductFromPackage_happyPath_removesProduct() {
        Product p = productFixture("p3");
        List<Product> initial = new ArrayList<>();
        initial.add(p);

        ProductPackage pkg = service.createPackage("PackR", initial);
        assertTrue(service.packageContainsProduct(pkg.getId(), p.getId()), "Setup must contain product");

        ProductPackage after = service.removeProductFromPackage(pkg.getId(), p.getId());

        assertAll("remove",
                () -> assertEquals(0, after.getProducts().size(), "Returned package must have no products"),
                () -> assertFalse(service.packageContainsProduct(pkg.getId(), p.getId()), "Product must be removed from package"),
                () -> assertEquals(0, service.getProductCount(pkg.getId()), "Product count must be zero after removal")
        );
    }

    @Test
    @DisplayName("removeProductFromPackage for missing product throws NotFoundException")
    void removeProductFromPackage_missingProduct_throwsNotFound() {
        ProductPackage pkg = service.createPackage("EmptyPack", new ArrayList<>());

        assertThrows(NotFoundException.class,
                () -> service.removeProductFromPackage(pkg.getId(), "no-such-product"));
    }

    @Test
    @DisplayName("updatePackageProducts replaces products list with defensive copies")
    void updatePackageProducts_replacesProductsList() {
        Product p1 = productFixture("p4");
        Product p2 = productFixture("p5");
        ProductPackage pkg = service.createPackage("PkgUp", Collections.singletonList(p1));

        ProductPackage updated = service.updatePackageProducts(pkg.getId(), Collections.singletonList(p2));

        assertAll("updateProducts",
                () -> assertEquals(1, updated.getProducts().size(), "Updated package must contain new list"),
                () -> assertEquals(p2.getId(), updated.getProducts().get(0).getId(), "Product id must match new product"),
                () -> assertTrue(service.packageContainsProduct(pkg.getId(), p2.getId()), "Service must reflect new product")
        );
    }

    @Test
    @DisplayName("updatePackageProducts with null list throws ValidationException")
    void updatePackageProducts_withNull_throwsValidationException() {
        ProductPackage pkg = service.createPackage("PkgX", new ArrayList<>());
        assertThrows(ValidationException.class,
                () -> service.updatePackageProducts(pkg.getId(), null));
    }

    @Test
    @DisplayName("updatePackageName updates name and normalizes it")
    void updatePackageName_updatesAndNormalizes() {
        ProductPackage pkg = service.createPackage("Orig", new ArrayList<>());

        ProductPackage updated = service.updatePackageName(pkg.getId(), "  New Name  ");

        assertEquals("New Name", updated.getName(), "Name must be trimmed/normalized");
    }

    @Test
    @DisplayName("updatePackageName with invalid name throws ValidationException")
    void updatePackageName_withInvalidName_throwsValidationException() {
        ProductPackage pkg = service.createPackage("Orig2", new ArrayList<>());
        assertThrows(ValidationException.class,
                () -> service.updatePackageName(pkg.getId(), "   "));
    }

    @Test
    @DisplayName("findPackageById returns defensive copy and unknown id throws NotFoundException")
    void findPackageById_returnsCopy_orThrows() {
        ProductPackage pkg = service.createPackage("FindMe", new ArrayList<>());

        ProductPackage fetched = service.findPackageById(pkg.getId());
        assertNotSame(pkg, fetched, "Returned package must be a defensive copy");
        assertEquals(pkg.getId(), fetched.getId(), "IDs must match");

        assertThrows(NotFoundException.class,
                () -> service.findPackageById("no-id"));
    }

    @Test
    @DisplayName("findPackagesByName is case-insensitive and returns matches, empty search returns empty list")
    void findPackagesByName_searchBehavior() {
        service.createPackage("Apple Bundle", new ArrayList<>());
        service.createPackage("apple pie pack", new ArrayList<>());
        service.createPackage("Banana", new ArrayList<>());

        List<ProductPackage> found = service.findPackagesByName("apple");
        assertEquals(2, found.size(), "Should find two packages containing 'apple'");

        List<ProductPackage> empty = service.findPackagesByName("   ");
        assertTrue(empty.isEmpty(), "Blank search term should return empty list");
    }

    @Test
    @DisplayName("getAllPackages returns unmodifiable list of defensive copies")
    void getAllPackages_returnsUnmodifiableList() {
        service.createPackage("A", new ArrayList<>());
        List<ProductPackage> all = service.getAllPackages();

        assertThrows(UnsupportedOperationException.class, () -> all.add(new ProductPackage()), "Returned list must be unmodifiable");
    }

    @Test
    @DisplayName("getProductCount and packageContainsProduct behave correctly and unknown package throws NotFoundException")
    void countsAndContainsBehavior() {
        Product p = productFixture("p6");
        ProductPackage pkg = service.createPackage("C", Collections.singletonList(p));

        assertEquals(1, service.getProductCount(pkg.getId()), "Product count must be correct");
        assertTrue(service.packageContainsProduct(pkg.getId(), p.getId()), "packageContainsProduct must return true for existing product");

        assertThrows(NotFoundException.class, () -> service.getProductCount("no-id"));
        assertThrows(NotFoundException.class, () -> service.packageContainsProduct("no-id", "x"));
    }

    @Nested
    @DisplayName("Validation helper tests")
    class ValidationTests {

        @Test
        @DisplayName("validatePackage throws when package is null")
        void validatePackage_throwsWhenNull() {
            assertThrows(ValidationException.class, () -> service.validatePackage(null));
        }

        @Test
        @DisplayName("validatePackage throws when package has no products")
        void validatePackage_throwsWhenNoProducts() {
            ProductPackage p = new ProductPackage("id-zz", "Name", Collections.emptyList());
            assertThrows(ValidationException.class, () -> service.validatePackage(p));
        }
    }

    @Test
    @DisplayName("calculatePackageValue returns product count as double (placeholder behavior)")
    void calculatePackageValue_returnsProductCountAsDouble() {
        ProductPackage p = service.createPackage("Val", Collections.emptyList());
        assertEquals(0.0, service.calculatePackageValue(p.getId()), 0.0);

        service.addProductToPackage(p.getId(), productFixture("p7"));
        assertEquals(1.0, service.calculatePackageValue(p.getId()), 0.0);
    }

    @Test
    @DisplayName("getPackageCount reflects number of packages in repository")
    void getPackageCount_reflectsNumberOfPackages() {
        int before = service.getPackageCount();
        service.createPackage("X1", new ArrayList<>());
        service.createPackage("X2", new ArrayList<>());

        assertEquals(before + 2, service.getPackageCount(), "Package count must increase after creation");
    }
}
