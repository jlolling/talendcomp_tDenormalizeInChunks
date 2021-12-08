package de.jlo.talendcomp.denormalize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class TestDenormalizer {

	@Test
	public void testIntegerWithKnownCount() {
		int count = 12;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(5);
		d.setDelimiter("|");
		d.setExpectedInputCount(count);
		String[] expected = new String[3];
		expected[0] = "0|1|2|3|4";
		expected[1] = "5|6|7|8|9";
		expected[2] = "10|11";
		List<String> actual = new ArrayList<>();
		int lineIndex = 0;
		for (int i = 0; i < count; i++) {
			d.addValue(i);
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
				if (lineIndex < 2) {
					assertEquals("Value count wrong in line: " + lineIndex, 5, d.getDenormalizedValueCount()); 
				} else {
					assertEquals("Value count wrong in line: " + lineIndex, 2, d.getDenormalizedValueCount());
				}
				lineIndex++;
			}
		}
		// test
		assertTrue("Number result lines does not match", actual.size() == expected.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}
	
	@Test
	public void testIntegerWithKnownCountMatchInputCount() {
		int count = 12;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(6);
		d.setDelimiter("|");
		d.setExpectedInputCount(count);
		String[] expected = new String[2];
		expected[0] = "0|1|2|3|4|5";
		expected[1] = "6|7|8|9|10|11";
		List<String> actual = new ArrayList<>();
		int lineIndex = 0;
		for (int i = 0; i < count; i++) {
			d.addValue(i);
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
				assertEquals("Value count wrong in line: " + lineIndex, 6, d.getDenormalizedValueCount()); 
				lineIndex++;
			}
		}
		// test
		assertTrue("Number result lines does not match", actual.size() == expected.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}

	@Test
	public void testAllNullWithKnownCount() {
		int count = 12;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(5);
		d.setDelimiter("|");
		d.setNoValuesReplacement("--");
		d.setExpectedInputCount(count);
		String[] expected = new String[1];
		expected[0] = "--";
		List<String> actual = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			d.addValue(null);
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
			}
		}
		// test
		assertEquals("Number result lines does not match", expected.length, actual.size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}

	@Test
	public void testStringWithKnownCount() {
		int count = 12;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(5);
		d.setDelimiter(",");
		d.setEnclosure("'");
		d.setExpectedInputCount(count);
		String[] expected = new String[3];
		expected[0] = "'string_0','string_1','string_2','string_3','string_4'";
		expected[1] = "'string_5','string_6','string_7','string_8','string_9'";
		expected[2] = "'string_10','string_11'";
		List<String> actual = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			d.addValue("string_" + i);
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
			}
		}
		// test
		assertTrue("Number result lines does not match", actual.size() == expected.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}

	@Test
	public void testStringWithEndMarkerSet() {
		int count = 13;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(5);
		d.setDelimiter(",");
		d.setEnclosure("'");
		d.setEndMarker("string_12");
		String[] expected = new String[3];
		expected[0] = "'string_0','string_1','string_2','string_3','string_4'";
		expected[1] = "'string_5','string_6','string_7','string_8','string_9'";
		expected[2] = "'string_10','string_11'";
		List<String> actual = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			d.addValue("string_" + i);
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
			}
		}
		// test
		assertTrue("Number result lines does not match", actual.size() == expected.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}

	@Test
	public void testStringWithEndMarkerNull() {
		int count = 13;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(5);
		d.setDelimiter(",");
		d.setEnclosure("'");
		d.setEndMarker(null);
		String[] expected = new String[3];
		expected[0] = "'string_0','string_1','string_2','string_3','string_4'";
		expected[1] = "'string_5','string_6','string_7','string_8','string_9'";
		expected[2] = "'string_10','string_11'";
		List<String> actual = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			if (i == 12) {
				d.addValue(null);
			} else {
				d.addValue("string_" + i);
			}
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
			}
		}
		// test
		assertTrue("Number result lines does not match", actual.size() == expected.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}

	@Test
	public void testDateWithEndMarkerNull() throws ParseException {
		int count = 7;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(4);
		d.setDelimiter(",");
		d.setEnclosure("'");
		d.setEndMarker(null);
		d.setDatePattern("yyyy-MM-dd");
		String[] expected = new String[2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		expected[0] = "'2021-12-01','2021-12-02','2021-12-03','2021-12-04'";
		expected[1] = "'2021-12-05','2021-12-06'";
		List<String> actual = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			if (i == count) {
				d.addValue(null);
			} else {
				Date date = sdf.parse("2021-12-0" + i);
				d.addValue(date);
			}
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
			}
		}
		// test
		assertEquals("Number result lines does not match", expected.length, actual.size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}

	@Test
	public void testDateWithEndMarkerSet() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int count = 7;
		Denormalize d = new Denormalize();
		d.setCountValuesInChain(4);
		d.setDelimiter(",");
		d.setEnclosure("'");
		d.setEndMarker(sdf.parse("9999-01-01"));
		d.setDatePattern("yyyy-MM-dd");
		String[] expected = new String[2];
		expected[0] = "'2021-12-01','2021-12-02','2021-12-03','2021-12-04'";
		expected[1] = "'2021-12-05','2021-12-06'";
		List<String> actual = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			if (i == count) {
				d.addValue(sdf.parse("9999-01-01"));
			} else {
				Date date = sdf.parse("2021-12-0" + i);
				d.addValue(date);
			}
			if (d.next()) {
				System.out.println(d.getDenormalizedValues());
				actual.add(d.getDenormalizedValues());
			}
		}
		// test
		assertEquals("Number result lines does not match", expected.length, actual.size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}

}
