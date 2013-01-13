package org.carvalho.lastfm;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.carvalho.lastfm.utils.NonNumericColumnConverter;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class NonNumericColumnConverterTest {

	@Test
	public void testConvert() throws IOException {
		File source = new File(NonNumericColumnConverterTest.class.getClassLoader().getResource("non-numeric-sample.csv").toExternalForm().substring("file:".length()));
		File target = File.createTempFile("numeric-sample", ".csv");
		target.delete();
		
		NonNumericColumnConverter converter = new NonNumericColumnConverter();
		converter.convert(source, target, ',', CSVWriter.NO_QUOTE_CHARACTER);
		CSVReader reader = new CSVReader(new FileReader(target));
		
		List<String[]> lines = reader.readAll();
		assertEquals(4, lines.size());
		
		String[] line1 = lines.get(0);
		assertArrayEquals(line1, new String[]{"0", "0", "1"});
		
		String[] line2 = lines.get(1);
		assertArrayEquals(line2, new String[]{"1", "1", "1"});
		
		String[] line3 = lines.get(2);
		assertArrayEquals(line3, new String[]{"2", "0", "1"});
		
		String[] line4 = lines.get(3);
		assertArrayEquals(line4, new String[]{"3", "1", "1"});
		
		reader.close();
	}

}
