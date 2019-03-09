package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.MoveDirection;

public class MoveShipGameAction {

    @JsonProperty private MoveDirection direction;
    @JsonProperty private String shipType;


    public MoveDirection getDirection() {
        return direction;
    }
    public String getShipType() {
        return shipType;
    }

}
