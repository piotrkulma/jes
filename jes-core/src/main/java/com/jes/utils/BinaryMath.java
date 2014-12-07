package com.jes.utils;

/**
 * Created by Piotr Kulma on 2014-11-23.
 */
public final class BinaryMath {
    public static final int HIGH_BYTE_START_INDEX   = 0;
    public static final int HIGH_BYTE_END_INDEX     = 7;
    public static final int LOW_BYTE_START_INDEX    = 8;
    public static final int LOW_BYTE_END_INDEX      = 15;

    public static final int DEFAULT_BYTE_LEN        = 8;

    public static final int[]POW_2_TO_X =
            {
                    1, 2, 4, 8, 16, 32, 64, 128, 256, 512,
                    1024, 2048, 4096, 8192, 16384, 32768, 65536
            };

    //TODO to BDC conversion
    //nie używane w Nes-ie, narazie można odpuścic
    public static byte bdc(byte op) {
        return op;
    }

    public static int bdc(int op) {
        return op;
    }

    public static byte high(int b) {
        int[] binaryArray = getBinaryArray(b, 16);
        int[] binaryArrayH =  CommonUtils.getPartialArray(binaryArray, HIGH_BYTE_START_INDEX, HIGH_BYTE_END_INDEX + 1);
        byte highByte = (byte)binaryToDecimal(binaryArrayH);

        return highByte;
    }

    public static byte low(int b) {
        int[] binaryArray = getBinaryArray(b, 16);
        int[] binaryArrayL =  CommonUtils.getPartialArray(binaryArray, LOW_BYTE_START_INDEX, LOW_BYTE_END_INDEX + 1);
        byte lowByte = (byte)binaryToDecimal(binaryArrayL);

        return lowByte;
    }

    public static int combineTwoBytes(byte msb, byte lsb) {
        int[] adr = CommonUtils.combineTwoArrays(getBinaryArray(msb), getBinaryArray(lsb));
        return binaryToDecimal(adr);
    }

    public static int binaryToDecimal(int[] bcdArray) {
        int result = 0;
        int exp = bcdArray.length - 1;
        for(int i=0; i<bcdArray.length; i++) {
            result += (bcdArray[i] * POW_2_TO_X[exp - i]);
        }

        return result;
    }

    public static int byteToIntCorrection(int data) {
        if(data < 0) {
            data = data + 256;
        }

        return data;
    }

    public static int[] getBinaryArray(int data, int bitLen) {
        int diff;
        int[] array = new int[bitLen];

        data = byteToIntCorrection(data);

        String bin = Integer.toBinaryString(data);

        diff = bitLen - bin.length();

        if(diff > 0) {
            bin = addZerosBefore(bin, diff);
        }

        if(diff < 0) {
            diff = Math.abs(diff);
            for (int i = diff; i < bin.length(); i++) {
                array[i-diff] = (bin.charAt(i) - 48);
            }
        } else {
            for (int i = 0; i < bin.length(); i++) {
                array[i] = (bin.charAt(i) - 48);
            }
        }

        return array;
    }

    public static int[] getBinaryArray(int data) {
        return getBinaryArray(data, DEFAULT_BYTE_LEN);
    }

    private static String addZerosBefore(String s, int zeros) {
        for(int i=0; i<zeros; i++) {
            s = "0" + s;
        }

        return s;
    }
}
