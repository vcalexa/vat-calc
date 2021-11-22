package com.globalblue.vat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.globalblue.vat.model.Price;
import com.globalblue.vat.service.VatService;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VatController.class)
class VatControllerTest {

  @InjectMocks private VatController vatController;
  @MockBean private VatService vatService;

  @Autowired private MockMvc mockMvc;

  private static final BigDecimal AMOUNT1 = BigDecimal.valueOf(1375.00);
  private static final BigDecimal AMOUNT2 = BigDecimal.valueOf(125.00);
  private static final BigDecimal AMOUNT3 = BigDecimal.valueOf(1250.00);
  private static final BigDecimal VAT10 = BigDecimal.valueOf(0.10);

  private static final Price PRICE1 = new Price(AMOUNT1, AMOUNT2, AMOUNT3);

  @Test
  public void getPriceFromBasePriceExpect200() throws Exception {

    when(vatService.getFinalPrice(eq(AMOUNT3), eq(VAT10), any(), any()))
        .thenReturn(ResponseEntity.status(OK).body(PRICE1));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/vat")
                .param("basePrice", String.valueOf(AMOUNT3))
                .param("vatRate", String.valueOf(VAT10))
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.vatAmount", Matchers.is(125.00)))
        .andExpect(jsonPath("$.basePrice", Matchers.is(1250.00)))
        .andExpect(jsonPath("$.totalPrice", Matchers.is(1375.00)));
  }

  @Test
  public void nonNumericVatBadRequest() throws Exception {

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/vat")
                .param("basePrice", String.valueOf(AMOUNT3))
                .param("vatRate", "non-numeric")
                .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
