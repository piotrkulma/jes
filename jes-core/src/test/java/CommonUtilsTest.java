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
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0}, CommonUtils.getBinaryArray(0, 4)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 1}, CommonUtils.getBinaryArray(1, 4)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 1, 0}, CommonUtils.getBinaryArray(2, 4)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 1, 1}, CommonUtils.getBinaryArray(3, 4)));
        assertTrue(isEqualsByContent(new int[]{0, 1, 0, 0}, CommonUtils.getBinaryArray(4, 4)));
        assertTrue(isEqualsByContent(new int[]{0, 1, 0, 1}, CommonUtils.getBinaryArray(5, 4)));
        assertTrue(isEqualsByContent(new int[]{0, 1, 1, 0}, CommonUtils.getBinaryArray(6, 4)));
        assertTrue(isEqualsByContent(new int[]{0, 1, 1, 1}, CommonUtils.getBinaryArray(7, 4)));
        assertTrue(isEqualsByContent(new int[]{1, 0, 0, 0}, CommonUtils.getBinaryArray(8, 4)));

        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, getBinaryArray(0, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 0, 0, 1}, CommonUtils.getBinaryArray(1, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 0, 1, 0}, CommonUtils.getBinaryArray(2, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 0, 1, 1}, CommonUtils.getBinaryArray(3, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 1, 0, 0}, CommonUtils.getBinaryArray(4, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 1, 0, 1}, CommonUtils.getBinaryArray(5, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 1, 1, 0}, CommonUtils.getBinaryArray(6, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 0, 1, 1, 1}, CommonUtils.getBinaryArray(7, 8)));
        assertTrue(isEqualsByContent(new int[]{0, 0, 0, 0, 1, 0, 0, 0}, CommonUtils.getBinaryArray(8, 8)));
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

    private boolean isEqualsByContent(int a[], int b[]) {
        if(a.length == b.length) {
            for(int i=0; i<a.length; i++) {
                if(a[i] != b[i]) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }
}
