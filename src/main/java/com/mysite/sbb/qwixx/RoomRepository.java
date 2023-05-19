package com.mysite.sbb.qwixx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
  List<Room> findByIsLive(Boolean isLive);
}
