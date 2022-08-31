package com.nttdata.bbva.purseyankioperation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventOperation {
    private String cellPhoneNumber; // Número de celular - Origen
    private String openAccountId; // Número de la cuenta - Origen
    private String cellPhoneNumberTransfer; // Número de celular - Destino
    private String openAccountIdTransfer; // Número de la cuenta - Destino
    private String operationType; // Transferencia: T
    private BigDecimal amount;
}
