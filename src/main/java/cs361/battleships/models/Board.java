package cs361.battleships.models;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class Board {


	private List<Result> attacks;
	private List<Ship> ships;


	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement

		// Create Lists
		attacks = new ArrayList<Result>();
		ships = new ArrayList<Ship>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement
		return false;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {

		System.out.println("Attacking...");
		// create square and result default
		var firePos = new Square(x, y);
		var result = new Result(firePos);
		result.setResult(AtackStatus.MISS);

		System.out.println("Checking if attack hit anything...");
        // search if there are any matching ship pos
		var shipOp = ships.stream().filter(ship ->
                ship.getOccupiedSquares().stream().anyMatch(sPos ->
                        sPos.equals(firePos))).findAny();

		// if ship present, mark hit and check if its a sink
		shipOp.ifPresent(ship -> {
			System.out.println("Hit!");
			result.setShip(ship);

		    //check if sink
			System.out.println("Checking if sink...");
		    if( ship.getOccupiedSquares().stream().allMatch(sPos ->
                    attacks.stream().anyMatch(att -> sPos.equals(att.getLocation())))) {
		    	System.out.println("Sunk!");
				result.setResult(AtackStatus.SUNK);
			}

		    else
		        result.setResult(AtackStatus.HIT);
        });

		System.out.println("Checking for endGame...");
		// check if game ends
        if( ships.stream().allMatch(ship ->
                ship.getOccupiedSquares().stream().allMatch(sPos ->
                        attacks.stream().anyMatch(att -> sPos.equals(att.getLocation())))) ) {
        	System.out.println("Game has ended!");
			result.setResult(AtackStatus.SURRENDER);
		}

		// return val
		attacks.add(result);
		return result;
	}

	public List<Ship> getShips() {
		//TODO implement
		return null;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
	}

	public List<Result> getAttacks() {
		return attacks;
	}

	public void setAttacks(List<Result> attacks) {
	    this.attacks = attacks;
	}
}