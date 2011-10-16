package net.justaprogrammer.poi.cleanser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import au.com.bytecode.opencsv.*;
import au.com.bytecode.opencsv.bean.*;

public final class PoiRecordList extends ArrayList<PoiRecord> {
	public String toString() {
		StringWriter stringWriter = new StringWriter();
		CSVWriter wtr = new CSVWriter(stringWriter);
		for (int i = 0; i < this.size(); i++)  {
			wtr.writeNext(new String [] { ((Double)this.get(i).getLongitude()).toString(), ((Double)this.get(i).getLatitude()).toString(), this.get(i).getRawName(), this.get(i).getRawAddress()});
		}
		try {
			wtr.flush();
			wtr.close();
		}
		catch (IOException ex) {
			// I don't see this actually happening since we are using a StringWriter(), and theres nothing to do here really.
		}
		// This is a hack to remove the quoting from the latitude and longitude column.
		return PoiRecord.latLongQuoted.matcher(stringWriter.getBuffer().toString()).replaceAll("$1,$2").trim();
	}
}
