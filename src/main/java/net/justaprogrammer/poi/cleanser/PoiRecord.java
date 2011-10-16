package net.justaprogrammer.poi.cleanser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
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
	private static final Pattern usAddressPattern = Pattern.compile(",?( [A-Z]{2} [0-9]{5} )");
	// Look for a truncated state/zip
	private static final Pattern usAddressTruncatedPattern = Pattern.compile(",?( [A-Z]{2} )(\\d{4} )");
	// Look for province/zip
	private static final Pattern canadianAddressPattern = Pattern.compile(",?( [A-Z]{2} [ABCEGHJKLMNPRSTVXY]\\d[A-Z] ?\\d[A-Z]\\d )");
	
	// We use this for a dirty hack in toString();
	static final Pattern latLongQuoted = Pattern.compile("\"(-?[0-9]*\\.[0-9]*)\",\"(-?[0-9]*\\.[0-9]*)\"");
	
	private static final ColumnPositionMappingStrategy poiColStrat = new ColumnPositionMappingStrategy();
	static {
		poiColStrat.setType(PoiRecord.class);
		String[] columns = new String[] {"latitude", "longitude", "rawName", "rawAddress" };
		poiColStrat.setColumnMapping(columns);
	}
	
	public static PoiRecord create(String csvRecord) {
		CsvToBean csv = new CsvToBean();
		List<PoiRecord> list = csv.parse(poiColStrat, new StringReader(csvRecord));
		return list.get(0);
	}
	
	public static List<PoiRecord> createList(String fileName) throws IOException, FileNotFoundException {
		Reader rdr = new FileReader(fileName);
		List<PoiRecord> ret = createList(rdr);
		rdr.close();
		return ret;
	}
	
	public static List<PoiRecord> createList(Reader csv) {
		CsvToBean csvToBean = new CsvToBean();
		return csvToBean.parse(poiColStrat, csv);
	}
	
	private double longitude;
	private double latitude;
	private String rawName;
	private String rawAddress;
	
	/**
	 * @return the longitude
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
	
	/**
	 * Ensures that there is a comma and a space delimiting the City and the State or Province.
	 */
	public void delimitCityState() {
		if (isUsAddressTruncated()) {
			setRawAddress(usAddressTruncatedPattern.matcher(getRawAddress()).replaceFirst(",$10$2"));
		} 
		else if (isUsAddress()) {
			setRawAddress(usAddressPattern.matcher(getRawAddress()).replaceFirst(",$1"));
		} 
		else if (isCanadianAddress()) {
			setRawAddress(canadianAddressPattern.matcher(getRawAddress()).replaceFirst(",$1"));
		}
	}
	
	/**
	 * @return true if the address is a Canadian address.
	 */
	public boolean isCanadianAddress() {
		return canadianAddressPattern.matcher(getRawAddress()).find();
	}
	
	/**
	 * @return true if the address is a US address.
	 */
	public boolean isUsAddress() {
		return usAddressPattern.matcher(getRawAddress()).find();
	}
	
	/**
	 * @return true if the address is a US address with the leading zero of the zip code truncated.
	 */
	public boolean isUsAddressTruncated() {
		return usAddressTruncatedPattern.matcher(getRawAddress()).find();
	}
	
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		CSVWriter wtr = new CSVWriter(stringWriter);
		
			wtr.writeNext(new String [] { ((Double)getLongitude()).toString(), ((Double)getLatitude()).toString(), getRawName(), getRawAddress()});
		try {
			wtr.flush();
			wtr.close();
		}
		catch (IOException ex) { }
		// This is a hack to remove the quoting from the latitude and longitude column.
		return latLongQuoted.matcher(stringWriter.getBuffer().toString()).replaceFirst("$1,$2").trim();
	}
	
	// This will work with OpenCSV 2.4
	/*
	public String toString() {
		StringWriter wtr = new StringWriter();
		BeanToCsv  csv = new BeanToCsv();
		List<PoiRecord> list = new ArrayList<PoiRecord>();
		list.add(this);
		try {
			csv.write(poiColStrat, wtr, list);
		}
		catch (IOException ex) {
			return ex.toString();
		}
		// This is a hack to remove the quoting from the latitude and longitude column.
		//return latLongQuoted.matcher(stringWriter.getBuffer().toString()).replaceFirst("$1,$2").trim();
		return stringWriter.getBuffer().toString().trim();
	}
	*/
}
