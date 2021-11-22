package com.globalblue.vat.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.globalblue.vat.service.VatService;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vat")
public class VatController {

  private final VatService vatService;

  public VatController(VatService vatService) {
    this.vatService = vatService;
  }

  @GetMapping()
  public ResponseEntity getFinalPrice(
      @RequestParam(required = false) BigDecimal vatRate,
      @RequestParam(required = false) BigDecimal basePrice,
      @RequestParam(required = false) BigDecimal totalPrice,
      @RequestParam(required = false) BigDecimal vatAmount) {
    try {
      return vatService.getFinalPrice(basePrice, vatRate, vatAmount, totalPrice);
    } catch (NumberFormatException e) {
      return ResponseEntity.status(BAD_REQUEST).body("Vat value is not numeric");
    }
  }
}
