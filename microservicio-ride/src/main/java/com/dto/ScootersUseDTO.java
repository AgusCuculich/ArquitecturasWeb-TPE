package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScootersUseDTO {
    private Long idUser;
    private long ridesCount;
    private List<Long> otherUsers;
}
