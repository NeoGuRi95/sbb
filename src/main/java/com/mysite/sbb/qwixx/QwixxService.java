package com.mysite.sbb.qwixx;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.qwixx.dto.GameInfoResponse;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@Service
@RequiredArgsConstructor
public class QwixxService {

  private final RoomSiteUserRepository roomSiteUserRepository;
  private final RoomRepository roomRepository;
  private final UserService userService;

  public List<Room> getRoomList() {
    return roomRepository.findAll();
  }

  public GameInfoResponse createGame(String username) {
    SiteUser roomCreator = userService.getUser(username);
    Room newRoom = Room.builder()
        .name(username + LocalDateTime.now())
        .build();
    RoomSiteUser firstRoomSiteUser = RoomSiteUser.builder()
        .siteUser(roomCreator)
        .room(newRoom)
        .build();
    firstRoomSiteUser.init();
    roomRepository.save(newRoom);
    roomSiteUserRepository.save(firstRoomSiteUser);

    GameInfoResponse response = new GameInfoResponse();
    response.setRoomId(newRoom.getId());
    response.addParticipant(firstRoomSiteUser);
    return response;
  }

  public Room getRoom(Long roomId) {
    return roomRepository.findById(roomId)
        .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
  }

  public GameInfoResponse getGame(Room room, SiteUser siteUser) {
    GameInfoResponse response = new GameInfoResponse();
    response.setRoomId(room.getId());
    Optional<RoomSiteUser> optRoomSiteUser = roomSiteUserRepository.findBySiteUserAndRoom(room, siteUser);
    if (optRoomSiteUser.isPresent()) {
      // 이미 해당 게임 방에 입장했던 경우
      List<RoomSiteUser> roomSiteUsers = roomSiteUserRepository.findAllByRoom(room);
      response.setParticipants(roomSiteUsers);
    } else {
      // 처음으로 해당 게임 방에 입장한 경우
      List<RoomSiteUser> roomSiteUsers = roomSiteUserRepository.findAllByRoom(room);
      if (roomSiteUsers.size() == 5) {
        response.setFullRoom(true);
      } else {
        RoomSiteUser joinRoomSiteUser = RoomSiteUser.builder()
            .siteUser(siteUser)
            .room(room)
            .build();
        roomSiteUserRepository.save(joinRoomSiteUser);
        roomSiteUsers.add(joinRoomSiteUser);
        response.setParticipants(roomSiteUsers);
      }
    }
    return response;
  }

  public void ready(Room room, SiteUser siteUser) {
    RoomSiteUser roomSiteUser = roomSiteUserRepository.findBySiteUserAndRoom(room, siteUser)
        .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
    roomSiteUser.setReady(true);
    roomSiteUserRepository.save(roomSiteUser);
  }

  public Boolean isAllReady(Room room) {
    List<RoomSiteUser> roomSiteUsers = roomSiteUserRepository.findAllByRoom(room);
    for (RoomSiteUser roomSiteUser : roomSiteUsers) {
      if (Boolean.FALSE.equals(roomSiteUser.getReady()))
        return false;
    }
    return true;
  }

  public Map<String, Integer> roll() {
    Map<String, Integer> rollInfo = new HashMap<>();
    rollInfo.put("white1", 0);
    rollInfo.put("white2", 0);
    rollInfo.put("red", 0);
    rollInfo.put("yellow", 0);
    rollInfo.put("green", 0);
    rollInfo.put("blue", 0);

    rollInfo.put("whiteSum", 0);
    rollInfo.put("redSum1", 0);
    rollInfo.put("redSum2", 0);
    rollInfo.put("yellowSum1", 0);
    rollInfo.put("yellowSum2", 0);
    rollInfo.put("greenSum1", 0);
    rollInfo.put("greenSum2", 0);
    rollInfo.put("blueSum1", 0);
    rollInfo.put("blueSum2", 0);
    return rollInfo;
  }
}
