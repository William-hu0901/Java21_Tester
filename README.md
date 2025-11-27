# Java21_Tester

A comprehensive test project for Java 21 features, demonstrating the latest capabilities of the Java platform.

## Features Tested

### Java 21 New Features
- **Record Patterns** - Pattern matching for record instances
- **Pattern Matching for switch** - Enhanced switch statements with pattern matching
- **String Templates** (Preview) - Convenient string creation with embedded expressions
- **Unnamed Patterns and Variables** - Using underscore for unused variables
- **Unnamed Classes and Instance Main Methods** - Simplified main method structure
- **Scoped Values** - Immutable data sharing within and across threads
- **Structured Concurrency** - Simplified concurrent programming
- **Virtual Threads** - Lightweight threads for high-throughput applications
- **Foreign Function & Memory API** (Third Preview) - Safe access to code outside JVM
- **Vector API** (Sixth Incubator) - Optimal vector instructions

### Enhanced Existing Features
- Enhanced Switch Expressions
- Text Blocks
- Sealed Classes
- Record Classes
- Pattern Matching for instanceof
- Stream API Enhancements
- Optional Enhancements
- Collection Factory Methods

## Project Structure

```
src/
├── test/
│   ├── java/
│   │   └── org/
│   │       └── daodao/
│   │           ├── Java21FeaturesTest.java    # Main feature tests
│   │           ├── MockitoIntegrationTest.java # Mockito integration tests
│   │           ├── FileProcessingTest.java     # File processing tests
│   │           └── TestSuite.java              # Test suite with logging
│   └── resources/
│       ├── logback-test.xml    # Logging configuration
│       ├── test-data.txt       # Test data file
│       └── sample.json         # Sample JSON for testing
└── pom.xml                     # Maven configuration
```

## Dependencies

- **JUnit 5** (5.10.1) - Testing framework
- **Mockito** (5.8.0) - Mocking framework
- **Lombok** (1.18.30) - Code generation
- **SLF4J** (2.0.9) - Logging facade
- **Logback** (1.4.14) - Logging implementation
- **AssertJ** (3.24.2) - Fluent assertions

## Running Tests

### Prerequisites
- Java 21 or later
- Maven 3.6.0 or later

### Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=Java21FeaturesTest

# Run test suite
mvn test -Dtest=TestSuite

# Run with verbose output
mvn test -X
```

## Test Coverage

The project includes comprehensive tests for:

1. **Core Java 21 Features** - All major new features with detailed examples
2. **Mockito Integration** - Demonstrates mocking with Java 21 features
3. **File Processing** - Real-world scenarios using new Java 21 capabilities
4. **Error Handling** - Proper exception handling and edge cases
5. **Logging** - Comprehensive logging using SLF4J and Logback

## Configuration

### Maven Configuration
- Java 21 source and target compatibility
- Preview features enabled for Java 21
- All dependencies are compatible and conflict-free

### Logging Configuration
- Console appender with pattern formatting
- DEBUG level for project classes
- INFO level for root logger

## Notes

- Some features (String Templates, Foreign Function & Memory API) are preview features and may require explicit enabling
- Virtual Threads and Structured Concurrency are simulated where direct implementation isn't available
- All tests are designed to be stable and pass consistently
- The project follows best practices for test organization and naming

## Building

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package (if needed)
mvn package
```

This project serves as both a learning resource and a validation suite for Java 21 features.
