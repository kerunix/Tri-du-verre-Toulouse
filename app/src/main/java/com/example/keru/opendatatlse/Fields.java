
package com.example.keru.opendatatlse;

import com.google.api.client.util.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fields {

    @Key
    private String commune;
    @Key
    private String adresse;
    @Key
    private String codeCom;
    @Key
    private List<Double> geoPoint2d = new ArrayList<Double>();
    @Key
    private String dmtType;
    @Key
    private GeoShape geoShape;
    @Key
    private String id;
    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodeCom() {
        return codeCom;
    }

    public void setCodeCom(String codeCom) {
        this.codeCom = codeCom;
    }

    public List<Double> getGeoPoint2d() {
        return geoPoint2d;
    }

    public void setGeoPoint2d(List<Double> geoPoint2d) {
        this.geoPoint2d = geoPoint2d;
    }

    public String getDmtType() {
        return dmtType;
    }

    public void setDmtType(String dmtType) {
        this.dmtType = dmtType;
    }

    public GeoShape getGeoShape() {
        return geoShape;
    }

    public void setGeoShape(GeoShape geoShape) {
        this.geoShape = geoShape;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
