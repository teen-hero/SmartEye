package com.yan.smarteye.integration.exception;

/**
自定义异常
 */
public class PhoneExistException extends RuntimeException {
	public PhoneExistException() {
		super("手机号存在");
	}
}
