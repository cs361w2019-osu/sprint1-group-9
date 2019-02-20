package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

	@JsonProperty private AttackStatus result;
	@JsonProperty private Square location;
	@JsonProperty private Ship ship;

	@SuppressWarnings("unused")
	public Result() {
	}

	public Result(Square location) {
		result = AttackStatus.MISS;
		this.location = location;
	}

	public AttackStatus getResult() {
		return result;
	}

	public void setResult(AttackStatus result) {
		this.result = result;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return location;
	}
}
