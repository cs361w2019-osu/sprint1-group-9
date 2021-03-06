package cs361.battleships.models.Ships;

import cs361.battleships.models.AttackStatus;
import cs361.battleships.models.Result;
import cs361.battleships.models.Ships.BasicShip;
import cs361.battleships.models.Ships.ShipBase;
import cs361.battleships.models.Ships.ShipUtility;
import cs361.battleships.models.Square;

import java.util.ArrayList;

public class Submarine extends BasicShip {

    private boolean submerged;
    private boolean isArmored;

    public Submarine (){
        this.kind = "SUBMARINE";
        this.isArmored = true;
        occupiedSquares = new ArrayList<>();
    }

    @Override
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

    @Override
    public void place(char col, int row, boolean isVertical) {
        Square temp;
        System.out.println("Got to place!!");
        for (int i=0; i<4; i++) {
            if (isVertical) {
                temp = new Square(row + i, col);
                if(i == 2) {
                    System.out.println("Hit here!");
                    this.occupiedSquares.add(new Square(row+i, (char)(col + 1)));
                }

            } else {
                temp = new Square(row, (char) (col + i));
                if(i == 2) {
                    System.out.println("Hit here!");
                    this.occupiedSquares.add(new Square(row-1,(char)(col + i)));
                }
            }
            if(i == 3) {
                CQuarters = temp;
                CQuarters.setCQ(true);
            }
            this.occupiedSquares.add(temp);
        }
    }


}