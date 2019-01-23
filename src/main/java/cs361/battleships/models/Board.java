package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board
{
	private List<Ship> ships;
	private List<Result> attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board()
	{
		this.ships = new ArrayList<>();
		this.attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical)
	{
		System.out.println("Attempting to place ship...");
		int shipSize = ship.getSize();

		System.out.println("Validating ship type " + ship.getKind() + "...");
		if (this.getShips().size() > 0)
		{
			if (!validShipType(ship)) return false;
		}

		System.out.println("Validating ship location " + x + y + "...");
		if (validLocation(shipSize, x, y, isVertical))
		{
			System.out.println("Success! Adding ship!");
			ship.populateSquares(x, y, isVertical);
			this.ships.add(ship);
			return true;
		}
		return false;
	}

	public boolean checkAttackValid(Result result){
		Square temp = result.getLocation();
		boolean check = attacks.stream().anyMatch(A->A.getLocation().equals(result.getLocation()));
		System.out.println(check);
		if (temp.getColumn() >= 'A' && temp.getColumn() <= 'J' ){
			if (temp.getRow() >= 1 && temp.getRow() <= 10 ){
				if (!check) return true;
			}
		}
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

		if (!checkAttackValid(result)){
			result.setResult(AtackStatus.INVALID);
			return result;
		}

		result.setResult(AtackStatus.MISS);
		attacks.add(result);

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
		System.out.println("Returning attack result " + result.getResult());
		return result;
	}

	public List<Ship> getShips()
	{
		return this.ships;
	}

	public void setShips(List<Ship> ships)
	{
		this.ships = ships;
	}

	public List<Result> getAttacks()
	{
		return attacks;
	}

	public void setAttacks(List<Result> attacks)
	{
		this.attacks = attacks;
	}

	/*
	This function takes proposed ship coords and returns true or false based on whether the proposed location is valid
	 */
	private boolean validLocation(int size, int x, char y, boolean vertical)
	{
		List<Square> allProposedSquares = new ArrayList<>();
		if (vertical)
		{
			if (x + size - 1 > 10 || (int) y > 74)
			{
				return false;
			}
			for (int i = 0; i < size; i++)
			{
				allProposedSquares.add(new Square(x + i, y));
			}
		}
		else
		{
			if ((int) y + size - 1 > 74 || x > 10)
			{
				return false;
			}
			for (int i = 0; i < size; i++)
			{
				allProposedSquares.add(new Square(x, (char) ((int) y + 1)));
			}
		}
		//if max range is outside grid, return false;
		//now checking if new ship would overlap with existing ships
		if (this.getShips().size() > 0)
		{
			for (Ship ships : this.getShips())
			{
				for (Square sq : ships.getOccupiedSquares())
				{
					for (int i = 0; i < allProposedSquares.size(); i++)
					{
						if (isSquareConflict(allProposedSquares.get(i), sq))
						{
							return false;
						}
					}
				}
			}
		}
		//otherwise we make it through the tests and return true
		return true;
	}

	private boolean validShipType(Ship ship)
	{
		for (Ship ships : this.getShips())
		{
			if (ship.getKind().equals(ships.getKind())) return false;
		}
		return true;
	}

	private boolean isSquareConflict(Square sq1, Square sq2)
	{
		if (sq1.getRow() == sq2.getRow() && sq1.getColumn() == sq2.getColumn())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}