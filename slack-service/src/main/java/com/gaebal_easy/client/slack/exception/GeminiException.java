package com.gaebal_easy.client.slack.exception;

import gaebal_easy.common.global.exception.BaseException;
import gaebal_easy.common.global.exception.Code;

public class GeminiException extends BaseException {
	public GeminiException(Code errorCode) {
		super(errorCode);
	}
	public static class GeminiParsingException extends GeminiException {
		public GeminiParsingException() {
			super(Code.GEMINI_PARSING_EXCEPTION);
		}
	}
}
