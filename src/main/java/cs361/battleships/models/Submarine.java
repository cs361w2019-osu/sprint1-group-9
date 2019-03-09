package cs361.battleships.models;

import cs361.battleships.models.Ships.BasicShip;
import cs361.battleships.models.Ships.ShipBase;
import cs361.battleships.models.Ships.ShipUtility;

public class Submarine extends BasicShip {

    private boolean submerged;

    public Submarine (boolean submerged){
        super ("SUBMARINE");
        this.submerged = submerged;
    }


    public boolean getSubmerged() { return submerged; }



}