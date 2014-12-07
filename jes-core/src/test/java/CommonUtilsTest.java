import com.jes.utils.BinaryMath;
import com.jes.utils.CommonUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static com.jes.utils.CommonUtils.*;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */

public class CommonUtilsTest {

    @Test
    public void getByteArrayTest() {
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0}, BinaryMath.getBinaryArray(0, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 1}, BinaryMath.getBinaryArray(1, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 1, 0}, BinaryMath.getBinaryArray(2, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 1, 1}, BinaryMath.getBinaryArray(3, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 1, 0, 0}, BinaryMath.getBinaryArray(4, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 1, 0, 1}, BinaryMath.getBinaryArray(5, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 1, 1, 0}, BinaryMath.getBinaryArray(6, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 1, 1, 1}, BinaryMath.getBinaryArray(7, 4)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{1, 0, 0, 0}, BinaryMath.getBinaryArray(8, 4)));

        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, BinaryMath.getBinaryArray(0, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 0, 0, 1}, BinaryMath.getBinaryArray(1, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 0, 1, 0}, BinaryMath.getBinaryArray(2, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 0, 1, 1}, BinaryMath.getBinaryArray(3, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 1, 0, 0}, BinaryMath.getBinaryArray(4, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 1, 0, 1}, BinaryMath.getBinaryArray(5, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 1, 1, 0}, BinaryMath.getBinaryArray(6, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 0, 1, 1, 1}, BinaryMath.getBinaryArray(7, 8)));
        assertTrue(CommonUtils.isArraysEqualByContent(new int[]{0, 0, 0, 0, 1, 0, 0, 0}, BinaryMath.getBinaryArray(8, 8)));
    }

    @Test
    public void byteArrayToStringTest() {
        assertEquals("[]", CommonUtils.byteArrayToString(new int[]{}));
        assertEquals("[0]", CommonUtils.byteArrayToString(new int[]{0}));
        assertEquals("[0, 0]", CommonUtils.byteArrayToString(new int[]{0, 0}));
        assertEquals("[0, 0, 0]", CommonUtils.byteArrayToString(new int[]{0, 0, 0}));
        assertEquals("[0, 0, 0, 0]", CommonUtils.byteArrayToString(new int[]{0, 0, 0, 0}));
        assertEquals("[0, 0, 0, 0, 0]", CommonUtils.byteArrayToString(new int[]{0, 0, 0, 0, 0}));
    }

    @Test
    public void combineTwoArraysTest() {
        assertEquals("10101111", byteArrayToBinaryString(combineTwoArrays(new int[]{1, 0, 1, 0}, new int[]{1, 1, 1, 1})));
        assertEquals("1111000110001111", byteArrayToBinaryString(combineTwoArrays(new int[]{1,1,1,1,0,0,0,1}, new int[]{1,0,0,0,1,1,1,1})));
        assertEquals("11000", byteArrayToBinaryString(combineTwoArrays(new int[]{1}, new int[]{1,0,0,0})));
    }
}
