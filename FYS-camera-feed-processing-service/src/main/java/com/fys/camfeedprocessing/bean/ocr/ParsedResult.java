package com.fys.camfeedprocessing.bean.ocr;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
final class ParsedResult {

    @JsonProperty("TextOverlay")
    private TextOverlay textOverlay;
    @JsonProperty("TextOrientation")
    private String textOrientation;
    @JsonProperty("FileParseExitCode")
    private Long fileParseExitCode;
    @JsonProperty("ParsedText")
    private String parsedText;
    @JsonProperty("ErrorMessage")
    private String errorMessage;
    @JsonProperty("ErrorDetails")
    private String errorDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("TextOverlay")
    public TextOverlay getTextOverlay() {
        return textOverlay;
    }

    @JsonProperty("TextOverlay")
    public void setTextOverlay(TextOverlay textOverlay) {
        this.textOverlay = textOverlay;
    }

    @JsonProperty("TextOrientation")
    public String getTextOrientation() {
        return textOrientation;
    }

    @JsonProperty("TextOrientation")
    public void setTextOrientation(String textOrientation) {
        this.textOrientation = textOrientation;
    }

    @JsonProperty("FileParseExitCode")
    public Long getFileParseExitCode() {
        return fileParseExitCode;
    }

    @JsonProperty("FileParseExitCode")
    public void setFileParseExitCode(Long fileParseExitCode) {
        this.fileParseExitCode = fileParseExitCode;
    }

    @JsonProperty("ParsedText")
    public String getParsedText() {
        return parsedText;
    }

    @JsonProperty("ParsedText")
    public void setParsedText(String parsedText) {
        this.parsedText = parsedText;
    }

    @JsonProperty("ErrorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("ErrorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty("ErrorDetails")
    public String getErrorDetails() {
        return errorDetails;
    }

    @JsonProperty("ErrorDetails")
    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("textOverlay", textOverlay).append("textOrientation", textOrientation).append("fileParseExitCode", fileParseExitCode).append("parsedText", parsedText).append("errorMessage", errorMessage).append("errorDetails", errorDetails).append("additionalProperties", additionalProperties).toString();
    }

}
