import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import au.com.bytecode.opencsv.*;
import au.com.bytecode.opencsv.bean.*;

/**
 * Represents a record in a POI file
 * @author Justin Dearing <zippy1981@gmail.com>
 *
 */
public class PoiRecord {
	// Look for State/Zip
	private static Pattern usAddressPattern = Pattern.compile(" [A-Z]{2} [0-9]{5}");
	// Look fot a truncated state/zip
	private static Pattern usAddressTruncatedPattern = Pattern.compile(" [A-Z]{2} [0-9]{4}");
	// Look for province/zip
	private static Pattern canadianAddressPattern = Pattern.compile(" [A-Z]{2} ^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}");
	private static Pattern latLongQuoted = Pattern.compile("\"(-?[0-9]*\\.[0-9]*)\",\"(-?[0-9]*\\.[0-9]*)\"");
	
	private double longitude;
	private double latitude;
	private String rawName;
	private String rawAddress;
	
	/**
	 * @return the longitudemvn
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * @return the name
	 */
	public String getRawName() {
		return rawName;
	}
	/**
	 * @param name the name to set
	 */
	public void setRawName(String name) {
		this.rawName = name;
	}
	
	/**
	 * @return the rawAddress
	 */
	public String getRawAddress() {
		return rawAddress;
	}
	/**
	 * @param rawAddress the rawAddress to set
	 */
	public void setRawAddress(String rawAddress) {
		this.rawAddress = rawAddress;
	}
	
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		CSVWriter wtr = new CSVWriter(stringWriter);
		try {
			wtr.writeNext(new String [] { ((Double)getLongitude()).toString(), ((Double)getLatitude()).toString(), getRawName(), getRawAddress()});
			wtr.flush(
					);
			wtr.close();
		}
		catch (IOException ex) {
			return ex.toString();
		}
		return latLongQuoted.matcher(stringWriter.getBuffer().toString()).replaceFirst("$1,$2").trim();
	}
}
