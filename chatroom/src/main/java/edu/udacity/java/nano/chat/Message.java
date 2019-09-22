package edu.udacity.java.nano.chat;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

/**
 * WebSocket message model
 */
public class Message {
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVal() {
    return val;
  }

  public void setVal(String val) {
    this.val = val;
  }

  public Integer getOnlineCount() {
    return onlineCount;
  }

  public void setOnlineCount(Integer onlineCount) {
    this.onlineCount = onlineCount;
  }

  private String name;
  private String val;
  private Integer onlineCount;

}



