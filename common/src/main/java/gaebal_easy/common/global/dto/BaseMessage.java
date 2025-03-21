package gaebal_easy.common.global.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseMessage {
    private String errorLocation;
    private String errorMessage;
    private LocalDateTime errorTimestamp;

    public void setErrorInfo(String errorLocation, String errorMessage) {
        this.errorLocation = errorLocation;
        this.errorMessage = errorMessage;
        this.errorTimestamp = LocalDateTime.now();
    }
}