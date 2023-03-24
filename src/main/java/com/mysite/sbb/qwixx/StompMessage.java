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
  private Long roomId;
  private Object data;
}
