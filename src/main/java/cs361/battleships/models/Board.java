package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty public List<Result> pings;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		if (ships.size() >= 3) {
			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());
		placedShip.place(y, x, isVertical);
		if (ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
			return false;
		}
		if (placedShip.getOccupiedSquares().stream().anyMatch(s -> s.isOutOfBounds())) {
			return false;
		}
		ships.add(placedShip);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result attackResult = attack(new Square(x, y));
		attacks.add(attackResult);
		return attackResult;
	}

	private Result attack(Square s) {
		if (attacks.stream().anyMatch(r -> r.getLocation().equals(s))) {
			var attackResult = new Result(s);
			attackResult.setResult(AttackStatus.INVALID);
			return attackResult;
		}
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			return attackResult;
		}
		var hitShip = shipsAtLocation.get(0);
		var attackResult = hitShip.attack(s.getRow(), s.getColumn());
		if (attackResult.getResult() == AttackStatus.SUNK) {
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.setResult(AttackStatus.SURRENDER);
			}
		}
		return attackResult;
	}


	List<Ship> getShips() {
		return ships;
	}



	/********************************************************************
	 * Function: getValidity()
	 * Description: Gets individual coordinates and tests their ranges.
	 * @param temp1
	 * @param temp2
	 * @return boolean true if in range; false if not.
	 ********************************************************************/

	public void checkAndAppend(int temp1, char temp2) {
		if (temp1 >= 1 && temp1 <= 10){
			if (temp2 >= 'A' && temp2 <= 'J'){
				System.out.println("Coordinates within range.\n");
				Square s = new Square(temp1, temp2);
				Result r = new Result(s);
				pings.add(r);
			}
		}
	}

	/********************************************************************
	 * Function: getPing()
	 * Description:
	 * @param square
	 * @return void
	 ********************************************************************/

	public List<Result> getPingedList(Square square) {
		int x = square.getRow();
		char y = square.getColumn();

		//Calculates the coordinates of the squares that should be returned using;
		int temp1;
		char temp2;

		//...in the +y-direction:
		for (int i = 1; i < 3; i++) {
			temp1 = x - i;
			temp2 = y;
			checkAndAppend(temp1, temp2);
		}

		//...in the -y-direction:
		for (int i = 1; i < 3; i++) {
			temp1 = x + i;
			temp2 = y;
			checkAndAppend(temp1, temp2);
		}

		//...in the +x-direction:
		for (int i = 1; i < 3; i++) {
			temp1 = x;
			temp2 = y += i;
			checkAndAppend(temp1, temp2);
		}

		//...in the -x-direction:
		for (int i = 1; i < 3; i++) {
			temp1 = x;
			temp2 = y -= i;
			checkAndAppend(temp1, temp2);
		}

		//NE/NW/SE/SW:
		temp1 = x+1;
		temp2 = y++;
		checkAndAppend(temp1, temp2);

		temp1 = x+1;
		temp2 = y--;
		checkAndAppend(temp1, temp2);

		temp1 = x-1;
		temp2 = y++;
		checkAndAppend(temp1, temp2);

		temp1 = x-1;
		temp2 = y--;
		checkAndAppend(temp1, temp2);

		return pings;
	}
}
