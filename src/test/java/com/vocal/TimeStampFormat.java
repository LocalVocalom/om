package com.vocal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampFormat {

	public static void main(String[] args) {
		String filename = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
		System.out.println("filename="+filename);
	}
}
