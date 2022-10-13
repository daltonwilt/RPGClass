package com.outcast.rpgclass.config.loot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperienceLootConfig {

    @JsonProperty("minimum")
    public Double MINIMUM = 100.0;

    @JsonProperty("maximum")
    public Double MAXIMUM = 200.0;

}
