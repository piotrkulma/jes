package com.jes.utils;

/**
 * Created by Piotr Kulma on 2014-11-23.
 */
public final class BinaryMath {
    public static final int DEFAULT_BYTE_LEN = 8;

    public static byte add(byte opA, byte opB) {
        return (byte)(opA + opB);
    }

    //TODO to BDC conversion
    public static byte bcd(byte op) {
        return op;
    }

    public static int combineTwoBytes(byte msb, byte lsb) {
        int[] adr = CommonUtils.combineTwoArrays(getBinaryArray(msb), getBinaryArray(lsb));
        return binaryToDecimal(adr);
    }

    public static int binaryToDecimal(int[] bcdArray) {
        int result = 0;
        int exp = bcdArray.length - 1;
        for(int i=0; i<bcdArray.length; i++) {
            result += (bcdArray[i] * Math.pow(2, exp - i));
        }

        return result;
    }

    public static int[] getBinaryArray(int data, int bitLen) {
        int diff = 0;
        int[] array = new int[bitLen];

        if(data < 0) {
            data = data + 256;
        }

        String bin = Integer.toBinaryString(data);

        diff = bitLen - bin.length();

        if(diff > 0) {
            bin = CommonUtils.addZerosBefore(bin, diff);
        }

        for(int i=0; i<bin.length(); i++) {
            array[i] = (bin.charAt(i) - 48);
        }

        return array;
    }

    public static int[] getBinaryArray(int data) {
        int diff = 0;
        int[] array = new int[DEFAULT_BYTE_LEN];

        if(data < 0) {
            data = data + 256;
        }

        String bin = Integer.toBinaryString(data);

        diff = DEFAULT_BYTE_LEN - bin.length();

        if(diff > 0) {
            bin = CommonUtils.addZerosBefore(bin, diff);
        }

        for(int i=0; i<bin.length(); i++) {
            array[i] = (bin.charAt(i) - 48);
        }

        return array;
    }
}
