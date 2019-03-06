package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveShipGameAction {

    @JsonProperty private int direction;
    @JsonProperty private String shipType;


    public int getDirection() {
        return direction;
    }
    public String getShipType() {
        return shipType;
    }

}
