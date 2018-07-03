package com.buddz.auth.controller;

import static com.buddz.auth.util.OTPUtil.generateOTP;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;

@RestController
public class OTPController {
			
	@GetMapping(path = "/sendOTP")
	public void sendOTP(@RequestParam String n) {
		
		String otp = generateOTP(n);
		System.out.println("OTP = " + otp);
			
	}
	
	@GetMapping(path = "/clearOTP")
	public void clearOTP(@RequestParam String n, @RequestParam String o) {
		clearOTP(n, o);
	}
	
}
