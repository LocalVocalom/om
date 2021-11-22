package com.vocal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MinutesDiff {

	public static void main(String[] args) throws ParseException {
		long currentTimeInMillies = 1601279944290l;
		long timeStampInMillies = 1600449693964l;
		long minutesDiff = (currentTimeInMillies - timeStampInMillies) / (1000 * 60);
		System.out.println("The minutes difference is=" + minutesDiff);
		System.out.println("=========================================");
		
		String utmCampaign = "202010031300";
		
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		Date earlierTime = df.parse(utmCampaign);
		
		Date currentTime = new Date();
		System.out.println("earlier date=" + df.format(earlierTime)+  ", \ncurrentDate=" + df.format(currentTime));
		long minutesElapsed = (currentTime.getTime() - earlierTime.getTime()) / (1000 * 60);
		System.out.println("time elapsed in minutes =" + minutesElapsed);
	}
}
