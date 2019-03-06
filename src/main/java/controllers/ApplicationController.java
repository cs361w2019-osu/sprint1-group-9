package controllers;

import com.google.inject.Singleton;
import cs361.battleships.models.Game;
import cs361.battleships.models.Ships.ShipBase;
import cs361.battleships.models.Ships.ShipUtility;
import ninja.Context;
import ninja.Result;
import ninja.Results;

@Singleton
public class ApplicationController {

    public Result index() {
        return Results.html();
    }

    public Result newGame() {
        return Results.json().render(Game.getInstance());
    }

    public Result placeShip(Context context, PlacementGameAction g) {
        ShipBase ship = ShipUtility.createShip(g.getShipType());
        boolean result = Game.getInstance().placeShip(ship, g.getActionRow(), g.getActionColumn(), g.isVertical());
        if (result) {
            return Results.json().render(Game.getInstance());
        } else {
            return Results.badRequest();
        }
    }

    public Result attack(Context context, AttackGameAction g) {
        boolean result = Game.getInstance().attack(g.getActionRow(), g.getActionColumn());
        if (result) {
            return Results.json().render(Game.getInstance());
        } else {
            return Results.badRequest();
        }
    }

    public Result moveShip(Context context, MoveShipGameAction g) {
        ShipBase ship = ShipUtility.createShip(g.getShipType());
        System.out.println("Got Request!");
        boolean result = Game.getInstance().moveShip(ship, g.getDirection());
        if (result) {
            return Results.json().render(Game.getInstance());
        } else {
            return Results.badRequest();
        }
    }

    public Result ping(Context context, AttackGameAction g) {
        var result = Game.getInstance().ping(g.getActionRow(), g.getActionColumn());
        if (result) {
            return Results.json().render(Game.getInstance());
        } else {
            return Results.badRequest();
        }
    }
}
