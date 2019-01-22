package cs361.battleships.models;

public class Result {

	private AtackStatus result;
	private Square location;

	public AtackStatus getResult() {
		return result;
	}

	public void setResult(AtackStatus result) {
		this.result = result;
	}

	public Ship getShip() {
		//TODO implement
		return null;
	}

	public void setShip(Ship ship) {
		//TODO implement
	}

	public Square getLocation() {
		return location;
	}

	public void setLocation(Square square) {
		location = square;
	}
}
