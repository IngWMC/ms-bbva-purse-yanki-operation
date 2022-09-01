package com.nttdata.bbva.purseyankioperation.services;

import java.math.BigDecimal;

@FunctionalInterface
public interface IOperation {
    BigDecimal calculate(BigDecimal val1, BigDecimal val2);
}
