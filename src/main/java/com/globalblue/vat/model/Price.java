package com.globalblue.vat.model;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
  private BigDecimal totalPrice;
  private BigDecimal vatAmount;
  private BigDecimal basePrice;

  public Price() {}

  public Price(BigDecimal totalPrice, BigDecimal vatAmount, BigDecimal basePrice) {
    this.totalPrice = totalPrice;
    this.vatAmount = vatAmount;
    this.basePrice = basePrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public void setVatAmount(BigDecimal vatAmount) {
    this.vatAmount = vatAmount;
  }

  public void setBasePrice(BigDecimal basePrice) {
    this.basePrice = basePrice;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public BigDecimal getVatAmount() {
    return vatAmount;
  }

  public BigDecimal getBasePrice() {
    return basePrice;
  }

  public Price setPriceFromTotalPrice(BigDecimal totalPrice, BigDecimal vatRate) {
    this.totalPrice = totalPrice.setScale(2, HALF_UP);
    this.basePrice = totalPrice.divide(BigDecimal.ONE.add(vatRate), 2, HALF_UP);
    this.vatAmount = totalPrice.subtract(this.basePrice);
    return this;
  }

  public Price setPriceFromVatAmount(BigDecimal vatAmount, BigDecimal vatRate) {
    this.vatAmount = vatAmount.setScale(2, HALF_UP);
    this.basePrice = vatAmount.divide(vatRate, 2, HALF_UP);
    this.totalPrice = basePrice.add(this.vatAmount);
    return this;
  }

  public Price setPriceFromBasePrice(BigDecimal basePrice, BigDecimal vatRate) {
    this.basePrice = basePrice.setScale(2, HALF_UP);
    this.totalPrice = basePrice.multiply(BigDecimal.ONE.add(vatRate)).setScale(2, HALF_UP);
    this.vatAmount = totalPrice.subtract(this.basePrice);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Price price = (Price) o;
    return totalPrice.stripTrailingZeros().equals(price.totalPrice.stripTrailingZeros())
        && vatAmount.stripTrailingZeros().equals(price.vatAmount.stripTrailingZeros())
        && basePrice.stripTrailingZeros().equals(price.basePrice.stripTrailingZeros());
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalPrice, vatAmount, basePrice);
  }
}
