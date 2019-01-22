package cs361.battleships.models;


import java.io.Console;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Board {

	private List<Result> attacks;
	private List<Ship> ships;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// Create Lists
		attacks = new ArrayList<Result>();
		ships = new ArrayList<Ship>();
  }

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		int shipSize = ship.getSize();
		if(this.getShips().size() > 0)
		{
			if (!validShipType(ship))
				return false;
	 	}

		if(validLocation(shipSize, x, y, isVertical))
		{
			ship.populatedSquares(x,y,isVertical);
			this.ships.add(ship);
			return true;
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

	public List<Result> getAttacks() {
		return attacks;
	}
  
  public void setAttacks(List<Result> attacks) {
    this.attacks = attacks;
  }
  
	private boolean validShipType(Ship ship)
	{
		for(Ship ships : this.getShips())
		{
			if(ship.getKind().equals(ships.getKind()))
				return false;
		}
		return true;
	}

	private boolean isSquareConflict(Square sq1, Square sq2)
	{
		if(sq1.getRow() == sq2.getRow() && sq1.getColumn() == sq2.getColumn())
			return true;
		else
			return false;
	}

	private boolean validLocation(int size, int x, char y, boolean vertical)
	{
		List<Square> allProposedSquares = new ArrayList<>();
		if(vertical)
		{
			if(x + size - 1 > 10 || (int)y > 74)
				return false;
			for(int i=0; i < size; ++i)
				allProposedSquares.add(new Square(x+i, y));
		}
		else
		{
			if((int) y + size - 1 > 74 || x > 10)
				return false;
			for(int i=0; i< size; ++i)
				allProposedSquares.add(new Square(x, (char) ((int) y + 1)));
		}

		if(this.getShips().size() > 0)
		{
			for(Ship ships : this.getShips())
			{
				for(Square sq : ships.getOccupiedSquares())
				{
					for(int i=0; i<allProposedSquares.size(); ++i)
					{
						if(isSquareConflict(allProposedSquares.get(i), sq))
							return false;
					}
				}
			}
		}
		return true;
	}
}