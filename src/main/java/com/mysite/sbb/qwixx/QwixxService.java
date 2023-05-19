package com.mysite.sbb.qwixx;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.qwixx.dto.GameInfoResponse;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

@Service
@RequiredArgsConstructor
public class QwixxService {

  private final ParticipantRepository participantRepository;
  private final RoomRepository roomRepository;
  private final UserService userService;
  private final QwixxUtil qwixxUtil;

  /*신규 게임 방을 생성하는 메서드
  * 신규 게임 방을 만든 참가자는 Master 권한이 부여됨.*/
  public Room createNewRoom(String username) {
    SiteUser roomCreator = userService.getUser(username);
    Room newRoom = Room.builder()
        .name(qwixxUtil.getRandomRoomName())
        .isLive(true)
        .build();
    Participant firstParticipant = Participant.builder()
        .isMaster(true)
        .siteUser(roomCreator)
        .room(newRoom)
        .build();
    firstParticipant.init();
    roomRepository.save(newRoom);
    participantRepository.save(firstParticipant);
    return newRoom;
  }

  /*게임 방의 정보를 가져오기 전에 인수의 SiteUser가 해당 게임 방의 기존 참가자인지 먼저 확인함.
  * 이후 새로운 참가자라면 기존 게임 방의 참가자수를 고려해 신규 참여 여부를 결정*/
  public GameInfoResponse getGameInfo(Room room, SiteUser siteUser) {
    GameInfoResponse response = new GameInfoResponse();
    response.setRoomId(room.getId());
    // 참가자 추가 여부 결정 로직
    Optional<Participant> optParticipant = participantRepository.findBySiteUserAndRoom(siteUser, room);
    if (optParticipant.isPresent()) {
      // 이미 해당 게임 방의 참가자인 경우
      List<Participant> participants = participantRepository.findAllByRoom(room);
      response.setParticipants(participants);
    } else {
      // 처음으로 해당 게임 방에 입장한 경우
      List<Participant> participants = participantRepository.findAllByRoom(room);
      // 기존 게임 방의 참가자 수를 확인
      if (participants.size() < 4) {
        // 풀방이 아니면 새로운 참가자 생성
        Participant newJoinParticipant = Participant.builder()
          .isMaster(false)
          .siteUser(siteUser)
          .room(room)
          .build();
        newJoinParticipant.init();
        participantRepository.save(newJoinParticipant);
        participants.add(newJoinParticipant);
      }
      response.setParticipants(participants);
    }
    return response;
  }

  /*해당 게임 방의 참가자 정보를 담아서 반환하는 메서드*/
  public GameInfoResponse getGameInfo(Room room) {
    GameInfoResponse response = new GameInfoResponse();
    response.setRoomId(room.getId());
    List<Participant> participants = participantRepository.findAllByRoom(room);
    response.setParticipants(participants);
    return response;
  }

  /*해당 게임 방의 참가자를 ready 상태로 변경하는 메서드*/
  public void setReady(Room room, SiteUser siteUser) {
    Participant participant = participantRepository.findBySiteUserAndRoom(siteUser, room)
        .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
    participant.setReady(true);
    participantRepository.save(participant);
  }

  /*해당 게임 방의 참가자를 삭제하는 메서드
  * 삭제 후 해당 게임 방에 참가자가 없을 경우 해당 게임 방을 비활성 상태로 변경*/
  @Transactional
  public void deleteParticipant(Room room, SiteUser siteUser) {
    participantRepository.deleteBySiteUserAndRoom(siteUser, room);
    if (participantRepository.findAllByRoom(room).isEmpty()) {
      room.deactive();
      roomRepository.save(room);
    }
  }

  public void turnEnd(Room room, SiteUser siteUser, List<String> clickList) {
    Participant participant = participantRepository.findBySiteUserAndRoom(siteUser, room)
        .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
    participant.setReady(true);
    for (String click : clickList) {
      String line = click.substring(0, click.length() - 1);
      Integer number = Integer.parseInt(click.substring(click.length() - 1));
      if (line.equals("red")) {
        List<Integer> redLine = participant.getRedLine();
        redLine.set(number - 2, 1);
        participant.setRedLine(redLine);
      } else if (line.equals("yellow")) {
        List<Integer> yellowLine = participant.getYellowLine();
        yellowLine.set(number - 2, 1);
        participant.setYellowLine(yellowLine);
      } else if (line.equals("green")) {
        List<Integer> greenLine = participant.getGreenLine();
        greenLine.set(-1 * (number - 12), 1);
        participant.setGreenLine(greenLine);
      } else if (line.equals("blue")) {
        List<Integer> blueLine = participant.getBlueLine();
        blueLine.set(-1 * (number - 12), 1);
        participant.setBlueLine(blueLine);
      }
    }
    participantRepository.save(participant);
  }

  /*해당 게임 방의 모든 참가자들의 준비 상태를 대기중(false)으로 변경*/
  public void initializeRoomReady(Room room) {
    List<Participant> participants = participantRepository.findAllByRoom(room);
    for (Participant participant : participants) {
      participant.setReady(false);
    }
    participantRepository.saveAll(participants);
  }
}
