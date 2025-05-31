package by.losik.Lab1sem4aois;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class OperationsTest {
    OperationsImpl operations = new OperationsImpl();
    @Test
    public void testBasicOperationsOnDoubles(){
        Assert.assertEquals(operations.addDoubles(1.0,3.5),4.5,0);
        Assert.assertEquals(operations.subtractDouble(1.0,3.5),-2.5,0);
        Assert.assertEquals(operations.multiplyDouble(1.0,3.5),3.5,0);
        Assert.assertEquals(String.valueOf(operations.divideDouble(1.0,3.5)),String.valueOf(Optional.ofNullable(0.2857142857142857)));
        Assert.assertEquals(operations.divideDouble(1.0,0),Optional.empty());
    }

    @Test
    public void testOperationsOnBinaryNumbers(){
        Assert.assertEquals(operations.addBinaryNumbers(operations.parseDoubleToBinary(1),operations.parseDoubleToBinary(-2)).getDecimal(),-1,0);
        Assert.assertEquals(operations.subtractBinaryNumbers(operations.parseDoubleToBinary(-3),operations.parseDoubleToBinary(-10)).getDecimal(),7,0);
        Assert.assertNull(operations.multiplyBinaryNumbers(operations.parseDoubleToBinary(10000),operations.parseDoubleToBinary(10000)));
        Assert.assertEquals(operations.parseDoubleToBinary(1).getDecimal(),1,0);
        Assert.assertEquals(operations.parseDoubleToBinary(0.5).getDecimal(),0.5,0);
        Assert.assertEquals(operations.parseDoubleToBinary(2).getDecimal(),2,0);
        Assert.assertEquals(operations.multiplyBinaryNumbers(operations.parseDoubleToBinary(2.5),operations.parseDoubleToBinary(2.5)).getDecimal(),6.25,0);
        Assert.assertEquals(operations.divideBinaryNumbers(operations.parseDoubleToBinary(1),operations.parseDoubleToBinary(0)),null);
        Assert.assertEquals(operations.divideBinaryNumbers(operations.parseDoubleToBinary(6), operations.parseDoubleToBinary(4)).getDecimal(),1.5,0.0001);
        Assert.assertEquals(3.49981689453125,operations.parseDoubleToBinary(3.49981689453125).getDecimal(),0.00001);
        Assert.assertEquals(Math.pow(-3.49981689453125,2),operations.multiplyBinaryNumbers(operations.parseDoubleToBinary(3.49981689453125), operations.parseDoubleToBinary(3.49981689453125)).getDecimal(), 0.000001);
        Assert.assertEquals(Math.pow(3.5,2),operations.multiplyBinaryNumbers(operations.parseDoubleToBinary(3.5), operations.parseDoubleToBinary(3.5)).getDecimal(), 0.000001);
        Assert.assertEquals(Math.pow(5,2),operations.multiplyBinaryNumbers(operations.parseDoubleToBinary(5), operations.parseDoubleToBinary(5)).getDecimal(), 0.000001);
        Assert.assertEquals(5.998046875,operations.subtractBinaryNumbers(operations.parseDoubleToBinary(6),operations.parseDoubleToBinary(0.001953125)).getDecimal(),0.0001);
        Assert.assertNotEquals(5.84765625,operations.subtractBinaryNumbers(operations.parseDoubleToBinary(6), operations.parseDoubleToBinary(operations.multiplyBinaryNumbers(operations.parseDoubleToBinary(4), operations.parseDoubleToBinary(9.1552734375E-4)).getDecimal())).getDecimal(), 0.00001);
        Assert.assertEquals(4,operations.multiplyBinaryNumbers(operations.parseDoubleToBinary(0.666666), operations.parseDoubleToBinary(6)).getDecimal(),0.001);
        Assert.assertNotEquals(operations.subtractBinaryNumbers(operations.parseDoubleToBinary(6), operations.parseDoubleToBinary(0.01318359375)).getDecimal(),3.8017578125,0.0001);
    }
}
