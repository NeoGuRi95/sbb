package com.mysite.sbb.tennis;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coat {
    @Id
    Integer id;

    String name;

    Integer coatNumber;

    LocalDate date;

    String reservation;

    String link;
}
