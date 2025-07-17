package library;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author Abirami Custom Methods of different Date format.
 */

public class DateFormatUtilities {
	
	private DateFormatUtilities(){
		
	}
	private static final Logger logger = LogManager.getLogger(DateFormatUtilities.class);
	public static String sYear;
	protected static String sMonth = "";
	protected static String sDay;
	protected static Date date;
	private static final String[] formats = { "MM/dd/yyyy", "dd/MM/yyyy", "yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
			"MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS", "MM/dd/yyyy'T'HH:mm:ssZ",
			"MM/dd/yyyy'T'HH:mm:ss", "yyyy:MM:dd HH:mm:ss", "yyyyMMdd", };


	// Check Date format(if Date is invalid it returns 'null'
	public static String checkDateFormat(String inputDate) {
		logger.info("Check Date Format");
		SimpleDateFormat dateFormats = new SimpleDateFormat("dd/MM/yyyy");
		date = null;
		if (null == inputDate) {
			return null + "";
		}
		try {
			dateFormats.setLenient(false);
			date = dateFormats.parse(inputDate);
			return inputDate;
		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return null + "";
	}

	// Check Date format(if Date is invalid it returns 'null'
	public static String checkDateFormatddmm(String inputDate) {

		SimpleDateFormat dateFormats = new SimpleDateFormat("dd/MM");
		date = null;
		if (null == inputDate) {
			return null + "";
		}
		try {
			dateFormats.setLenient(false);
			date = dateFormats.parse(inputDate);
			return inputDate;
		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return null + "";
	}

	// Compare Two Dates if both same it returns True other wise false
	public static boolean compareDatesForEqual(String date1, String date2) throws ParseException {
		boolean flag = false;
		if (!checkDateFormat(date1).equalsIgnoreCase("null") && !checkDateFormat(date2).equalsIgnoreCase("null")) {
			SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
			Date d1 = format.parse(date1);
			
			Date d2 = format.parse(date2);
			if (d1.compareTo(d2) != 0) {
				flag = true;
			}
		}
		return flag;
	}

	// Seperate year in dd/MM/YYY
	public static String seperateYear(String dateString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/yyyy");
		Date date =  formatter.parse(dateString);
		DateFormat year = new SimpleDateFormat("yyyy");
		return year.format(date);
	}

	// Seperate month in dd/MM/YYY
	public static String seperateMonth(String dateString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/yyyy");
		Date date =  formatter.parse(dateString);
		DateFormat month = new SimpleDateFormat("MM");
		return month.format(date);

	}

	// Seperate Days in dd/MM/YYY hh:mm:ss formate
	public static String seperateDate_Time(String dateString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/yyyy");
		Date date =  formatter.parse(dateString);
		DateFormat year = new SimpleDateFormat("dd");
		return year.format(date);
	}

	
	// Separate Month From Date if input 5/6/2014 it returns June
	public static String separateMonthFromDate(String dateString) throws ParseException {
		String sMonth = "";
		SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		Date date =  formatter.parse(dateString);
		DateFormat month = new SimpleDateFormat("MMMM");
		sMonth = month.format(date);
		return sMonth;

	}

	// Separate Day from Date ( Exp u input 05/06/2015 it return 5)
	public static String separateDayFromDate(String dateString) throws ParseException {
		String sDay = "";
		SimpleDateFormat formatter = new SimpleDateFormat("dd/Mm/yyyy");
		Date date =  formatter.parse(dateString);
		DateFormat day = new SimpleDateFormat("dd");
		sDay = day.format(date);
		return sDay;

	}

	// Calculate Difference between two Dates in Days
	public static int dateDifferenceInDays(String dateStart, String dateStop){

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("DD/mm/yyyy");
		int dayDiff = 0;
		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();
			long diffDays = (diff / (24 * 60 * 60 * 1000)) + 1;
			dayDiff = (int) diffDays;

		} catch (Exception e) {

			logger.info(e.getMessage());
		}
		return dayDiff;

	}

	// Calculate Months Difference from Current Date
	public static int diffInMonthsFromCurrent(String Date)  {

		String curDate = DateFormatUtilities.getCurrentDate();

		String[] inputDate = Date.split("/");
		String[] currentDate = curDate.split("/");

		int imon = Integer.parseInt(inputDate[1]);
		int cmon = Integer.parseInt(currentDate[1]);
		int iyear = Integer.parseInt(inputDate[2]);
		int cyear = Integer.parseInt(currentDate[2]);

		int imonths = iyear * 12 + imon;
		int Cmonths = cyear * 12 + cmon;

		return imonths - Cmonths + 1;

	}

	// Calculate Months Difference from Two Dates
	public static int diffInMonthsInTwoDates(String startdate, String enddate){

		String[] sdate = startdate.split("/");
		String[] edate = enddate.split("/");

		int cmon = Integer.parseInt(sdate[1]);
		int imon = Integer.parseInt(edate[1]);

		int iyear = Integer.parseInt(edate[2]);
		int cyear = Integer.parseInt(sdate[2]);

		int imonths = iyear * 12 + imon;
		int Cmonths = cyear * 12 + cmon;

		return imonths - Cmonths + 1;

	}

	// Compare Two Dates
	public static int dateComparison(String date1, String date2) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
		Date d1 = format.parse(date1);
		
		Date d2 = format.parse(date2);
		return d1.compareTo(d2);
		
	}

	// Addition Number of days in a specific date(dd/MM/yyyy Format)
	public static String addDaysToDate(String aDate, int NofDays) throws ParseException{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(aDate));
		cal.add(Calendar.DATE, NofDays);
		return dateFormat.format(cal.getTime());
	}

	// Addition Number of days in a specific date(dd/MM/yyyy Format)
	public static String removeDaysFromDate(String aDate, int NofDays) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(aDate));
		cal.add(Calendar.DATE, -NofDays);
		return dateFormat.format(cal.getTime());
	
	}

	// convert Date into Specific Date Format D/M/YYYY
	public static String convertDateIntoSpecificFormat(String dateInString) {

		SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
		String convertDate = null;

		try {

			Date date = formatter.parse(dateInString);
			convertDate = formatter.format(date);

		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return convertDate;

	}

	// convert date From DD-MMM-YYYY Into M/D/YYYY
	public static String convertDateDDMMMYYYYToDMYYYY(String originalDateString) {

		String finalDateForamt = "";
		SimpleDateFormat sourseFormat = new SimpleDateFormat("dd-MMM-yyyy");

		try {
			Date date = sourseFormat.parse(originalDateString);

			SimpleDateFormat destinationForamt = new SimpleDateFormat("M/d/yyyy");
			finalDateForamt = destinationForamt.format(date);

		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return finalDateForamt;

	}

	// convert date From DD/MM/YYYY to MM/D/YY Into
	public static String convertDateDDMMYYYYToMMDYY(String originalDateString) {

		String finalDateForamt = "";
		SimpleDateFormat sourseFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			Date date = sourseFormat.parse(originalDateString);

			SimpleDateFormat destinationForamt = new SimpleDateFormat("MM/d/yy");
			finalDateForamt = destinationForamt.format(date);

		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return finalDateForamt;

	}

	// convert date From DD/MM/YYYY to MM/D/YY Into
	public static String convertDateDDMMYYYYToMDYY(String originalDateString) {
		String finalDateForamt = "";
		SimpleDateFormat sourseFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = sourseFormat.parse(originalDateString);
			SimpleDateFormat destinationForamt = new SimpleDateFormat("M/d/yy");
			finalDateForamt = destinationForamt.format(date);
		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return finalDateForamt;
	}

	// convert date From DD/MM/YYYY to MM/D/YYYY Into
	public static String convertDateDDMMYYYYToMMDYYYY(String originalDateString) {

		String finalDateForamt = "";
		SimpleDateFormat sourseFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			Date date = sourseFormat.parse(originalDateString);

			SimpleDateFormat destinationForamt = new SimpleDateFormat("MM/d/yyyy");
			finalDateForamt = destinationForamt.format(date);

		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return finalDateForamt;

	}

	// convert date From MM/D/YY to DD/MM/YYYY Into
	public static String convertDateMMDYYToDDMMYYYY(String originalDateString) {

		String finalDateForamt = "";
		SimpleDateFormat sourseFormat = new SimpleDateFormat("mm/D/yy");

		try {
			Date date = sourseFormat.parse(originalDateString);

			SimpleDateFormat destinationForamt = new SimpleDateFormat("DD/mm/yyyy");
			finalDateForamt = destinationForamt.format(date);

		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return finalDateForamt;

	}

	// Get Todays Date in '07-Feb-2014' Format
	public static String getCurrentDateDDMMMYYYYFormat() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		return formatter.format(currentDate.getTime());

	}

	// Get Todays Date in '07/26/2013' Format
	public static String getCurrentDate() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		return formatter.format(currentDate.getTime());
	}

	// Get Any Date in MM/dd/yyyy Format
	public static String getDate_MM_DD_YYYY(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try {
			date =  formatter.parse(dateString);
		} catch (ParseException e) {

			logger.info(e.getMessage());
		}
		return formatter.format(date);
		
	}


	// get the date with Day in 'Friday, July 26, 2013' format
	public static String getCurrentDayWithDate() {

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
		return formatter.format(currentDate.getTime());
		
	}

	// get the date with time in '2013.Jul.26 02.58.55.PM' format
	public static String getCurrentDateTime() {

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MMM.dd.hh.mm.ss.a");
		return formatter.format(currentDate.getTime());
		
	}

	// get the date with time in 'July 26, 2013 02:16:45 PM' format
	public static String getCurrentDateTimeFormatted() {

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a");
		return formatter.format(currentDate.getTime());
		}

	// get the date with time in 'July 26, 2013 02:16:45 PM' format
	public static String getDateTimeFormattedDDMMYYYHHMMSS(String date1) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a");

		Date date =  formatter.parse(date1);
		return formatter.format(date);
		
	}

	// get the date with time in 'July 26, 2013 02:16:45 PM' format
	public static String Convert_DateTime_DDMMYYYHHMMSS_to_DDMMYYYY(String date1) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

		Date date =  formatter.parse(date1);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(date);
	}

	// get the before Date of number of day
	public static String getBeforeDate(int beforeDay) {

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, -beforeDay);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(currentDate.getTime());
		
	}

	// get the after Date of number of day
	public static String getAfterDate(int afterDay) {

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, afterDay);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(currentDate.getTime());
		
	}

	// get the Yesterday Date
	public static String getYesterdayDate() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(currentDate.getTime());
		
	}

	// get the Tomorrow Date
	public static String getTomorrowDate() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, 1);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(currentDate.getTime());
		
	}

	// get the Tomorrow Date with Hyphen format
	public static String getTomorrowDateWithHyphenFormat() {
		String dateStringwithHifen = getTomorrowDate();
		String [] dateWithSlash = dateStringwithHifen.split("/");
		String monthInChar = dateWithSlash[0];
		String day = dateWithSlash[1];
		String yearInChar = dateWithSlash[2];
		if (monthInChar.equals("01"))
			monthInChar = "Jan";
		else if (monthInChar.equals("02"))
			monthInChar = "Feb";
		else if (monthInChar.equals("03"))
			monthInChar = "Mar";
		else if (monthInChar.equals("04"))
			monthInChar = "Apr";
		else if (monthInChar.equals("05"))
			monthInChar = "May";
		else if (monthInChar.equals("06"))
			monthInChar = "Jun";
		else if (monthInChar.equals("07"))
			monthInChar = "Jul";
		else if (monthInChar.equals("08"))
			monthInChar = "Aug";
		else if (monthInChar.equals("09"))
			monthInChar = "Sep";
		else if (monthInChar.equals("10"))
			monthInChar = "Oct";
		else if (monthInChar.equals("11"))
			monthInChar = "Nov";
		else if (monthInChar.equals("12"))
			monthInChar = "Dec";
	
		return day + "-" + monthInChar + "-" + yearInChar;
	}

	/*
	 * this is used to get nect Day date of the date passing in argument +1 for next
	 * day, -1 for previouss day.
	 */
	public static String getNextOrPreviousDayDate(String aDate, int NofDays) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date myDate = dateFormat.parse(aDate);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(myDate);
		cal1.add(Calendar.DAY_OF_YEAR, NofDays);
		Date Date = cal1.getTime();
		String d = String.valueOf(Date);

		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

		Date date =  formatter.parse(d);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(date);
		
	}

	public static void change_SysytemDate() throws IOException, InterruptedException, ParseException {
		
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String value = "02/03/2017";
			Date date = dateFormat.parse(value);
			value = dateFormat.format(date);
			final Process dateProcess = Runtime.getRuntime()
					.exec("cmd /c date " + value.substring(0, value.lastIndexOf(' ')));
			dateProcess.waitFor();
			dateProcess.exitValue();
			final Process timeProcess = Runtime.getRuntime()
					.exec("cmd /c time " + value.substring(value.lastIndexOf(' ') + 1));
			timeProcess.waitFor();
			timeProcess.exitValue();	
	}

	/*
	 * @Author: Abirami
	 * 
	 * Purpose: This is used to generate randomString
	 */
	public static String generateRandomStringFromDateWithoutSpecialChars() {

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss a");
		String dateNow = formatter.format(currentDate.getTime());
		return dateNow.replace(" ", "").replace(":", "").replace(",", "");
	}

	/*
	 * Get No. of Weeks between two dates
	 * 
	 * @author:Abirami
	 *  
	 * @Purpose: This function is used to Get No. of Weeks between two dates
	 */
	public static int getWeeksBetween(String dateA, String dateB) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date a = null;
		Date b = null;
		a =  formatter.parse(dateA);
		b =  formatter.parse(dateB);
		if (b.before(a)) {
			return 0;
		}
		a = resetTime(a);
		b = resetTime(b);

		Calendar cal = new GregorianCalendar();
		cal.setTime(a);
		int weeks = 0;
		while (cal.getTime().before(b)) {
			// add another week
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			weeks++;
		}
		return weeks;
	}

	public static Date resetTime(Date d) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		return cal.getTime();
	}

	/**
	 * @author Abirami
	 * @purpose to get business working days between two dates
	 * @param startDate
	 * @param endDate
	 * @return count
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static int getWorkingDaysBetweenTwoDates(String startDate, String endDate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date sDate = null;
		Date eDate = null;
		sDate =  formatter.parse(startDate);
		eDate = formatter.parse(endDate);
		Calendar startCal;
		Calendar endCal;
		startCal = Calendar.getInstance();
		startCal.setTime(sDate);
		endCal = Calendar.getInstance();
		endCal.setTime(eDate);
		int workDays = 0;
		// Return 0 if start and end are the same
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}
		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(sDate);
			endCal.setTime(eDate);
		}
		do {
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
		} while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
		return workDays;
	}

	/**
	 * @author Abirami
	 * @purpose to convert date in required format from any format
	 * @param originalDateString
	 * @param fromFormat
	 * @param toFormat
	 * @return
	 */
	public static String convertDateFromAnyFormatToAnyFormart(String originalDateString, String fromFormat,
			String toFormat) {
		String finalDateForamt = "";
		SimpleDateFormat sourseFormat = new SimpleDateFormat(fromFormat);

		try {
			Date date = sourseFormat.parse(originalDateString);

			SimpleDateFormat destinationForamt = new SimpleDateFormat(toFormat);
			finalDateForamt = destinationForamt.format(date);

		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		return finalDateForamt;
	}

	/**
	 * @author Abirami
	 * @purpose This method will return the value of next nth date from the current
	 *          date
	 * @param difference
	 * @return
	 */
	public static String getNthDateFromCurrentDate(int difference) {
		String nthDate = null;
		Calendar cal = Calendar.getInstance();
		// adding days into Date in Java
		cal.add(Calendar.DATE, difference - 1);

		nthDate = "" + cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
		return nthDate;
	}

	/**
	 * @author Abirami
	 * @purpose To get Current Week Monday
	 * @return date of Monday from current week
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static String getMondayofWeek(String date) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(date));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return dateFormat.format(cal.getTime());
	}

	/**
	 * @author Abirami
	 * @purpose To get Tuesday of given date week
	 * @return date of Tuesday of given date week
	 * @throws Exception
	 */
	public static String getTuesdayofWeek(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(date));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		return dateFormat.format(cal.getTime());
	}

	/**
	 * @author Abirami
	 * @purpose To get Thursday of given date week
	 * @return date of Thursday of given date week
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static String getThursdayofWeek(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(date));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		return dateFormat.format(cal.getTime());
	}

	/**
	 * @author Abirami
	 * @purpose To get Current Week Sunday
	 * @return date of Sunday from current week
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static String getSundayofWeek(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(date));
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return dateFormat.format(cal.getTime());
	}

	/**
	 * @author Abirami
	 * @purpose To Add Business Days to Date
	 * @return Date with added business days
	 * @throws Exception
	 */
	public static String addBusinessDaytoDate(String aDate, Integer numBusinessDays) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(aDate));

		if (numBusinessDays == null || numBusinessDays.intValue() == 0) {
			return dateFormat.format(cal.getTime());
			
		}
		int numDays = Math.abs(numBusinessDays.intValue());
		int dateAddition = numBusinessDays.intValue() < 0 ? -1 : 1;// if numBusinessDays is negative
		int businessDayCount = 0;
		while (businessDayCount < numDays) {
			cal.add(Calendar.DATE, dateAddition);
			// check weekend
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				continue;// adds another day
			}

			businessDayCount++;
		}
		return dateFormat.format(cal.getTime());
		
	}

	/**
	 * @author Abirami
	 * @purpose To Check if the given date is with in specified range
	 * @return True/False if date is in given range
	 * @throws ParseException 
	 * @throws Exception
	 */
	public static boolean checkDateBetweenDateRange(String minDate, String maxDate, String checkDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date min = dateFormat.parse(minDate);
		Date max = dateFormat.parse(maxDate);
		Date d = dateFormat.parse(checkDate);
		return d.compareTo(min) >= 0 && d.compareTo(max) <= 0;
	}

	/**
	 * @author Abirami
	 * @purpose This method will return the true is date in argument is before the
	 *          current date else return false
	 * @return
	 * @throws ParseException
	 */
	public static boolean beforeDateVerification(String dateToVerify) throws ParseException {
		boolean booleanResult = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date currentdate = new Date();
		String formatedDate = dateFormat.format(currentdate);
		Date dateToBeVeirfied = dateFormat.parse(dateToVerify);
		if (dateToBeVeirfied.before(currentdate))
			booleanResult = true;

		return booleanResult;
	}

	/**
	 * @author Abirami
	 * @purpose This method will return the true is darte in argument is before the
	 *          current date else return false
	 * @return
	 * @throws ParseException
	 */
	public static boolean afterDateVerification(String dateToVerify) throws ParseException {
		boolean booleanResult = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date currentdate = new Date();

		Date dateToBeVeirfied = dateFormat.parse(dateToVerify);
		if (dateToBeVeirfied.after(currentdate))
			booleanResult = true;

		return booleanResult;
	}

	// get the day of Week name
	public static String getDaysOfWeeks(int i) {
		String day = null;
		if (i == 0)
			day = "MONDAY";
		else if (i == 1)
			day = "TUESDAY";
		else if (i == 2)
			day = "WEDNESDAY";
		else if (i == 3)
			day = "THURADAY";
		else if (i == 4)
			day = "FRIDAY";
		else if (i == 5)
			day = "SATURDAY";
		else if (i == 6)
			day = "SUNDAY";

		return day;
	}

	// get the day of Week number
	public static int getDaysOfWeeks(String day) {
		int dayNumber = 0;
		if (day.toUpperCase().equalsIgnoreCase("MONDAY"))
			dayNumber = 0;
		else if (day.toUpperCase().equalsIgnoreCase("TUESDAY"))
			dayNumber = 1;
		else if (day.toUpperCase().equalsIgnoreCase("WEDNESDAY"))
			dayNumber = 2;
		else if (day.toUpperCase().equalsIgnoreCase("THURSDAY"))
			dayNumber = 3;
		else if (day.toUpperCase().equalsIgnoreCase("FRIDAY"))
			dayNumber = 4;
		else if (day.toUpperCase().equalsIgnoreCase("SATURDAY"))
			dayNumber = 5;
		else if (day.toUpperCase().equalsIgnoreCase("SUNDAY"))
			dayNumber = 6;
		return dayNumber;
	}

	/**
	 * @author Abirami
	 * @purpose get DateTime format string value of DateTime String value
	 * @param dateTimeString
	 */
	public static String parseGetDateTimeFormat(String dateTimeString) {
		String dateFormat = null;
		if (dateTimeString != null) {
			for (String parse : formats) {
				SimpleDateFormat sdf = new SimpleDateFormat(parse);
				try {
					sdf.parse(dateTimeString);
					dateFormat = parse;
					break;
				} catch (ParseException e) {
					logger.info(e.getMessage());
				}
			}
		}
		return dateFormat;
	}

	/**
	 * @author Abirami
	 * @purpose To get Day from week
	 * @return day from current week by date
	 * @throws Exception
	 */
	public static String getDayDateofWeek(String date, String day) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(date));
		if (day.equalsIgnoreCase("Monday"))
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		if (day.equalsIgnoreCase("Tuesday"))
			cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		if (day.equalsIgnoreCase("Wednesday"))
			cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		if (day.equalsIgnoreCase("Thursday"))
			cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		if (day.equalsIgnoreCase("Friday"))
			cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		if (day.equalsIgnoreCase("Saturday"))
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		if (day.equalsIgnoreCase("Sunday"))
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return dateFormat.format(cal.getTime());
	}

	/**
	 * @author Abirami
	 * @purpose To Get Current Date Time in common MM-dd-yyyy HH-mm-ss format
	 * @note
	 */
	public static Date getDateTimeCurrent() {
		Date date = new Date();
		DateFormat dateTime = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");
		String cDate = dateTime.format(date).trim();
		try {
			return dateTime.parse(cDate);
		} catch (ParseException e) {
			return null;
		}
	}
	// get the date with time in '7/26/2013 14:24' format
		public static String getTodaysDateTime() {

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HH_mm_ss");
			return formatter.format(currentDate.getTime());
		}
}
