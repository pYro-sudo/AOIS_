package by.losik.Lab1sem4aois;

import java.util.HashMap;

public class FloatingPoint extends BinaryNumber{
    protected HashMap<Integer, Integer> exponent = new HashMap<>();
    protected HashMap<Integer, Integer> mantissa = new HashMap<>();

    public int getExponentValue() {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            value += this.exponent.getOrDefault(i, 0) * (1 << i);
        }
        return value;
    }
    public void setExponentValue(int value) {
        this.exponent.clear();
        int i = 0;
        while (value > 0 && i < 8) {
            this.exponent.put(i, value % 2);
            value /= 2;
            i++;
        }
        // Заполняем оставшиеся биты нулями
        for (; i < 8; i++) {
            this.exponent.put(i, 0);
        }
    }
    public void incrementExponent() {
        int currentValue = getExponentValue();
        setExponentValue(currentValue + 1);
    }
    public void decrementExponent() {
        int currentValue = getExponentValue();
        setExponentValue(currentValue - 1);
    }
    public void setExponent(HashMap<Integer, Integer> exponent) {
        this.exponent = exponent;
    }

    public HashMap<Integer, Integer> getExponent() {
        return exponent;
    }
    public void setMantissa(HashMap<Integer, Integer> mantissa) {
        this.mantissa = mantissa;
    }

    public HashMap<Integer, Integer> getMantissa() {
        return mantissa;
    }

    @Override
    public void setSign(int sign) {
        super.setSign(sign);
    }

    @Override
    public double getDecimal() {
        int biasedExponent = getExponentValue();
        double fraction = 0.0;
        for (int i = -1; i >= -23; i--) {
            fraction += mantissa.getOrDefault(i, 0) * Math.pow(2, i);
        }
        double m = 1.0 + fraction; // Учитываем скрытый бит
        int actualExp = biasedExponent - 127;
        return sign == 0 ? m * Math.pow(2, actualExp) : -m * Math.pow(2, actualExp);
    }

    public FloatingPoint convertBinaryFixedToFloating(BinaryNumber binaryNumber) {
        FloatingPoint floatingPoint = new FloatingPoint();

        if (binaryNumber == null) {
            for (int i = 0; i < 8; i++) {
                floatingPoint.exponent.put(i, 0);
            }
            for (int i = -1; i >= -23; i--) {
                floatingPoint.mantissa.put(i, 0);
            }
            floatingPoint.setSign(0);
            return floatingPoint;
        }

        int exp = 15;
        while(exp >= -16 && binaryNumber.getBinaryNumber().getOrDefault(exp, 0) != 1) {
            --exp;
        }

        if (exp < -16) {
            for (int i = 0; i < 8; i++) {
                floatingPoint.exponent.put(i, 0);
            }
            for (int i = -1; i >= -23; i--) {
                floatingPoint.mantissa.put(i, 0);
            }
            floatingPoint.setSign(binaryNumber.getSign());
            return floatingPoint;
        }

        int biasedExponent = exp + 127;
        int index = 0;
        int temp = biasedExponent;
        while (temp != 0 && index < 8) {
            floatingPoint.exponent.put(index++, temp % 2);
            temp = temp / 2;
        }
        for (int i = 0; i < 8; i++) {
            floatingPoint.exponent.putIfAbsent(i, 0);
        }
        int currentBit = exp - 1;
        for (int i = -1; i >= -23; i--) {
            if (currentBit >= -16) {
                floatingPoint.mantissa.put(i, binaryNumber.getBinaryNumber().getOrDefault(currentBit, 0));
            } else {
                floatingPoint.mantissa.put(i, 0);
            }
            currentBit--;
        }
        floatingPoint.setSign(binaryNumber.getSign());
        return floatingPoint;
    }

    @Override
    public String toString(){
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("[").append(sign).append(" ");
        for (int i = 7; i >= 0; i--) {
            stringBuffer.append(exponent.getOrDefault(i, 0));
        }
        stringBuffer.append(" ");
        for (int i = -1; i >= -23; i--) {
            stringBuffer.append(mantissa.getOrDefault(i, 0));
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
