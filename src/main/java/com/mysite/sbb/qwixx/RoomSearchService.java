package com.mysite.sbb.qwixx;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomSearchService {

  private final RoomRepository roomRepository;

  public Room getRoom(Long roomId) {
    return roomRepository.findById(roomId)
      .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
  }

  public List<Room> getLiveRoomList() {
    return roomRepository.findByIsLive(true);
  }

}
