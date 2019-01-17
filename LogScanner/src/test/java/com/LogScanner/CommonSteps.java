package com.LogScanner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CommonSteps {

	ConfigFileReader configFileReader;
	LogScanner meth;
	
	Logger log = Logger.getLogger("LOG");

	static int summaryRowNumber = 0;

	public CommonSteps() {
		configFileReader = new ConfigFileReader();
		meth = new LogScanner();
	}

	static String reportDataFilePath = "";
	static int plCrdCurrPgCount = 1;
	static int trc2CurrPgCount = 1;
	static int tagCurrPgCount = 1;

	static XSSFWorkbook workbook = null;
	static XSSFSheet sheet = null;

	public void initiateXSSFWorkbook() {
		workbook = new XSSFWorkbook();
	}

	public void initiateSheet(String sheet1) {
		sheet = workbook.createSheet(sheet1);
	}

	public void closeXSSFWorkbook() {
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	public void updateExcel(ArrayList<ArrayList<String>> bookData, String attribute) {

		if (attribute.equals("PLAINCARDNUMBER")) {
			reportDataFilePath = configFileReader.getReportDataFilePathPlanCard() + "_" + plCrdCurrPgCount + ".xls";
		} else if (attribute.equals("TRACK2")) {
			reportDataFilePath = configFileReader.getReportDataFilePathTrack2() + "_" + trc2CurrPgCount + ".xls";
		} else if (attribute.equals("TAG")) {
			reportDataFilePath = configFileReader.getReportDataFilePathTag() + "_" + tagCurrPgCount + ".xls";
		}

		try {
			ArrayList<Integer> numberArr = new ArrayList<Integer>();
			numberArr = getCount(bookData.size(), 100000);
			for (int j = 0; j < numberArr.size(); j++) {
				int rowCount = 0;
				int columnCount = 0;
				if (j == 0) {
					for (int k = 0; k < numberArr.get(j); k++) {
						rowCount = Integer.valueOf(bookData.get(k).get(bookData.get(k).size() - 2));
						Row row = sheet.createRow(rowCount);
						columnCount = Integer.valueOf(bookData.get(k).get(bookData.get(k).size() - 1));
						for (int i = 0; i < bookData.get(k).size() - 2; i++) {
							Cell cell = row.createCell(columnCount++);
							if (bookData.get(k).get(i) instanceof String) {
								cell.setCellValue(bookData.get(k).get(i));
							} else {
								cell.setCellValue(Integer.valueOf(bookData.get(k).get(i)));
							}
						}
					}
				} else {
					for (int k = numberArr.get(j - 1); k < numberArr.get(j); k++) {
						rowCount = Integer.valueOf(bookData.get(k).get(bookData.get(k).size() - 2));
						Row row = sheet.createRow(rowCount);
						columnCount = Integer.valueOf(bookData.get(k).get(bookData.get(k).size() - 1));
						for (int i = 0; i < bookData.get(k).size() - 2; i++) {
							Cell cell = row.createCell(columnCount++);
							if (bookData.get(k).get(i) instanceof String) {
								cell.setCellValue(bookData.get(k).get(i));
							} else {
								cell.setCellValue(Integer.valueOf(bookData.get(k).get(i)));
							}
						}
					}
				}

				try (FileOutputStream outputStream = new FileOutputStream(reportDataFilePath)) {
					autoSizeColumns();
					workbook.write(outputStream);
					outputStream.flush();
					outputStream.close();

				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage(), e);
				}

			}

		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage(), e1);
		}

	}

	public void savePlainCardNumberToExcel() {

		ArrayList<ArrayList<String>> headine = new ArrayList<>();
		ArrayList<String> headineData = new ArrayList<>();

		headineData.add("Card number exist line number");
		headineData.add("Card number");
		headineData.add("File path");
		headineData.add("0");
		headineData.add("0");

		headine.add(headineData);

		try {
			if (!meth.masterCard.isEmpty() || !meth.visaCard.isEmpty() || !meth.amexCard.isEmpty()
					|| !meth.jcbCard.isEmpty() || !meth.upiCard.isEmpty() || !meth.maestroCard.isEmpty()
					|| !meth.otherCard.isEmpty()) {

				if (meth.plcrdpageCount() <= configFileReader.getpageCountConfg()) {
					if (!meth.masterCard.isEmpty()) {

						initiateSheet("MasterCardData");

						updateExcel(headine, "PLAINCARDNUMBER");

						for (int i = 0; i < meth.masterCard.size(); i++) {
							meth.masterCard.get(i).add(Integer.toString(i + 2));
							meth.masterCard.get(i).add("0");

						}
						updateExcel(meth.masterCard, "PLAINCARDNUMBER");
					}

					if (!meth.visaCard.isEmpty()) {

						initiateSheet("visaCardData");

						updateExcel(headine, "PLAINCARDNUMBER");

						for (int i = 0; i < meth.visaCard.size(); i++) {
							meth.visaCard.get(i).add(Integer.toString(i + 2));
							meth.visaCard.get(i).add("0");

						}
						updateExcel(meth.visaCard, "PLAINCARDNUMBER");
					}

					if (!meth.amexCard.isEmpty()) {

						initiateSheet("amexCardData");

						updateExcel(headine, "PLAINCARDNUMBER");

						for (int i = 0; i < meth.amexCard.size(); i++) {
							meth.amexCard.get(i).add(Integer.toString(i + 2));
							meth.amexCard.get(i).add("0");

						}
						updateExcel(meth.amexCard, "PLAINCARDNUMBER");
					}

					if (!meth.jcbCard.isEmpty()) {

						initiateSheet("jcbCardData");

						updateExcel(headine, "PLAINCARDNUMBER");

						for (int i = 0; i < meth.jcbCard.size(); i++) {

							meth.jcbCard.get(i).add(Integer.toString(i + 2));
							meth.jcbCard.get(i).add("0");

						}
						updateExcel(meth.jcbCard, "PLAINCARDNUMBER");
					}

					if (!meth.upiCard.isEmpty()) {

						initiateSheet("upiCardData");

						updateExcel(headine, "PLAINCARDNUMBER");

						for (int i = 0; i < meth.upiCard.size(); i++) {
							meth.upiCard.get(i).add(Integer.toString(i + 2));
							meth.upiCard.get(i).add("0");

						}
						updateExcel(meth.upiCard, "PLAINCARDNUMBER");
					}

					if (!meth.maestroCard.isEmpty()) {

						initiateSheet("maestroCardData");

						updateExcel(headine, "PLAINCARDNUMBER");

						for (int i = 0; i < meth.maestroCard.size(); i++) {
							meth.maestroCard.get(i).add(Integer.toString(i + 2));
							meth.maestroCard.get(i).add("0");

						}
						updateExcel(meth.maestroCard, "PLAINCARDNUMBER");

					}

					if (!meth.otherCard.isEmpty()) {

						initiateSheet("otherCard");

						updateExcel(headine, "PLAINCARDNUMBER");

						for (int i = 0; i < meth.otherCard.size(); i++) {
							meth.otherCard.get(i).add(Integer.toString(i + 2));
							meth.otherCard.get(i).add("0");

						}
						updateExcel(meth.otherCard, "PLAINCARDNUMBER");
					}

					System.out.println("finished to write in " + configFileReader.getReportDataFilePathPlanCard() + "_"
							+ plCrdCurrPgCount + ".xls");
					plCrdCurrPgCount++;

				} else {

					int pageCount = meth.plcrdpageCount() / configFileReader.getpageCountConfg();

					if (meth.plcrdpageCount() % configFileReader.getpageCountConfg() > 0) {
						pageCount++;
					}

					ArrayList<Integer> tmpmasterCardlength = new ArrayList<Integer>();
					tmpmasterCardlength = getCount((meth.masterCard.size()), configFileReader.getpageCountConfg());

					ArrayList<Integer> tmpvisaCardlength = new ArrayList<Integer>();
					tmpvisaCardlength = getCount((meth.visaCard.size()), configFileReader.getpageCountConfg());

					ArrayList<Integer> tmpamexCardlength = new ArrayList<Integer>();
					tmpamexCardlength = getCount((meth.amexCard.size()), configFileReader.getpageCountConfg());

					ArrayList<Integer> tmpjcbCardlength = new ArrayList<Integer>();
					tmpjcbCardlength = getCount((meth.jcbCard.size()), configFileReader.getpageCountConfg());

					ArrayList<Integer> tmpupiCardlength = new ArrayList<Integer>();
					tmpupiCardlength = getCount((meth.upiCard.size()), configFileReader.getpageCountConfg());

					ArrayList<Integer> tmpmaestroCardlength = new ArrayList<Integer>();
					tmpmaestroCardlength = getCount((meth.maestroCard.size()), configFileReader.getpageCountConfg());

					ArrayList<Integer> tmpotherCardlength = new ArrayList<Integer>();
					tmpotherCardlength = getCount((meth.otherCard.size()), configFileReader.getpageCountConfg());

					while (plCrdCurrPgCount <= pageCount) {

						if (plCrdCurrPgCount != 1) {
							System.out
									.println("Starting to write in " + configFileReader.getReportDataFilePathPlanCard()
											+ "_" + plCrdCurrPgCount + ".xls");
						}

						if (!meth.masterCard.isEmpty() && ((plCrdCurrPgCount - 1)
								* configFileReader.getpageCountConfg() < meth.masterCard.size())) {

							initiateSheet("MasterCardData");
							updateExcel(headine, "PLAINCARDNUMBER");
							ArrayList<ArrayList<String>> tmpmasterCard = new ArrayList<ArrayList<String>>();

							if (plCrdCurrPgCount == 1) {
								for (int j = 0; j < tmpmasterCardlength.get(plCrdCurrPgCount - 1); j++) {
									ArrayList<String> tmpmasterCarddetails = new ArrayList<String>();
									tmpmasterCarddetails.add(meth.masterCard.get(j).get(0));
									tmpmasterCarddetails.add(meth.masterCard.get(j).get(1));
									tmpmasterCarddetails.add(meth.masterCard.get(j).get(2));
									tmpmasterCarddetails.add(Integer.toString(j + 2));
									tmpmasterCarddetails.add("0");
									tmpmasterCard.add(tmpmasterCarddetails);
								}
								updateExcel(tmpmasterCard, "PLAINCARDNUMBER");
							}

							else {
								int columnnum = 0;
								for (int j = tmpmasterCardlength.get(plCrdCurrPgCount - 2); j < (tmpmasterCardlength
										.get(plCrdCurrPgCount - 1)); j++) {
									ArrayList<String> tmpmasterCarddetails = new ArrayList<String>();
									tmpmasterCarddetails.add(meth.masterCard.get(j).get(0));
									tmpmasterCarddetails.add(meth.masterCard.get(j).get(1));
									tmpmasterCarddetails.add(meth.masterCard.get(j).get(2));
									tmpmasterCarddetails.add(Integer.toString(columnnum++ + 2));
									tmpmasterCarddetails.add("0");
									tmpmasterCard.add(tmpmasterCarddetails);
								}
								updateExcel(tmpmasterCard, "PLAINCARDNUMBER");

							}
							tmpmasterCard.clear();
						}

						if (!meth.visaCard.isEmpty() && ((plCrdCurrPgCount - 1)
								* configFileReader.getpageCountConfg() < meth.visaCard.size())) {

							initiateSheet("VisaCardData");
							updateExcel(headine, "PLAINCARDNUMBER");
							ArrayList<ArrayList<String>> tmpvisaCard = new ArrayList<ArrayList<String>>();

							if (plCrdCurrPgCount == 1) {
								for (int j = 0; j < tmpvisaCardlength.get(plCrdCurrPgCount - 1); j++) {
									ArrayList<String> tmpvisaCarddetails = new ArrayList<String>();
									tmpvisaCarddetails.add(meth.visaCard.get(j).get(0));
									tmpvisaCarddetails.add(meth.visaCard.get(j).get(1));
									tmpvisaCarddetails.add(meth.visaCard.get(j).get(2));
									tmpvisaCarddetails.add(Integer.toString(j + 2));
									tmpvisaCarddetails.add("0");
									tmpvisaCard.add(tmpvisaCarddetails);
								}
								updateExcel(tmpvisaCard, "PLAINCARDNUMBER");

							}

							else {
								int columnnum = 0;
								for (int j = tmpvisaCardlength.get(plCrdCurrPgCount - 2); j < (tmpvisaCardlength
										.get(plCrdCurrPgCount - 1)); j++) {
									ArrayList<String> tmpvisaCarddetails = new ArrayList<String>();
									tmpvisaCarddetails.add(meth.visaCard.get(j).get(0));
									tmpvisaCarddetails.add(meth.visaCard.get(j).get(1));
									tmpvisaCarddetails.add(meth.visaCard.get(j).get(2));
									tmpvisaCarddetails.add(Integer.toString(columnnum++ + 2));
									tmpvisaCarddetails.add("0");
									tmpvisaCard.add(tmpvisaCarddetails);
								}
								updateExcel(tmpvisaCard, "PLAINCARDNUMBER");

							}
							tmpvisaCard.clear();
						}

						if (!meth.amexCard.isEmpty() && ((plCrdCurrPgCount - 1)
								* configFileReader.getpageCountConfg() < meth.amexCard.size())) {

							initiateSheet("AmexCardData");
							updateExcel(headine, "PLAINCARDNUMBER");
							ArrayList<ArrayList<String>> tmpamexCard = new ArrayList<ArrayList<String>>();

							if (plCrdCurrPgCount == 1) {

								for (int j = 0; j < tmpamexCardlength.get(plCrdCurrPgCount - 1); j++) {
									ArrayList<String> tmpamexCarddetails = new ArrayList<String>();
									tmpamexCarddetails.add(meth.amexCard.get(j).get(0));
									tmpamexCarddetails.add(meth.amexCard.get(j).get(1));
									tmpamexCarddetails.add(meth.amexCard.get(j).get(2));
									tmpamexCarddetails.add(Integer.toString(j + 2));
									tmpamexCarddetails.add("0");
									tmpamexCard.add(tmpamexCarddetails);
								}
								updateExcel(tmpamexCard, "PLAINCARDNUMBER");
							}

							else {
								int columnnum = 0;
								for (int j = tmpamexCardlength.get(plCrdCurrPgCount - 2); j < (tmpamexCardlength
										.get(plCrdCurrPgCount - 1)); j++) {
									ArrayList<String> tmpamexCarddetails = new ArrayList<String>();
									tmpamexCarddetails.add(meth.amexCard.get(j).get(0));
									tmpamexCarddetails.add(meth.amexCard.get(j).get(1));
									tmpamexCarddetails.add(meth.amexCard.get(j).get(2));
									tmpamexCarddetails.add(Integer.toString(columnnum++ + 2));
									tmpamexCarddetails.add("0");
									tmpamexCard.add(tmpamexCarddetails);
								}
								updateExcel(tmpamexCard, "PLAINCARDNUMBER");
							}
							tmpamexCard.clear();
						}

						if (!meth.jcbCard.isEmpty() && ((plCrdCurrPgCount - 1)
								* configFileReader.getpageCountConfg() < meth.jcbCard.size())) {

							initiateSheet("JcbCardData");
							updateExcel(headine, "PLAINCARDNUMBER");
							ArrayList<ArrayList<String>> tmpjcbCard = new ArrayList<ArrayList<String>>();

							if (plCrdCurrPgCount == 1) {

								for (int j = 0; j < tmpjcbCardlength.get(plCrdCurrPgCount - 1); j++) {
									ArrayList<String> tmpjcbCarddetails = new ArrayList<String>();
									tmpjcbCarddetails.add(meth.jcbCard.get(j).get(0));
									tmpjcbCarddetails.add(meth.jcbCard.get(j).get(1));
									tmpjcbCarddetails.add(meth.jcbCard.get(j).get(2));
									tmpjcbCarddetails.add(Integer.toString(j + 2));
									tmpjcbCarddetails.add("0");
									tmpjcbCard.add(tmpjcbCarddetails);
								}
								updateExcel(tmpjcbCard, "PLAINCARDNUMBER");
							}

							else {
								int columnnum = 0;
								for (int j = tmpjcbCardlength.get(plCrdCurrPgCount - 2); j < (tmpjcbCardlength
										.get(plCrdCurrPgCount - 1)); j++) {
									ArrayList<String> tmpjcbCarddetails = new ArrayList<String>();
									tmpjcbCarddetails.add(meth.jcbCard.get(j).get(0));
									tmpjcbCarddetails.add(meth.jcbCard.get(j).get(1));
									tmpjcbCarddetails.add(meth.jcbCard.get(j).get(2));
									tmpjcbCarddetails.add(Integer.toString(columnnum++ + 2));
									tmpjcbCarddetails.add("0");
									tmpjcbCard.add(tmpjcbCarddetails);
								}
								updateExcel(tmpjcbCard, "PLAINCARDNUMBER");
							}
							tmpjcbCard.clear();
						}

						if (!meth.upiCard.isEmpty() && ((plCrdCurrPgCount - 1)
								* configFileReader.getpageCountConfg() < meth.upiCard.size())) {

							initiateSheet("UpiCardData");
							updateExcel(headine, "PLAINCARDNUMBER");
							ArrayList<ArrayList<String>> tmpupiCard = new ArrayList<ArrayList<String>>();

							if (plCrdCurrPgCount == 1) {

								for (int j = 0; j < tmpupiCardlength.get(plCrdCurrPgCount - 1); j++) {
									ArrayList<String> tmpupiCarddetails = new ArrayList<String>();
									tmpupiCarddetails.add(meth.upiCard.get(j).get(0));
									tmpupiCarddetails.add(meth.upiCard.get(j).get(1));
									tmpupiCarddetails.add(meth.upiCard.get(j).get(2));
									tmpupiCarddetails.add(Integer.toString(j + 2));
									tmpupiCarddetails.add("0");
									tmpupiCard.add(tmpupiCarddetails);
								}
								updateExcel(tmpupiCard, "PLAINCARDNUMBER");
							}

							else {
								int columnnum = 0;
								for (int j = tmpupiCardlength.get(plCrdCurrPgCount - 2); j < (tmpupiCardlength
										.get(plCrdCurrPgCount - 1)); j++) {
									ArrayList<String> tmpupiCarddetails = new ArrayList<String>();
									tmpupiCarddetails.add(meth.upiCard.get(j).get(0));
									tmpupiCarddetails.add(meth.upiCard.get(j).get(1));
									tmpupiCarddetails.add(meth.upiCard.get(j).get(2));
									tmpupiCarddetails.add(Integer.toString(columnnum++ + 2));
									tmpupiCarddetails.add("0");
									tmpupiCard.add(tmpupiCarddetails);
								}
								updateExcel(tmpupiCard, "PLAINCARDNUMBER");

							}
							tmpupiCard.clear();
						}

						if (!meth.maestroCard.isEmpty() && ((plCrdCurrPgCount - 1)
								* configFileReader.getpageCountConfg() < meth.maestroCard.size())) {

							initiateSheet("MaestroCardData");
							updateExcel(headine, "PLAINCARDNUMBER");
							ArrayList<ArrayList<String>> tmpmaestroCard = new ArrayList<ArrayList<String>>();

							if (plCrdCurrPgCount == 1) {

								for (int j = 0; j < tmpmaestroCardlength.get(plCrdCurrPgCount - 1); j++) {
									ArrayList<String> tmpmaestroCarddetails = new ArrayList<String>();
									tmpmaestroCarddetails.add(meth.maestroCard.get(j).get(0));
									tmpmaestroCarddetails.add(meth.maestroCard.get(j).get(1));
									tmpmaestroCarddetails.add(meth.maestroCard.get(j).get(2));
									tmpmaestroCarddetails.add(Integer.toString(j + 2));
									tmpmaestroCarddetails.add("0");
									tmpmaestroCard.add(tmpmaestroCarddetails);
								}
								updateExcel(tmpmaestroCard, "PLAINCARDNUMBER");
							}

							else {
								int columnnum = 0;
								for (int j = tmpmaestroCardlength.get(plCrdCurrPgCount - 2); j < (tmpmaestroCardlength
										.get(plCrdCurrPgCount - 1)); j++) {
									ArrayList<String> tmpmaestroCarddetails = new ArrayList<String>();
									tmpmaestroCarddetails.add(meth.maestroCard.get(j).get(0));
									tmpmaestroCarddetails.add(meth.maestroCard.get(j).get(1));
									tmpmaestroCarddetails.add(meth.maestroCard.get(j).get(2));
									tmpmaestroCarddetails.add(Integer.toString(columnnum++ + 2));
									tmpmaestroCarddetails.add("0");
									tmpmaestroCard.add(tmpmaestroCarddetails);
								}
								updateExcel(tmpmaestroCard, "PLAINCARDNUMBER");

							}
							tmpmaestroCard.clear();
						}

						if (!meth.otherCard.isEmpty() && ((plCrdCurrPgCount - 1)
								* configFileReader.getpageCountConfg() < meth.otherCard.size())) {

							initiateSheet("OtherCardData");
							updateExcel(headine, "PLAINCARDNUMBER");
							ArrayList<ArrayList<String>> tmpotherCard = new ArrayList<ArrayList<String>>();

							if (plCrdCurrPgCount == 1) {

								for (int j = 0; j < tmpotherCardlength.get(plCrdCurrPgCount - 1); j++) {
									ArrayList<String> tmpotherCarddetails = new ArrayList<String>();
									tmpotherCarddetails.add(meth.otherCard.get(j).get(0));
									tmpotherCarddetails.add(meth.otherCard.get(j).get(1));
									tmpotherCarddetails.add(meth.otherCard.get(j).get(2));
									tmpotherCarddetails.add(Integer.toString(j + 2));
									tmpotherCarddetails.add("0");
									tmpotherCard.add(tmpotherCarddetails);
								}
								updateExcel(tmpotherCard, "PLAINCARDNUMBER");

							}

							else {
								int columnnum = 0;
								for (int j = tmpotherCardlength.get(plCrdCurrPgCount - 2); j < (tmpotherCardlength
										.get(plCrdCurrPgCount - 1)); j++) {
									ArrayList<String> tmpotherCarddetails = new ArrayList<String>();
									tmpotherCarddetails.add(meth.otherCard.get(j).get(0));
									tmpotherCarddetails.add(meth.otherCard.get(j).get(1));
									tmpotherCarddetails.add(meth.otherCard.get(j).get(2));
									tmpotherCarddetails.add(Integer.toString(columnnum++ + 2));
									tmpotherCarddetails.add("0");
									tmpotherCard.add(tmpotherCarddetails);
								}
								updateExcel(tmpotherCard, "PLAINCARDNUMBER");

							}
							tmpotherCard.clear();
						}
						System.out.println("Finished to write in " + configFileReader.getReportDataFilePathPlanCard()
								+ "_" + plCrdCurrPgCount + ".xls");
						plCrdCurrPgCount++;

						if (plCrdCurrPgCount <= pageCount) {
							closeXSSFWorkbook();
							initiateXSSFWorkbook();
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			meth.masterCard.clear();
			meth.visaCard.clear();
			meth.amexCard.clear();
			meth.jcbCard.clear();
			meth.upiCard.clear();
			meth.maestroCard.clear();
			meth.otherCard.clear();
		}
	}

	public void sendMail(ArrayList<String> attribute) {
		String fromEmailUserName = configFileReader.getLoginEmailUserName();
		final String password = configFileReader.getLoginEmailPassword();

		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmailUserName, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmailUserName));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(configFileReader.getToEmailUserName()));
			message.setSubject("Log Scanner Report");

			Multipart multipart = new MimeMultipart();

			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText("Hi,\n\nPlease find the attached Log Scanner Report");

			if (attribute.contains("PLAINCARDNUMBER")) {
				for (int i = 1; i <= plCrdCurrPgCount - 1; i++) {
					MimeBodyPart attachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource("config/ReportData_PlainCardNumber_" + i + ".xls");
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					attachmentBodyPart.setFileName("ReportData_PlainCardNumber_" + i + ".xls");
					multipart.addBodyPart(attachmentBodyPart);
				}

			}
			if (attribute.contains("TRACK2")) {
				for (int i = 1; i <= trc2CurrPgCount - 1; i++) {
					MimeBodyPart attachmentBodyPart1 = new MimeBodyPart();
					DataSource source1 = new FileDataSource("config/ReportData_Track2_" + i + ".xls");
					attachmentBodyPart1.setDataHandler(new DataHandler(source1));
					attachmentBodyPart1.setFileName("ReportData_Track2_" + i + ".xls");
					multipart.addBodyPart(attachmentBodyPart1);
				}

			}
			if (attribute.contains("TAG")) {
				for (int i = 1; i <= tagCurrPgCount - 1; i++) {
					MimeBodyPart attachmentBodyPart2 = new MimeBodyPart();
					DataSource source2 = new FileDataSource("config/ReportData_Tag_" + i + ".xls");
					attachmentBodyPart2.setDataHandler(new DataHandler(source2));
					attachmentBodyPart2.setFileName("ReportData_Tag_" + i + ".xls");
					multipart.addBodyPart(attachmentBodyPart2);
				}

			}
			multipart.addBodyPart(textBodyPart);
			message.setContent(multipart);
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage(), e1);
		}
	}

	public void updateDetailsSheettoplncrd(String filePath) {

		ArrayList<ArrayList<String>> b = new ArrayList<>();
		ArrayList<String> a1 = new ArrayList<>();

		a1.add("Plain card number exist file path");
		a1.add("0");
		a1.add("0");

		b.add(a1);

		updateExcel(b, "PLAINCARDNUMBER");

		int existRowValue = 1;

		for (int i = 0; i < meth.cardNumberExistFileName.size(); i++) {
			meth.cardNumberExistFileName.get(i).add(Integer.toString(i + 1));
			meth.cardNumberExistFileName.get(i).add("0");
		}

		updateExcel(meth.cardNumberExistFileName, "PLAINCARDNUMBER");

		existRowValue = meth.cardNumberExistFileName.size() + 1;

		b = new ArrayList<>();
		a1 = new ArrayList<>();

		a1.add("Plain card number not exist file path");
		existRowValue++;
		a1.add(Integer.toString(existRowValue++));
		a1.add("0");

		b.add(a1);

		updateExcel(b, "PLAINCARDNUMBER");

		b = new ArrayList<>();

		for (int i = 0; i < meth.cardNumberNotExistFileName.size(); i++) {

			a1 = new ArrayList<>();

			a1.add(meth.cardNumberNotExistFileName.get(i));
			a1.add(Integer.toString(existRowValue++));
			a1.add("0");

			b.add(a1);
		}

		updateExcel(b, "PLAINCARDNUMBER");

		b = new ArrayList<>();
		a1 = new ArrayList<>();

		a1.add("Not scanned file path");
		existRowValue++;
		a1.add(Integer.toString(existRowValue++));
		a1.add("0");

		b.add(a1);

		updateExcel(b, "PLAINCARDNUMBER");

		ArrayList<String> a2 = new ArrayList<>();
		b = new ArrayList<>();

		for (int i = 0; i < meth.folderList.size(); i++) {

			a1 = new ArrayList<>();

			a1.add(meth.folderList.get(i));
			a1.add(Integer.toString(existRowValue++));
			a1.add("0");

			b.add(a1);
		}

		updateExcel(b, "PLAINCARDNUMBER");
	}

	public void updateDetailsSheettotrc2(String filePath) {

		ArrayList<ArrayList<String>> b = new ArrayList<>();
		ArrayList<String> a1 = new ArrayList<>();

		a1.add("Track2 exist file path");
		a1.add("0");
		a1.add("0");

		b.add(a1);

		updateExcel(b, "TRACK2");

		int existRowValue = 1;

		for (int i = 0; i < meth.track2ExistFileName.size(); i++) {
			meth.track2ExistFileName.get(i).add(Integer.toString(i + 1));
			meth.track2ExistFileName.get(i).add("0");
		}

		updateExcel(meth.track2ExistFileName, "TRACK2");

		existRowValue = meth.track2ExistFileName.size() + 1;

		b = new ArrayList<>();
		a1 = new ArrayList<>();

		a1.add("Track2 not exist tables");
		existRowValue++;
		a1.add(Integer.toString(existRowValue++));
		a1.add("0");

		b.add(a1);

		updateExcel(b, "TRACK2");

		b = new ArrayList<>();

		for (int i = 0; i < meth.track2NotExistFileName.size(); i++) {
			a1 = new ArrayList<>();

			a1.add(meth.track2NotExistFileName.get(i));
			a1.add(Integer.toString(existRowValue++));
			a1.add("0");

			b.add(a1);
		}
		updateExcel(b, "TRACK2");

		b = new ArrayList<>();
		a1 = new ArrayList<>();

		a1.add("Not scanned file path");
		existRowValue++;
		a1.add(Integer.toString(existRowValue++));
		a1.add("0");

		b.add(a1);

		updateExcel(b, "TRACK2");

		b = new ArrayList<>();

		for (int i = 0; i < meth.folderList.size(); i++) {

			a1 = new ArrayList<>();

			a1.add(meth.folderList.get(i));
			a1.add(Integer.toString(existRowValue++));
			a1.add("0");

			b.add(a1);
		}

		updateExcel(b, "TRACK2");
	}

	public void updateDetailsSheettoTag(String filePath) {

		ArrayList<ArrayList<String>> b = new ArrayList<>();
		ArrayList<String> a1 = new ArrayList<>();

		a1.add("Scanned file path");
		a1.add("0");
		a1.add("0");

		b.add(a1);

		updateExcel(b, "TAG");

		int existRowValue = 1;

		b = new ArrayList<>();

		for (int i = 0; i < meth.filePathList.size(); i++) {

			a1 = new ArrayList<>();

			a1.add(meth.filePathList.get(i));
			a1.add(Integer.toString(existRowValue++));
			a1.add("0");

			b.add(a1);
		}

		updateExcel(b, "TAG");

		b = new ArrayList<>();
		a1 = new ArrayList<>();

		a1.add("Not scanned file path");
		existRowValue++;
		a1.add(Integer.toString(existRowValue++));
		a1.add("0");

		b.add(a1);

		updateExcel(b, "TAG");

		b = new ArrayList<>();

		for (int i = 0; i < meth.folderList.size(); i++) {

			a1 = new ArrayList<>();

			a1.add(meth.folderList.get(i));
			a1.add(Integer.toString(existRowValue++));
			a1.add("0");

			b.add(a1);
		}

		updateExcel(b, "TAG");

		ArrayList<String> a2 = new ArrayList<>();

		for (int i = 0; i < meth.tagValueTotal.size(); i++) {
			if (Integer.parseInt(meth.tagValueTotal.get(i).get(1)) > 0) {
				a1 = new ArrayList<>();
				a2 = new ArrayList<>();
				b = new ArrayList<>();
				a1.add(meth.tagValueTotal.get(i).get(0) + " exist file path");
				existRowValue++;
				a1.add(Integer.toString(existRowValue++));
				a1.add("0");
				b.add(a1);
				updateExcel(b, "TAG");

				b = new ArrayList<>();

				String tempTagValue = "";
				String tempPathValue = "";

				for (int j = 0; j < meth.tagValueList.size(); j++) {
					if (meth.tagValueList.get(j).get(0).equals(meth.tagValueTotal.get(i).get(0))) {
						if (!tempTagValue.equals(meth.tagValueList.get(j).get(0))
								|| !tempPathValue.equals(meth.tagValueList.get(j).get(3))) {
							a2 = new ArrayList<>();
							a2.add(meth.tagValueList.get(j).get(3));
							a2.add(Integer.toString(existRowValue++));
							a2.add("0");
							b.add(a2);
							tempTagValue = meth.tagValueList.get(j).get(0);
							tempPathValue = meth.tagValueList.get(j).get(3);
						}
					}
				}
				updateExcel(b, "TAG");
			}
		}

	}

	public void updatTrack2eData() {
		if (!meth.track2Value.isEmpty()) {
			try {
				ArrayList<ArrayList<String>> headine = new ArrayList<>();
				ArrayList<String> headineData = new ArrayList<>();

				headineData.add("Track2 exist line number");
				headineData.add("Track2");
				headineData.add("File path");
				headineData.add("0");
				headineData.add("0");

				headine.add(headineData);

				if (meth.track2Value.size() <= configFileReader.getpageCountConfg()) {
					initiateSheet("Data");

					updateExcel(headine, "TRACK2");

					for (int i = 0; i < meth.track2Value.size(); i++) {
						meth.track2Value.get(i).add(Integer.toString(i + 1));
						meth.track2Value.get(i).add("0");
					}
					updateExcel(meth.track2Value, "TRACK2");
					System.out.println("Finished to write in " + configFileReader.getReportDataFilePathTrack2() + "_"
							+ trc2CurrPgCount + ".xls");
					trc2CurrPgCount++;
					closeXSSFWorkbook();

				} else {

					int pageCount = meth.track2Value.size() / configFileReader.getpageCountConfg();

					if (meth.track2Value.size() / configFileReader.getpageCountConfg() > 0) {
						pageCount++;
					}

					ArrayList<Integer> tmptrc2length = new ArrayList<Integer>();
					tmptrc2length = getCount((meth.track2Value.size() - 1), configFileReader.getpageCountConfg());

					while (trc2CurrPgCount <= pageCount) {

						if (trc2CurrPgCount != 1) {
							System.out.println("Starting to write in " + configFileReader.getReportDataFilePathTrack2()
									+ "_" + trc2CurrPgCount + ".xls");
						}

						initiateSheet("Data");
						updateExcel(headine, "TRACK2");
						ArrayList<ArrayList<String>> tmptrc2 = new ArrayList<ArrayList<String>>();

						if (trc2CurrPgCount == 1) {

							for (int j = 0; j <= tmptrc2length.get(trc2CurrPgCount - 1); j++) {
								ArrayList<String> tmptrc2details = new ArrayList<String>();
								tmptrc2details.add(meth.track2Value.get(j).get(0));
								tmptrc2details.add(meth.track2Value.get(j).get(1));
								tmptrc2details.add(meth.track2Value.get(j).get(2));
								tmptrc2details.add(Integer.toString(j + 2));
								tmptrc2details.add("0");
								tmptrc2.add(tmptrc2details);
							}
							updateExcel(tmptrc2, "TRACK2");
						}

						else {
							int columnnum = 0;
							for (int j = (tmptrc2length.get(trc2CurrPgCount - 2) + 1); j <= (tmptrc2length
									.get(trc2CurrPgCount - 1)); j++) {
								ArrayList<String> tmptrc2details = new ArrayList<String>();
								tmptrc2details.add(meth.track2Value.get(j).get(0));
								tmptrc2details.add(meth.track2Value.get(j).get(1));
								tmptrc2details.add(meth.track2Value.get(j).get(2));
								tmptrc2details.add(Integer.toString(columnnum++ + 2));
								tmptrc2details.add("0");
								tmptrc2.add(tmptrc2details);
							}
							updateExcel(tmptrc2, "TRACK2");
						}

						System.out.println("finished to write in " + configFileReader.getReportDataFilePathTrack2()
								+ "_" + trc2CurrPgCount + ".xls");
						trc2CurrPgCount++;
						tmptrc2.clear();
						closeXSSFWorkbook();
						if (trc2CurrPgCount <= pageCount) {
							initiateXSSFWorkbook();
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			} finally {
				meth.track2Value.clear();
			}

		} else {
			System.out.println("finished to write in " + configFileReader.getReportDataFilePathTrack2() + "_"
					+ trc2CurrPgCount + ".xls");
			trc2CurrPgCount++;
		}
	}

	public void updatTagData() {

		boolean isTagValue = false;
		for (int i = 0; i < meth.tagValueTotal.size(); i++) {
			if (Integer.parseInt(meth.tagValueTotal.get(i).get(1)) > 0) {
				isTagValue = true;
				break;
			}
		}

		try {

			if (isTagValue) {

				int total = 0;

				for (int i = 0; i < meth.tagValueTotal.size(); i++) {
					if (Integer.parseInt(meth.tagValueTotal.get(i).get(1)) > 0) {
						total = total + Integer.parseInt(meth.tagValueTotal.get(i).get(1));
					}
				}

				if (total <= configFileReader.getpageCountConfg()) {

					ArrayList<ArrayList<String>> b = new ArrayList<ArrayList<String>>();
					ArrayList<String> a1 = new ArrayList<>();
					ArrayList<String> a2 = new ArrayList<>();

					initiateSheet("Data");

					int existRowValue = 0;

					for (int i = 0; i < meth.tagValueTotal.size(); i++) {
						if (Integer.parseInt(meth.tagValueTotal.get(i).get(1)) > 0) {
							a1 = new ArrayList<>();

							a1.add(meth.tagValueTotal.get(i).get(0) + " exist line number");
							a1.add(meth.tagValueTotal.get(i).get(0) + " value");
							a1.add("File path");
							a1.add(Integer.toString(existRowValue++));
							a1.add("0");

							b.add(a1);

							existRowValue++;

							for (int j = 0; j < meth.tagValueList.size(); j++) {
								if (meth.tagValueList.get(j).get(0).equals(meth.tagValueTotal.get(i).get(0))) {
									a2 = new ArrayList<>();
									a2.add(meth.tagValueList.get(j).get(1));
									a2.add(meth.tagValueList.get(j).get(2));
									a2.add(meth.tagValueList.get(j).get(3));
									a2.add(Integer.toString(existRowValue++));
									a2.add("0");
									b.add(a2);
								}
							}
							existRowValue++;
						}
					}
					updateExcel(b, "TAG");
					System.out.println("finished to write in " + configFileReader.getReportDataFilePathTag() + "_"
							+ tagCurrPgCount + ".xls");
					tagCurrPgCount++;

					closeXSSFWorkbook();

				} else {

					int pageCount = total / configFileReader.getpageCountConfg();

					if (total / configFileReader.getpageCountConfg() > 0) {
						pageCount++;
					}

					ArrayList<ArrayList<String>> b = new ArrayList<ArrayList<String>>();
					ArrayList<String> a1 = new ArrayList<>();
					ArrayList<String> a2 = new ArrayList<>();

					ArrayList<ArrayList<String>> tmptrc2 = new ArrayList<ArrayList<String>>();

					initiateSheet("Data");

					if (tagCurrPgCount != 1) {
						System.out.println("Starting to write in " + configFileReader.getReportDataFilePathTag() + "_"
								+ tagCurrPgCount + ".xls");
					}

					int existRowValue = 0;

					for (int i = 0; i < meth.tagValueTotal.size(); i++) {
						if (Integer.parseInt(meth.tagValueTotal.get(i).get(1)) > 0) {
							a1 = new ArrayList<>();

							a1.add(meth.tagValueTotal.get(i).get(0) + " exist line number");
							a1.add(meth.tagValueTotal.get(i).get(0) + " value");
							a1.add("File path");
							a1.add(Integer.toString(existRowValue++));
							a1.add("0");

							b.add(a1);

							existRowValue++;

							for (int j = 0; j < meth.tagValueList.size(); j++) {
								if (meth.tagValueList.get(j).get(0).equals(meth.tagValueTotal.get(i).get(0))) {
									a2 = new ArrayList<>();
									a2.add(meth.tagValueList.get(j).get(1));
									a2.add(meth.tagValueList.get(j).get(2));
									a2.add(meth.tagValueList.get(j).get(3));
									a2.add(Integer.toString(existRowValue++));
									a2.add("0");
									b.add(a2);
								}
								if (existRowValue > configFileReader.getpageCountConfg()) {
									updateExcel(b, "TAG");
									System.out.println(
											"Finished to write in " + configFileReader.getReportDataFilePathTag() + "_"
													+ tagCurrPgCount + ".xls");
									tagCurrPgCount++;
									closeXSSFWorkbook();
									if (tagCurrPgCount != 1) {
										System.out.println(
												"Starting to write in " + configFileReader.getReportDataFilePathTag()
														+ "_" + tagCurrPgCount + ".xls");
									}
									initiateXSSFWorkbook();
									initiateSheet("Data");
									existRowValue = 2;
									b = new ArrayList<>();
									ArrayList<String> a3 = new ArrayList<>();
									a3.add(meth.tagValueTotal.get(i).get(0) + " exist line number");
									a3.add(meth.tagValueTotal.get(i).get(0) + " value");
									a3.add("File path");
									a3.add("0");
									a3.add("0");
									b.add(a3);
								}

							}
							existRowValue++;
						}
					}
					updateExcel(b, "TAG");
					System.out.println("Finished to write in " + configFileReader.getReportDataFilePathTag() + "_"
							+ tagCurrPgCount + ".xls");
					tagCurrPgCount++;
				}

				closeXSSFWorkbook();

			} else {
				System.out.println("Starting to write in " + configFileReader.getReportDataFilePathTag() + "_"
						+ tagCurrPgCount + ".xls");
				tagCurrPgCount++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			meth.tagValueList.clear();
		}
	}

	public ArrayList<Integer> getCount(int count, int constant) {
		ArrayList<Integer> countArray = new ArrayList<Integer>();
		int limitCount = constant;
		int a = 0;
		int b = 0;
		a = count / limitCount;

		for (int i = 1; i <= a; i++) {
			countArray.add(i * limitCount);
		}
		countArray.add(count);
		return countArray;
	}

	public void autoSizeColumns() {
		try {
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}

	}
	
	public void startPropertyFile() {
		   PropertyConfigurator.configure("config/Configuration.properties");
	}

}
