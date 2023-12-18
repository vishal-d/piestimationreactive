package com.piestimation.reactive.rest.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.piestimation.reactive.dto.PiEstimationRequest;
import com.piestimation.reactive.service.Pi;

import lombok.extern.slf4j.Slf4j;

@ComponentScan(basePackages = "com.piestimation.reactive.application")
@SpringBootTest(classes = {PiEstimation.class, Pi.class})
@AutoConfigureMockMvc
@Slf4j
public class PiEstimationTests {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void estimatePiWithSmallDataSet() {
    PiEstimationRequest request =
        PiEstimationRequest.builder().radius(50.5).totalPoints(100).build();

    webTestClient.post().uri("/piestimation/monte-carlo").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request).exchange().expectStatus().isOk().expectBodyList(Double.class)
        .consumeWith(response -> {
          log.info("Estimated value of Pi: {}", response.getResponseBody());
        });
  }

  @Test
  void estimatePiWithLargeDataSet() {
    PiEstimationRequest request =
        PiEstimationRequest.builder().radius(100.5).totalPoints(100000).build();

    webTestClient.post().uri("/piestimation/monte-carlo").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request).exchange().expectStatus().isOk().expectBodyList(Double.class)
        .consumeWith(response -> {
          log.info("Estimated value of Pi: {}", response.getResponseBody());
        });
  }

  @Test
  void estimatePiWithInvalidRadius() {
    PiEstimationRequest request =
        PiEstimationRequest.builder().radius(-1.0).totalPoints(10000).build();

    webTestClient.post().uri("/piestimation/monte-carlo").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request).exchange().expectStatus().is4xxClientError();
  }

  @Test
  void estimatePiWithInvalidTotalPoints() {
    PiEstimationRequest request =
        PiEstimationRequest.builder().radius(100.0).totalPoints(-100).build();

    webTestClient.post().uri("/piestimation/monte-carlo").contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request).exchange().expectStatus().is4xxClientError();
  }

}


