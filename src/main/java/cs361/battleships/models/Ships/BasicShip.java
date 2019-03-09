package cs361.battleships.models.Ships;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cs361.battleships.models.AttackStatus;
import cs361.battleships.models.Result;
import cs361.battleships.models.Square;

import java.util.ArrayList;

public class BasicShip extends ShipBase {

    private boolean isArmored;
    private int cqPos;


    public BasicShip() {
        occupiedSquares = new ArrayList<>();
    }

    public BasicShip(String kind) {
        this();
        this.kind = kind;
        switch(kind) {
            case "MINESWEEPER":
                size = 2;
                cqPos = 0;
                isArmored = false;
                break;
            case "DESTROYER":
                size = 3;
                cqPos = 1;
                isArmored = true;
                break;
            case "BATTLESHIP":
                size = 4;
                cqPos = 2;
                isArmored = true;
                break;
        }
    }


    public Result attack(int x, char y) {
        var attackedLocation = new Square(x, y);
        var square = getOccupiedSquares().stream().filter(s -> s.equals(attackedLocation)).findFirst();

        // check if miss
        if (!square.isPresent()) {
            return new Result(attackedLocation);
        }


        // its a hit! now check for cq
        var attackedSquare = square.get();
        var result = new Result(attackedSquare);
        if (attackedSquare.getCQ())
        {
            if (!this.isArmored && !this.isSunk())
            {
                getOccupiedSquares().forEach( elem -> elem.hit());
                result.setShip(this);
                result.setResult(AttackStatus.SUNK);
                return result;
            }
            else if (this.isSunk())
            {
                result.setResult(AttackStatus.INVALID);
                return result;
            }
            else
            {
                this.isArmored = false;
                result.setShip(this);
                result.setResult(AttackStatus.MISS);
                return result;
            }
        }
        if (attackedSquare.isHit() && !attackedSquare.getCQ()) {
            result.setResult(AttackStatus.INVALID);
            return result;
        }
        attackedSquare.hit();
        result.setShip(this);
        if (isSunk()) {
            result.setResult(AttackStatus.SUNK);
        } else {
            result.setResult(AttackStatus.HIT);
        }
        return result;
    }

    public void place(char col, int row, boolean isVertical) {
        Square temp;
        for (int i=0; i<size; i++) {
            if (isVertical) {
                temp = new Square(row + i, col);

            } else {
                temp = new Square(row, (char) (col + i));
            }
            if(i == this.cqPos) {
                CQuarters = temp;
                CQuarters.setCQ(true);
            }
            this.occupiedSquares.add(temp);
        }
    }
}
