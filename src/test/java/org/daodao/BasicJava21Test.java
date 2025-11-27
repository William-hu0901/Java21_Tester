package org.daodao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Basic Test class for Java 21 features
 */
@Slf4j
public class BasicJava21Test {

    @BeforeEach
    void setUp() {
        log.info("Setting up test for Java 21 features");
    }

    @AfterEach
    void tearDown() {
        log.info("Tearing down test for Java 21 features");
    }

    /**
     * Java 21 Feature: Record Patterns
     */
    @Test
    @DisplayName("Test Record Patterns")
    void testRecordPatterns() {
        record Point(int x, int y) {}
        
        Point point = new Point(10, 20);
        
        if (point instanceof Point(int x, int y)) {
            assertThat(x).isEqualTo(10);
            assertThat(y).isEqualTo(20);
            log.debug("Record pattern matching successful: x={}, y={}", x, y);
        }
    }

    /**
     * Java 21 Feature: Pattern Matching for switch
     */
    @Test
    @DisplayName("Test Pattern Matching for switch")
    void testPatternMatchingForSwitch() {
        Object obj = "Hello World";
        
        String result = switch (obj) {
            case String s when s.length() > 5 -> "Long string: " + s;
            case String s -> "Short string: " + s;
            case Integer i -> "Number: " + i;
            case null -> "Null value";
            default -> "Unknown type";
        };
        
        assertThat(result).isEqualTo("Long string: Hello World");
        log.debug("Pattern matching for switch result: {}", result);
    }

    /**
     * Test Virtual Threads
     */
    @Test
    @DisplayName("Test Virtual Threads")
    void testVirtualThreads() throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<String> results = new ArrayList<>();
            
            for (int i = 0; i < 5; i++) {
                final int taskId = i;
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    log.debug("Virtual thread {} running", taskId);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return "Task-" + taskId;
                }, executor);
                results.add(future.get());
            }
            
            assertThat(results).hasSize(5);
            assertThat(results).contains("Task-0", "Task-4");
            
            log.debug("Virtual threads test completed with {} results", results.size());
        }
    }

    /**
     * Test Enhanced Switch Expressions
     */
    @Test
    @DisplayName("Test Enhanced Switch Expressions")
    void testEnhancedSwitchExpressions() {
        int day = 1; // Monday
        
        int workLoad = switch (day) {
            case 1, 2, 3, 4, 5 -> 8;
            case 6, 7 -> 0;
            default -> 0;
        };
        
        assertThat(workLoad).isEqualTo(8);
        log.debug("Work load for day {}: {} hours", day, workLoad);
    }

    /**
     * Test Record Classes
     */
    @Test
    @DisplayName("Test Record Classes")
    void testRecordClasses() {
        record Person(String name, int age) {}
        
        Person person = new Person("Alice", 30);
        
        assertThat(person.name()).isEqualTo("Alice");
        assertThat(person.age()).isEqualTo(30);
        assertThat(person.toString()).contains("Alice");
        assertThat(person.toString()).contains("30");
        
        log.debug("Record test completed for person: {}", person);
    }

    /**
     * Test Pattern Matching for instanceof
     */
    @Test
    @DisplayName("Test Pattern Matching for instanceof")
    void testPatternMatchingForInstanceof() {
        Object obj = "Hello World";
        
        if (obj instanceof String s && s.length() > 5) {
            assertThat(s).isEqualTo("Hello World");
            assertThat(s.length()).isGreaterThan(5);
            log.debug("Pattern matching for instanceof successful: {}", s);
        }
        
        obj = 42;
        if (obj instanceof Integer i) {
            assertThat(i).isEqualTo(42);
            log.debug("Integer pattern matching successful: {}", i);
        }
    }

    /**
     * Test Stream API Enhancements
     */
    @Test
    @DisplayName("Test Stream API Enhancements")
    void testStreamAPIEnhancements() {
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date");
        
        // Test toList() method (Java 16)
        List<String> filtered = words.stream()
            .filter(s -> s.length() > 5)
            .toList();
        
        assertThat(filtered).containsExactly("banana", "cherry");
        log.debug("Stream toList() result: {}", filtered);
    }

    /**
     * Test Collection Factory Methods
     */
    @Test
    @DisplayName("Test Collection Factory Methods")
    void testCollectionFactoryMethods() {
        // Test List.of() (Java 9)
        List<String> list = List.of("a", "b", "c");
        assertThat(list).hasSize(3);
        assertThat(list).contains("a", "b", "c");
        
        // Test Set.of() (Java 9)
        Set<String> set = Set.of("x", "y", "z");
        assertThat(set).hasSize(3);
        assertThat(set).contains("x", "y", "z");
        
        // Test Map.of() (Java 9)
        Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
        assertThat(map).hasSize(3);
        assertThat(map.get("one")).isEqualTo(1);
        assertThat(map.get("two")).isEqualTo(2);
        assertThat(map.get("three")).isEqualTo(3);
        
        log.debug("Collection factory methods test completed");
    }
}