package com.gaebal_easy.client.slack.exception;

import gaebal_easy.common.global.exception.BaseException;
import gaebal_easy.common.global.exception.Code;

public class SlackException extends BaseException {
	public SlackException(Code errorCode) {
		super(errorCode);
	}
	public static class SlackMessageException extends SlackException {
		public SlackMessageException() {
			super(Code.SLACK_MESSAGE_NOT_SEND_EXCEPTION);
		}
	}
	public static class SlackNotAuthorizedException extends SlackException {
		public SlackNotAuthorizedException() {
			super(Code.SLACK_AUTH_EXCEPTION);
		}
	}

	public static class SlackInvalidUserException extends SlackException {
		public SlackInvalidUserException() {
			super(Code.SLACK_INVALID_USER_ID_EXCEPTION);
		}
	}
	public static class SlackRateLimitedException extends SlackException {
		public SlackRateLimitedException() {
			super(Code.SLACK_RATE_LIMITED_EXCEPTION);
		}
	}
	public static class SlackDMChannelException extends SlackException {
		public SlackDMChannelException() {
			super(Code.SLACK_DM_CHANNEL_OPEN_FAIL);
		}
	}
}
