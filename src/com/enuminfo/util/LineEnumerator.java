/**
 * 
 */
package com.enuminfo.util;

import java.util.Enumeration;

/**
 * @author Kumar
 */
public class LineEnumerator implements Enumeration<Object> {

	private final String s;
	private final String separator;
	public final String CR = "\r";
	public final String LF = "\n";
	public final String CRLF = "\r\n";
	int offset;
	int eolOffset;

	public LineEnumerator(String s) {
		this.s = s;
		if (s.indexOf(CR) != -1) {
			if (s.indexOf(CRLF) != -1)
				separator = CRLF;
			else
				separator = CR;
		} else {
			separator = LF;
		}
		eolOffset = -separator.length();
	}

	public boolean hasMoreElements() {
		return eolOffset != s.length();
	}

	public Object nextElement() {
		offset = eolOffset + separator.length();
		eolOffset = s.indexOf(separator, offset);
		if (eolOffset == -1)
			eolOffset = s.length();
		return s.substring(offset, eolOffset);
	}
}
