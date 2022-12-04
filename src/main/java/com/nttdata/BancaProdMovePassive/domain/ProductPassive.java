package com.nttdata.BancaProdMovePassive.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ProductPassive {

    private String idProductPassive;
    private String account;
    private String identityNumber;
    private String typeAccount;
    private LocalDate dateRegister;
    private Double availableAmount;

}
