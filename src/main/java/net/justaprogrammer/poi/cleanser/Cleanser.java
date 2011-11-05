package net.justaprogrammer.poi.cleanser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Iterator;

public class Cleanser {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		if (args.length == 0) {
			printUsage(System.out);
			return;
		}
		File csvFile = new File(args[0]);
		if (!csvFile.exists()) {
			System.err.printf("File \"%s\" does not exist.", csvFile.getAbsolutePath());
			System.err.println();
			return;
		}
		
		PoiRecordList records = new PoiRecordList(PoiRecord.createList(args[0]));
		records.cleanse();
		System.out.print(records.toString());
		
		return;

	}
	
	private static void printUsage(PrintStream stream) {
		stream.println("Usage:");
		stream.println("\tJAVA_COMMAND PATH_TO_POICSV");
		stream.println();
		/*
		RuntimeMXBean runtimeMXBean =ManagementFactory.getRuntimeMXBean(); 
		stream.println(runtimeMXBean.getInputArguments());
		//stream.print("Library path: ");
		//stream.println(ManagementFactory.getRuntimeMXBean().getLibraryPath());
		stream.println("System properties:");
		Map <String,String> properties = runtimeMXBean.getSystemProperties();
		Iterator<String> keys = properties.keySet().iterator();
		//stream.println(keys.toString());
		String key = null;
		while (keys.hasNext()) {
			key = keys.next();
			// TODO: do the printf thing
			stream.println("Key: " + key + " Value: " + properties.get(key));
		}
		//*/
		
			
	}

}
