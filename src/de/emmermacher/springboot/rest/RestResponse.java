/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author emmermacherp
 */
public class RestResponse {

  private final Status              status;
  private final Map<String, String> headers = new HashMap<String, String>();
  private JSONArray                 results = new JSONArray();
  private String                    message;

  public RestResponse(Status status) {
    if (status == null) {
      throw new IllegalArgumentException("status cannot be null");
    }
    this.status = status;
  }

  public RestResponse putHeaders(Map<String, String> headers) {
    if (headers != null && !headers.isEmpty()) {
      this.headers.putAll(headers);
    }
    return this;
  }

  public RestResponse putHeader(String key, String value) {
    if (key != null && !key.isEmpty() && value != null)
      this.headers.put(key, value);
    return this;
  }

  public RestResponse setResults(JSONArray results) {
    if (results != null) {
      this.results = results;
    }
    return this;
  }

  public RestResponse setResults(JSONObject result) {
    if (results != null) {
      this.results = new JSONArray().put(result);
    }
    return this;
  }

  public RestResponse addResult(Object o) {
    if (o instanceof Boolean
        || o instanceof Double
        || o instanceof Integer
        || o instanceof JSONArray
        || o instanceof JSONObject
        || o instanceof Long
        || o instanceof String) {
      results.put(o);
    } else {
      throw new IllegalArgumentException("class is not supported to be added to JSONArray");
    }
    return this;
  }
  
  public RestResponse setMessage(String message) {
    if (message != null && !message.isEmpty()) {
      this.message = message;
    }
    return this;
  }

  private JSONObject getHeaders() {
    JSONObject headers = new JSONObject();
    for (String key : this.headers.keySet()) {
      headers.put(key, headers.get(key));
    }
    return headers;
  }

  private JSONObject toJSON() {
    return new JSONObject()
        .put("message", message != null && !message.isEmpty() ? message : status.getReasonPhrase())
        .put("results", results)
        .put("headers", getHeaders());
  }

  @Override
  public String toString() {
    return toJSON().toString();
  }

  public Response build() {
    return Response.status(status)
        .entity(toString())
        .build();
  }
}