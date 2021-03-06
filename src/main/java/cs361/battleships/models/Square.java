package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Square {

	@JsonProperty private int row;
	@JsonProperty private char column;
	@JsonProperty private boolean hit = false;
    @JsonProperty private boolean cap = false;

	public Square() {
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

	public boolean getCQ(){ return cap;}
    public void setCQ(boolean s) { cap = s; }

	public char getColumn() {
		return column;
	}
	public int getRow() {
		return row;
	}

	public void setColumn(char column) {this.column = column;}
	public void setRow(int row) {this.row = row;}


	@Override
	public boolean equals(Object other) {
		if (other instanceof Square) {
			return ((Square) other).row == this.row && ((Square) other).column == this.column;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 31 * row + column;
	}

	@JsonIgnore
	public boolean isOutOfBounds() {
		return row > Game.BOARD_SIZE || row < 1 || column > (char)(64 + Game.BOARD_SIZE) || column < 'A';
	}

	public boolean isHit() {
		return hit;
	}

	public void hit() {
		hit = true;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ')';
	}
}
