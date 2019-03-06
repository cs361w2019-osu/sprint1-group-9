package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Ships.ShipBase;

public class Result {

	@JsonProperty private AttackStatus result;
	@JsonProperty private Square location;
	@JsonProperty private ShipBase ship;

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

	public ShipBase getShip() {
		return ship;
	}

	public void setShip(ShipBase ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return location;
	}


}
