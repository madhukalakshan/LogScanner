package com.LogScanner;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

public class LogScanner {

	public static ArrayList<ArrayList<String>> lengthSixteenValue = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> track2Value = new ArrayList<ArrayList<String>>();

	public static ArrayList<ArrayList<String>> cardNumberExistFileName = new ArrayList<ArrayList<String>>();
	public static ArrayList<String> cardNumberNotExistFileName = new ArrayList<String>();

	public static ArrayList<ArrayList<String>> track2ExistFileName = new ArrayList<ArrayList<String>>();
	public static ArrayList<String> track2NotExistFileName = new ArrayList<String>();

	public static ArrayList<ArrayList<String>> masterCard = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> visaCard = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> amexCard = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> jcbCard = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> upiCard = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> maestroCard = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> otherCard = new ArrayList<ArrayList<String>>();

	public static ArrayList<String> filePathList = new ArrayList<String>();
	public static ArrayList<String> folderList = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> tagValueList = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> tagValueTotal = new ArrayList<ArrayList<String>>();

	static ConfigFileReader configFileReader;

	public LogScanner() {
		configFileReader = new ConfigFileReader();
	}

	public static void searchPlainCardNumber(String s, int line, String path) {
		Pattern p = Pattern.compile("[\\d]+");
		Matcher m = p.matcher(s);
		boolean isDigit = false;

		while (m.find()) {
			boolean isCharX = true;
			String tempattributeValue = m.group();
			String attributeValue = m.group().replaceAll("\\s+", "");
			int avSize = attributeValue.length();
			if (!attributeValue.equals("") && (avSize == 16 || avSize == 17 || avSize == 18 || avSize == 19)) {
				int x = s.indexOf(tempattributeValue);
				int z = x + tempattributeValue.length();
				if (x - 2 >= 0) {
					char c = s.substring(x - 2, x - 1).charAt(0);
					isDigit = Character.isDigit(c);
					isCharX = false;
				}

				if (isCharX || !s.substring(x - 1, x).equals(".") || !isDigit) {
					if (!(z + 1 <= s.length()) || !s.substring(z, z + 1).equals(".")) {
						ArrayList<String> lengthSixteenValueDetails = new ArrayList<String>();
						lengthSixteenValueDetails.add(Integer.toString(line));
						lengthSixteenValueDetails.add(attributeValue);
						lengthSixteenValueDetails.add(path);
						lengthSixteenValue.add(lengthSixteenValueDetails);
					}
				}
			}
		}
	}

	public static void searchTrack2(String s, int line, String path) {
		Pattern p = Pattern.compile("\\d+=\\d+");
		Matcher m = p.matcher(s);
		while (m.find()) {
			int x = m.group().length();
			if (x >= 33 && ((m.group().indexOf("=") == 16) || (m.group().indexOf("=") == 17)
					|| (m.group().indexOf("=") == 18) || (m.group().indexOf("=") == 19))) {
				ArrayList<String> track2ValueDetails = new ArrayList<String>();
				track2ValueDetails.add(Integer.toString(line));
				track2ValueDetails.add(m.group());
				track2ValueDetails.add(path);
				track2Value.add(track2ValueDetails);
			}
		}
	}

	public static void isCardNumber() {

		try {

			String masterCardRegex = "((5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12})";
			String visaCardRegex = "(4[0-9]{12}[0-9]{3})";
			String amexCardRegex = "(347[0-9]{13})";
			String jcbCardRegex = "((2131|1800|35{3})[0-9]{12})";
			String upiCardRegex = "(62[0-9]{14,17})";
			String maestroCardRegex = "((5018|5020|5038|6304|6759|6761|6763)[0-9]{8,15})";

			for (int k = 0; k < lengthSixteenValue.size(); k++) {

				ArrayList<String> masterCardDetails = new ArrayList<String>();
				ArrayList<String> visaCardDetails = new ArrayList<String>();
				ArrayList<String> amexCardDetails = new ArrayList<String>();
				ArrayList<String> jcbCardDetails = new ArrayList<String>();
				ArrayList<String> upiCardDetails = new ArrayList<String>();
				ArrayList<String> maestroCardDetails = new ArrayList<String>();
				ArrayList<String> otherCardDetails = new ArrayList<String>();

				boolean notRecognizeIndicator = true;
				if (lengthSixteenValue.get(k).get(1).matches(masterCardRegex)) {
					masterCardDetails.add(lengthSixteenValue.get(k).get(0));
					masterCardDetails.add(lengthSixteenValue.get(k).get(1));
					masterCardDetails.add(lengthSixteenValue.get(k).get(2));
					masterCard.add(masterCardDetails);
					notRecognizeIndicator = false;

				} else if (lengthSixteenValue.get(k).get(1).matches(visaCardRegex)) {
					visaCardDetails.add(lengthSixteenValue.get(k).get(0));
					visaCardDetails.add(lengthSixteenValue.get(k).get(1));
					visaCardDetails.add(lengthSixteenValue.get(k).get(2));
					visaCard.add(visaCardDetails);
					notRecognizeIndicator = false;

				} else if (lengthSixteenValue.get(k).get(1).matches(amexCardRegex)) {
					amexCardDetails.add(lengthSixteenValue.get(k).get(0));
					amexCardDetails.add(lengthSixteenValue.get(k).get(1));
					amexCardDetails.add(lengthSixteenValue.get(k).get(2));
					amexCard.add(amexCardDetails);
					notRecognizeIndicator = false;

				} else if (lengthSixteenValue.get(k).get(1).matches(jcbCardRegex)) {
					jcbCardDetails.add(lengthSixteenValue.get(k).get(0));
					jcbCardDetails.add(lengthSixteenValue.get(k).get(1));
					jcbCardDetails.add(lengthSixteenValue.get(k).get(2));
					jcbCard.add(jcbCardDetails);
					notRecognizeIndicator = false;

				} else if (lengthSixteenValue.get(k).get(1).matches(upiCardRegex)) {
					upiCardDetails.add(lengthSixteenValue.get(k).get(0));
					upiCardDetails.add(lengthSixteenValue.get(k).get(1));
					upiCardDetails.add(lengthSixteenValue.get(k).get(2));
					upiCard.add(upiCardDetails);
					notRecognizeIndicator = false;

				} else if (lengthSixteenValue.get(k).get(1).matches(maestroCardRegex)) {
					maestroCardDetails.add(lengthSixteenValue.get(k).get(0));
					maestroCardDetails.add(lengthSixteenValue.get(k).get(1));
					maestroCardDetails.add(lengthSixteenValue.get(k).get(2));
					maestroCard.add(maestroCardDetails);
					notRecognizeIndicator = false;

				} else if (notRecognizeIndicator) {
					boolean isFound = false;
					for (String a : configFileReader.getBin()) {
						if (lengthSixteenValue.get(k).get(1).startsWith(a)) {
							otherCardDetails.add(lengthSixteenValue.get(k).get(0));
							otherCardDetails.add(lengthSixteenValue.get(k).get(1));
							otherCardDetails.add(lengthSixteenValue.get(k).get(2));
							otherCard.add(otherCardDetails);
							isFound = true;
							break;
						}
					}
					if (!isFound) {
						lengthSixteenValue.remove(k);
						k = k - 1;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean existPlanCardNumber() {
		boolean existPlanCardNumberIndicator;
		if (!masterCard.isEmpty() || !visaCard.isEmpty() || !amexCard.isEmpty() || !jcbCard.isEmpty()
				|| !upiCard.isEmpty() || maestroCard.isEmpty())
			existPlanCardNumberIndicator = true;
		else
			existPlanCardNumberIndicator = false;
		return existPlanCardNumberIndicator;
	}

	// Read file list in directory
	public void readFileList(String directoryName) {
		try {

			File file = new File(directoryName);

			File[] subDirs = file.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}
			});

			for (File subDir : subDirs) {
				folderList.add(directoryName + subDir.getName());
			}

			String[] fileList = file.list();

			for (int i = 0; i < fileList.length; i++) {
				if (!folderList.contains(directoryName + fileList[i])) {

					if (fileList[i].length() >= 5
							&& configFileReader.getFilePrefix().contains(fileList[i].substring(0, 5))) {

					} else {
						String path = directoryName + fileList[i];
						filePathList.add(path);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw (e);
		}

	}

	public void updtRecurRdFleList(String directoryName) {

		List<File> files = new ArrayList<File>();

		listf(directoryName, files);
		try {
			for (File x : files) {
				boolean isFind = true;
				for (String ext : configFileReader.ignoreFileExtention()) {

					if (FilenameUtils.isExtension(x.getName().toUpperCase(), ext.toUpperCase())) {
						isFind = false;
						break;
					}
				}

				if (isFind) {
					if (x.getName().length() >= 5
							&& configFileReader.getFilePrefix().contains(x.getName().substring(0, 5))) {

					} else {
						filePathList.add(x.toString());
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void listf(String directoryName, List<File> files) {
		File directory = new File(directoryName);

		// Get all the files from a directory.
		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
					listf(file.getAbsolutePath(), files);
				}
			}
		}

	}

	public void updateCardNumberExistFile() {
		String tempFilePath = "";
		ArrayList<String> cardNumberOnlyExistFileName = new ArrayList<String>();
		for (int i = 0; i < lengthSixteenValue.size(); i++) {
			ArrayList<String> tempArray = new ArrayList<String>();
			if (i == 0) {
				tempFilePath = lengthSixteenValue.get(i).get(2);
				tempArray.add(tempFilePath);
				cardNumberExistFileName.add(tempArray);
				cardNumberOnlyExistFileName.add(tempFilePath);
			} else {
				if (!lengthSixteenValue.get(i).get(2).equals(tempFilePath)) {
					tempFilePath = lengthSixteenValue.get(i).get(2);
					tempArray.add(tempFilePath);
					cardNumberExistFileName.add(tempArray);
					cardNumberOnlyExistFileName.add(tempFilePath);
				}
			}
		}
		for (int i = 0; i < filePathList.size(); i++) {
			if (!cardNumberOnlyExistFileName.contains(filePathList.get(i))) {
				cardNumberNotExistFileName.add(filePathList.get(i));
			}
		}
	}

	public void updatetrack2ExistFileName() {
		String tempFilePath = "";
		ArrayList<String> track2OnlyExistFileName = new ArrayList<String>();
		for (int i = 0; i < track2Value.size(); i++) {
			ArrayList<String> tempArray = new ArrayList<String>();
			if (i == 0) {
				tempFilePath = track2Value.get(i).get(2);
				tempArray.add(tempFilePath);
				track2ExistFileName.add(tempArray);
				track2OnlyExistFileName.add(tempFilePath);
			} else {
				if (!track2Value.get(i).get(2).equals(tempFilePath)) {
					tempFilePath = track2Value.get(i).get(2);
					tempArray.add(tempFilePath);
					track2ExistFileName.add(tempArray);
					track2OnlyExistFileName.add(tempFilePath);
				}
			}
		}
		for (int i = 0; i < filePathList.size(); i++) {
			if (!track2OnlyExistFileName.contains(filePathList.get(i))) {
				track2NotExistFileName.add(filePathList.get(i));
			}
		}
	}

	public void tagValidation(ArrayList<String> tagList, String currenctLine, int lineNum, String path)
			throws FileNotFoundException {
		try {
			for (int i = 0; i < tagList.size(); i++) {

				ArrayList<String> a = new ArrayList<String>();
				String tagListValue = tagList.get(i);
				if (currenctLine.contains(tagListValue)) {
					String xmlTagStart = "<" + tagListValue + ">";
					String xmlTagEnd = "</" + tagListValue + ">";
					int xmlDataStartIndex = currenctLine.indexOf(xmlTagStart) + xmlTagStart.length();
					int xmlDataEndIndex = currenctLine.indexOf(xmlTagEnd);
					if (xmlDataStartIndex != -1 && xmlDataEndIndex != -1 && (xmlDataEndIndex >= xmlDataStartIndex)) {
						String xmlData = currenctLine.substring(xmlDataStartIndex, xmlDataEndIndex);
						String x = xmlData.replaceAll("\\s+", "");
						if (!xmlData.isEmpty() && !xmlData.contains("X") && x.matches("[a-zA-Z0-9]*")) {
							a.add(tagList.get(i));
							a.add(Integer.toString(lineNum));
							a.add(xmlData);
							a.add(path);
							tagValueList.add(a);
						}

					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getTagCount(ArrayList<String> tagValues) {

		int[] counts = new int[tagValues.size()];

		for (ArrayList<String> s : tagValueList) {
			for (int j = 0; j < tagValues.size(); j++) {
				if (s.get(0).equals(tagValues.get(j))) {
					counts[j]++;
				}
			}
		}
		for (int i = 0; i < tagValues.size(); i++) {
			ArrayList<String> tmp = new ArrayList<>();
			tmp.add(tagValues.get(i));
			tmp.add(Integer.toString(counts[i]));
			tagValueTotal.add(tmp);
		}

	}

	public boolean isNumeric(String strNum) {
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	public void updateFolderList(String folderPath) {
		folderList.add(folderPath);
	}

	public void removeFiles() {
		for (String x : filePathList) {
			configFileReader.getFilePrefix().contains(x.substring(0, 5));
			filePathList.remove(x);
		}
	}

	public int plcrdpageCount() {
		int[] a = { (masterCard.size() - 1), (visaCard.size() - 1), (amexCard.size() - 1), (jcbCard.size() - 1),
				(upiCard.size() - 1), (maestroCard.size() - 1), (otherCard.size() - 1) };
		List b = Arrays.asList(ArrayUtils.toObject(a));
		return (int) Collections.max(b);
	}

}
