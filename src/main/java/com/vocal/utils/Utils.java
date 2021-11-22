package com.vocal.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HttpHeaders;

public class Utils {
	
	private static Random rand = new Random();

	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
	
	public static String getClientIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String x;
		LOGGER.debug("IP via method={}", remoteAddr);
		if ((x = request.getHeader(HttpHeaders.X_FORWARDED_FOR)) != null) {
			remoteAddr = x;
			LOGGER.debug("requestIP={}", remoteAddr);
			int idx = remoteAddr.indexOf(',');
			if (idx > -1) {
				remoteAddr = remoteAddr.substring(0, idx);
				LOGGER.debug("IP from header={}", remoteAddr);
			}
		}
		return remoteAddr;
	}
	
	public String getUserAgentOS(HttpServletRequest req) {
		String userAgentRawString = req.getHeader("User-Agent");
		LOGGER.debug("User-Agent header={}", userAgentRawString);
		String userAgent = userAgentRawString.toLowerCase();
		String os; 
		if(userAgent.indexOf("android") >= 0) {
			os = "Android";
		} else if(userAgent.indexOf("windows") >= 0) {
			os = "Windows";
		} else if(userAgent.indexOf("mac") >= 0) {
			os = "Mac";
		} else if(userAgent.indexOf("x11") >= 0) {
			os = "Unix";
		} else {
			os = "Unknown";
		}
		LOGGER.debug("extracted OS from User-Agent={}", os);
		return os;
	}
	
	public static String getFileName(String url) {
		String fileName = "";
		try {
			String sbuff[] = url.split("\\/");
			int len = sbuff.length;
			LOGGER.info("Last part of filepath={}", sbuff[len - 1]);
			LOGGER.info("Filepath before last part={}", sbuff[len - 2]);
			fileName = sbuff[len - 1];
		} catch (Exception e) {
			LOGGER.info("Failed to extract filename, exited with exception={}" + e.getMessage());
		}
		return fileName;
	}
	
	public synchronized static String getID() {
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");
		String tid = df.format(date);
		tid = tid + "9";
		return tid;
	}
	
	public static int generateOtp() {
		Random r = new Random(System.currentTimeMillis());
		return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}
	
	public static String VerifyGoogleToken(String token) throws Exception
	{
		String url = Constants.TOKEN_INFO_ENDPOINT + token;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		
		LOGGER.info("Response Code:" + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println("["+Utils.getID()+"] "+response.toString());
		return response.toString();
	}
	
	public static boolean isValidGoogleToken(String token) {
		boolean isAuthorizedToken = false;
		String googleResponse;
		try {
			googleResponse = Utils.VerifyGoogleToken(token);
			JSONObject obj 		= new JSONObject(googleResponse);
			String Gname 		= obj.getString("name");
			String Gemail 		= obj.getString("email");
			String Gemailverified 	= obj.getString("email_verified");
			String Gazp           	= obj.getString("azp");
			String Gpicture        	= obj.getString("picture");
			LOGGER.info("VOKAL: Gname:"+Gname+" Gemail:"+Gemail+" Gemailverified:"+Gemailverified+" Gazp:"+Gazp + "Gpicture"+Gpicture);
			if(Gemailverified.equals("true"))
			{
				LOGGER.info("VOKAL: Gname:{},Gemail:{},Gemailverified:{},Gazp:{}", Gname, Gemail, Gemailverified, Gazp);
				LOGGER.info("Gazp={}", Gazp);
				if(Gazp.equals("789096192065-afpufc5kf15l5rrimks74712d7onjovn.apps.googleusercontent.com"))
				{
					//email = Gemail;
					LOGGER.info("azp matched");
				}
				isAuthorizedToken = true;
			}
		} catch (Exception e) {
			LOGGER.info("Unable to connect to google to vefify token, occured={}", e.getMessage());
		}
		
		return isAuthorizedToken;
	}
	
	public static boolean isTenDigitLongNumber(long mobileNum) {
//		if(mobileNum > 0) {
//			int numDigits = (int) Math.log10(mobileNum) + 1;
//			return numDigits == 10;
//		} else { 
//			return false;
//		}
		long minNumOfElevenDigit = 1_000_000_000_0L;
		long maxNumOfNineDigit = 99_999_999_9L;
		return (mobileNum > maxNumOfNineDigit) && (mobileNum < minNumOfElevenDigit);
	}
	
	
	public static String sendUDP(String strFinal, String ip, String port) {
		LOGGER.info("sendUDP, strFinal={}, ip={}, port={}", strFinal, ip, port);
		String resp = "";
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			int localport = clientSocket.getLocalPort();
			String portn = localport + "";
			strFinal = strFinal.replace("LPORT", portn);
			InetAddress IPAddress = InetAddress.getByName(ip);
			DatagramPacket sendPacket = new DatagramPacket(strFinal.getBytes(), strFinal.getBytes().length, IPAddress,
					Integer.parseInt(port));
			clientSocket.send(sendPacket);
			LOGGER.info("sendUDP[{}] IP:{} , Port:{}", strFinal, ip, port);
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("sendUDP, exception={}" + e.getMessage());
		}
		return resp;
	}
	
	public static long getDifferenceInDays(Date d1, Date d2) {
		return ChronoUnit.DAYS.between(d1.toInstant(), d2.toInstant());
	}
	
	public static UtmParameters extractUtmParameters(String utmSource, String utmMedium, String utmTerm, String utmCampaign) {
		UtmParameters utmParameters = new UtmParameters(utmSource, utmTerm, utmCampaign, utmMedium);
		
		try {
			String[] values = utmSource.split("\\&");
			if(values.length > 1) {
				for(String value : values) {
					String[] keyValuePair = value.split("=");
					String key = keyValuePair[0];
					String val = keyValuePair[1];
					if(key.equals("utm_source")) {
						utmParameters.setUtmSource(val);
						LOGGER.debug("utm_source updated");
					}
					else if(key.equals("utm_medium")) {
						utmParameters.setUtmMedium(val);
						LOGGER.debug("utm_medium updated");
					}
					else if(key.equals("utm_term")) {
						utmParameters.setUtmTerm(val);
						LOGGER.debug("utm_term updated");
					} else if(key.equals("utm_campaign")) {
						utmParameters.setUtmCampaign(val);
						LOGGER.debug("utm_campaign updated");
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("Exception while extracting user source info, exp=", ex.getMessage());
		}
		LOGGER.debug("utm parameters after extraction");
		LOGGER.debug("UTM_SOURCE={}, UTM_MEDIUM={}, UTM_TERM={}, UTM_CAMPAIGN={}",  utmParameters.getUtmSource(), utmParameters.getUtmMedium(), utmParameters.getUtmTerm(), utmParameters.getUtmCampaign());
		return utmParameters;
	}
	
	public static int nextRandomInt() {
		return rand.nextInt();
	}
	
	public static int nextRandomInt(int bound) {
		return rand.nextInt(bound);
	}
}
