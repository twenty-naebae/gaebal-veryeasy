package com.gaebal_easy.client.store.exception;

import gaebal_easy.common.global.exception.BaseException;
import gaebal_easy.common.global.exception.Code;

public class StoreException extends BaseException {

	public StoreException(Code errorCode) {
		super(errorCode);
	}
	public static class StoreNotFoundException extends StoreException {
		public StoreNotFoundException() {
			super(Code.STORE_NOT_FOUND_EXCEPTION);
		}
	}
	public static class StoreManagerNotFoundException extends StoreException {
		public StoreManagerNotFoundException() {
			super(Code.STORE_MANAGER_NOT_FOUND_EXCEPTION);
		}
	}
}
