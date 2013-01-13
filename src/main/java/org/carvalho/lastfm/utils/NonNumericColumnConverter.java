package org.carvalho.lastfm.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mahout.math.Arrays;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class NonNumericColumnConverter {

	private static final Logger LOGGER = Logger.getLogger(NonNumericColumnConverter.class);
	
	public void convert(File source, File target, char separator, char quoteCharacter) throws IOException {
		long currentUserId = 0l;
		long currentItemId = 0l;
		Map<String,Long> userIds = new HashMap<String, Long>();
		Map<String,Long> itemIds = new HashMap<String, Long>();
		CSVReader reader = new CSVReader(new FileReader(source), separator);
		CSVWriter writer = new CSVWriter(new FileWriter(target, true), separator, quoteCharacter);
		
		if (!target.exists())
			target.createNewFile();
		
		try {
			String[] line = reader.readNext();
			while (line != null) {
				try {
					String userId = line[0];
					String itemId = line[1];
					String score = line[2];
					
					currentUserId = setId(userIds, currentUserId, userId);
					currentItemId = setId(itemIds, currentItemId, itemId);
					
					writer.writeNext(new String[]{Long.toString(userIds.get(userId)), Long.toString(itemIds.get(itemId)), score});
				} catch (ArrayIndexOutOfBoundsException e) {
					LOGGER.debug("Failed to convert line "+Arrays.toString(line), e);
				}
				line = reader.readNext();
			}
		} finally {
			userIds.clear();
			itemIds.clear();
			reader.close();
			writer.close();
		}
	}
	
	private long setId(Map<String, Long> ids, long currentId, String key) {
		Long id = ids.get(key);
		if (id == null) {
			ids.put(key, currentId);
			id = currentId;
			currentId += 1;
		}
		return currentId;
	}
	
	private static final String USAGE = "<SOURCE_FILE> <TARGET_FILE> <SEPARATOR_CHARACTER> [QUOTE_CHARACTER]";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.out.println(USAGE);
			System.exit(1);
		}
		
		char separator = '\t';
		if (args[2].equals(","))
			separator = ',';
		
		char quoteCharacter = CSVWriter.NO_QUOTE_CHARACTER;
		if (args.length > 3)
			quoteCharacter = args[3].charAt(0);
		
		new NonNumericColumnConverter().convert(new File(args[0]), new File(args[1]), separator, quoteCharacter);
	}

}
