/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ubuntu
 *
 */
public class SearchModel {
  private Map<String, String> attributes = new LinkedHashMap<String, String>();
  private String              url;

  private String              searchText;
  private String              searchAttribute;

  public SearchModel() {
  }

  public SearchModel(String url, Map<String, String> attributes) {
    this.url = url;
    this.attributes.putAll(attributes);
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Map<String, String> getAttributes() {
    if (attributes == null) {
      this.attributes = new HashMap<String, String>();
    }
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  public String getSearchText() {
    return searchText;
  }

  public void setSearchText(String searchText) {
    this.searchText = searchText;
  }

  public String getSearchAttribute() {
    return searchAttribute;
  }

  public void setSearchAttribute(String searchAttribute) {
    this.searchAttribute = searchAttribute;
  }

}
