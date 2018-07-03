package com.buddz.auth.communication;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;

public class NexmoStrategy implements OTPStrategy {

	@Value("${nexmo.api.key}")
    String nexmoAPIKey;
	
	@Value("${nexmo.api.secret}")
    String nexmoAPISecret;
	
	private AuthMethod auth; 
	private NexmoClient client; 
	
	NexmoStrategy() {
		AuthMethod auth = new TokenAuthMethod(nexmoAPIKey, nexmoAPISecret);
		NexmoClient client = new NexmoClient(auth);
	}
	
	@Override
	public void send(String mobileNo, String message) {
		
		SmsSubmissionResult[] responses = null;
		try {
			responses = client.getSmsClient().submitMessage(new TextMessage(
			        "Eneven Computing",
			        mobileNo,
			        message));
		} catch (IOException | NexmoClientException e) {
			e.printStackTrace();
		}
		for (SmsSubmissionResult response : responses) {
		    System.out.println(response);
		}

	}
}
