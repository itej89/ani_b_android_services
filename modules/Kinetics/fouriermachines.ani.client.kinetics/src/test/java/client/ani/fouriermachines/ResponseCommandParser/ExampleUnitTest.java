package client.ani.fouriermachines.ResponseCommandParser;

import org.junit.Test;

import static org.junit.Assert.*;
import Framework.DataTypes.Parsers.ResponseCommandParser.KineticsResponse;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        KineticsResponse kRespons = new KineticsResponse("TRG#O:DPW#8#O");
        assertEquals(4, 2 + 2);
    }
}