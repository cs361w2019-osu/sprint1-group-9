package cs361.battleships.models;

public class Result {

	private Ship ship;
	private Square square;
	private AtackStatus result;


	public Result(Square square) {
		this.square = square;
	}

	public AtackStatus getResult() {
		return result;
	}

	public void setResult(AtackStatus result) {
		this.result = result;
	}

	public Ship getShip() {
		return this.ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return this.square;
	}

	public void setLocation(Square square) {
		this.square = square;
	}
}
