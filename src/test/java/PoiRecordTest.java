import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

/**
 * 
 */

/**
 * @author Justin Dearing <zippy1981@gmail.com>
 *
 */
public class PoiRecordTest {

	/*
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	*/
	@Test
	public void testPoiRecordToString() {
		String input = "-72.789915,41.695685,\"Wal-Mart  -  New Britain, #2570;\",\"655 Farmington Ave, New Britain CT 6053 (860) 223-3222;\"";
		
		PoiRecord record = new PoiRecord();
		record.setLongitude(-72.789915);
		record.setLatitude(41.695685);
		record.setRawName("Wal-Mart  -  New Britain, #2570;");
		record.setRawAddress("655 Farmington Ave, New Britain CT 6053 (860) 223-3222;");
		Assert.assertEquals(input, record.toString());
	}

}
