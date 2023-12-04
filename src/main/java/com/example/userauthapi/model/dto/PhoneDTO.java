package com.example.userauthapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDTO {

  private long number;

  @JsonProperty(value = "citycode")
  private int cityCode;

  @JsonProperty(value = "countrycode")
  private String countryCode;
}
