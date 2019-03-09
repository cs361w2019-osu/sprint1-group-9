package cs361.battleships.models;

import cs361.battleships.models.Ships.BasicShip;
import cs361.battleships.models.Ships.ShipBase;
import cs361.battleships.models.Ships.ShipUtility;

public class Submarine extends BasicShip {

    private boolean submerged;
    private boolean isArmored;

    public Submarine (){
        this.kind = "SUBMARINE";
        this.isArmored = true;
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

    public void place(char col, int row, boolean isVertical) {
        Square temp;
        for (int i=0; i<4; i++) {
            if (isVertical) {
                temp = new Square(row + i, col);
                if(i == 2) {
                    this.occupiedSquares.add(new Square(row+i, (char)((int)col + 1)));
                }

            } else {
                temp = new Square(row, (char) (col + i));
                if(i == 2) {
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

    //@Override
    //public boolean overlaps(ShipBase other) {

    //}

    public boolean getSubmerged() { return submerged; }



}