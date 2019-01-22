package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static cs361.battleships.models.AtackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
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
        System.out.println("Attack request came in!");
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == AtackStatus.INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == AtackStatus.INVALID);

        return true;
    }

    private char randCol() {
        Random rand = new Random();
        char randchar = (char)(rand.nextInt(10) + 'a');
        return randchar;
    }

    private int randRow() {
        Random rand = new Random();
        int randnum = rand.nextInt(10)+1;
        return randnum;
    }

    private boolean randVertical() {
        Random rand = new Random();
        boolean tf = rand.nextBoolean();
        return tf;
    }
}
