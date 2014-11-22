package com.jes.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */
public class CommonUtils {
    public static Logger LOG = LogManager.getLogger(CommonUtils.class);

    public static final int INDEX_OPERATION_CODE    = 0;
    public static final int INDEX_MNEMONIC          = 1;
    public static final int INDEX_ADDRESS_MODE      = 2;
    public static final int INDEX_BYTES_NUMBER      = 3;

    public static final String[] HEX_VALUES = {
            "0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E",
            "F"};

    public static int[] getByteArray(int data, int bitLen) {
        int diff = 0;
        int[] array = new int[bitLen];

        if(data < 0) {
            data = data + 256;
        }

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

    public static byte[] getPartialArray(byte[] array, int beginIndex, int endIndex) {
        int index = 0;
        byte[] result = new byte[endIndex - beginIndex];

        for(int i=beginIndex; i<endIndex; i++) {
            result[index++] = array[i];
        }

        return result;
    }

    public static String convertBCDtoHex(byte bcd) {
        int[] bcdArray = CommonUtils.getByteArray(bcd, 8);

        int upper = binaryToDecimal(new int[]{bcdArray[0], bcdArray[1], bcdArray[2], bcdArray[3]});
        int lower = binaryToDecimal(new int[]{bcdArray[4], bcdArray[5], bcdArray[6], bcdArray[7]});

        StringBuilder builder = new StringBuilder();
        builder.append(HEX_VALUES[upper]);
        builder.append(HEX_VALUES[lower]);

        return builder.toString();
    }

    public static int binaryToDecimal(int[] bcdArray) {
        int result = 0;
        int exp = bcdArray.length - 1;
        for(int i=0; i<bcdArray.length; i++) {
            result += (bcdArray[i] * Math.pow(2, exp - i));
        }

        return result;
    }
}
