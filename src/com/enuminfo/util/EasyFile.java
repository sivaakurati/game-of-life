/**
 * 
 */
package com.enuminfo.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author Kumar
 */
@SuppressWarnings("deprecation")
public class EasyFile {

	private String filepath;
	private String filename;
	private InputStream textFileReader;
	private OutputStream textFileWriter;
	private final int bufferLength = 1024;
	private Frame parent;
	private String title;
	private String fileExtension;

	public EasyFile(String filepath) {
		this.filepath = filepath;
		textFileReader = null;
		textFileWriter = null;
		fileExtension = null;
	}

	public EasyFile(URL url) throws IOException {
		this.filepath = null;
		textFileWriter = null;
		fileExtension = null;
		textFileReader = url.openStream();
	}

	public EasyFile(Frame parent, String title) {
		this.parent = parent;
		this.title = title;
		textFileReader = null;
		textFileWriter = null;
		fileExtension = null;
	}

	public EasyFile(InputStream textFileReader) {
		this.textFileReader = textFileReader;
		textFileWriter = null;
		filepath = null;
		fileExtension = null;
	}

	public EasyFile(OutputStream textFileWriter) {
		this.textFileWriter = textFileWriter;
		textFileReader = null;
		filepath = null;
		fileExtension = null;
	}

	public String readText() throws IOException {
		int bytesRead;
		if (textFileReader == null) {
			if (filepath == null) {
				FileDialog filedialog = new FileDialog(parent, title, FileDialog.LOAD);
				filedialog.setFile(filename);
				filedialog.show();
				if (filedialog.getFile() != null) {
					filename = filedialog.getFile();
					filepath = filedialog.getDirectory() + filename;
				} else
					return "";
			}
			textFileReader = new FileInputStream(filepath);
		}
		StringBuffer text = new StringBuffer();
		byte[] buffer = new byte[bufferLength];

		try {
			while ((bytesRead = textFileReader.read(buffer, 0, bufferLength)) != -1)
				text.append(new String(buffer, 0, bytesRead));
		} finally {
			textFileReader.close();
		}
		return text.toString();
	}

	public void writeText(String text) throws IOException {
		if (textFileWriter == null) {
			if (filepath == null) {
				FileDialog filedialog = new FileDialog(parent, title, FileDialog.SAVE);
				filedialog.setFile(filename);
				filedialog.show();
				if (filedialog.getFile() != null) {
					filename = filedialog.getFile();
					filepath = filedialog.getDirectory() + filename;
					if (fileExtension != null && filepath.indexOf('.') == -1) {
						filepath = filepath + fileExtension;
					}
				} else
					return;
			}
			textFileWriter = new FileOutputStream(filepath);
		}
		try {
			textFileWriter.write(text.getBytes());
		} finally {
			textFileWriter.close();
		}
	}

	public void setFileName(String s) {
		filename = s;
	}

	public String getFileName() {
		return filename == null ? filepath : filename;
	}

	public void setFileExtension(String s) {
		fileExtension = s;
	}
}
