package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private List<Ship> ships;
	private List<Result> attacks;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {

		this.attacks = new ArrayList<>();
		this.ships = new ArrayList<>();

	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		this.ships.add(ship);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		return this.ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		return this.attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}
}
