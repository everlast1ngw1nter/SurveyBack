package com.example.demo.dto.dtoForParseBody;

import com.example.demo.dto.AccessData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessDataAndEmail {

    @JsonProperty("email")
    public String email;

    @JsonProperty("data")
    public AccessData accessData;

    public AccessDataAndEmail() {

    }
}
