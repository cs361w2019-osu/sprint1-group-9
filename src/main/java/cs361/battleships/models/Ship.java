package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship
{

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String kind;
	@JsonProperty private int size;

	public Ship()
	{
		this.occupiedSquares = new ArrayList<>();
		this.kind = new String();
		this.size=0;
	}

	public Ship(String kind)
	{
		this.kind = kind;
		this.size=0;
		if(kind.equals("MINESWEEPER"))
		{
			this.size=2;
		}
		else if(kind.equals("DESTROYER"))
		{
			this.size=3;
		}
		else if(kind.equals("BATTLESHIP"))
		{
			this.size=4;
		}
		this.occupiedSquares = new ArrayList<>();
	}

	public List<Square> getOccupiedSquares()
	{
		return this.occupiedSquares;
	}

	public String getKind()
	{
		return this.kind;
	}

	public void populateSquares(int x, char y, boolean vert)
	{
		for(int i = 0; i<this.size; i++)
		{
			this.occupiedSquares.add(new Square(x,y));
			if(vert) x++;
			else y++;
		}
	}

	public int getSize()
	{
		return this.size;
	}
}