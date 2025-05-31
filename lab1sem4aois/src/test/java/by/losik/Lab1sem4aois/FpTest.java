package by.losik.Lab1sem4aois;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class FpTest {
    @Test
    public void testFloat(){
        FloatingPoint floatingPoint = new FloatingPoint();
        floatingPoint.setSign(2);
        floatingPoint.setMantissa(new HashMap<>());
        floatingPoint.setExponent(new HashMap<>());
        Assert.assertTrue(floatingPoint.exponent.size() == 0 && floatingPoint.mantissa.size() == 0);
        Assert.assertEquals(0, floatingPoint.sign);
        OperationsImpl operations = new OperationsImpl();
        floatingPoint = new FloatingPoint().convertBinaryFixedToFloating(operations.parseDoubleToBinary(10));
        Assert.assertNotNull(floatingPoint.toString());
        Assert.assertEquals(floatingPoint.getDecimal(), 10, 0);
        Assert.assertEquals(operations.addFloatingPointNumbers(operations.parseDoubleToBinary(10), operations.parseDoubleToBinary(10)).getDecimal(), 20, 0);
    }
}
