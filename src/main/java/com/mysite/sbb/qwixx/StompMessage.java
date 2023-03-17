package com.mysite.sbb.qwixx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StompMessage {
  private String sender;
  private String roomId;
  private String actionType;
  private Object data;
}
