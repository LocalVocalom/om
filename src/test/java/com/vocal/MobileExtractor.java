package com.vocal;

public class MobileExtractor {

	public static void main(String[] args) {
		String str = "+919651496634";
		long mobileNum = +919651496634l;
		long beforeOpeationTime = System.currentTimeMillis();
		System.out.println(str.substring(3));
		System.out.println("Took time in msecs=" + (System.currentTimeMillis() - beforeOpeationTime));
		
		beforeOpeationTime = System.currentTimeMillis();
		System.out.println(mobileNum % 10000000000l);
		System.out.println("Took time in msecs=" + (System.currentTimeMillis() - beforeOpeationTime) );
		
		// They are taking almost same time
		
		
		String mobile = "9651496634";
		long mobileNumber = 0l;
		try {
			mobileNumber = Long.parseLong(mobile);
			if( mobile.length() > 10) {
				mobileNumber = Long.parseLong(mobile.substring(mobile.length() - 10));
				System.out.println("From trueCaller, mobile after extracting=" + mobileNumber);
			}
		} catch(NumberFormatException nfe) {
			System.out.println("failed to parse mobile number,mobile=" + mobile);
		}
		System.out.println("mobile num="+ mobileNumber);
		
		
		
		
	}
}
