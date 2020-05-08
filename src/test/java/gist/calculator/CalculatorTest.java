package gist.calculator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumingThat;

/**
 * https://stackabuse.com/unit-testing-in-java-with-junit-5/
 */
@Slf4j
class CalculatorTest {

    Calculator calc;
    boolean bool;

    @BeforeAll
    static void start() {
        System.out.println("{inside @BeforeAll}");
    }

    @BeforeEach
    void init() {
        System.out.println("inside @BeforeEach");
        bool = false;
        calc = new Calculator();
    }

    @Test
    @DisplayName("Testing addition")
    void additionTest() {
        System.out.println("2+2");
        System.out.println("1+1");
        System.out.println("-1+1");

        assertAll(
                () -> assertEquals(4, calc.add(1,3), "Ths output should be the sum of the two arguments."),
                () -> assertEquals(2, calc.add(1,1), "Doesn't add two positive numbers properly"),
                () -> assertEquals(0, calc.add(-1,1), "Doesn't add a negative and a positive number properly"),
                () -> assertNotNull(calc, "The calc variable should be initialized")
        );
    }

    @Test
    @DisplayName("Testing division")
    void divisionTest() {
        assumeFalse(0 > 5, "This message won't be displayed, and the test will proceed");
        assumingThat(!bool, () -> System.out.println("\uD83D\uDC4C"));
        System.out.println("inside divisionTest");
        assertThrows(ArithmeticException.class, () -> calc.divide(2,0));
    }

    @AfterEach
    void afterEach() {
        System.out.println("inside @AfterEach");
    }

    @AfterAll
    static void close() {
        System.out.println("{inside @AfterAll}");
    }
}