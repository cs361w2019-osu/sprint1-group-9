package cs361.battleships.models;

import cs361.battleships.models.Ships.BasicShip;
import cs361.battleships.models.Ships.ShipBase;
import cs361.battleships.models.Ships.ShipUtility;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testInvalidPlacement() {
        assertFalse(board.placeShip(new BasicShip("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testPlaceMinesweeper() {
        assertTrue(board.placeShip(new BasicShip("MINESWEEPER"), 1, 'A', true));
    }

    @Test
    public void testAttackEmptySquare() {
        board.placeShip(new BasicShip("MINESWEEPER"), 1, 'A', true);
        Result result = board.attack(2, 'E');
        assertEquals(AttackStatus.MISS, result.getResult());
    }

    @Test
    public void testAttackShip() {
        ShipBase minesweeper = new BasicShip("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        minesweeper = board.getShips().get(0);
        Result result = board.attack(2, 'A');
        assertEquals(AttackStatus.HIT, result.getResult());
        assertEquals(minesweeper, result.getShip());
    }

    @Test
    public void testAttackSameSquareMultipleTimes() {
        ShipBase minesweeper = new BasicShip("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        board.attack(1, 'A');
        Result result = board.attack(1, 'A');
        assertEquals(AttackStatus.INVALID, result.getResult());
    }

    @Test
    public void testAttackSameEmptySquareMultipleTimes() {
        Result initialResult = board.attack(1, 'A');
        assertEquals(AttackStatus.MISS, initialResult.getResult());
        Result result = board.attack(1, 'A');
        assertEquals(AttackStatus.INVALID, result.getResult());
    }

    @Test
    public void testSurrender() {
        board.placeShip(new BasicShip("MINESWEEPER"), 1, 'A', true);
        board.attack(2, 'A');
        var result = board.attack(1, 'A');
        assertEquals(AttackStatus.SURRENDER, result.getResult());
    }

    @Test
    public void testPlaceMultipleShipsOfSameType() {
        assertTrue(board.placeShip(new BasicShip("MINESWEEPER"), 1, 'A', true));
        assertFalse(board.placeShip(new BasicShip("MINESWEEPER"), 5, 'D', true));

    }

    @Test
    public void testCantPlaceMoreThan3Ships() {
        assertTrue(board.placeShip(new BasicShip("MINESWEEPER"), 1, 'A', true));
        assertTrue(board.placeShip(new BasicShip("BATTLESHIP"), 5, 'D', true));
        assertTrue(board.placeShip(new BasicShip("DESTROYER"), 6, 'A', false));
        assertFalse(board.placeShip(new BasicShip("DESTROYER"), 8, 'A', false));

    }

    @Test
    public void testMoveShip() {

        board.placeShip(new BasicShip("DESTROYER"), 1, 'A', false);
        board.placeShip(new BasicShip("MINESWEEPER"), 5, 'D', false);
        board.placeShip(new BasicShip("BATTLESHIP"), 1, 'H', true);
        board.moveShip("MINESWEEPER", ShipUtility.MOVE_UP);
        var ship = board.getShips().get(1);
        assertTrue(ship.getOccupiedSquares().get(0).getColumn() == 'D');
        assertTrue(ship.getOccupiedSquares().get(0).getRow() == 4);
        assertTrue(ship.getOccupiedSquares().get(1).getColumn() == 'E');
        assertTrue(ship.getOccupiedSquares().get(1).getRow() == 4);
    }

    @Test
    public void testMoveShipError() {
        board.placeShip(new BasicShip("MINESWEEPER"), 1, 'A', false);
        board.placeShip(new BasicShip("DESTROYER"), 5, 'D', false);
        board.placeShip(new BasicShip("BATTLESHIP"), 1, 'H', true);
        board.moveShip("MINESWEEPER", ShipUtility.MOVE_UP);
        assertFalse(board.moveShip("MINESWEEPER", ShipUtility.MOVE_UP));
    }
}
