package gist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

class SpelInListTest {

    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Test
    void testInList() {
        test("{1,2,3}.contains(5)", Boolean.FALSE);
        test("{1,2,3}.contains(2)", Boolean.TRUE);
        test("{1,2,3}.contains(2) AND false", Boolean.FALSE);
        test("{1,2,3}.contains(3)", Boolean.TRUE);
        test("{1,2,3}.size() == 3", Boolean.TRUE);
        test("{1,1,2,2,3,3,3}.stream().distinct().count() > 3", Boolean.FALSE);
    }

    private void test(String val, Boolean expected) {
        Expression exp = parser.parseExpression(val);
        Object message = exp.getValue();
        System.out.println(val + " =>" + message + " (expected="+expected+")");
        Assertions.assertEquals(message, expected);
    }
}
