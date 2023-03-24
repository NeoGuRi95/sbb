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
    GameResponse response = qwixxService.createGame(principal.getName());
    model.addAttribute("gameResponse", response);
    return "qwixx";
  }

  @GetMapping("/qwixx/room/{id}")
  @PreAuthorize("isAuthenticated()")
  public String getRoom(@PathVariable("id") Long id, Principal principal, Model model) {
    SiteUser siteUser = userService.getUser(principal.getName());
    Room room = qwixxService.getRoom(id);
    GameResponse response = qwixxService.getGame(room, siteUser);
    if (Boolean.TRUE.equals(response.getFullRoom())) {
      return "redirect:/";
    } else {
      model.addAttribute("gameResponse", response);
      return "qwixx";
    }
  }

  @MessageMapping("/qwixx/roll")
  public void roll(StompMessage message) {
    // simpMessageSendingOperations.convertAndSend("/topic/qwixx/" +
    // message.getRoomId(), qwixxService.roll());
    simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + message.getRoomId(), "test");
  }

  @MessageMapping("/qwixx/ready")
  public void ready(StompMessage message) {
    SiteUser siteUser = userService.getUser(message.getSender());
    Room room = qwixxService.getRoom(message.getRoomId());
    qwixxService.ready(room, siteUser);
    simpMessageSendingOperations.convertAndSend("/topic/qwixx/" + message.getRoomId(), qwixxService.isAllReady(room));
  }
}
