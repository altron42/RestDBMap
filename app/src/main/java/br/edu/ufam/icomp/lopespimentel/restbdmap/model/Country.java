package br.edu.ufam.icomp.lopespimentel.restbdmap.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by micael on 4/15/17.
 */

public class Country {

    public static final String NAME = "name";
    public static final String CAPITAL = "capital";
    public static final String LATLNG = "latlng";

    private String name;
    private String capital;
    private List<Double> latlng = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Country(String name, String capital, List<Double> latlng) {
        this.name = name;
        this.capital = capital;
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public List<Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(List<Double> latlng) {
        this.latlng = latlng;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}