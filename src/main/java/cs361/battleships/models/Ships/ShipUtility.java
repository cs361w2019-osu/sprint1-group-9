package cs361.battleships.models.Ships;

import cs361.battleships.models.Submarine;

public class ShipUtility {

    public static final int MOVE_UP = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_RIGHT = 4;

    public static ShipBase createShip(String kind) {
        if(kind == "Submarine") {
            return new Submarine();
        }
        return new BasicShip(kind);
    }

}
