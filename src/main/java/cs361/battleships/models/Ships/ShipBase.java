package cs361.battleships.models.Ships;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.AttackStatus;
import cs361.battleships.models.Game;
import cs361.battleships.models.Result;
import cs361.battleships.models.Square;
import com.google.common.collect.Sets;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ShipBase {



    @JsonProperty protected String kind;
    @JsonProperty protected List<Square> occupiedSquares;
    @JsonProperty protected Square CQuarters;
    @JsonProperty protected int size;


    public List<Square> getOccupiedSquares() {
        return occupiedSquares;
    }


    public boolean move(int direction) {

        switch (direction) {
            case ShipUtility.MOVE_UP:
                return moveVertically(-1);
            case ShipUtility.MOVE_DOWN:
                return moveVertically(-1);
            case ShipUtility.MOVE_RIGHT:
                return moveHorizontally(1);
            case ShipUtility.MOVE_LEFT:
                return moveHorizontally(-1);
        }
        return false;
    }


    public boolean overlaps(ShipBase other) {
        Set<Square> thisSquares = Set.copyOf(getOccupiedSquares());
        Set<Square> otherSquares = Set.copyOf(other.getOccupiedSquares());
        Sets.SetView<Square> intersection = Sets.intersection(thisSquares, otherSquares);
        return intersection.size() != 0;
    }

    public boolean isAtLocation(Square location) {
        return getOccupiedSquares().stream().anyMatch(s -> s.equals(location));
    }

    public String getKind() {
        return kind;
    }

    public abstract Result attack(int x, char y);
    public abstract void place(char col, int row, boolean isVertical);


    private boolean moveVertically(int modifier) {
        var newRows = new ArrayList<Integer>();
        int row;

        // make the potential new rows
        for (Square pos : occupiedSquares) {
            row = pos.getRow() + modifier;
            if(row < 0 || row > Game.BOARD_SIZE) {
                return false;
            }
            newRows.add(row);
        }

        //move them over
        occupiedSquares.forEach(s -> s.setRow(newRows.remove(0)));
        return true;
    }

    private boolean moveHorizontally(int modifier) {
        var newCols = new ArrayList<Integer>();
        int col;

        // make the potential new rows
        for (Square pos : occupiedSquares) {
            col = (int)pos.getColumn() + modifier;
            if(col < 65 || col > (Game.BOARD_SIZE + 64)) {
                return false;
            }
            newCols.add(col);
        }

        //move them over
        occupiedSquares.forEach(s -> s.setColumn( (char)(int)(newCols.remove(0))));
        return true;
    }


    @JsonIgnore
    public boolean isSunk() {
        return getOccupiedSquares().stream().allMatch(s -> s.isHit());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ShipBase)) {
            return false;
        }
        var otherShip = (ShipBase) other;

        return this.kind.equals(otherShip.kind)
                && this.size == otherShip.size
                && this.occupiedSquares.equals(otherShip.occupiedSquares);
    }

    @Override
    public int hashCode() {
        return 33 * kind.hashCode() + 23 * size + 17 * occupiedSquares.hashCode();
    }

    @Override
    public String toString() {
        return kind + occupiedSquares.toString();
    }
}
