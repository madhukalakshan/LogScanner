package com.LogScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class RunnerTest {

	public static void main(String[] args) {
		LogScanner meth = new LogScanner();
		CommonSteps commonStps = new CommonSteps();
		ConfigFileReader config = new ConfigFileReader();

		DateFormat dateFormat = new SimpleDateFormat(config.getDateFormat());
		String date = dateFormat.format(new Date());
		long startTime = System.currentTimeMillis();

		String currenctLine;
		String attribute;
		String isTagAllow;
		String filePath;
		String recursiveSearch;

		ArrayList<String> attributeList = new ArrayList<String>();
		ArrayList<String> tagValues = new ArrayList<String>();

		Scanner scanner = new Scanner(System.in);

		System.out.println("Please enter the file path(ex:C:/test/folder/) : ");
		filePath = scanner.nextLine();
		File file1 = new File(filePath);
		if (!file1.exists()) {
			System.out.println("Exit and path is incorrect");
			System.exit(0);
		}
		System.out.println("Do you want recursive search(Y/N)");
		recursiveSearch = scanner.nextLine();
		if (!(recursiveSearch.toUpperCase().equals("Y") || recursiveSearch.toUpperCase().equals("N"))) {
			System.out.println("Exit and please enter correct values");
			System.exit(0);
		}

		System.out.println("What attribute do you want to search (Plain card number,Track 2)?");
		attribute = scanner.nextLine();
		String[] attributeValue = attribute.split(",");
		for (int i = 0; i < attributeValue.length; i++) {
			String tmpValue = attributeValue[i].replaceAll("\\s", "").toUpperCase();
			if (tmpValue.equals("PLAINCARDNUMBER") || tmpValue.equals("TRACK2")) {
				attributeList.add(tmpValue);
			} else {
				System.out.println("Exit and please enter valid attributes again");
				System.exit(0);
			}
		}

		System.out.println("Do you want to scan tag values were masked (Y/N)?");
		isTagAllow = scanner.nextLine().replaceAll("\\s", "").toUpperCase();

		if (isTagAllow.equals("Y")) {
			System.out.println(
					"Do you want to scan all mentioned tag "+config.getTagValue()+" values (Y/N)?");
			String allowAllTage = scanner.nextLine().replaceAll("\\s", "").toUpperCase();
			if (allowAllTage.equals("Y")) {
				tagValues=config.getTagValue();
				
			} else {
				if (allowAllTage.equals("N")) {
					System.out.println("Please eneter tag values (ex:DE126_10,DE14)");
					String entertagValue = scanner.nextLine();
					String[] tagValueArray = entertagValue.split(",");
					for (String x : tagValueArray) {
						tagValues.add(x);
					}
				} else {
					System.out.println("Exit and please enter correct values");
					System.exit(0);
				}

			}

		} else {
			if (!isTagAllow.equals("N")) {
				System.out.println("Exit and please enter correct values");
				System.exit(0);
			}
		}
		if (recursiveSearch.toUpperCase().equals("Y")) {
			meth.updtRecurRdFleList(filePath);

		} else {
			meth.readFileList(filePath);
		}

		for (int j = 0; j < meth.filePathList.size(); j++) {
			int lineNumber = 0;
			boolean isFilePath=true;
			String filepath = meth.filePathList.get(j);
			File file = new File(filepath);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));
				try {
					while ((currenctLine = reader.readLine()) != null) {
						lineNumber++;

						String a = currenctLine;
						
						if (attributeList.contains("PLAINCARDNUMBER")) {
							meth.searchPlainCardNumber(a, lineNumber, filepath);
						}
						if (attributeList.contains("TRACK2")) {
							meth.searchTrack2(a, lineNumber, filepath);
						}
						if (isTagAllow.equals("Y")) {
							meth.tagValidation(tagValues, a, lineNumber, filepath);
						}
						
						currenctLine = "";
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} catch (FileNotFoundException e1) {
				System.out.println("--FileNotFoundException--"+filepath);
				isFilePath=false;
				meth.updateFolderList(filepath);
			}
			if(isFilePath){
				System.out.println(filepath + " file scan was success");
			}
			else{
				System.out.println(filepath + " file scan was failed");
			}
		}

		if (attributeList.contains("PLAINCARDNUMBER")) {
			
			System.out.println("Starting to write in "+config.getReportDataFilePathPlanCard() + "_" + commonStps.plCrdCurrPgCount + ".xls");

			commonStps.initiateXSSFWorkbook();
			meth.isCardNumber();
			meth.updateCardNumberExistFile();

			ArrayList<ArrayList<String>> summary = new ArrayList<>();

			ArrayList<String> summaryData1 = new ArrayList<>();
			summaryData1.add("Service start up time");
			summaryData1.add(date);
			summaryData1.add("0");
			summaryData1.add("0");

			ArrayList<String> summaryData2 = new ArrayList<>();
			summaryData2.add("Service end timess");
			summaryData2.add(dateFormat.format(new Date()));
			summaryData2.add("1");
			summaryData2.add("0");

			ArrayList<String> summaryData4 = new ArrayList<>();
			summaryData4.add("Scan file count");
			summaryData4.add(Integer.toString(meth.filePathList.size()));
			summaryData4.add("3");
			summaryData4.add("0");

			ArrayList<String> summaryData5 = new ArrayList<>();
			summaryData5.add("Found plain card number count");
			long totalCount = meth.masterCard.size() + meth.visaCard.size() + meth.amexCard.size() + meth.jcbCard.size()
					+ meth.upiCard.size() + meth.maestroCard.size() + meth.otherCard.size();
			summaryData5.add(Long.toString(totalCount));
			summaryData5.add("4");
			summaryData5.add("0");

			long finishTime = System.currentTimeMillis();
			long timePeriod = finishTime - startTime;
			int h = (int) ((timePeriod / 1000) / 3600);
			int m = (int) (((timePeriod / 1000) / 60) % 60);
			int s = (int) ((timePeriod / 1000) % 60);

			ArrayList<String> summaryData3 = new ArrayList<>();
			summaryData3.add("Scan time period from millisecond");
			summaryData3.add(h + " Hours " + m + " Minutes " + s + " Seconds (" + timePeriod + " Milliseconds )");
			summaryData3.add("2");
			summaryData3.add("0");

			summary.add(summaryData1);
			summary.add(summaryData2);
			summary.add(summaryData3);
			summary.add(summaryData4);
			summary.add(summaryData5);

			commonStps.initiateSheet("Summary");
			commonStps.updateExcel(summary, "PLAINCARDNUMBER");

			commonStps.initiateSheet("Details");
			commonStps.updateDetailsSheettoplncrd(filePath);
			commonStps.savePlainCardNumberToExcel();

			commonStps.closeXSSFWorkbook();

		}

		if (attributeList.contains("TRACK2")) {
			
			System.out.println("Starting to write in "+config.getReportDataFilePathTrack2() + "_" + commonStps.trc2CurrPgCount + ".xls");
			
			commonStps.initiateXSSFWorkbook();
			meth.updatetrack2ExistFileName();

			ArrayList<ArrayList<String>> summary = new ArrayList<>();

			ArrayList<String> summaryData1 = new ArrayList<>();
			summaryData1.add("Service start up time");
			summaryData1.add(date);
			summaryData1.add("0");
			summaryData1.add("0");

			ArrayList<String> summaryData2 = new ArrayList<>();
			summaryData2.add("Service end time");
			summaryData2.add(dateFormat.format(new Date()));
			summaryData2.add("1");
			summaryData2.add("0");

			ArrayList<String> summaryData4 = new ArrayList<>();
			summaryData4.add("Scan file count");
			summaryData4.add(Integer.toString(meth.filePathList.size()));
			summaryData4.add("3");
			summaryData4.add("0");

			ArrayList<String> summaryData5 = new ArrayList<>();
			summaryData5.add("Found track2 number count");
			summaryData5.add(Long.toString(meth.track2Value.size()));
			summaryData5.add("4");
			summaryData5.add("0");

			long finishTime = System.currentTimeMillis();
			long timePeriod = finishTime - startTime;
			int h = (int) ((timePeriod / 1000) / 3600);
			int m = (int) (((timePeriod / 1000) / 60) % 60);
			int s = (int) ((timePeriod / 1000) % 60);

			ArrayList<String> summaryData3 = new ArrayList<>();
			summaryData3.add("Scan time period from millisecond");
			summaryData3.add(h + " Hours " + m + " Minutes " + s + " Seconds (" + timePeriod + " Milliseconds )");
			summaryData3.add("2");
			summaryData3.add("0");

			summary.add(summaryData1);
			summary.add(summaryData2);
			summary.add(summaryData3);
			summary.add(summaryData4);
			summary.add(summaryData5);

			commonStps.initiateSheet("Summary");
			commonStps.updateExcel(summary, "TRACK2");

			commonStps.initiateSheet("Details");
			commonStps.updateDetailsSheettotrc2(filePath);
			commonStps.updatTrack2eData();

			commonStps.closeXSSFWorkbook();

		}

		if (isTagAllow.equals("Y")) {
			
			System.out.println("Starting to write in "+config.getReportDataFilePathTag() + "_" + commonStps.tagCurrPgCount + ".xls");

			commonStps.initiateXSSFWorkbook();

			ArrayList<ArrayList<String>> summary = new ArrayList<>();

			ArrayList<String> summaryData1 = new ArrayList<>();
			summaryData1.add("Service start up time");
			summaryData1.add(date);
			summaryData1.add("0");
			summaryData1.add("0");

			ArrayList<String> summaryData2 = new ArrayList<>();
			summaryData2.add("Service end time");
			summaryData2.add(dateFormat.format(new Date()));
			summaryData2.add("1");
			summaryData2.add("0");

			long finishTime = System.currentTimeMillis();
			long timePeriod = finishTime - startTime;
			int h = (int) ((timePeriod / 1000) / 3600);
			int m = (int) (((timePeriod / 1000) / 60) % 60);
			int s = (int) ((timePeriod / 1000) % 60);

			ArrayList<String> summaryData3 = new ArrayList<>();
			summaryData3.add("Scan time period from millisecond");
			summaryData3.add(h + " Hours " + m + " Minutes " + s + " Seconds (" + timePeriod + " Milliseconds )");
			summaryData3.add("2");
			summaryData3.add("0");

			ArrayList<String> summaryData4 = new ArrayList<>();
			summaryData4.add("Scan file count");
			summaryData4.add(Integer.toString(meth.filePathList.size()));
			summaryData4.add("3");
			summaryData4.add("0");

			summary.add(summaryData1);
			summary.add(summaryData2);
			summary.add(summaryData3);
			summary.add(summaryData4);

			meth.getTagCount(tagValues);

			for (int i = 0; i < meth.tagValueTotal.size(); i++) {
				ArrayList<String> summaryData5 = new ArrayList<>();
				summaryData5.add("Found plain " + meth.tagValueTotal.get(i).get(0) + " tag count");
				summaryData5.add(meth.tagValueTotal.get(i).get(1));
				summaryData5.add(Integer.toString(4 + i));
				summaryData5.add("0");
				summary.add(summaryData5);

			}

			commonStps.initiateSheet("Summary");
			commonStps.updateExcel(summary, "TAG");

			commonStps.initiateSheet("Details");
			commonStps.updateDetailsSheettoTag(filePath);
			commonStps.updatTagData();

			commonStps.closeXSSFWorkbook();
		}
		
		if (isTagAllow.equals("Y")) {
			attributeList.add("TAG");
		}

		try {
			commonStps.sendMail(attributeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("finished");
	}
}
