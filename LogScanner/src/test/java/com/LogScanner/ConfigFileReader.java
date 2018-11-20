package com.LogScanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigFileReader {

	private Properties properties;
	private final String propertyFilePath = "config/Configuration.properties";

	public ConfigFileReader() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(propertyFilePath));
			properties = new Properties();

			try {
				properties.load(reader);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error:" + e);
			throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
		}
	}

	public String getDateFormat() {
		String dateFormat = properties.getProperty("DATEFORMAT");
		if (dateFormat != null)
			return dateFormat;
		else
			throw new RuntimeException("DATEFORMAT not specified in the Configuration.properties file.");
	}

	public String getLogPropertyFilePath() {
		String logPropertyFilePath = properties.getProperty("LOGPROPERTYFILEPATH");
		if (logPropertyFilePath != null)
			return logPropertyFilePath;
		else
			throw new RuntimeException("LOGPROPERTYFILEPATH not specified in the Configuration.properties file.");
	}

	public String getReportDataFilePathPlanCard() {
		String reptDatFilePathPlnCrd = properties.getProperty("REPORTDATAFILEPATHPLANCARD");
		if (reptDatFilePathPlnCrd != null) {
			return reptDatFilePathPlnCrd;
		} else
			throw new RuntimeException(
					"REPORTDATAFILEPATHPLANCARD not specified in the Configuration.properties file.");
	}

	public String getReportDataFilePathTrack2() {
		String reptDatFilePathtrack2 = properties.getProperty("REPORTDATAFILEPATHTRACK2");
		if (reptDatFilePathtrack2 != null) {
			return reptDatFilePathtrack2;
		} else
			throw new RuntimeException("REPORTDATAFILEPATHTRACK2 not specified in the Configuration.properties file.");
	}

	public String getLoginEmailUserName() {
		String loginEmailUserName = properties.getProperty("LOGINEMAILUSERNAME");
		if (loginEmailUserName != null)
			return loginEmailUserName;
		else
			throw new RuntimeException("LOGINEMAILUSERNAME not specified in the Configuration.properties file.");
	}

	public String getLoginEmailPassword() {
		String loginEmailPassword = properties.getProperty("LOGINEMAILPASSWORD");
		if (loginEmailPassword != null)
			return loginEmailPassword;
		else
			throw new RuntimeException("LOGINEMAILPASSWORD not specified in the Configuration.properties file.");
	}

	public String getToEmailUserName() {
		String toEmailUserName = properties.getProperty("TOEMAILUSERNAME");
		if (toEmailUserName != null)
			return toEmailUserName;
		else
			throw new RuntimeException("TOEMAILUSERNAME not specified in the Configuration.properties file.");
	}

	public String getReportDataFilePathTag() {
		String filePath = properties.getProperty("REPORTDATAFILEPATHPLANTAG");
		if (filePath != null)
			return filePath;
		else
			throw new RuntimeException("REPORTDATAFILEPATHPLANTAG not specified in the Configuration.properties file.");
	}

	public ArrayList<String> ignoreFileExtention() {
		String ignoreFileExt = properties.getProperty("IGNOREFILEEXTENTION");
		ArrayList<String> removeTable = new ArrayList<String>();
		String[] sparatedValue = ignoreFileExt.split(",");
		for (int i = 0; i < sparatedValue.length; i++) {
			removeTable.add(sparatedValue[i]);
		}
		if (ignoreFileExt != null)
			return removeTable;
		else
			throw new RuntimeException("IGNOREFILEEXTENTION not specified in the Configuration.properties file.");
	}

	public ArrayList<String> getBin() {
		String binValue = properties.getProperty("BIN");
		ArrayList<String> removeTable = new ArrayList<String>();
		String[] sparatedValue = binValue.split(",");
		for (int i = 0; i < sparatedValue.length; i++) {
			removeTable.add(sparatedValue[i]);
		}
		if (binValue != null)
			return removeTable;
		else
			throw new RuntimeException("BIN not specified in the Configuration.properties file.");
	}

	public ArrayList<String> getFilePrefix() {
		String binValue = properties.getProperty("FILEPREFIX");
		ArrayList<String> removeTable = new ArrayList<String>();
		String[] sparatedValue = binValue.split(",");
		for (int i = 0; i < sparatedValue.length; i++) {
			removeTable.add(sparatedValue[i]);
		}
		if (binValue != null)
			return removeTable;
		else
			throw new RuntimeException("FILEPREFIX not specified in the Configuration.properties file.");
	}

	public int getpageCountConfg() {
		String pageCount = properties.getProperty("PAGECOUNT");
		if (pageCount != null)
			return Integer.parseInt(pageCount);
		else
			throw new RuntimeException("PAGECOUNT not specified in the Configuration.properties file.");
	}

	public ArrayList<String> getTagValue() {
		String tagValue = properties.getProperty("TAGVALUE");
		ArrayList<String> tagArray = new ArrayList<String>();
		String[] sparatedValue = tagValue.split(",");
		for (int i = 0; i < sparatedValue.length; i++) {
			tagArray.add(sparatedValue[i]);
		}
		if (tagValue != null)
			return tagArray;
		else
			throw new RuntimeException("TAGVALUE not specified in the Configuration.properties file.");
	}

}
