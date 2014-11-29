import com.jes.utils.BinaryMath;
import com.jes.utils.CommonUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Piotrek on 2014-11-23.
 */
public class BinaryMathTest {
   @Test
    public void highTest() {
        assertTrue(
                CommonUtils.isEqualsByContent(
                        new int[]{1, 1, 1, 1, 1, 1, 1, 1},
                        get16BitHighValue(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0})
                )
        );

       assertTrue(
               CommonUtils.isEqualsByContent(
                       new int[]{0, 1, 0, 1, 0, 1, 0, 1},
                       get16BitHighValue(new int[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0})
               )
       );

       assertTrue(
               CommonUtils.isEqualsByContent(
                       new int[]{0, 1, 0, 1, 0, 1, 0, 1},
                       get16BitHighValue(new int[]{0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1})
               )
       );

       assertTrue(
               CommonUtils.isEqualsByContent(
                       new int[]{1, 1, 1, 1, 0, 0, 0, 0},
                       get16BitHighValue(new int[]{1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1})
               )
       );
    }

    @Test
    public void lowTest() {
        assertTrue(
                CommonUtils.isEqualsByContent(
                        new int[]{1, 1, 1, 1, 1, 1, 1, 1},
                        get16BitLowValue(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1})
                )
        );

        assertTrue(
                CommonUtils.isEqualsByContent(
                        new int[]{0, 1, 0, 1, 0, 1, 0, 1},
                        get16BitLowValue(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1})
                )
        );

        assertTrue(
                CommonUtils.isEqualsByContent(
                        new int[]{0, 1, 0, 1, 0, 1, 0, 1},
                        get16BitLowValue(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1})
                )
        );

        assertTrue(
                CommonUtils.isEqualsByContent(
                        new int[]{1, 1, 1, 1, 0, 0, 0, 0},
                        get16BitLowValue(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0})
                )
        );
    }

    @Test
    public void generalMath() {
        //0xAA -> 10101010
        //0xFF -> 11111111
        int addrL = 0xAA;
        int addrH = 0xFF << 8;
        int[] array = BinaryMath.getBinaryArray((addrH | addrL), 16);

        assertEquals(0xFFAA, (int)(addrH | addrL));
        assertTrue("1111111110101010".equals(CommonUtils.byteArrayToBinaryString(array)));
    }

    @Test
    public void powArrayTest() {
        for(int i=0; i<=16; i++) {
            int pow = (int)Math.pow(2, i);

            assertEquals(BinaryMath.POW_2_TO_X[i], pow);
        }
    }

    private int[] get16BitHighValue(int[] _16bit) {
        int high = BinaryMath.high(BinaryMath.binaryToDecimal(_16bit));
        int[] array = BinaryMath.getBinaryArray(high);
        return array;
    }

    private int[] get16BitLowValue(int[] _16bit) {
        int low = BinaryMath.low(BinaryMath.binaryToDecimal(_16bit));
        int[] array = BinaryMath.getBinaryArray(low);
        return array;
    }
}
