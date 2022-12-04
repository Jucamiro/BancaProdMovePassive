package com.nttdata.BancaProdMovePassive.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@ToString
@EqualsAndHashCode(of = {"identityNumber"})
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "movePassive")
public class MovePassive {
    @Id
    private String idMovePassive;

    private String account;

    private Double amount;

    private String operationType;

    private LocalDate dateRegister;

}
