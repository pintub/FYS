package com.fys.camfeedprocessing.bean.ocr;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class OcrDetectionResponse {

    @JsonProperty("ParsedResults")
    private List<ParsedResult> parsedResults = null;
    @JsonProperty("OCRExitCode")
    private Long oCRExitCode;
    @JsonProperty("IsErroredOnProcessing")
    private Boolean isErroredOnProcessing;
    @JsonProperty("ProcessingTimeInMilliseconds")
    private String processingTimeInMilliseconds;
    @JsonProperty("SearchablePDFURL")
    private String searchablePDFURL;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ParsedResults")
    public List<ParsedResult> getParsedResults() {
        return parsedResults;
    }

    @JsonProperty("ParsedResults")
    public void setParsedResults(List<ParsedResult> parsedResults) {
        this.parsedResults = parsedResults;
    }

    @JsonProperty("OCRExitCode")
    public Long getOCRExitCode() {
        return oCRExitCode;
    }

    @JsonProperty("OCRExitCode")
    public void setOCRExitCode(Long oCRExitCode) {
        this.oCRExitCode = oCRExitCode;
    }

    @JsonProperty("IsErroredOnProcessing")
    public Boolean getIsErroredOnProcessing() {
        return isErroredOnProcessing;
    }

    @JsonProperty("IsErroredOnProcessing")
    public void setIsErroredOnProcessing(Boolean isErroredOnProcessing) {
        this.isErroredOnProcessing = isErroredOnProcessing;
    }

    @JsonProperty("ProcessingTimeInMilliseconds")
    public String getProcessingTimeInMilliseconds() {
        return processingTimeInMilliseconds;
    }

    @JsonProperty("ProcessingTimeInMilliseconds")
    public void setProcessingTimeInMilliseconds(String processingTimeInMilliseconds) {
        this.processingTimeInMilliseconds = processingTimeInMilliseconds;
    }

    @JsonProperty("SearchablePDFURL")
    public String getSearchablePDFURL() {
        return searchablePDFURL;
    }

    @JsonProperty("SearchablePDFURL")
    public void setSearchablePDFURL(String searchablePDFURL) {
        this.searchablePDFURL = searchablePDFURL;
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
        return new ToStringBuilder(this).append("parsedResults", parsedResults).append("oCRExitCode", oCRExitCode).append("isErroredOnProcessing", isErroredOnProcessing).append("processingTimeInMilliseconds", processingTimeInMilliseconds).append("searchablePDFURL", searchablePDFURL).append("additionalProperties", additionalProperties).toString();
    }

    public List<String> getParsedText(){
        return this.getParsedResults().stream().map(result -> result.getParsedText()).collect(Collectors.toList());
    }

}
