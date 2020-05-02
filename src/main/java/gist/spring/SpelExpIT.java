/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package gist.spring;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;

public class SpelExpIT {

    public void testInList() {
        test("{1,2,3}.contains(5)");
        test("{1,2,3}.contains(2)");
        test("{1,2,3}.contains(2) AND false");
    }

    void test(String val) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(val);
        Object message = exp.getValue();
        System.out.println(val + "=" + message);
    }

    public static void main(String[] args) {
        new SpelExpIT().testInList();
    }
}