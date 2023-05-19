package com.mysite.sbb.qwixx;

import com.mysite.sbb.qwixx.dto.GameInfoResponse;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QwixxMessageController {

  private final SimpMessageSendingOperations simpMessageSendingOperations;

  private final QwixxService qwixxService;
  private final UserService userService;
  private final RoomSearchService roomSearchService;
  private final QwixxUtil qwixxUtil;

  @MessageMapping("/qwixx")
  public void qwixxMessageManage(StompMessage message) {
    Long roomId = message.getRoomId();
    String username = message.getUsername();
    String actionType = message.getActionType();
    List<String> clickList = message.getClickList();
    if (actionType.equals("ready")) {
      SiteUser siteUser = userService.getUser(username);
      Room room = roomSearchService.getRoom(roomId);
      qwixxService.setReady(room, siteUser);
      GameInfoResponse gameInfoResponse = qwixxService.getGameInfo(room);
      simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + roomId, gameInfoResponse);
      if (gameInfoResponse.getIsAllReady()) qwixxService.initializeRoomReady(room);
    }
    else if (actionType.equals("roll")) {
      Room room = roomSearchService.getRoom(roomId);
      GameInfoResponse gameInfoResponse = qwixxService.getGameInfo(room);
      qwixxUtil.rollDice(gameInfoResponse);
      simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + roomId, gameInfoResponse);
    }
    else if (actionType.equals("exit")) {
      SiteUser siteUser = userService.getUser(username);
      Room room = roomSearchService.getRoom(roomId);
      qwixxService.deleteParticipant(room, siteUser);
      GameInfoResponse gameInfoResponse = qwixxService.getGameInfo(room);
      if (!gameInfoResponse.getUsernames().isEmpty()) {
        simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + roomId, gameInfoResponse);
      }
    }
    else if (actionType.equals("enter")) {
      Room room = roomSearchService.getRoom(roomId);
      GameInfoResponse gameInfoResponse = qwixxService.getGameInfo(room);
      simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + roomId, gameInfoResponse);
    }
    else if (actionType.equals("turnEnd")) {
      SiteUser siteUser = userService.getUser(username);
      Room room = roomSearchService.getRoom(roomId);
      qwixxService.turnEnd(room, siteUser, clickList);
      GameInfoResponse gameInfoResponse = qwixxService.getGameInfo(room);
      if (gameInfoResponse.getIsAllReady()) {
        simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + roomId, gameInfoResponse);
        qwixxService.initializeRoomReady(room);
      }
    }
  }

}
