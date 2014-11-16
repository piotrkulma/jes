package com.jes.utils;

/**
 * Created by Piotrek on 2014-11-16.
 */
public class CommonUtils {
    public static int[] getByteArray(int data, int bitLen) {
        int diff = 0;
        int[] array = new int[bitLen];

        String bin = Integer.toBinaryString(data);

        diff = bitLen - bin.length();

        if(diff > 0) {
            bin = addZerosBefore(bin, diff);
        }

        for(int i=0; i<bin.length(); i++) {
            array[i] = (bin.charAt(i) - 48);
        }

        return array;
    }

    public static String addZerosBefore(String s, int zeros) {
        for(int i=0; i<zeros; i++) {
            s = "0" + s;
        }

        return s;
    }

    public static String byteArrayToString(byte array[]) {
        String data = "[";

        if(array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                data += array[i] + ", ";
            }

            data = data.substring(0, data.length() - 2);
        }

        data+= "]";

        return data;
    }

    public static String byteArrayToString(int array[]) {
        String data = "[";

        if(array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                data += array[i] + ", ";
            }

            data = data.substring(0, data.length() - 2);
        }

        data+= "]";

        return data;
    }
}
