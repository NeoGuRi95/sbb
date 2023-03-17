package com.mysite.sbb.qwixx;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mysite.sbb.qwixx.dto.GameCreateResponse;
import com.mysite.sbb.qwixx.dto.GameResponse;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QwixxController {
  private final SimpMessageSendingOperations simpMessageSendingOperations;

  private final QwixxService qwixxService;
  private final UserService userService;

  @PostMapping("/qwixx/room")
  @PreAuthorize("isAuthenticated()")
  public String create(Principal principal, Model model) {
    GameCreateResponse response = qwixxService.createGame(principal.getName());
    model.addAttribute("gameDto", response);
    return "gameDetail";
  }

  @GetMapping("/qwixx/room/{id}")
  @PreAuthorize("isAuthenticated()")
  public String getRoom(@PathVariable("id") Long id, Principal principal, Model model) {
    SiteUser siteUser = userService.getUser(principal.getName());
    Room room = qwixxService.getRoom(id);
    if (room.getParticipantsNumber() == 5)
      return "redirect:/";
    GameResponse response = qwixxService.getGame(room, siteUser);
    model.addAttribute("gameDto", response);
    return "gameDetail";
  }

  @MessageMapping("/qwixx")
  public void message(StompMessage message) {
    if (message.getActionType().equals("roll")) {
      simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + message.getRoomId(), 6);
    } else if (message.getActionType().equals("ready")) {
      simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + message.getRoomId(), message);
    }
  }
}
