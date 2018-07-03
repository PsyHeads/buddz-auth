package com.buddz.auth.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPUtil {

 // TODO: Move this to redis later
  private static Map<String, String> otpCache = new HashMap<>();
  
  public static String generateOTP(String mobileNo) {
	  Random random = new Random();
	  String otp = new Integer(100000 + random.nextInt(900000)).toString();
	  otpCache.put(mobileNo, otp);
	  return otp;
  }
  
  public static void clearOTP(String mobileNo) {
	  otpCache.remove(mobileNo);
  }
  
  public static boolean doesOTPMatch(String mobileNo, String otp) {
	  return otpCache.get(mobileNo).equals(otp);
  }
}
