package com.nttdata.BancaProdMovePassive.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovePassiveModel {
    @JsonIgnore
    private String idMovePassive;

    private String account;

    private Double amount;

    private String operationType;

    private LocalDate dateRegister;
}
