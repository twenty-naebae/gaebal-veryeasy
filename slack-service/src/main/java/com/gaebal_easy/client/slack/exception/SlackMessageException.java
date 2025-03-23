package com.gaebal_easy.client.slack.exception;

import gaebal_easy.common.global.exception.BaseException;
import gaebal_easy.common.global.exception.Code;

public class SlackMessageException extends BaseException {
	public SlackMessageException(Code errorCode) {
		super(errorCode);
	}
	public static class SlackMessageNotFoundException extends SlackMessageException {
		public SlackMessageNotFoundException() {
			super(Code.SLACK_MESSAGE_NOT_SEND_EXCEPTION);
		}
	}
	public static class SlackMessageNotSendException extends SlackMessageException {
		public SlackMessageNotSendException() {
			super(Code.SLACK_MESSAGE_NOT_SEND_EXCEPTION);
		}
	}
	public static class SlackNotAuthorizedMessageException extends SlackMessageException {
		public SlackNotAuthorizedMessageException() {
			super(Code.SLACK_AUTH_EXCEPTION);
		}
	}

	public static class SlackInvalidUserMessageException extends SlackMessageException {
		public SlackInvalidUserMessageException() {
			super(Code.SLACK_INVALID_USER_ID_EXCEPTION);
		}
	}
	public static class SlackRateLimitedMessageException extends SlackMessageException {
		public SlackRateLimitedMessageException() {
			super(Code.SLACK_RATE_LIMITED_EXCEPTION);
		}
	}
	public static class SlackDMChannelMessageException extends SlackMessageException {
		public SlackDMChannelMessageException() {
			super(Code.SLACK_DM_CHANNEL_OPEN_FAIL);
		}
	}
}
