package com.mysite.sbb.qwixx;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.qwixx.dto.GameCreateResponse;
import com.mysite.sbb.qwixx.dto.GameResponse;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@Service
@RequiredArgsConstructor
public class QwixxService {

  private final RoomSiteUserRepository roomSiteUserRepository;
  private final RoomRepository roomRepository;
  private final UserService userService;

  public GameCreateResponse createGame(String username) {
    SiteUser roomCreator = userService.getUser(username);
    Room newRoom = Room.builder()
        .status(true)
        .participantsNumber(1)
        .build();
    RoomSiteUser firstRoomSiteUser = RoomSiteUser.builder()
        .siteUser(roomCreator)
        .room(newRoom)
        .build();
    firstRoomSiteUser.init();
    roomRepository.save(newRoom);
    roomSiteUserRepository.save(firstRoomSiteUser);

    GameCreateResponse dto = new GameCreateResponse();
    dto.setRoomId(newRoom.getId());
    dto.setParticipants(firstRoomSiteUser);
    return dto;
  }

  public GameResponse getGame(Room room, SiteUser siteUser) {
    Optional<RoomSiteUser> optRoomSiteUser = roomSiteUserRepository.findBySiteUserAndRoom(room, siteUser);
    GameResponse dto = new GameResponse();
    dto.setRoomId(room.getId());
    if (optRoomSiteUser.isPresent()) {
      // 이미 해당 게임 방에 입장했던 경우
      List<RoomSiteUser> roomSiteUsers = roomSiteUserRepository.findAllByRoom(room);
      dto.setParticipants(roomSiteUsers);
    } else {
      // 처음으로 해당 게임 방에 입장한 경우
      RoomSiteUser joinRoomSiteUser = RoomSiteUser.builder()
          .siteUser(siteUser)
          .room(room)
          .build();
      dto.addRoomSiteUser(joinRoomSiteUser);
    }
    return dto;
  }

  public Room getRoom(Long roomId) {
    return roomRepository.findById(roomId)
        .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
  }
}
