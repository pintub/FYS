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

@JsonInclude(JsonInclude.Include.NON_NULL)
final class TextOverlay {

    @JsonProperty("Lines")
    private List<Object> lines = null;
    @JsonProperty("HasOverlay")
    private Boolean hasOverlay;
    @JsonProperty("Message")
    private String message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Lines")
    public List<Object> getLines() {
        return lines;
    }

    @JsonProperty("Lines")
    public void setLines(List<Object> lines) {
        this.lines = lines;
    }

    @JsonProperty("HasOverlay")
    public Boolean getHasOverlay() {
        return hasOverlay;
    }

    @JsonProperty("HasOverlay")
    public void setHasOverlay(Boolean hasOverlay) {
        this.hasOverlay = hasOverlay;
    }

    @JsonProperty("Message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
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
        return new ToStringBuilder(this).append("lines", lines).append("hasOverlay", hasOverlay).append("message", message).append("additionalProperties", additionalProperties).toString();
    }

}
