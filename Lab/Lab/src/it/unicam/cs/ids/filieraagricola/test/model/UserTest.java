package it.unicam.cs.ids.filieraagricola.test.model;

import it.unicam.cs.ids.filieraagricola.model.User;
import it.unicam.cs.ids.filieraagricola.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the User class.
 * Tests all constructors, methods, validation logic, and edge cases.
 */
@DisplayName("User Class Tests")
class UserTest {

    private User testUser;
    private static final int VALID_ID = 1;
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PASSWORD = "password123";
    private static final String VALID_EMAIL = "john.doe@example.com";

    @BeforeEach
    void setUp() {
        testUser = new User(VALID_ID, VALID_NAME, VALID_PASSWORD, VALID_EMAIL, UserRole.GENERIC_USER);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should initialize with default values")
        void testDefaultConstructor() {
            User user = new User();

            assertEquals(0, user.getId());
            assertNull(user.getName());
            assertNull(user.getPassword());
            assertNull(user.getEmail());
            assertNotNull(user.getPermissions());
            assertEquals(0, user.getPermissions().length);
        }

        @Test
        @DisplayName("Full constructor should initialize all fields correctly")
        void testFullConstructor() {
            User user = new User(VALID_ID, VALID_NAME, VALID_PASSWORD, VALID_EMAIL, UserRole.GENERIC_USER);

            assertEquals(VALID_ID, user.getId());
            assertEquals(VALID_NAME, user.getName());
            assertEquals(VALID_PASSWORD, user.getPassword());
            assertEquals(VALID_EMAIL, user.getEmail());
            assertNotNull(user.getPermissions());
            assertEquals(0, user.getPermissions().length);
        }

        @Test
        @DisplayName("Full constructor should trim name and email")
        void testFullConstructorTrimsFields() {
            String nameWithSpaces = "  " + VALID_NAME + "  ";
            String emailWithSpaces = "  " + VALID_EMAIL + "  ";

            User user = new User(VALID_ID, nameWithSpaces, VALID_PASSWORD, emailWithSpaces, UserRole.GENERIC_USER);

            assertEquals(VALID_NAME, user.getName());
            assertEquals(VALID_EMAIL, user.getEmail());
        }

        @Test
        @DisplayName("Copy constructor should create deep copy")
        void testCopyConstructor() {
            testUser.setPermissions(new String[]{"read", "write"});

            User copiedUser = new User(testUser);

            assertEquals(testUser.getId(), copiedUser.getId());
            assertEquals(testUser.getName(), copiedUser.getName());
            assertEquals(testUser.getPassword(), copiedUser.getPassword());
            assertEquals(testUser.getEmail(), copiedUser.getEmail());
            assertArrayEquals(testUser.getPermissions(), copiedUser.getPermissions());

            // Verify deep copy - modifying original shouldn't affect copy
            testUser.addPermission("admin");
            assertNotEquals(testUser.getPermissions().length, copiedUser.getPermissions().length);
        }

        @Test
        @DisplayName("Copy constructor should throw exception for null user")
        void testCopyConstructorWithNull() {
            assertThrows(NullPointerException.class, () -> new User(null));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Constructor should reject negative ID")
        void testConstructorRejectsNegativeId() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(-1, VALID_NAME, VALID_PASSWORD, VALID_EMAIL, UserRole.GENERIC_USER)
            );
            assertEquals("User id cannot be negative", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Constructor should reject invalid names")
        void testConstructorRejectsInvalidNames(String invalidName) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(VALID_ID, invalidName, VALID_PASSWORD, VALID_EMAIL, UserRole.GENERIC_USER)
            );
            assertEquals("User name cannot be null or empty", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Constructor should reject invalid passwords")
        void testConstructorRejectsInvalidPasswords(String invalidPassword) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(VALID_ID, VALID_NAME, invalidPassword, VALID_EMAIL, UserRole.GENERIC_USER)
            );
            assertEquals("Password cannot be null or empty", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Constructor should reject invalid emails")
        void testConstructorRejectsInvalidEmails(String invalidEmail) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new User(VALID_ID, VALID_NAME, VALID_PASSWORD, invalidEmail, UserRole.GENERIC_USER)
            );
            assertEquals("Email cannot be null or empty", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("ID getter and setter should work correctly")
        void testIdGetterSetter() {
            assertEquals(VALID_ID, testUser.getId());

            testUser.setId(42);
            assertEquals(42, testUser.getId());

            testUser.setId(0);
            assertEquals(0, testUser.getId());
        }

        @Test
        @DisplayName("ID setter should reject negative values")
        void testIdSetterRejectsNegative() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> testUser.setId(-1)
            );
            assertEquals("User id cannot be negative", exception.getMessage());
        }

        @Test
        @DisplayName("Name getter and setter should work correctly")
        void testNameGetterSetter() {
            assertEquals(VALID_NAME, testUser.getName());

            String newName = "Jane Smith";
            testUser.setName(newName);
            assertEquals(newName, testUser.getName());

            // Test trimming
            testUser.setName("  Trimmed Name  ");
            assertEquals("Trimmed Name", testUser.getName());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("Name setter should reject invalid values")
        void testNameSetterRejectsInvalid(String invalidName) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> testUser.setName(invalidName)
            );
            assertEquals("User name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Password getter and setter should work correctly")
        void testPasswordGetterSetter() {
            assertEquals(VALID_PASSWORD, testUser.getPassword());

            String newPassword = "newPassword456";
            testUser.setPassword(newPassword);
            assertEquals(newPassword, testUser.getPassword());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Password setter should reject invalid values")
        void testPasswordSetterRejectsInvalid(String invalidPassword) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> testUser.setPassword(invalidPassword)
            );
            assertEquals("Password cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Email getter and setter should work correctly")
        void testEmailGetterSetter() {
            assertEquals(VALID_EMAIL, testUser.getEmail());

            String newEmail = "jane.smith@example.com";
            testUser.setEmail(newEmail);
            assertEquals(newEmail, testUser.getEmail());

            // Test trimming
            testUser.setEmail("  trimmed@email.com  ");
            assertEquals("trimmed@email.com", testUser.getEmail());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("Email setter should reject invalid values")
        void testEmailSetterRejectsInvalid(String invalidEmail) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> testUser.setEmail(invalidEmail)
            );
            assertEquals("Email cannot be null or empty", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Permissions Management Tests")
    class PermissionsTests {

        @Test
        @DisplayName("Permissions should return defensive copy")
        void testPermissionsDefensiveCopy() {
            String[] permissions = {"read", "write"};
            testUser.setPermissions(permissions);

            String[] retrieved = testUser.getPermissions();
            retrieved[0] = "modified";

            // Original permissions should be unchanged
            assertArrayEquals(new String[]{"read", "write"}, testUser.getPermissions());
        }

        @Test
        @DisplayName("Set permissions should handle null input")
        void testSetPermissionsWithNull() {
            testUser.setPermissions(null);

            String[] permissions = testUser.getPermissions();
            assertNotNull(permissions);
            assertEquals(0, permissions.length);
        }

        @Test
        @DisplayName("Set permissions should create defensive copy")
        void testSetPermissionsDefensiveCopy() {
            String[] original = {"read", "write"};
            testUser.setPermissions(original);

            original[0] = "modified";

            // User's permissions should be unchanged
            assertArrayEquals(new String[]{"read", "write"}, testUser.getPermissions());
        }

        @Test
        @DisplayName("Add permission should work correctly")
        void testAddPermission() {
            testUser.addPermission("admin");

            String[] permissions = testUser.getPermissions();
            assertEquals(1, permissions.length);
            assertEquals("admin", permissions[0]);

            testUser.addPermission("moderator");
            permissions = testUser.getPermissions();
            assertEquals(2, permissions.length);
            assertEquals("admin", permissions[0]);
            assertEquals("moderator", permissions[1]);
        }

        @Test
        @DisplayName("Add permission should trim input")
        void testAddPermissionTrims() {
            testUser.addPermission("  admin  ");

            String[] permissions = testUser.getPermissions();
            assertEquals("admin", permissions[0]);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("Add permission should reject invalid values")
        void testAddPermissionRejectsInvalid(String invalidPermission) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> testUser.addPermission(invalidPermission)
            );
            assertEquals("Permission cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Remove permission should work correctly")
        void testRemovePermission() {
            testUser.setPermissions(new String[]{"read", "write", "admin"});

            assertTrue(testUser.removePermission("write"));
            assertArrayEquals(new String[]{"read", "admin"}, testUser.getPermissions());

            assertFalse(testUser.removePermission("nonexistent"));
            assertArrayEquals(new String[]{"read", "admin"}, testUser.getPermissions());
        }

        @Test
        @DisplayName("Remove permission should handle null input")
        void testRemovePermissionWithNull() {
            testUser.setPermissions(new String[]{"read", "write"});

            assertFalse(testUser.removePermission(null));
            assertArrayEquals(new String[]{"read", "write"}, testUser.getPermissions());
        }

        @Test
        @DisplayName("Remove permission should handle empty permissions")
        void testRemovePermissionEmptyArray() {
            assertFalse(testUser.removePermission("admin"));
        }

        @Test
        @DisplayName("Remove permission should trim input")
        void testRemovePermissionTrims() {
            testUser.setPermissions(new String[]{"admin"});

            assertTrue(testUser.removePermission("  admin  "));
            assertEquals(0, testUser.getPermissions().length);
        }
    }

    @Nested
    @DisplayName("Prototype Pattern Tests")
    class PrototypeTests {

        @Test
        @DisplayName("Clone should create identical copy")
        void testClone() {
            testUser.setPermissions(new String[]{"read", "write"});

            User cloned = testUser.clone();

            assertEquals(testUser.getId(), cloned.getId());
            assertEquals(testUser.getName(), cloned.getName());
            assertEquals(testUser.getPassword(), cloned.getPassword());
            assertEquals(testUser.getEmail(), cloned.getEmail());
            assertArrayEquals(testUser.getPermissions(), cloned.getPermissions());
        }

        @Test
        @DisplayName("Clone should create independent copy")
        void testCloneIndependence() {
            testUser.setPermissions(new String[]{"read"});

            User cloned = testUser.clone();

            // Modify original
            testUser.setName("Modified Name");
            testUser.addPermission("admin");

            // Clone should be unchanged
            assertEquals(VALID_NAME, cloned.getName());
            assertEquals(1, cloned.getPermissions().length);
            assertEquals("read", cloned.getPermissions()[0]);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Equals should work with ID-based comparison")
        void testEqualsWithId() {
            User user1 = new User(1, "Name1", "pass1", "email1@test.com", UserRole.GENERIC_USER);
            User user2 = new User(1, "Name2", "pass2", "email2@test.com", UserRole.GENERIC_USER);
            User user3 = new User(2, "Name1", "pass1", "email1@test.com", UserRole.GENERIC_USER);

            assertEquals(user1, user2); // Same ID
            assertNotEquals(user1, user3); // Different ID
        }

        @Test
        @DisplayName("Equals should fall back to email when ID is zero")
        void testEqualsWithEmailFallback() {
            User user1 = new User(0, "Name1", "pass1", "same@email.com", UserRole.GENERIC_USER);
            User user2 = new User(0, "Name2", "pass2", "same@email.com", UserRole.GENERIC_USER);
            User user3 = new User(0, "Name1", "pass1", "different@email.com", UserRole.GENERIC_USER);

            assertEquals(user1, user2); // Same email, both ID = 0
            assertNotEquals(user1, user3); // Different email
        }

        @Test
        @DisplayName("Equals should handle null and different types")
        void testEqualsEdgeCases() {
            assertNotEquals(testUser, null);
            assertNotEquals(testUser, "not a user");
            assertEquals(testUser, testUser); // Reflexive
        }

        @Test
        @DisplayName("HashCode should be consistent with equals")
        void testHashCodeConsistency() {
            User user1 = new User(1, "Name1", "pass1", "email1@test.com", UserRole.GENERIC_USER);
            User user2 = new User(1, "Name2", "pass2", "email2@test.com", UserRole.GENERIC_USER);

            assertEquals(user1, user2);
            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        @DisplayName("HashCode should use email when ID is zero")
        void testHashCodeWithEmailFallback() {
            User user1 = new User(0, "Name1", "pass1", "same@email.com", UserRole.GENERIC_USER);
            User user2 = new User(0, "Name2", "pass2", "same@email.com", UserRole.GENERIC_USER);

            assertEquals(user1.hashCode(), user2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString should include key fields but not password")
        void testToString() {
            testUser.setPermissions(new String[]{"read", "write"});

            String result = testUser.toString();

            assertTrue(result.contains("id=" + VALID_ID));
            assertTrue(result.contains("name='" + VALID_NAME + "'"));
            assertTrue(result.contains("email='" + VALID_EMAIL + "'"));
            assertTrue(result.contains("permissions=[read, write]"));
            assertFalse(result.contains(VALID_PASSWORD)); // Password should not be in toString
        }

        @Test
        @DisplayName("ToString should handle null permissions")
        void testToStringWithNullPermissions() {
            User user = new User();
            String result = user.toString();

            assertTrue(result.contains("permissions=[]"));
        }
    }
}
