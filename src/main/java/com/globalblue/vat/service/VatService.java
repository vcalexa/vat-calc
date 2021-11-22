package com.globalblue.vat.service;

import com.globalblue.vat.model.Price;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VatService {

  private static final BigDecimal VAT10 = BigDecimal.valueOf(0.10);
  private static final BigDecimal VAT13 = BigDecimal.valueOf(0.13);
  private static final BigDecimal VAT20 = BigDecimal.valueOf(0.20);

  public ResponseEntity getFinalPrice(
      BigDecimal basePrice, BigDecimal vatRate, BigDecimal vatAmount, BigDecimal totalPrice) {

    if ((vatRate == null))
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vat Rate value is missing!");

    if (!VAT10.equals(vatRate.stripTrailingZeros())
        && !VAT13.equals(vatRate.stripTrailingZeros())
        && !VAT20.equals(vatRate.stripTrailingZeros())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Incorrect VAT! Please select vat values from 0.13, 0.10 or 0.20");
    }

      // Check if at least one request is param present and return bad request if so
    if ((basePrice == null) && (totalPrice == null) && (vatAmount == null))
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("You must enter at least one of base price, total price or vat amount!");

      // Check if there is more than one request param present and return bad request if so
    if ((basePrice != null && totalPrice != null)
        || (basePrice != null && vatAmount != null)
        || (totalPrice != null && vatAmount != null))
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("You must enter at most one of base price, total price or vat amount!");

    if (basePrice != null) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new Price().setPriceFromBasePrice(basePrice, vatRate));
    } else if (totalPrice != null) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new Price().setPriceFromTotalPrice(totalPrice, vatRate));
    } else if (vatAmount != null) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(new Price().setPriceFromVatAmount(vatAmount, vatRate));
    } else {
      return ResponseEntity.status(HttpStatus.OK).body(null);
    }
  }
}
