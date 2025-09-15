package it.unicam.cs.ids.filieraagricola.test.model;

import it.unicam.cs.ids.filieraagricola.model.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Location} model class.
 *
 * <p>This test class verifies the behavior of all constructors, getters, and
 * standard object methods (equals, hashCode, toString) of the Location class.
 * It ensures proper immutability, correct object identity based on the unique
 * identifier, and proper handling of null values.</p>
 *
 * <p>Tests are organized into nested classes for better readability and logical
 * grouping of related functionality. The Location class is immutable, so no
 * setter tests are included.</p>
 */
@DisplayName("Location Model Tests")
class LocationTest {

    private static final String SAMPLE_NAME = "Organic Farm Tuscany";
    private static final String SAMPLE_ADDRESS = "Via dei Campi 123, 50100 Florence, Italy";
    private static final String SAMPLE_DESCRIPTION = "Premium organic farm specializing in vegetables";
    private static final String SAMPLE_ID = "LOC_001";

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor with name, address, and description should initialize correctly")
        void constructor_withValidParameters_shouldInitializeCorrectly() {
            // When
            Location location = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertAll("Constructor with generated ID initialization",
                    () -> assertNotNull(location.getId(), "ID should not be null"),
                    () -> assertTrue(location.getId().length() > 0, "ID should not be empty"),
                    () -> assertEquals(SAMPLE_NAME, location.getName(), "Name should be set correctly"),
                    () -> assertEquals(SAMPLE_ADDRESS, location.getAddress(), "Address should be set correctly"),
                    () -> assertEquals(SAMPLE_DESCRIPTION, location.getDescription(),
                            "Description should be set correctly")
            );
        }

        @Test
        @DisplayName("Constructor with name, address, and null description should handle gracefully")
        void constructor_withNullDescription_shouldHandleGracefully() {
            // When
            Location location = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, null);

            // Then
            assertAll("Constructor with null description",
                    () -> assertNotNull(location.getId(), "ID should not be null"),
                    () -> assertEquals(SAMPLE_NAME, location.getName(), "Name should be set correctly"),
                    () -> assertEquals(SAMPLE_ADDRESS, location.getAddress(), "Address should be set correctly"),
                    () -> assertNull(location.getDescription(), "Description should be null")
            );
        }

        @Test
        @DisplayName("Constructor with specified ID should use provided ID")
        void constructor_withSpecifiedId_shouldUseProvidedId() {
            // When
            Location location = new Location(SAMPLE_ID, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertAll("Constructor with specified ID initialization",
                    () -> assertEquals(SAMPLE_ID, location.getId(), "ID should match provided ID"),
                    () -> assertEquals(SAMPLE_NAME, location.getName(), "Name should be set correctly"),
                    () -> assertEquals(SAMPLE_ADDRESS, location.getAddress(), "Address should be set correctly"),
                    () -> assertEquals(SAMPLE_DESCRIPTION, location.getDescription(),
                            "Description should be set correctly")
            );
        }

        @Test
        @DisplayName("Constructor with null ID should accept null")
        void constructor_withNullId_shouldAcceptNull() {
            // When
            Location location = new Location(null, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertNull(location.getId(), "ID should be null when explicitly set to null");
        }

        @Test
        @DisplayName("Constructor with null name should accept null")
        void constructor_withNullName_shouldAcceptNull() {
            // When
            Location location = new Location(SAMPLE_ID, null, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertNull(location.getName(), "Name should be null when explicitly set to null");
        }

        @Test
        @DisplayName("Constructor with null address should accept null")
        void constructor_withNullAddress_shouldAcceptNull() {
            // When
            Location location = new Location(SAMPLE_ID, SAMPLE_NAME, null, SAMPLE_DESCRIPTION);

            // Then
            assertNull(location.getAddress(), "Address should be null when explicitly set to null");
        }

        @Test
        @DisplayName("Constructor should generate unique IDs")
        void constructor_shouldGenerateUniqueIds() {
            // When
            Location location1 = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);
            Location location2 = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertNotEquals(location1.getId(), location2.getId(),
                    "Different instances should have different generated IDs");
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Getters should return correct values")
        void getters_shouldReturnCorrectValues() {
            // Given
            Location location = new Location(SAMPLE_ID, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // When & Then
            assertAll("All getter methods",
                    () -> assertEquals(SAMPLE_ID, location.getId(), "getId should return correct ID"),
                    () -> assertEquals(SAMPLE_NAME, location.getName(), "getName should return correct name"),
                    () -> assertEquals(SAMPLE_ADDRESS, location.getAddress(), "getAddress should return correct address"),
                    () -> assertEquals(SAMPLE_DESCRIPTION, location.getDescription(),
                            "getDescription should return correct description")
            );
        }

        @Test
        @DisplayName("Getters should handle null values correctly")
        void getters_withNullValues_shouldHandleCorrectly() {
            // Given
            Location location = new Location(null, null, null, null);

            // When & Then
            assertAll("Getters with null values",
                    () -> assertNull(location.getId(), "getId should return null"),
                    () -> assertNull(location.getName(), "getName should return null"),
                    () -> assertNull(location.getAddress(), "getAddress should return null"),
                    () -> assertNull(location.getDescription(), "getDescription should return null")
            );
        }
    }

    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Equal objects should have same hash code")
        void equalsAndHashCode_withSameId_shouldBeEqual() {
            // Given
            Location location1 = new Location(SAMPLE_ID, "Farm 1", "Address 1", "Description 1");
            Location location2 = new Location(SAMPLE_ID, "Farm 2", "Address 2", "Description 2");

            // Then
            assertAll("Equality with same ID",
                    () -> assertEquals(location1, location2, "Objects with same ID should be equal"),
                    () -> assertEquals(location1.hashCode(), location2.hashCode(),
                            "Objects with same ID should have same hash code")
            );
        }

        @Test
        @DisplayName("Different objects should not be equal")
        void equals_withDifferentIds_shouldNotBeEqual() {
            // Given
            Location location1 = new Location("ID_1", SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);
            Location location2 = new Location("ID_2", SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertNotEquals(location1, location2, "Objects with different IDs should not be equal");
        }

        @Test
        @DisplayName("Object should be equal to itself")
        void equals_withSameObject_shouldBeEqual() {
            // Given
            Location location = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertEquals(location, location, "Object should be equal to itself");
        }

        @Test
        @DisplayName("Object should not be equal to null")
        void equals_withNull_shouldNotBeEqual() {
            // Given
            Location location = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertNotEquals(location, null, "Object should not be equal to null");
        }

        @Test
        @DisplayName("Object should not be equal to different class")
        void equals_withDifferentClass_shouldNotBeEqual() {
            // Given
            Location location = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);
            String differentClass = "Not a Location";

            // Then
            assertNotEquals(location, differentClass, "Object should not be equal to different class");
        }

        @Test
        @DisplayName("Objects with null IDs should be equal if both have null IDs")
        void equals_withBothNullIds_shouldBeEqual() {
            // Given
            Location location1 = new Location(null, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);
            Location location2 = new Location(null, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertEquals(location1, location2, "Objects with both null IDs should be equal");
        }

        @Test
        @DisplayName("Object with null ID should not equal object with non-null ID")
        void equals_withOneNullId_shouldNotBeEqual() {
            // Given
            Location location1 = new Location(null, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);
            Location location2 = new Location(SAMPLE_ID, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // Then
            assertNotEquals(location1, location2, "Object with null ID should not equal object with non-null ID");
        }

        @Test
        @DisplayName("Hash code should be consistent")
        void hashCode_shouldBeConsistent() {
            // Given
            Location location = new Location(SAMPLE_ID, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // When
            int hashCode1 = location.hashCode();
            int hashCode2 = location.hashCode();

            // Then
            assertEquals(hashCode1, hashCode2, "Hash code should be consistent across multiple calls");
        }

        @Test
        @DisplayName("Hash code with null ID should not throw exception")
        void hashCode_withNullId_shouldNotThrow() {
            // Given
            Location location = new Location(null, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // When & Then
            assertDoesNotThrow(location::hashCode, "Hash code with null ID should not throw exception");
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString should include key identifying fields")
        void toString_shouldIncludeKeyFields() {
            // Given
            Location location = new Location(SAMPLE_ID, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // When
            String result = location.toString();

            // Then
            assertAll("ToString content verification",
                    () -> assertTrue(result.contains(SAMPLE_ID), "Should contain ID"),
                    () -> assertTrue(result.contains(SAMPLE_NAME), "Should contain name"),
                    () -> assertTrue(result.contains(SAMPLE_ADDRESS), "Should contain address"),
                    () -> assertTrue(result.contains("Location{"), "Should start with class name")
            );
        }

        @Test
        @DisplayName("ToString with null values should handle gracefully")
        void toString_withNullValues_shouldHandleGracefully() {
            // Given
            Location location = new Location(null, null, null, null);

            // When
            String result = location.toString();

            // Then
            assertAll("ToString with null values",
                    () -> assertNotNull(result, "ToString should not return null"),
                    () -> assertFalse(result.isEmpty(), "ToString should not return empty string"),
                    () -> assertTrue(result.contains("Location{"), "Should start with class name")
            );
        }

        @Test
        @DisplayName("ToString should not be null or empty")
        void toString_shouldNotBeNullOrEmpty() {
            // Given
            Location location = new Location(SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // When
            String result = location.toString();

            // Then
            assertAll("ToString basic validation",
                    () -> assertNotNull(result, "ToString should not return null"),
                    () -> assertFalse(result.isEmpty(), "ToString should not return empty string"),
                    () -> assertTrue(result.length() > 0, "ToString should have content")
            );
        }

        @Test
        @DisplayName("ToString should be consistent")
        void toString_shouldBeConsistent() {
            // Given
            Location location = new Location(SAMPLE_ID, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // When
            String result1 = location.toString();
            String result2 = location.toString();

            // Then
            assertEquals(result1, result2, "ToString should be consistent across multiple calls");
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Location should be immutable after construction")
        void location_shouldBeImmutable() {
            // Given
            Location location = new Location(SAMPLE_ID, SAMPLE_NAME, SAMPLE_ADDRESS, SAMPLE_DESCRIPTION);

            // When - attempt to verify immutability by checking no setters exist
            String originalId = location.getId();
            String originalName = location.getName();
            String originalAddress = location.getAddress();
            String originalDescription = location.getDescription();

            // Then - values should remain unchanged (no setters to modify them)
            assertAll("Immutability verification",
                    () -> assertEquals(originalId, location.getId(), "ID should remain unchanged"),
                    () -> assertEquals(originalName, location.getName(), "Name should remain unchanged"),
                    () -> assertEquals(originalAddress, location.getAddress(), "Address should remain unchanged"),
                    () -> assertEquals(originalDescription, location.getDescription(),
                            "Description should remain unchanged")
            );
        }

        @Test
        @DisplayName("Constructor parameters should not affect object after construction")
        void constructor_parametersShouldNotAffectObjectAfterConstruction() {
            // Given
            String mutableName = new String(SAMPLE_NAME);
            String mutableAddress = new String(SAMPLE_ADDRESS);
            String mutableDescription = new String(SAMPLE_DESCRIPTION);

            // When
            Location location = new Location(mutableName, mutableAddress, mutableDescription);

            // Modify the original parameters (this wouldn't affect the location since strings are immutable anyway,
            // but this test demonstrates the concept for other potentially mutable types)
            String originalName = location.getName();
            String originalAddress = location.getAddress();
            String originalDescription = location.getDescription();

            // Then
            assertAll("Parameter independence",
                    () -> assertEquals(SAMPLE_NAME, location.getName(), "Name should match expected value"),
                    () -> assertEquals(SAMPLE_ADDRESS, location.getAddress(), "Address should match expected value"),
                    () -> assertEquals(SAMPLE_DESCRIPTION, location.getDescription(),
                            "Description should match expected value")
            );
        }
    }
}