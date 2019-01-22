package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {

	@JsonProperty private ArrayList<Ship> ships;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board()
	{
		this.ships = new ArrayList<>();
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
		//TODO Implement
		return null;
	}

	public List<Ship> getShips()
	{
		return this.ships;
	}

	public void setShips(ArrayList<Ship> ships)
	{
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
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
