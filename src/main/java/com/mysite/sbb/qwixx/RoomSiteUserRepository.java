package com.mysite.sbb.qwixx;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.user.SiteUser;

public interface RoomSiteUserRepository extends JpaRepository<RoomSiteUser, Long> {

  List<RoomSiteUser> findAllByRoom(Room room);

  Optional<RoomSiteUser> findBySiteUserAndRoom(Room room, SiteUser siteUser);
}
