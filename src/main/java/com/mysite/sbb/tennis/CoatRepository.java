package com.mysite.sbb.tennis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoatRepository extends JpaRepository<Coat, Integer> {
    
}
