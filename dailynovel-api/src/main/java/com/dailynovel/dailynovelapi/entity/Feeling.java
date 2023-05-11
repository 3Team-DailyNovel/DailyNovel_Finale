package com.dailynovel.dailynovelapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Feeling {

    @Id
    private Integer Id;
    @Column(name="member_id")
    private Integer memberId;
    private String feeling;
    
}
