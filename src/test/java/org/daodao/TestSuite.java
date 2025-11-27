package org.daodao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.platform.suite.api.*;

/**
 * Test Suite for Java 21 Features
 * This suite runs all tests related to Java 21 features and provides comprehensive logging.
 */
@Slf4j
@Suite
@SuiteDisplayName("Java 21 Features Test Suite")
@SelectClasses({
    BasicJava21Test.class,
    MockitoIntegrationTest.class,
    FileProcessingTest.class,
    Java21NewFeaturesTest.class
})
public class TestSuite {

    @BeforeAll
    static void setUpSuite() {
        log.info("=================================================");
        log.info("Starting Java 21 Features Test Suite");
        log.info("Java Version: {}", System.getProperty("java.version"));
        log.info("Java Vendor: {}", System.getProperty("java.vendor"));
        log.info("Java Home: {}", System.getProperty("java.home"));
        log.info("=================================================");
    }

    @AfterAll
    static void tearDownSuite() {
        log.info("=================================================");
        log.info("Java 21 Features Test Suite Completed");
        log.info("=================================================");
    }
}