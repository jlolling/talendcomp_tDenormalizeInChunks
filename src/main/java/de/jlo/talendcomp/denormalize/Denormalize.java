package de.jlo.talendcomp.denormalize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Denormalize {
	
	private int expectedInputCount = 0;
	private int currentInputCount = 0;
	private int countValuesInLine = 0;
	private Object endMarker = null;
	private String enclosure = null;
	private int countValuesInChain = 10;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String noValuesReplacement = "-1";
	private String delimiter = ",";
	private boolean ignoreDuplicates = false;
	private List<Object> values = new ArrayList<>();
	private StringBuilder result = new StringBuilder();
	private boolean hasNext = false;
	private boolean resetCountValues = false;
	
	public void addValue(Object value) {
		if (resetCountValues) {
			countValuesInLine = 0; // reset because former bunch is delivered
			resetCountValues = false;
		}
		// add the values
		boolean isEndMarker = isEndMarker(value);
		if (value != null && isEndMarker == false) {
			if (ignoreDuplicates || values.contains(value) == false) {
				if (value instanceof String) {
					if (((String) value).trim().isEmpty() == false) {
						values.add(value);
						countValuesInLine++;
					}
				} else {
					values.add(value);
					countValuesInLine++;
				}
			}
		}
		currentInputCount++;
		// check if we are ready to hand out a bunch of de-normalised values
		// we reach the expected number of input records
		// or we detect an end marker
		// or the number of values to combine is reached
		if (currentInputCount == expectedInputCount ||
				isEndMarker ||
				values.size() == countValuesInChain) {
			if (values.size() == 0) {
				if (noValuesReplacement != null && noValuesReplacement.trim().isEmpty() == false) {
					values.add(noValuesReplacement);
				}
			}
			// build the line
			result.setLength(0); // reset line
			boolean firstLoop = true;
			for (Object v : values) {
				if (firstLoop) {
					firstLoop = false;
				} else {
					result.append(delimiter);
				}
				if (enclosure != null) {
					result.append(enclosure);
				}
				result.append(convertToStringValue(v));
				if (enclosure != null) {
					result.append(enclosure);
				}
			}
			values.clear(); // clear the received values
			hasNext = true;
		}
	}
	
	boolean isEndMarker(Object value) {
		if (endMarker != null && value != null) {
			return endMarker.equals(value);
		} else if (endMarker == null && value == null && expectedInputCount == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private String convertToStringValue(Object value) {
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Number) {
			return ((Number) value).toString();
		} else if (value instanceof Boolean) {
			return value.toString();
		} else if (value instanceof Date) {
			return sdf.format((Date) value);
		} else if (value != null) {
			throw new IllegalStateException("Value: " + value + " with class: " + value.getClass() + " cannot be converted into a String");
		} else {
			throw new IllegalArgumentException("value cannot be null");
		}
	}
	
	public boolean next() {
		boolean next = hasNext;
		hasNext = false;
		if (next) {
			resetCountValues = true;
		}
		return next;
	}
	
	public String getDenormalizedValues() {
		return result.toString();
	}
	
	public int getDenormalizedValueCount() {
		return countValuesInLine;
	}

	public int getExpectedInputCount() {
		return expectedInputCount;
	}

	public void setExpectedInputCount(Integer expectedInputCount) {
		if (expectedInputCount != null) {
			this.expectedInputCount = expectedInputCount;
		} else {
			this.expectedInputCount = 0;
		}
	}

	public Object getEndMarker() {
		return endMarker;
	}

	public void setEndMarker(Object endMarker) {
		this.endMarker = endMarker;
	}

	public String getEnclosure() {
		return enclosure;
	}

	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}

	public int getCountValuesInChain() {
		return countValuesInChain;
	}

	public void setCountValuesInChain(int countValuesInChain) {
		this.countValuesInChain = countValuesInChain;
	}

	public String getNoValuesReplacement() {
		return noValuesReplacement;
	}

	public void setNoValuesReplacement(String noValuesReplacement) {
		this.noValuesReplacement = noValuesReplacement;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public boolean isIgnoreDuplicates() {
		return ignoreDuplicates;
	}

	public void setIgnoreDuplicates(Boolean ignoreDuplicates) {
		if (ignoreDuplicates != null) {
			this.ignoreDuplicates = ignoreDuplicates;
		} else {
			this.ignoreDuplicates = false;
		}
	}
	
	public void setDatePattern(String pattern) {
		if (pattern != null && pattern.trim().isEmpty() == false) {
			sdf = new SimpleDateFormat(pattern);
		}
	}
	
}
