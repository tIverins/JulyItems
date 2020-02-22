package github.july_summer.julyitems.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

public class Util {

    public static int getRandom(int min, int max){
        if(max < min || (max == 0 && min == 0))
            return 0;

        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    public static boolean isChance(int chance)
    {
        return getRandom(0, 100) < chance;
    }

    public static boolean isNumber(String str){
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static String addColor(String str){
        StringBuilder sb = new StringBuilder();
        for(char c : str.toCharArray()){
            sb.append("§" + c);
        }
        return sb.toString();
    }

    public static String removeColor(String str){
        return str.replace("§", "");
    }

    public static int objectToInteger(Object obj){
        return Integer.parseInt(String.valueOf(obj));
    }

    public static boolean objectToBoolean(Object obj){
        return Boolean.parseBoolean(String.valueOf(obj));
    }

    public static String toBinaryString(String str){
        StringBuilder sb = new StringBuilder();
        char[] c = str.toCharArray();
        for(char c1 : c){
            sb.append(Integer.toBinaryString(c1) + " ");
        }

        return sb.toString();
    }

    public static String binaryToString(String binary) {
        String[] tempStr = binary.split(" ");
        char[] tempChar = new char[tempStr.length];
        for (int i = 0; i < tempStr.length; i++) {
            //System.out.print(BinstrToChar(tempStr[i]));
            tempChar[i] = BinstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }


    //将二进制字符串转换成int数组
    public static int[] BinstrToIntArray(String binStr) {
        char[] temp = binStr.toCharArray();
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            result[i] = temp[i] - 48;
        }
        return result;
    }

    //将二进制转换成字符
    public static char BinstrToChar(String binStr) {
        int[] temp = BinstrToIntArray(binStr);
        int sum = 0;
        for (int i = 0; i < temp.length; i++) {
            sum += temp[temp.length - 1 - i] << i;
        }
        return (char) sum;
    }

    //将数组转换成String
    public static String argsToString(Object[] args){
        StringBuilder sb = new StringBuilder();
        Arrays.asList(args).forEach(data -> sb.append(data + " "));
        return sb.toString().substring(0, sb.toString().length() - 1);
    }


}
