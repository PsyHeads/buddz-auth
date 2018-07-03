package com.buddz.auth.communication;

public interface OTPStrategy {

  public void send(String mobileNo, String message);
}
