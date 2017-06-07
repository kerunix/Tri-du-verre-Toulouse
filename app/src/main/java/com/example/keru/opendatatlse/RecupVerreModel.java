
package com.example.keru.opendatatlse;

import com.google.api.client.util.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecupVerreModel {

     @Key
    private Integer nhits;
     @Key
    private Parameters parameters;
     @Key
    private List<Record> records = new ArrayList<Record>();
     @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getNhits() {
        return nhits;
    }

    public void setNhits(Integer nhits) {
        this.nhits = nhits;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
