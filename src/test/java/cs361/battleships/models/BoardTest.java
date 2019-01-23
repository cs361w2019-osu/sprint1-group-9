package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class BoardTest {

    /*****************************************
     *
     * Placing a MINESWEEPER; should return TRUE
     * (Placed vertically at coordinates (6, F))
     *
     ****************************************/
    @Test
    public void testValidPlacementMinesweeper(){
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 6, 'F', true));
    }

    /*****************************************
     *
     * Placing a DESTROYER; should return TRUE
     * (Placed horizontally at coordinates (4, B))
     *
     ****************************************/
    @Test
    public void testValidPlacementDestroyer() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("DESTROYER"), 4, 'B', false));
    }

    /*****************************************
     *
     * Placing a BATTLESHIP; should return TRUE
     * (Placed horizontally at coordinates (2, B))
     *
     ****************************************/
    @Test
    public void testValidPlacementBattleship() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 2, 'B', false ));
    }

    /*****************************************
     *
     * Placing a MINESWEEPER; should return FALSE
     * (Placed horizontally at coordinates (11, C))
     *
     ****************************************/
    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    /*****************************************
     *
     * Placing a BATTLESHIP hanging off the edge
     *      of the board; should return FALSE.
     * (Placed horizontally at coordinates (6, J))
     *
     ****************************************/
    @Test
    public void testInvalidEdgePlacement(){
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 6, 'J', false ));
    }

    /*****************************************
     *
     * Placing BATTLESHIP on established BATTLESHIP;
     *      should return FALSE.
     * (Placed horizontally at coordinates (2, B))
     *
     ****************************************/
    @Test
    public void testInvalidOverlapPlacement(){
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 2, 'B', false );
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 2, 'B', false));
    }

    /*****************************************
     *
     * Testing an INVALID ATTACK; should return
     *      FALSE.
     * (Attacking at coordinates (5, K))
     *
     ****************************************/
    @Test
    public void testInvalidAttack(){
        Board board = new Board();
        AtackStatus aStatus = AtackStatus.INVALID;
        assertEquals(aStatus, board.attack(5, 'K').getResult());
    }

    /*****************************************
     *
     * Testing a VALID ATTACK; should return
     *      TRUE.
     * (Attacking at coordinates (2, B))
     *
     ****************************************/
    @Test
    public void testValidAttack(){
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 2, 'B', false );
        AtackStatus bStatus = AtackStatus.HIT;
        assertEquals(bStatus, board.attack(2, 'B').getResult());
    }
}