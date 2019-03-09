package cs361.battleships.models.Ships;

import cs361.battleships.models.Submarine;

public class ShipUtility {

    public static ShipBase createShip(String kind) {
        if(kind == "Submarine") {
            return new Submarine();
        }
        return new BasicShip(kind);
    }

}
