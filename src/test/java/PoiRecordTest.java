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

	@Test
	public void testPoiRecordIsFoo() {
		String truncatedUsAddress = "-72.789915,41.695685,\"Wal-Mart  -  New Britain, #2570;\",\"655 Farmington Ave, New Britain CT 6053 (860) 223-3222;\"";
		String usAddress = "-72.789915,41.695685,\"Wal-Mart  -  New Britain, #2570;\",\"655 Farmington Ave, New Britain CT 06053 (860) 223-3222;\"";
		String canadianAddress = "-114.21823,51.15389,\"Supercentre  -  Calgary (Royal Oak) , #5726;\",\"8888 Country Hills Blvd #200, Calgary, AB T3G 5T4 (403) 567-1502;  (NOP)\"";
		
		boolean done = false;
		
		while (done == false) {
			PoiRecord truncatedUsAddressRecord = PoiRecord.create(truncatedUsAddress);
			Assert.assertFalse(truncatedUsAddressRecord.isCanadianAddress());
			Assert.assertFalse(truncatedUsAddressRecord.isUsAddress());
			Assert.assertTrue(truncatedUsAddressRecord.isUsAddressTruncated());
			
			PoiRecord usAddressRecord = PoiRecord.create(usAddress);
			Assert.assertFalse(usAddressRecord.isCanadianAddress());
			Assert.assertTrue(usAddressRecord.isUsAddress());
			Assert.assertFalse(usAddressRecord.isUsAddressTruncated());
			
			PoiRecord canadianAddressRecord = PoiRecord.create(canadianAddress);
			Assert.assertTrue(canadianAddressRecord.isCanadianAddress());
			Assert.assertFalse(canadianAddressRecord.isUsAddress());
			Assert.assertFalse(canadianAddressRecord.isUsAddressTruncated());
			if (truncatedUsAddressRecord.getRawAddress().toCharArray()[0] == ',') { 
				done = true;
				continue;
			}
			// We tested the regex without commas, now we test with commas.
			truncatedUsAddressRecord.setRawAddress("," + truncatedUsAddressRecord.getRawAddress());
			truncatedUsAddress = truncatedUsAddressRecord.toString();
			usAddressRecord.setRawAddress("," + usAddressRecord.getRawAddress());
			usAddress = usAddressRecord.toString();
			canadianAddressRecord.setRawAddress("," + canadianAddressRecord.getRawAddress());
			canadianAddress = canadianAddressRecord.toString();
		}
	}

	@Test
	public void testPoiRecorddelimitState() {
		String truncatedUsAddress = "-72.789915,41.695685,\"Wal-Mart  -  New Britain, #2570;\",\"655 Farmington Ave, New Britain CT 6053 (860) 223-3222;\"";
		String usAddress = "-72.789915,41.695685,\"Wal-Mart  -  New Britain, #2570;\",\"655 Farmington Ave, New Britain CT 06053 (860) 223-3222;\"";
		String canadianAddress = "-114.21823,51.15389,\"Supercentre  -  Calgary (Royal Oak) , #5726;\",\"8888 Country Hills Blvd #200, Calgary AB T3G 5T4 (403) 567-1502;  (NOP)\"";
		
		String truncatedUsAddressDelimited = "-72.789915,41.695685,\"Wal-Mart  -  New Britain, #2570;\",\"655 Farmington Ave, New Britain, CT 06053 (860) 223-3222;\"";
		String usAddressDelimited = "-72.789915,41.695685,\"Wal-Mart  -  New Britain, #2570;\",\"655 Farmington Ave, New Britain, CT 06053 (860) 223-3222;\"";
		String canadianAddressDelimited = "-114.21823,51.15389,\"Supercentre  -  Calgary (Royal Oak) , #5726;\",\"8888 Country Hills Blvd #200, Calgary, AB T3G 5T4 (403) 567-1502;  (NOP)\"";
		
		PoiRecord truncatedUsAddressRecord = PoiRecord.create(truncatedUsAddress);
		PoiRecord truncatedUsAddressDelimitedRecord = PoiRecord.create(truncatedUsAddressDelimited);
		Assert.assertFalse(truncatedUsAddressDelimitedRecord.getRawAddress() == truncatedUsAddressRecord.getRawAddress());
		truncatedUsAddressRecord.delimiteCityState();
		Assert.assertEquals(truncatedUsAddressDelimitedRecord.getRawAddress(), truncatedUsAddressRecord.getRawAddress());
		
		PoiRecord usAddressRecord = PoiRecord.create(usAddress);
		PoiRecord usAddressDelimitedRecord = PoiRecord.create(usAddressDelimited);
		Assert.assertFalse(usAddressDelimitedRecord.getRawAddress() == usAddressRecord.getRawAddress());
		usAddressRecord.delimiteCityState();
		Assert.assertEquals(usAddressDelimitedRecord.getRawAddress(), usAddressRecord.getRawAddress());
		
		PoiRecord canadianAddressRecord = PoiRecord.create(canadianAddress);
		PoiRecord canadianAddressDelimitedRecord = PoiRecord.create(canadianAddressDelimited);
		Assert.assertFalse(canadianAddressDelimitedRecord.getRawAddress() == canadianAddressRecord.getRawAddress());
		canadianAddressRecord.delimiteCityState();
		Assert.assertEquals(canadianAddressDelimitedRecord.getRawAddress(), canadianAddressRecord.getRawAddress());
	}
	
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
