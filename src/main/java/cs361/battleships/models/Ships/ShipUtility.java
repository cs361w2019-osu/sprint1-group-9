package cs361.battleships.models.Ships;

public class ShipUtility {

    public static ShipBase createShip(String kind) {
        if(kind == "Submarine") {
            return null;
        }
        return new BasicShip(kind);
    }

}
