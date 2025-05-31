package by.losik.Lab1sem4aois;

import java.util.HashMap;
import java.util.Map;

public class BinaryNumber {
    protected int sign;
    private HashMap<Integer, Integer> binaryNumber = new HashMap<>();

    public int getSign() {
        return sign;
    }

    public double getDecimal(){
        double decimalNumber = 0;
        for(Map.Entry<Integer, Integer> entry : binaryNumber.entrySet()){
            decimalNumber += Math.pow(2, entry.getKey())*entry.getValue();
        }
        return sign == 0 ? decimalNumber : -decimalNumber;
    }

    public void setSign(int sign) {
        this.sign = Math.floorMod(sign,2);
    }

    public HashMap<Integer, Integer> getBinaryNumber() {
        return binaryNumber;
    }

    public void setBinaryNumber(HashMap<Integer, Integer> binaryNumber) {
        this.binaryNumber = binaryNumber;
    }

    public void setBitAtPosition(Integer position, Integer value){
        if(!(value == 0 || value == 1)){
            return;
        }
        this.binaryNumber.put(position, value);
    }

    public BinaryNumber inverseOfTheBinaryNumber(){
        BinaryNumber binaryNumber = new BinaryNumber();
        binaryNumber.setSign(Math.floorMod(this.sign+1,2));
        for(Map.Entry<Integer,Integer> entry : this.binaryNumber.entrySet()){
            binaryNumber.setBitAtPosition(entry.getKey(), Math.floorMod(entry.getValue()+1,2));
        }
        for(int i = 15; i >= -15; --i){
            binaryNumber.getBinaryNumber().put(i,this.binaryNumber.getOrDefault(i,1));
        }
        return binaryNumber;
    }
    public BinaryNumber additionOfTheBinaryNumber(){
        BinaryNumber binaryNumber = inverseOfTheBinaryNumber();
        binaryNumber.getBinaryNumber().replace(-15,Math.floorMod(binaryNumber.getBinaryNumber().get(-15)+1,2));
        return binaryNumber;
    }

    @Override
    public String toString(){
        StringBuilder stringView = new StringBuilder();
        stringView.append("[");
        stringView.append(sign).append(" ");
        for(int i = 15;i>-16;--i){
            stringView.append(binaryNumber.get(i));
        }
        stringView.append("]");
        return String.valueOf(stringView);
    }
}
