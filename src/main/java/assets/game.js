var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var ping = false;
var moveShips = {
    direction: "NULL",
    pressed: false
};


function makeGrid(table, isPlayer) {
    // add colmn header
    let titrow = document.createElement('tr');
    titrow.appendChild(document.createElement("td"));
    for (i=0; i<10; i++) {
        let ele = document.createElement('td');
        ele.classList.add('col-header');
        ele.innerText = String.fromCharCode(97 + i);
        titrow.appendChild(ele);
    }
    table.appendChild(titrow);
    
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        // add row header
        let rowtit = document.createElement('td');
        rowtit.innerText = i + 1;
        rowtit.classList.add('col-header');
        row.appendChild(rowtit);
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.classList.add("square");
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}


function getSquare(row, col, board) {
    return board.rows[row].cells[col.charCodeAt(0) - 'A'.charCodeAt(0) + 1];
}


function markShipSunk(elementId, ship) {
    var board = document.getElementById(elementId);
    ship.occupiedSquares.forEach((s) => {
        let tile = getSquare(s.row, s.column, board);
        tile.innerHTML = "";
        tile.classList.add('sink');
    });
}


function sinkShip(board, elementId, attack) {
    board.ships.forEach((ship) => {
        ship.occupiedSquares.forEach((s) => {
            if(attack.location.row == s.row && attack.location.col == s.col) {
                markShipSunk(elementId, ship);
                return;
            }
        });
    });
}


function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK") {
            sinkShip(board, elementId, attack);
            return;
        }
        else if (attack.result === "SURRENDER") {
            alert(surrenderText);
            return;
        }
        else
            return;
        let square = document.getElementById(elementId).rows[attack.location.row].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0) + 1];
        let mark = document.createElement('div');
        mark.classList.add('mark-circle');
        mark.classList.add(className);
        square.appendChild(mark);
    });
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0) + 1].classList.add("occupied");
    }));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=1; i<11; i++) {
        for (j=1; j<11; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}


function pingBoard(pingList) {
    pingList.forEach(r => {
        let square = getSquare(r.location.row, r.location.column, document.getElementById('opponent'));
        if(r.result === "HIT") {
            square.classList.add("ping-hit");
        } else {
            square.classList.add("ping-miss");
        }

    });
    var pingButton = document.getElementById('ping-button');
    pingButton.innerText = "Ping: " + game.opponentsBoard.pingsLeft;
}

function cellClick() {
    let row = this.parentNode.rowIndex;
    let col = String.fromCharCode(this.cellIndex + 64);
    if (isSetup) {
        sendXhr("POST", "/place", { shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            placedShips++;
            if (placedShips == 3) {
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    } else if(ping) {
        ping = false;
        sendXhr("POST", "/ping", { x: row, y: col}, function(data) {
            game = data;
            pingBoard(data.opponentsBoard.pings);
        });

    } else if((moveShips.pressed == true)){
              sendXhr("POST", "/move", {shipType: shipType, direction: moveShips}, function(data) {
                  game = data;
                  moveShips.direction = "NULL";
                  moveShips.pressed = false;
              });


     }else {
        sendXhr("POST", "/attack", { x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            alert("Error:\nCannot place ships on ships!\nCannot place off the grid!\nCannot attack the same square twice!");
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
    });
    document.getElementById("ping-button").addEventListener("click", function(e) {
        ping = true;
    });
    document.getElementById("move-north").addEventListener("click", function(e) {
        moveShips.direction = "UP";
        moveShips.pressed = true;
    });
    document.getElementById("move-south").addEventListener("click", function(e) {
        moveShips.direction = "DOWN";
        moveShips.pressed = true;
    });
    document.getElementById("move-west").addEventListener("click", function(e) {
        moveShips.direction = "LEFT";
        moveShips.pressed = true;
    });
    document.getElementById("move-east").addEventListener("click", function(e) {
        moveShips.direction = "EAST";
        moveShips.pressed = true;
    });

    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};