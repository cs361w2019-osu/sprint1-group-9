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
