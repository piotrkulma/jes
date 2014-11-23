import com.jes.utils.BinaryMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by Piotrek on 2014-11-23.
 */
public class BinaryMathTest {
    @Test
    public void additionTest() {
        assertEquals((byte)255, BinaryMath.add((byte)255, (byte)0));
        assertEquals((byte)0, BinaryMath.add((byte)255, (byte)1));
        assertEquals((byte)1, BinaryMath.add((byte)255, (byte)2));
        assertEquals((byte)255, BinaryMath.add((byte)200, (byte)55));
    }
}
