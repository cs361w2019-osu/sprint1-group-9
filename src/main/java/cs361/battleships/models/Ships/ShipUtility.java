package cs361.battleships.models.Ships;

public class ShipUtility {

    public static ShipBase createShip(String kind) {
        if(kind.equals("SUBMARINE")) {
            System.out.println("Hit it!");
            return new Submarine();
        }
        return new BasicShip(kind);
    }

}
