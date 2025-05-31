package by.losik.Lab1sem4aois;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class BinaryTest {
    BinaryNumber binaryNumber = new BinaryNumber();
    @Test
    public void testSign(){
        binaryNumber.setSign(-1);
        Assert.assertTrue(binaryNumber.getSign()==1);
        binaryNumber.setSign(-2);
        Assert.assertTrue(binaryNumber.getSign()==0);
        binaryNumber.setSign(1);
        Assert.assertTrue(binaryNumber.getSign()==1);
        binaryNumber.setSign(2);
        Assert.assertTrue(binaryNumber.getSign()==0);
    }

    @Test
    public void testSettingBitsAtPosition(){
        binaryNumber.getBinaryNumber().put(1,1);
        Assert.assertTrue(binaryNumber.getBinaryNumber().get(1) == 1);
    }

    @Test
    public void testBinaryHashMap(){
        Assert.assertTrue(new OperationsImpl().parseDoubleToBinary(10.00).getSign() == 0);
        Assert.assertTrue(new OperationsImpl().parseDoubleToBinary(10.00).getBinaryNumber().get(1) == 1);
        Assert.assertTrue(new OperationsImpl().parseDoubleToBinary(10.00).getBinaryNumber().get(-1) == 0);
        Assert.assertTrue(new OperationsImpl().parseDoubleToBinary(10.00).getBinaryNumber().get(-2) == 0);
        Assert.assertTrue(new OperationsImpl().parseDoubleToBinary(10.00).getBinaryNumber().get(3) == 1);
    }

    @Test
    public void testDecimal(){
        HashMap<Integer, Integer> testMap = new HashMap<>();
        testMap.put(0,1);
        testMap.put(1,1);
        binaryNumber.setBinaryNumber(testMap);
        binaryNumber.setSign(-1);
        Assert.assertEquals(binaryNumber.getDecimal(),-3,0);
        HashMap<Integer, Integer> testMapForFractional = new HashMap<>();
        testMapForFractional.put(-1,1);
        binaryNumber.setBinaryNumber(testMapForFractional);
        Assert.assertTrue(binaryNumber.getDecimal()==-0.5);
    }

    @Test
    public void testInverse(){
        HashMap<Integer, Integer> testMap = new HashMap<>();
        testMap.put(0,1);
        testMap.put(1,1);
        binaryNumber.setBinaryNumber(testMap);
        binaryNumber.setSign(-1);
        Assert.assertEquals(binaryNumber.inverseOfTheBinaryNumber().getDecimal(),65535.99996948242,0.000000000001);
    }

    @Test
    public void testAdditionOfTheNumber(){
        HashMap<Integer, Integer> testMap = new HashMap<>();
        testMap.put(0,1);
        testMap.put(1,1);
        binaryNumber.setBinaryNumber(testMap);
        binaryNumber.setSign(-1);
        Assert.assertEquals(binaryNumber.additionOfTheBinaryNumber().getDecimal(),65535.999938964844,0.000000000001);
    }
}
