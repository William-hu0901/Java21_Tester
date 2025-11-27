package org.daodao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.function.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive Test Suite for Java 21 Major Features
 * This class consolidates all major Java 21 features with detailed comments
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class Java21NewFeaturesTest {
    
    // Sealed interfaces for testing - defined at class level
    sealed interface Shape permits Circle, Rectangle, Triangle {}
    final record Circle(double radius) implements Shape {
        public double area() { return Math.PI * radius * radius; }
    }
    final record Rectangle(double width, double height) implements Shape {
        public double area() { return width * height; }
    }
    final record Triangle(double base, double height) implements Shape {
        public double area() { return 0.5 * base * height; }
    }

    @BeforeEach
    void setUp() {
        log.info("Setting up Java 21 features test");
    }

    @AfterEach
    void tearDown() {
        log.info("Completed Java 21 features test");
    }

    /**
     * Java 21 Feature: Record Patterns
     * Record patterns allow destructuring of record objects in pattern matching
     */
    @Test
    @DisplayName("Test Record Patterns - Destructuring Records")
    void testRecordPatterns() {
        record Point(int x, int y) {}
        record Circle(Point center, int radius) {}
        
        Point point = new Point(10, 20);
        Circle circle = new Circle(point, 5);
        
        String result = switch (circle) {
            case Circle(Point(int x, int y), int r) -> 
                String.format("Circle at (%d,%d) with radius %d", x, y, r);
        };
        
        assertThat(result).isEqualTo("Circle at (10,20) with radius 5");
        log.debug("Record patterns test: {}", result);
    }

    /**
     * Java 21 Feature: Pattern Matching for switch
     * Enhanced switch expressions with type patterns and guards
     */
    @Test
    @DisplayName("Test Pattern Matching for switch - Type Patterns")
    void testPatternMatchingForSwitch() {
        Object[] values = {"Hello", 42, List.of("a", "b"), null};
        
        List<String> results = Arrays.stream(values)
            .map(value -> {
                if (value instanceof String s && s.length() > 3) {
                    return "Long string: " + s;
                } else if (value instanceof String s) {
                    return "Short string: " + s;
                } else if (value instanceof Integer i) {
                    return "Number: " + i;
                } else if (value instanceof List<?> list && !list.isEmpty()) {
                    return "List with " + list.size() + " items";
                } else if (value == null) {
                    return "Null value";
                } else {
                    return "Unknown type";
                }
            })
            .toList();
        
        assertThat(results).containsExactly("Long string: Hello", "Number: 42", 
                                          "List with 2 items", "Null value");
        log.debug("Pattern matching for switch results: {}", results);
    }

    /**
     * Java 21 Feature: Virtual Threads
     * Lightweight threads that enable high-concurrency applications
     */
    @Test
    @DisplayName("Test Virtual Threads - High Concurrency")
    void testVirtualThreads() throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> futures = IntStream.range(0, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    log.debug("Virtual thread {} executing", i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return "Task-" + i + "-" + Thread.currentThread().threadId();
                }, executor))
                .toList();
            
            List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();
            
            assertThat(results).hasSize(10);
            assertThat(results).allMatch(s -> s.startsWith("Task-"));
            
            log.debug("Virtual threads completed with {} results", results.size());
        }
    }

    /**
     * Java 21 Feature: Enhanced Switch Expressions
     * More expressive and concise switch statements
     */
    @Test
    @DisplayName("Test Enhanced Switch Expressions")
    void testEnhancedSwitchExpressions() {
        enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
        
        Map<Day, String> activities = Map.of(
            Day.MONDAY, "Meeting",
            Day.TUESDAY, "Coding",
            Day.WEDNESDAY, "Testing",
            Day.THURSDAY, "Review",
            Day.FRIDAY, "Deployment",
            Day.SATURDAY, "Rest",
            Day.SUNDAY, "Planning"
        );
        
        List<String> workDays = activities.entrySet().stream()
            .filter(entry -> switch (entry.getKey()) {
                case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> true;
                case SATURDAY, SUNDAY -> false;
            })
            .map(Map.Entry::getValue)
            .toList();
        
        assertThat(workDays).hasSize(5);
        assertThat(workDays).contains("Meeting", "Coding", "Testing", "Review", "Deployment");
        log.debug("Work days activities: {}", workDays);
    }

    /**
     * Java 21 Feature: Record Classes
     * Immutable data carriers with automatically generated methods
     */
    @Test
    @DisplayName("Test Record Classes - Immutable Data Carriers")
    void testRecordClasses() {
        record Department(String name, String location) {}
        record Employee(String name, int id, Department department) {}
        
        Department tech = new Department("Technology", "Floor 5");
        Employee emp1 = new Employee("Alice", 1001, tech);
        Employee emp2 = new Employee("Alice", 1001, tech);
        
        // Test automatic equals(), hashCode(), toString()
        assertThat(emp1).isEqualTo(emp2);
        assertThat(emp1.hashCode()).isEqualTo(emp2.hashCode());
        assertThat(emp1.toString()).contains("Alice");
        assertThat(emp1.toString()).contains("1001");
        
        // Test accessor methods
        assertThat(emp1.name()).isEqualTo("Alice");
        assertThat(emp1.id()).isEqualTo(1001);
        assertThat(emp1.department().name()).isEqualTo("Technology");
        
        log.debug("Record classes test completed for employee: {}", emp1);
    }

    /**
     * Java 21 Feature: Pattern Matching for instanceof
     * Simplified type checking and casting
     */
    @Test
    @DisplayName("Test Pattern Matching for instanceof")
    void testPatternMatchingForInstanceof() {
        Object[] objects = {"Hello World", 42, List.of("a", "b"), 3.14};
        
        List<String> results = new ArrayList<>();
        
        for (Object obj : objects) {
            if (obj instanceof String s && s.length() > 5) {
                results.add("Long string: " + s);
            } else if (obj instanceof String s) {
                results.add("Short string: " + s);
            } else if (obj instanceof Integer i && i > 40) {
                results.add("Large number: " + i);
            } else if (obj instanceof Integer i) {
                results.add("Small number: " + i);
            } else if (obj instanceof List<?> list && !list.isEmpty()) {
                results.add("List with " + list.size() + " elements");
            } else {
                results.add("Other type: " + obj.getClass().getSimpleName());
            }
        }
        
        assertThat(results).containsExactly("Long string: Hello World", "Large number: 42",
                                          "List with 2 elements", "Other type: Double");
        log.debug("Pattern matching for instanceof results: {}", results);
    }

    /**
     * Java 21 Feature: Stream API Enhancements
     * New stream operations and improvements
     */
    @Test
    @DisplayName("Test Stream API Enhancements")
    void testStreamAPIEnhancements() {
        // Test mapMulti for one-to-many transformations
        List<String> sentences = List.of(
            "Java 21 is powerful",
            "Stream API has new features",
            "Pattern matching is useful"
        );
        
        List<String> words = sentences.stream()
            .mapMulti((String sentence, Consumer<String> sink) -> {
                for (String word : sentence.split("\\s+")) {
                    if (word.length() > 3) {
                        sink.accept(word.toLowerCase());
                    }
                }
            })
            .distinct()
            .sorted()
            .toList();
        
        assertThat(words).contains("features", "java", "matching", "pattern", 
                                 "powerful", "stream", "useful");
        
        // Test takeWhile and dropWhile
        List<Integer> numbers = IntStream.range(1, 20).boxed().toList();
        
        List<Integer> smallNumbers = numbers.stream()
            .takeWhile(n -> n < 10)
            .toList();
        
        List<Integer> largeNumbers = numbers.stream()
            .dropWhile(n -> n < 10)
            .toList();
        
        assertThat(smallNumbers).isEqualTo(IntStream.range(1, 10).boxed().toList());
        assertThat(largeNumbers).isEqualTo(IntStream.range(10, 20).boxed().toList());
        
        log.debug("Stream API enhancements - words: {}, small numbers: {}, large numbers: {}", 
                 words.size(), smallNumbers.size(), largeNumbers.size());
    }

    /**
     * Java 21 Feature: Optional Enhancements
     * New methods for working with Optional
     */
    @Test
    @DisplayName("Test Optional Enhancements")
    void testOptionalEnhancements() {
        // Test Optional.or() method
        Optional<String> present = Optional.of("Hello");
        Optional<String> empty = Optional.empty();
        
        String result1 = present.or(() -> Optional.of("Default")).get();
        String result2 = empty.or(() -> Optional.of("Default")).get();
        
        assertThat(result1).isEqualTo("Hello");
        assertThat(result2).isEqualTo("Default");
        
        // Test Optional stream transformation
        List<Optional<String>> optionals = List.of(
            Optional.of("First"),
            Optional.empty(),
            Optional.of("Third"),
            Optional.empty(),
            Optional.of("Fifth")
        );
        
        List<String> presentValues = optionals.stream()
            .flatMap(Optional::stream)
            .toList();
        
        assertThat(presentValues).containsExactly("First", "Third", "Fifth");
        
        log.debug("Optional enhancements - present: {}, default: {}, stream: {}", 
                 result1, result2, presentValues);
    }

    /**
     * Java 21 Feature: Sealed Classes and Interfaces
     * Restricted inheritance hierarchies
     */
    @Test
    @DisplayName("Test Sealed Classes and Interfaces")
    void testSealedClasses() {
        List<Shape> shapes = List.of(
            new Circle(2.0),
            new Rectangle(3.0, 4.0),
            new Triangle(6.0, 8.0)
        );
        
        List<Double> areas = shapes.stream()
            .map(shape -> switch (shape) {
                case Circle(double r) -> Math.PI * r * r;
                case Rectangle(double w, double h) -> w * h;
                case Triangle(double b, double h) -> 0.5 * b * h;
            })
            .toList();
        
        assertThat(areas.get(0)).isEqualTo(Math.PI * 4.0);
        assertThat(areas.get(1)).isEqualTo(12.0);
        assertThat(areas.get(2)).isEqualTo(24.0);
        
        log.debug("Sealed classes areas: {}", areas);
    }

    /**
     * Java 21 Feature: Collection Factory Methods
     * Convenient creation of immutable collections
     */
    @Test
    @DisplayName("Test Collection Factory Methods")
    void testCollectionFactoryMethods() {
        // Test List.of()
        List<String> immutableList = List.of("Java", "Python", "JavaScript");
        
        // Test Set.of()
        Set<Integer> immutableSet = Set.of(1, 2, 3, 4, 5);
        
        // Test Map.of()
        Map<String, Integer> immutableMap = Map.of(
            "one", 1,
            "two", 2,
            "three", 3
        );
        
        // Test immutability
        assertThatThrownBy(() -> immutableList.add("Rust"))
            .isInstanceOf(UnsupportedOperationException.class);
        
        assertThatThrownBy(() -> immutableSet.add(6))
            .isInstanceOf(UnsupportedOperationException.class);
        
        assertThatThrownBy(() -> immutableMap.put("four", 4))
            .isInstanceOf(UnsupportedOperationException.class);
        
        // Test content
        assertThat(immutableList).containsExactly("Java", "Python", "JavaScript");
        assertThat(immutableSet).containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        assertThat(immutableMap).hasSize(3);
        assertThat(immutableMap.get("two")).isEqualTo(2);
        
        log.debug("Collection factory methods test completed");
    }

    /**
     * Java 21 Feature: Text Blocks (Refined)
     * Multi-line string literals with improved formatting
     */
    @Test
    @DisplayName("Test Text Blocks")
    void testTextBlocks() {
        String json = """
            {
                "name": "Java 21",
                "features": [
                    "Record Patterns",
                    "Virtual Threads",
                    "Pattern Matching"
                ],
                "version": 21
            }
            """;
        
        assertThat(json).contains("\"name\": \"Java 21\"");
        assertThat(json).contains("Record Patterns");
        assertThat(json).contains("\"version\": 21");
        
        // Test text block with formatting
        String query = """
            SELECT * FROM users 
            WHERE age > %d 
            AND status = '%s'
            ORDER BY name
            """.formatted(18, "active");
        
        assertThat(query).contains("age > 18");
        assertThat(query).contains("status = 'active'");
        
        log.debug("Text blocks test completed");
    }
}