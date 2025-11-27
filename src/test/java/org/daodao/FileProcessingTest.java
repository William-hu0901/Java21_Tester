package org.daodao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for file processing with Java 21 features
 */
@Slf4j
public class FileProcessingTest {
    
    // Sealed interfaces for testing
    sealed interface FileProcessor permits TextProcessor, JsonProcessor {}
    final class TextProcessor implements FileProcessor {
        public String process(String content) {
            return "Text: " + content.toUpperCase();
        }
    }
    final class JsonProcessor implements FileProcessor {
        public String process(String content) {
            return "JSON: " + content.replace("\n", " ").trim();
        }
    }

    private static Path testDir;
    private static Path testFile;

    @BeforeAll
    static void setUpClass() throws IOException {
        testDir = Files.createTempDirectory("java21-test-");
        testFile = testDir.resolve("test-data.txt");
        log.info("Created test directory: {}", testDir);
    }

    @AfterAll
    static void tearDownClass() throws IOException {
        // Clean up test files
        if (Files.exists(testFile)) {
            Files.delete(testFile);
        }
        if (Files.exists(testDir)) {
            Files.delete(testDir);
        }
        log.info("Cleaned up test directory: {}", testDir);
    }

    @BeforeEach
    void setUp() throws IOException {
        // Create a test file with sample data
        String content = """
            Line 1: Hello World
            Line 2: Java 21 Features
            Line 3: Record Patterns
            Line 4: Virtual Threads
            Line 5: String Templates
            """;
        Files.writeString(testFile, content);
        log.debug("Created test file: {}", testFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(testFile)) {
            Files.delete(testFile);
        }
        log.debug("Cleaned up test file");
    }

    /**
     * Java 21 Feature: Enhanced File Processing with Text Blocks and Stream API
     */
    @Test
    @DisplayName("Test File Processing with Text Blocks")
    void testFileProcessingWithTextBlocks() throws IOException {
        // Create a JSON file using text blocks
        Path jsonFile = testDir.resolve("data.json");
        String jsonContent = """
            {
                "name": "Java 21 Test",
                "features": [
                    "Record Patterns",
                    "Virtual Threads",
                    "String Templates"
                ],
                "version": 21
            }
            """;
        
        Files.writeString(jsonFile, jsonContent);
        
        // Read and process the file
        String content = Files.readString(jsonFile);
        assertThat(content).contains("\"name\": \"Java 21 Test\"");
        assertThat(content).contains("Record Patterns");
        assertThat(content).contains("\"version\": 21");
        
        log.debug("JSON file created and read successfully");
        
        // Clean up
        Files.delete(jsonFile);
    }

    /**
     * Java 21 Feature: File Processing with Record Patterns
     */
    @Test
    @DisplayName("Test File Processing with Record Patterns")
    void testFileProcessingWithRecordPatterns() throws IOException {
        record FileLine(int lineNumber, String content) {}
        
        // Read file and create record instances
        List<FileLine> lines = Files.lines(testFile)
            .map(line -> new FileLine(0, line)) // Simplified for demo
            .toList();
        
        assertThat(lines).hasSize(5);
        
        // Process with record patterns
        for (FileLine fileLine : lines) {
            if (fileLine instanceof FileLine(int num, String content)) {
                assertThat(content).isNotEmpty();
                log.debug("Processed line: {}", content);
            }
        }
        
        log.info("File processing with record patterns completed, processed {} lines", lines.size());
    }

    /**
     * Java 21 Feature: File Processing with Pattern Matching for switch
     */
    @Test
    @DisplayName("Test File Processing with Pattern Matching")
    void testFileProcessingWithPatternMatching() throws IOException {
        // Read file and process lines with pattern matching
        List<String> processedLines = Files.lines(testFile)
            .map(line -> processLineWithPatternMatching(line))
            .toList();
        
        assertThat(processedLines).hasSize(5);
        assertThat(processedLines.get(0)).contains("Processed:");
        
        log.info("File processing with pattern matching completed");
    }

    private String processLineWithPatternMatching(String line) {
        return switch (line) {
            case String s when s.contains("Java") -> "Java Feature: " + s;
            case String s when s.contains("Record") -> "Record Feature: " + s;
            case String s when s.contains("Virtual") -> "Concurrency Feature: " + s;
            case String s when s.contains("String") -> "String Feature: " + s;
            case String s -> "Processed: " + s;
            case null -> "Null line";
        };
    }

    /**
     * Java 21 Feature: File Processing with Stream API Enhancements
     */
    @Test
    @DisplayName("Test File Processing with Stream API")
    void testFileProcessingWithStreamAPI() throws IOException {
        // Use enhanced stream operations
        List<String> filteredLines = Files.lines(testFile)
            .filter(line -> !line.contains("Hello World"))
            .map(String::toUpperCase)
            .toList();
        
        assertThat(filteredLines).hasSize(4); // All lines except "Line 1: Hello World"
        
        // Use mapMulti for complex processing
        List<String> words = Files.lines(testFile)
            .mapMulti((String line, Consumer<String> sink) -> {
                String[] parts = line.split("\\s+");
                for (String part : parts) {
                    if (part.length() > 3) {
                        sink.accept(part.toLowerCase());
                    }
                }
            })
            .distinct()
            .sorted()
            .toList();
        
        assertThat(words).contains("features", "java", "patterns", "record", "string", "templates", "threads", "virtual");
        
        log.debug("Stream API processing completed, found {} unique words", words.size());
    }

    /**
     * Java 21 Feature: File Processing with Virtual Threads Simulation
     */
    @Test
    @DisplayName("Test Concurrent File Processing")
    void testConcurrentFileProcessing() throws Exception {
        // Create multiple test files
        List<Path> testFiles = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Path file = testDir.resolve("test-file-" + i + ".txt");
            Files.writeString(file, "Content of file " + i + "\nLine 2 of file " + i);
            testFiles.add(file);
        }
        
        // Process files concurrently (simulating virtual threads)
        List<CompletableFuture<Integer>> futures = testFiles.stream()
            .map(file -> CompletableFuture.supplyAsync(() -> {
                try {
                    return Files.lines(file).mapToInt(String::length).sum();
                } catch (IOException e) {
                    log.error("Error processing file: {}", file, e);
                    return 0;
                }
            }))
            .toList();
        
        // Wait for all to complete
        int totalLength = futures.stream()
            .mapToInt(CompletableFuture::join)
            .sum();
        
        assertThat(totalLength).isGreaterThan(0);
        
        // Clean up
        for (Path file : testFiles) {
            Files.delete(file);
        }
        
        log.info("Concurrent file processing completed, total length: {}", totalLength);
    }

    /**
     * Java 21 Feature: File Processing with Sealed Classes
     */
    @Test
    @DisplayName("Test File Processing with Sealed Classes")
    void testFileProcessingWithSealedClasses() throws IOException {
        // Process file with sealed classes
        String content = Files.readString(testFile);
        TextProcessor processor = new TextProcessor();
        String result = processor.process(content);
        
        assertThat(result).startsWith("Text:");
        assertThat(result).contains("HELLO WORLD");
        
        log.debug("File processing with sealed classes completed");
    }

    /**
     * Java 21 Feature: File Processing with Optional Enhancements
     */
    @Test
    @DisplayName("Test File Processing with Optional")
    void testFileProcessingWithOptional() throws IOException {
        // Find specific line using Optional
        Optional<String> javaLine = Files.lines(testFile)
            .filter(line -> line.contains("Java"))
            .findFirst();
        
        assertThat(javaLine).isPresent();
        assertThat(javaLine.get()).contains("Java 21");
        
        // Use Optional.or() for fallback
        Optional<String> nonExistentLine = Files.lines(testFile)
            .filter(line -> line.contains("NonExistent"))
            .findFirst()
            .or(() -> Optional.of("Default line"));
        
        assertThat(nonExistentLine).hasValue("Default line");
        
        log.debug("Optional processing completed");
    }

    /**
     * Java 21 Feature: File Processing with Collection Factory Methods
     */
    @Test
    @DisplayName("Test File Processing with Collection Factories")
    void testFileProcessingWithCollectionFactories() throws IOException {
        // Use collection factory methods for file processing
        Set<String> uniqueWords = Files.lines(testFile)
            .flatMap(line -> Arrays.stream(line.split("\\s+")))
            .filter(word -> word.length() > 3)
            .collect(Collectors.toSet());
        
        assertThat(uniqueWords).isNotEmpty();
        assertThat(uniqueWords).contains("Java", "Features", "Record", "Patterns");
        
        // Create immutable collections
        List<String> firstLines = Files.lines(testFile)
            .limit(3)
            .toList();
        
        Map<String, Integer> lineLengths = Map.of(
            firstLines.get(0), firstLines.get(0).length(),
            firstLines.get(1), firstLines.get(1).length(),
            firstLines.get(2), firstLines.get(2).length()
        );
        
        assertThat(lineLengths).hasSize(3);
        
        log.debug("Collection factory processing completed");
    }
}