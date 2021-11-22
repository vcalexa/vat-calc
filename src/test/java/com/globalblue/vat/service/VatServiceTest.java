package com.globalblue.vat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.globalblue.vat.model.Price;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class VatServiceTest {
  VatService vatService = new VatService();

  private static final BigDecimal VAT10 = BigDecimal.valueOf(0.10);
  private static final BigDecimal VAT13 = BigDecimal.valueOf(0.13);
  private static final BigDecimal VAT20 = BigDecimal.valueOf(0.20);
  private static final BigDecimal INCORRECT_VAT = BigDecimal.valueOf(0.201);

  private static final BigDecimal AMOUNT1 = BigDecimal.valueOf(1375.0);
  private static final BigDecimal AMOUNT2 = BigDecimal.valueOf(125.0);
  private static final BigDecimal AMOUNT3 = BigDecimal.valueOf(1250.0);

  private static final Price PRICE1 = new Price(AMOUNT1, AMOUNT2, AMOUNT3);

  @Test
  public void nullVatRateShouldReturnStatus400() {
    var result = vatService.getFinalPrice(AMOUNT1, null, AMOUNT2, AMOUNT3);

    assertEquals(BAD_REQUEST, result.getStatusCode());
    assertEquals("Vat Rate value is missing!", result.getBody());
  }

  @Test
  public void tooManyInputValuesShouldReturnStatus400() {
    var result = vatService.getFinalPrice(AMOUNT1, VAT10, AMOUNT2, AMOUNT3);

    assertEquals(BAD_REQUEST, result.getStatusCode());
    assertEquals(
        "You must enter at most one of base price, total price or vat amount!", result.getBody());
  }

  @Test
  public void incorrectVatShouldReturnStatus400() {
    var result = vatService.getFinalPrice(AMOUNT1, INCORRECT_VAT, AMOUNT2, AMOUNT3);

    assertEquals(BAD_REQUEST, result.getStatusCode());
    assertEquals(
        "Incorrect VAT! Please select vat values from 0.13, 0.10 or 0.20", result.getBody());
  }

  @Test
  public void validBasePriceShouldReturnCorrectPrice() {
    var result = vatService.getFinalPrice(AMOUNT3, VAT10, null, null);

    assertEquals(OK, result.getStatusCode());
    assertTrue(PRICE1.equals(result.getBody()));
  }

  @Test
  public void validTotalPriceShouldReturnCorrectPrice() {
    var result = vatService.getFinalPrice(null, VAT10, null, AMOUNT1);

    assertEquals(OK, result.getStatusCode());
    assertTrue(PRICE1.equals(result.getBody()));
  }

  @Test
  public void validVatAmountShouldReturnCorrectPrice() {
    var result = vatService.getFinalPrice(null, VAT10, AMOUNT2, null);

    assertEquals(OK, result.getStatusCode());
    assertTrue(PRICE1.equals(result.getBody()));
  }
}
