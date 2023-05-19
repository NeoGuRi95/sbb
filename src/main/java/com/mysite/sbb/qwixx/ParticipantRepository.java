package com.mysite.sbb.qwixx;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.user.SiteUser;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

  List<Participant> findAllByRoom(Room room);

  Optional<Participant> findBySiteUserAndRoom(SiteUser siteUser, Room room);

  void deleteBySiteUserAndRoom(SiteUser siteUser, Room room);
}
