package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Ships.ShipBase;

import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AttackStatus.*;

public class Game {

    @JsonIgnore private static Game instance;

    private Game() {
        playersBoard = new Board();
        opponentsBoard = new Board();
    }

    public static Game getInstance() {
        if(instance == null) {
            instance = new Game();
        }
        return instance;
    }


    @JsonProperty private Board playersBoard;
    @JsonProperty private Board opponentsBoard;



    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(ShipBase ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(ship, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);

        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }


    public boolean moveShip(String shipName, int direction) {
        playersBoard.moveShip(shipName, direction);
        return true;
    }

    public boolean ping(int x, char y) {
        return opponentsBoard.getPingedList(new Square(x, y));

    }

    public void reset() {
        playersBoard = new Board();
        opponentsBoard = new Board();
    }

    private char randCol() {
        int random = new Random().nextInt(10);
        return (char) ('A' + random);
    }

    private int randRow() {
        return  new Random().nextInt(10) + 1;
    }

    private boolean randVertical() {
        return new Random().nextBoolean();
    }
}
