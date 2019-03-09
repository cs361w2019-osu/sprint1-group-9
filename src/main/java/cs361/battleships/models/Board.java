package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Ships.BasicShip;
import cs361.battleships.models.Ships.ShipBase;
import cs361.battleships.models.Ships.ShipUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

	@JsonProperty private List<ShipBase> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty private List<Result> pings;
	@JsonProperty private int pingsLeft;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
		pingsLeft = 2;
	}


	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(ShipBase ship, int x, char y, boolean isVertical) {

		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = ShipUtility.createShip(ship.getKind());
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


	private boolean move(ShipBase ship, MoveDirection direction) {

		switch (direction) {
			case UP:
				return moveVertically(ship,-1);
			case DOWN:
				return moveVertically(ship,-1);
			case RIGHT:
				return moveHorizontally(ship,1);
			case LEFT:
				return moveHorizontally(ship,-1);
		}
		return false;
	}

	private boolean isShip(Square pos) {
		return ships.stream().anyMatch(ship ->
				ship.getOccupiedSquares().stream().anyMatch( s ->
						s.equals(pos)));
	}

	private boolean moveVertically(ShipBase ship, int modifier) {
		var newRows = new ArrayList<Integer>();
		int row;

		// make the potential new rows
		for (Square pos : ship.getOccupiedSquares()) {
			row = pos.getRow() + modifier;
			if(row < 0 || row > Game.BOARD_SIZE) {
				return false;
			}
			newRows.add(row);
		}

		//move them over
		ship.getOccupiedSquares().forEach(s -> s.setRow(newRows.remove(0)));
		return true;
	}

	private boolean moveHorizontally(ShipBase ship, int modifier) {
		var newCols = new ArrayList<Integer>();
		int col;

		// make the potential new rows
		for (Square pos : ship.getOccupiedSquares()) {
			col = (int)pos.getColumn() + modifier;
			if(col < 65 || col > (Game.BOARD_SIZE + 64)) {
				return false;
			}
			if(isShip(pos)) {
				return false;
			}
			newCols.add(col);
		}

		//move them over
		ship.getOccupiedSquares().forEach(s -> s.setColumn( (char)(int)(newCols.remove(0))));
		return true;
	}

	public boolean moveShip(String name, MoveDirection direction) {

		// find the ship
		var ship = ships.stream().filter(s -> s.getKind().equals(name)).findFirst().orElse(null);

		// test if moveable
		if(ship.isSunk()) {
			return false;
		}

		//move it
		return move(ship, direction);
	}


	private boolean isCQ(Square pos) {
	    return ships.stream().anyMatch(ship ->
                ship.getOccupiedSquares().stream().filter(s ->
                    s.getCQ()).anyMatch(cq -> cq.equals(pos)));
    }

	private Result attack(Square s) {
		if (attacks.stream().anyMatch(r -> r.getLocation().equals(s) && !isCQ(r.getLocation()))) {
			var attackResult = new Result(s);
			attackResult.setResult(AttackStatus.INVALID);
			return attackResult;
		}
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			return attackResult;
		}

		Result attackResult = null;

		for(ShipBase ship: shipsAtLocation) {
			if(ship.getKind().equals("SUBMARINE")) {
				var isSubmerged = true;
				if(isSubmerged && Game.getInstance().getIsLaserAvailable()) {
					attackResult = ship.attack(s.getRow(), s.getColumn());
				}
			}
			attackResult = ship.attack(s.getRow(), s.getColumn());
		}

		System.out.println("ATTACK: " + attackResult.getResult() + "\t" + "BOOL: " + Game.getInstance().getIsLaserAvailable());

		if (attackResult.getResult() == AttackStatus.SUNK) {
			Game.getInstance().setIsLaserAvailable(true);
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.setResult(AttackStatus.SURRENDER);
			}
		}
		System.out.println("ATTACK AFTER\t" + "BOOL: " + Game.getInstance().getIsLaserAvailable());


		return attackResult;
	}


	List<ShipBase> getShips() {
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
		if (temp1 >= 1 && temp1 <= Game.BOARD_SIZE){
			if (temp2 >= 'A' && temp2 <= (char)(64 + Game.BOARD_SIZE)){

				boolean rep = false;

				Square y = new Square(temp1, temp2);
				Result p = new Result(y);
				for (int i = 0; i < pings.size(); i++){
					if (p == pings.get(i)){
						rep = true;
						break;
					}
				}

				if (rep == false) {
				//	System.out.println("Coordinates within range.\n");
					Square pingPos = new Square(temp1, temp2);

					Result r = new Result(pingPos);

					if(isShip(pingPos)) {
						r.setResult(AttackStatus.HIT);
					} else {
						r.setResult(AttackStatus.MISS);
					}
					pings.add(r);
				}
			}
		}
	}

	/********************************************************************
	 * Function: getPing()
	 * Description:
	 * @param square
	 * @return void
	 ********************************************************************/

	public Boolean getPingedList(Square square) {
		System.out.println(pingsLeft);

		if (pingsLeft == 0){
			System.out.println("YES");
			return false;
		}

		int x = square.getRow();
		char y = square.getColumn();
		pings = new ArrayList<Result>();

		Square s = new Square(x, y);
		Result r = new Result(s);
		checkAndAppend(x, y);

		//Calculates the coordinates of the squares that should be returned using;
		int temp1;
		char temp2;

		//...in the +y-direction: //WORKS
		for (int i = 1; i < 3; i++) {
			temp1 = x - i;
			temp2 = y;
			checkAndAppend(temp1, temp2);
		}

		//...in the -y-direction: //WORKS
		for (int i = 1; i < 3; i++) {
			temp1 = x + i;
			temp2 = y;
			checkAndAppend(temp1, temp2);
		}

		//...in the +x-direction: //WORKS
		int a;
		temp2 = y;
		a = (int)temp2;

		for (int i = 1; i < 3; i++) {
			temp1 = x;
			temp2 = (char)(a + i);
			checkAndAppend(temp1, temp2);
		}

		//...in the -x-direction:
		int b;
		temp2 = y;
		b = (int)temp2;

		for (int i = 1; i < 3; i++) {
			temp1 = x;
			temp2 = (char)(b - i);
			checkAndAppend(temp1, temp2);
		}

		//NE/NW/SE/SW:

		int g = 0;
		temp2 = y;
		g = (int)temp2;

		temp1 = x+1;
		temp2 = (char)(g+1);
		checkAndAppend(temp1, temp2);

		int h = 0;
		temp2 = y;
		h = (int)temp2;

		temp1 = x-1;
		temp2 = (char)(h+1);
		checkAndAppend(temp1, temp2);


		int o = 0;
		temp2 = y;
		o = (int)temp2;

		temp1 = x+1;
		temp2 = (char)(o-1);
		checkAndAppend(temp1, temp2);

		int n = 0;
		temp2 = y;
		n = (int)temp2;

		temp1 = x-1;
		temp2 = (char)(n-1);
		checkAndAppend(temp1, temp2);


		pingsLeft--;
		return true;
	}
}
