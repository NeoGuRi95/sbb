package com.mysite.sbb.qwixx;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mysite.sbb.qwixx.dto.GameInfoResponse;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QwixxController {

  private final QwixxService qwixxService;
  private final UserService userService;
  private final RoomSearchService roomSearchService;

  @GetMapping("/qwixx/room/list")
  public String getQwixxRoomList(Model model) {
    model.addAttribute("roomList", roomSearchService.getLiveRoomList());
    return "qwixx_room_list";
  }

  @GetMapping("/qwixx/room/{id}")
  @PreAuthorize("isAuthenticated()")
  public String getRoom(@PathVariable("id") Long id, Principal principal, Model model) {
    SiteUser siteUser = userService.getUser(principal.getName());
    Room room = roomSearchService.getRoom(id);
    GameInfoResponse response = qwixxService.getGameInfo(room, siteUser);
    if (response.isParticipant(siteUser.getUsername())) {
      model.addAttribute("gameResponse", response);
      return "qwixx";
    } else {
      return "redirect:/qwixx/room/list";
    }
  }

  @PostMapping("/qwixx/room")
  @PreAuthorize("isAuthenticated()")
  public String create(Principal principal) {
    Room newRoom = qwixxService.createNewRoom(principal.getName());
    return "redirect:/qwixx/room/" + newRoom.getId();
  }
}
