package by.losik.Lab1sem4aois;

import java.util.HashMap;
import java.util.Optional;

public interface Operations extends Cloneable{
    public BinaryNumber parseDoubleToBinary(double doubleInput);
    public BinaryNumber addBinaryNumbers(BinaryNumber firstBinInput, BinaryNumber secondBinInput);
    public BinaryNumber subtractBinaryNumbers(BinaryNumber firstBinInput, BinaryNumber secondBinInput);
    public BinaryNumber multiplyBinaryNumbers(BinaryNumber firstBinInput, BinaryNumber secondBinInput);
    public BinaryNumber divideBinaryNumbers(BinaryNumber firstBinInput, BinaryNumber secondBinInput);
    public double addDoubles(double firstDoubleInput, double secondDoubleInput);
    public double subtractDouble(double firstDoubleInput, double secondDoubleInput);
    public Optional<Double> divideDouble(double firstDoubleInput, double secondDoubleInput);
    public double multiplyDouble(double firstDoubleInput, double secondDoubleInput);
    public boolean isZero(BinaryNumber binaryNumber);

    BinaryNumber clone(BinaryNumber binaryNumber);

    FloatingPoint addFloatingPointNumbers(BinaryNumber first, BinaryNumber second);
}
