/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.model;

import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * @author ubuntu
 *
 */
public class SortModel {

  private String  sortAttribute;
  private Boolean sortAsc;

  public SortModel(String sortAttribute, Boolean sortAsc) {
    this.sortAttribute = sortAttribute;
    this.sortAsc = sortAsc;
  }

  public String getSortAttribute() {
    return sortAttribute;
  }

  public void setSortAttribute(String sortAttribute) {
    this.sortAttribute = sortAttribute;
  }

  public Boolean getSortAsc() {
    return sortAsc;
  }

  public void setSortAsc(Boolean sortAsc) {
    this.sortAsc = sortAsc;
  }
  
  public Bson getBson() {
    return new Document(sortAttribute, sortAsc ? 1 : -1);
  }
}
