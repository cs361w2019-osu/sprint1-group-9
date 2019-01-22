package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	private List<Square> occupiedSquares;
	@JsonProperty private String kind;
	@JsonProperty private int size;

	/*public Ship()
	{
		System.out.println("###public ship");
		this.occupiedSquares = new ArrayList<Square>();
		this.kind = new String();
		this.size = 0;
	}
	*/
	public Ship(String kind)
	{
		occupiedSquares = new ArrayList<Square>();
		System.out.println("###Define ship");
		this.kind = kind;
		this.size = 0;
		if(kind.equals("MINESWEEPER"))
		{
			this.size = 2;
		}
		else if(kind.equals("DESTROYER"))
		{
			this.size = 3;
		}
		else //BATTLESHIP
		{
			this.size = 4;
		}
	}

	public List<Square> getOccupiedSquares()
	{
		return this.occupiedSquares;
	}

	public String getKind()		//Getting for Kind
	{
		System.out.println("###kind of ship");
		return this.kind;
	}

	public void populatedSquares(int x, char y, boolean vert)
	{
		System.out.println("###Populate squares");
		for(int i=0; i < this.size; ++i)
		{
			System.out.println("###Start Loop");
			this.occupiedSquares.add(new Square(x,y));
			System.out.println("###Loop after this.occ");
			if(vert) x++;
			else y++;
			System.out.println("###End Loop");
		}
		System.out.println("###Populate squares DONE");
	}

	public int getSize()
	{
		System.out.println("###get size ship");
		return this.size;
	}
}
