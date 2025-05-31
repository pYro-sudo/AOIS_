package by.losik.Lab1sem4aois;

import java.util.*;

public class OperationsImpl implements Operations{
    @Override
    public BinaryNumber parseDoubleToBinary(double doubleInput) {
        if (doubleInput == 0.0) {
            BinaryNumber zero = new BinaryNumber();
            zero.setSign(0);
            for (int i = 15; i >= -15; i--) {
                zero.setBitAtPosition(i, 0);
            }
            return zero;
        }

        // Остальная часть метода остается без изменений
        if((int)Math.abs(doubleInput) > (int)Math.pow(2,16)){return null;}
        if(Math.abs(doubleInput) < Math.pow(2,-16)){return null;}
        BinaryNumber binaryNumber = new BinaryNumber();
        binaryNumber.setSign(doubleInput == Math.abs(doubleInput) ? 0 : 1);
        int intPart = (int)Math.abs(doubleInput);
        int index = 0;
        while (intPart > 0) {
            binaryNumber.setBitAtPosition(index++,Math.floorMod(intPart, 2));
            intPart = intPart / 2;
        }
        double fracPart = doubleInput - (int)Math.abs(doubleInput);
        for(int i=0;i<15;++i){
            binaryNumber.setBitAtPosition(-(i+1),Math.floorMod((int)(fracPart*2),2));
            fracPart = fracPart * 2;
        }
        for(int i = 15; i >= -15; --i){
            binaryNumber.getBinaryNumber().put(i,binaryNumber.getBinaryNumber().getOrDefault(i,0));
        }
        return binaryNumber;
    }

    @Override
    public BinaryNumber addBinaryNumbers(BinaryNumber firstBinArgument, BinaryNumber secondBinArgument) {
        BinaryNumber binaryNumber = new BinaryNumber();
        BinaryNumber firstBinInput = clone(firstBinArgument);
        BinaryNumber secondBinInput = clone(secondBinArgument);
        if(firstBinInput.getSign() == 1 && secondBinInput.getSign() == 0){
            firstBinInput.setSign(0);
            return subtractBinaryNumbers(secondBinInput, firstBinInput);
        }
        else if(secondBinInput.getSign() == 1 && firstBinInput.getSign() == 1){
            firstBinInput.setSign(0);
            secondBinInput.setSign(0);
            BinaryNumber resultBinary = addBinaryNumbers(firstBinInput, secondBinInput);
            resultBinary.setSign(1);
            return resultBinary;
        }
        else if(firstBinInput.getSign() == 0 && secondBinInput.getSign() == 1){
            secondBinInput.setSign(secondBinInput.getSign()+1);
            return subtractBinaryNumbers(firstBinInput, secondBinInput);
        }
        int remaining = 0;
        for(int i = -15; i<16;++i){
            if(firstBinInput.getBinaryNumber().get(i) + secondBinInput.getBinaryNumber().get(i) + remaining >= 2){
                binaryNumber.getBinaryNumber().put(i,Math.floorMod(firstBinInput.getBinaryNumber().get(i) + secondBinInput.getBinaryNumber().get(i) + remaining,2));
                remaining = Math.floorDiv(firstBinInput.getBinaryNumber().get(i) + secondBinInput.getBinaryNumber().get(i)+remaining,2);
            }
            else{
                binaryNumber.getBinaryNumber().put(i,firstBinInput.getBinaryNumber().get(i) + secondBinInput.getBinaryNumber().get(i) + remaining);
                remaining = 0;
            }
        }
        return binaryNumber;
    }

    @Override
    public BinaryNumber subtractBinaryNumbers(BinaryNumber firstBinArgument, BinaryNumber secondBinArgument) {
        BinaryNumber binaryNumber = new BinaryNumber();
        BinaryNumber firstBinInput = clone(firstBinArgument);
        BinaryNumber secondBinInput = clone(secondBinArgument);
        if(secondBinInput.getSign() == 1 && firstBinInput.getSign() == 0) {
            firstBinInput.setSign(1);
            BinaryNumber binaryNumber1 = addBinaryNumbers(firstBinInput, secondBinInput);
            binaryNumber1.setSign(1);
            return binaryNumber1;
        }
        else if(firstBinInput.getSign() == 1 && secondBinInput.getSign() == 0){
            firstBinInput.setSign(0);
            BinaryNumber binaryNumber1 = addBinaryNumbers(firstBinInput, secondBinInput);
            binaryNumber1.setSign(1);
            return binaryNumber1;
        }
        else if(secondBinInput.getSign() == 1 && firstBinInput.getSign() == 1){
            secondBinInput.setSign(secondBinInput.getSign()+1);
            firstBinInput.setSign(firstBinInput.getSign()+1);
            return subtractBinaryNumbers(secondBinInput, firstBinInput);
        }
        for(int i = -15; i<16;++i){
            if(firstBinInput.getBinaryNumber().get(i) - secondBinInput.getBinaryNumber().get(i) < 0){
                binaryNumber.setBitAtPosition(i,1);
                int k = 1;
                try{
                    while(firstBinInput.getBinaryNumber().get(i+k) != 1){
                        k++;
                    }
                }
                catch (Exception e){
                    BinaryNumber binaryNumber1 = subtractBinaryNumbers(secondBinInput, firstBinInput);
                    binaryNumber1.setSign(1);
                    return binaryNumber1;
                }
                firstBinInput.getBinaryNumber().replace(i+k,0);
                --k;
                while(k>0){
                    firstBinInput.getBinaryNumber().replace(i+k,1);
                    --k;
                }
            }
            else{
                binaryNumber.getBinaryNumber().put(i,firstBinInput.getBinaryNumber().get(i) - secondBinInput.getBinaryNumber().get(i));
            }
        }

        return binaryNumber;
    }

    @Override
    public BinaryNumber multiplyBinaryNumbers(BinaryNumber firstBinArgument, BinaryNumber secondBinArgument) {
        BinaryNumber firstBinInput = clone(firstBinArgument);
        BinaryNumber secondBinInput = clone(secondBinArgument);
        int exp1 = 15;
        int exp2 = 15;
        while(firstBinInput.getBinaryNumber().get(exp1) == 0){
            --exp1;
        }
        while (secondBinInput.getBinaryNumber().get(exp2) == 0){
            --exp2;
        }
        if(exp1+exp2-1>15){
            return null;
        }
        List<HashMap<Integer, Integer>> hashMapList = new ArrayList<>();
        for(int j = 0;j<31;++j){
            hashMapList.add(new HashMap<>());
            for(int i = -15;i<16;++i){
                hashMapList.get(hashMapList.size() - 1).put(i,
                        firstBinInput.getBinaryNumber().get(i) * secondBinInput.getBinaryNumber().get(j - 15));
            }
        }
        HashMap<Integer, Integer> multiplicationResult = new HashMap<>();
        int hashMapIndex = -30;
        int remaining = 0;
        int horCount = -15;
        int vertCount = 0;
        while(horCount < 16){
            int diagonalSum = 0;
            int xCount = horCount;
            int yCount = vertCount;
            while(xCount > -16 && yCount < hashMapList.size()){
                diagonalSum += hashMapList.get(yCount).get(xCount);
                xCount--;
                yCount++;
            }
            if(diagonalSum + remaining >= 2){
                multiplicationResult.put(hashMapIndex++,Math.floorMod(diagonalSum + remaining,2));
                remaining = Math.floorDiv(diagonalSum+remaining,2);
            }
            else{
                multiplicationResult.put(hashMapIndex++, diagonalSum + remaining);
                remaining = 0;
            }
            horCount++;
        }
        vertCount = 1;
        horCount = 15;
        while(vertCount < hashMapList.size()){
            int diagonalSum1 = 0;
            int xCount = horCount;
            int yCount = vertCount;
            while(xCount > -16 && yCount < hashMapList.size()){
                diagonalSum1 += hashMapList.get(yCount).get(xCount);
                xCount--;
                yCount++;
            }
            if(diagonalSum1 + remaining >= 2){
                multiplicationResult.put(hashMapIndex++,Math.floorMod(diagonalSum1 + remaining,2));
                remaining = Math.floorDiv(diagonalSum1+remaining,2);
            }
            else{
                multiplicationResult.put(hashMapIndex++, diagonalSum1 + remaining);
                remaining = 0;
            }
            vertCount++;
        }
        while(remaining != 0){
            multiplicationResult.put(hashMapIndex++,Math.floorMod(remaining,2));
            remaining = Math.floorDiv(remaining,2);
        }
        BinaryNumber binaryNumber = new BinaryNumber();
        binaryNumber.setBinaryNumber(multiplicationResult);
        binaryNumber.setSign(Math.floorMod(firstBinInput.getSign()+secondBinInput.getSign(),2));
        return binaryNumber;
    }

    @Override
    public BinaryNumber divideBinaryNumbers(BinaryNumber firstBinArgument, BinaryNumber secondBinArgument) {
        if(secondBinArgument == null || firstBinArgument == null){
            return null;
        }
        boolean isZeroDivisor = true;
        for(Map.Entry<Integer,Integer> entry : secondBinArgument.getBinaryNumber().entrySet()){
            if(entry.getValue() != 0){
                isZeroDivisor = false;
            }
        }
        if(isZeroDivisor){
            return null;
        }
        BinaryNumber firstBinInput = clone(firstBinArgument);
        BinaryNumber secondBinInput = clone(secondBinArgument);
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(-15,1);
        for(int i=15;i>-16;--i){
            hashMap.putIfAbsent(i,0);
        }
        BinaryNumber theMinimal = new BinaryNumber();
        theMinimal.setBinaryNumber(hashMap);
        BinaryNumber divisionResult = new BinaryNumber();
        divisionResult.setBinaryNumber(hashMap);
        int sign = Math.floorMod(firstBinInput.getSign() + secondBinInput.getSign(),2);
        firstBinInput.setSign(0);
        secondBinInput.setSign(0);
        divisionResult.setSign(0);
        while(!isZero(subtractBinaryNumbers(firstBinInput,multiplyBinaryNumbers(secondBinInput,divisionResult)))){
            divisionResult = addBinaryNumbers(divisionResult,theMinimal);
        }
        divisionResult.setSign(sign);
        return divisionResult;
    }

    @Override
    public double addDoubles(double firstDoubleInput, double secondDoubleInput) {
        return firstDoubleInput + secondDoubleInput;
    }

    @Override
    public double subtractDouble(double firstDoubleInput, double secondDoubleInput) {
        return firstDoubleInput - secondDoubleInput;
    }

    @Override
    public Optional<Double> divideDouble(double firstDoubleInput, double secondDoubleInput) {
        return Optional.ofNullable(secondDoubleInput == 0 ? null : firstDoubleInput / secondDoubleInput);
    }

    @Override
    public double multiplyDouble(double firstDoubleInput, double secondDoubleInput) {
        return firstDoubleInput * secondDoubleInput;
    }

    @Override
    public boolean isZero(BinaryNumber binaryNumber) {
        for(int i = 15; i > -15;--i){
            if(binaryNumber.getBinaryNumber().get(i) == 1){
                return false;
            }
        }
        return true;
    }

    @Override
    public BinaryNumber clone(BinaryNumber cloneableBinaryNumber){
        BinaryNumber binaryNumber = new BinaryNumber();
        binaryNumber.setSign(cloneableBinaryNumber.getSign());
        for(int i = 15; i > -16; --i){
            binaryNumber.setBitAtPosition(i,cloneableBinaryNumber.getBinaryNumber().get(i));
        }
        return binaryNumber;
    }

    @Override
    public FloatingPoint addFloatingPointNumbers(BinaryNumber first, BinaryNumber second) {
        FloatingPoint result = new FloatingPoint();
        FloatingPoint fp1 = new FloatingPoint().convertBinaryFixedToFloating(first);
        FloatingPoint fp2 = new FloatingPoint().convertBinaryFixedToFloating(second);

        System.out.println("First: " + fp1);
        System.out.println("Second: " + fp2);

        if (fp1.getSign() != fp2.getSign()) {
            if (fp1.getSign() == 1) {
                fp1.setSign(0);
                return new FloatingPoint().convertBinaryFixedToFloating(subtractBinaryNumbers(second, first));
            } else {
                fp2.setSign(0);
                return new FloatingPoint().convertBinaryFixedToFloating(subtractBinaryNumbers(first, second));
            }
        }

        result.setSign(fp1.getSign());

        int exp1 = fp1.getExponentValue() - 127;
        int exp2 = fp2.getExponentValue() - 127;

        int expDiff = Math.abs(exp1 - exp2);
        FloatingPoint larger = exp1 >= exp2 ? fp1 : fp2;
        FloatingPoint smaller = exp1 >= exp2 ? fp2 : fp1;

        result.exponent = new HashMap<>(larger.exponent);

        HashMap<Integer, Integer> alignedMantissa = new HashMap<>();
        alignedMantissa.put(0, 1);
        for (int i = -1; i >= -23; i--) {
            alignedMantissa.put(i, smaller.mantissa.getOrDefault(i, 0));
        }

        for (int i = 0; i < expDiff; i++) {
            for (int j = -23; j < 0; j++) {
                alignedMantissa.put(j, alignedMantissa.getOrDefault(j + 1, 0));
            }
            alignedMantissa.put(0, 0);
        }

        HashMap<Integer, Integer> largerMantissa = new HashMap<>();
        largerMantissa.put(0, 1);
        for (int i = -1; i >= -23; i--) {
            largerMantissa.put(i, larger.mantissa.getOrDefault(i, 0));
        }

        int carry = 0;
        for (int i = -23; i <= 0; i++) {
            int sum = largerMantissa.getOrDefault(i, 0) +
                    alignedMantissa.getOrDefault(i, 0) +
                    carry;
            result.mantissa.put(i, sum % 2);
            carry = sum / 2;
        }

        if (carry == 1) {
            for (int i = -23; i < 0; i++) {
                result.mantissa.put(i, result.mantissa.getOrDefault(i + 1, 0));
            }
            result.mantissa.put(0, 1);
            result.incrementExponent();
        } else {
            int firstOnePos = 0;
            for (int i = 0; i >= -23; i--) {
                if (result.mantissa.getOrDefault(i, 0) == 1) {
                    firstOnePos = i;
                    break;
                }
            }

            if (firstOnePos < 0) {
                int shift = -firstOnePos;
                for (int i = -23; i <= -shift; i++) {
                    result.mantissa.put(i, result.mantissa.getOrDefault(i + shift, 0));
                }
                for (int i = -shift + 1; i <= 0; i++) {
                    result.mantissa.put(i, 0);
                }
                result.setExponentValue(result.getExponentValue() - shift);
            }
        }

        result.mantissa.remove(0);

        return result;
    }
}
