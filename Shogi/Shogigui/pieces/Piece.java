package Shogigui.pieces;

import java.util.ArrayList;

import Shogigui.Board;
import Shogigui.Board.Coordinate;

public class Piece {
    private int x;
    private int y;
    private boolean is_white;
    private String file_path;
    public Board board;

    boolean is_captured = false;
    boolean is_promoted;
    boolean is_protected;
    boolean is_checking;
    boolean is_blocking_check; 
    Piece is_blocking_check_from;

    ArrayList<Piece> Attackers;
    ArrayList<Piece> Defenders;

    public Piece(int x, int y, boolean is_white, String file_path, Board board, boolean is_promoted) {
        this.is_white = is_white;
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
        this.is_promoted = is_promoted;
    }

    public boolean moveIsOutOfBounds(int destination_x, int destination_y) {

        if (destination_x > board.getROWS() - 1 || destination_x < 0 || destination_y > board.getCOLS() - 1
                || destination_y < 0) {
            return true;
        }
        return false;

    }

    public boolean moveIsOnTopOfOwnPiece(int destination_x, int destination_y) {

        if (this.is_checking == false) {

            Piece possiblePiece = board.getPiece(destination_x, destination_y);

            if (possiblePiece != null) {
                if (possiblePiece.isWhite() && this.isWhite()) {
                    return true;
                }
                if (possiblePiece.isBlack() && this.isBlack()) {
                    return true;
                }
            }
        }

        return false;

    }

    public String getMoveDirection(int destination_x, int destination_y) {

        String direction = "";

        if (destination_y > this.getY()) {
            direction = "S";
        }
        if (destination_y < this.getY()) {
            direction = "N";
        }
        if (destination_x > this.getX()) {
            direction = "E";
        }
        if (destination_x < this.getX()) {
            direction = "W";
        }
        if (destination_x < this.getX() && destination_y < this.getY()) {
            direction = "NW";
        }
        if (destination_x > this.getX() && destination_y < this.getY()) {
            direction = "NE";
        }
        if (destination_x > this.getX() && destination_y > this.getY()) {
            direction = "SE";
        }
        if (destination_x < this.getX() && destination_y > this.getY()) {
            direction = "SW";
        }
        return direction;
    }

    public String getFilePath() {
        return file_path;
    }

    public void setFilePath(String path) {
        this.file_path = path;
    }

    public boolean isWhite() {
        return is_white;
    }

    public boolean isBlack() {
        return !is_white;
    }
    //
    public void changeColour(){

        if (this.isWhite()){
            is_white=false;
        }
        else{
            is_white=true;
        }
    }
    //
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean canMove(int destination_x, int destination_y) {
        return false;
    }

    // ++
    // ++ PLAYER IS IN CHECK THEN DROP MUST BLOCK CHECK! ++ DROP CANNOT BE OUT OF
    // BOUNDS
    // ++

    public boolean canBeDropped(int destination_x, int destination_y) {

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null && this.moveIsOutOfBounds(destination_x, destination_y)==false) {

            // allow drop anywhere if not on piece unless in check then only allow drop to
            // block check

            return true;
        }

        return false;
    }

    public boolean canBeDroppedToStopCheck(int destination_x, int destination_y){

        ArrayList<Board.Coordinate> squares= new ArrayList<Board.Coordinate>();

        Piece king;

        ArrayList<Piece> piecesCheckingKing;

        // check if piece can move into space blocking check

        if (board.whiteIschecked()) {
            king = board.getKing(board.getWhitePieces());

            piecesCheckingKing= board.getPiecesCheckingWhiteKing();

        } else {
            king = board.getKing(board.getBlackPieces());

            piecesCheckingKing= board.getPiecesCheckingBlackKing();

        }
  
        for (Piece piece: piecesCheckingKing){          // + + fix for double check, must block both check paths

            if (board.getSquaresBetween(piece.getX(),piece.getY(),king.getX(),king.getY())!=null){
                squares.addAll(board.getSquaresBetween(piece.getX(),piece.getY(),king.getX(),king.getY()));
            }
        }

        if (squares != null) {

            for (Coordinate square : squares) {

                // System.out.println("square: ( " + square.x + " , " + square.y + " )");

                if (destination_x == square.x && destination_y == square.y) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean moveChecksOwnKing(int destination_x, int destination_y) {

        Piece king;
        ArrayList<Board.Coordinate> SquaresBetweenPieceAndKing = null;

        if (this.isWhite()) {
            king = board.getKing(board.getWhitePieces());
        } else {
            king = board.getKing(board.getBlackPieces());
        }

        if (this.is_blocking_check_from != null) {

            SquaresBetweenPieceAndKing = board.getSquaresBetween(this.is_blocking_check_from.getX(),
                    this.is_blocking_check_from.getY(),
                    king.getX(), king.getY());
        }

        if (this.is_blocking_check) {

            // allow piece to move into squares between king and threatening peice, and
            // allow it to capture the piece it is blocking check from

            if (SquaresBetweenPieceAndKing != null
                    && SquaresBetweenPieceAndKing.isEmpty() == false & this.getClass() != King.class) {

                for (Board.Coordinate Square : SquaresBetweenPieceAndKing) {

                    if (destination_x == Square.x && destination_y == Square.y
                            || (destination_x == this.is_blocking_check_from.getX()
                                    && destination_y == this.is_blocking_check_from.getY())) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    public boolean canMoveToStopCheck(int destination_x, int destination_y) { //+ + fix for double check, must block both check paths

        ArrayList<Board.Coordinate> squares;

        ArrayList<Piece> checkingPieces;

        Piece king;

        // check if piece can move into space blocking check

        if (board.whiteIschecked()) {
            king = board.getKing(board.getWhitePieces());

            checkingPieces = board.getPiecesCheckingWhiteKing();

        } else {
            king = board.getKing(board.getBlackPieces());

            checkingPieces = board.getPiecesCheckingBlackKing();

        }

        squares = board.getMovesThatBlockCheck(king);

        if (squares != null) {

            for (Coordinate square : squares) {

                // System.out.println("square: ( " + square.x + " , " + square.y + " )");

                if (destination_x == square.x && destination_y == square.y && this.getClass() != King.class &&this.is_captured==false) {
                    return true;
                }
            }
        }

        // check if piece can capture the opposing piece putting king in check
        // king cannot capture piece checking it if piece is protected

        for (Piece p : checkingPieces) {

            if (destination_x == p.getX() && destination_y == p.getY()) {

                if (this.getClass() == King.class && p.is_protected == true) {
                    return false;
                } else {
                    return true;
                }
            }

        }

        // check if king can move into space to get out of check

        if (this.getClass() == King.class && this.canMove(destination_x, destination_y)) {

            // make sure it cannot move into a space that is in the path of the piece
            // checking it

            for (Piece p : checkingPieces) {

                // check if piece checking it can move multiple squares if so

                if (p.getClass() != Bishop.class && p.getClass() != Lance.class && p.getClass() != Rook.class) {
                    return true; // ++ fix extendability...
                } else {

                    // make sure it cannot move into a space that is in the path of the piece
                    // checking it

                    String direction = p.getMoveDirection(this.getX(), this.getY()); // ++ [test] fix efficiancy


                    ArrayList<Coordinate> pMovementRange = p.getMovementRange(direction);

                    if (pMovementRange != null) {

                        for (Coordinate square : pMovementRange) {

                            if (destination_x == square.x &&  square.y == destination_y) {

                                return false;
                            }
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

    public boolean is_captured() {
        return is_captured;
    }

    public void captured(boolean is_captured) {
        this.is_captured = is_captured;
    }

    public boolean is_promoted() {
        return is_promoted;
    }

    public void promote(boolean is_promoted) {
        this.is_promoted = is_promoted;
    }

    public boolean is_protected() {
        return is_protected;
    }

    public void set_protected(boolean is_protected) {
        this.is_protected = is_protected;
    }

    //
    public boolean is_checking() {
        return is_checking;
    }

    public void set_checking(boolean is_checking) {
        this.is_checking = is_checking;
    }

    //

    public boolean is_blocking_check() {
        return is_blocking_check;
    }

    public void set_is_blocking_check(boolean is_blocking_check) {
        this.is_blocking_check = is_blocking_check;
    }

    public void is_blocking_check_from(Piece Piece) {
        this.is_blocking_check_from = Piece;
    }

    public Piece get_is_blocking_check_from() {
        return is_blocking_check_from;
    }
    //
    public ArrayList<Piece> getAttackers() {
        return Attackers;
    }

    public ArrayList<Piece> getDefenders() {
        return Defenders;
    }
    public void addAttackers(Piece Attacker) {
        this.Attackers.add(Attacker);
    }

    public void addDefenders(Piece Defender) {
        this.Defenders.add(Defender);
    }
    public void setAttackers(ArrayList<Piece> Attackers) {
        this.Attackers=Attackers;
    }
    public void setDefenders(ArrayList<Piece> Defenders) {
        this.Defenders=Defenders;
    }
    //
    public ArrayList<Board.Coordinate> getMovementRange(String movementDirection) {

        int ChangeInX = 0;
        int ChangeInY = 0;

        Coordinate Square = board.new Coordinate(this.getX(), this.getY());

        ArrayList<Board.Coordinate> Squares = new ArrayList<Board.Coordinate>();

        if (((movementDirection == "N" || movementDirection == "E" || movementDirection == "S" // ++ fix extendability
                                                                                               // ...
                || movementDirection == "W") && this.getClass() == Rook.class)
                || (movementDirection == "N" && this.isBlack() && this.getClass() == Lance.class)
                || (movementDirection == "E" && this.isWhite() && this.getClass() == Lance.class)
                || ((movementDirection == "NE" || movementDirection == "NW" || movementDirection == "SE"
                        || movementDirection == "SW") && this.getClass() == Bishop.class)) {

            if (movementDirection.contains("N")) {
                ChangeInY = -1;

            }
            if (movementDirection.contains("S")) {
                ChangeInY = +1;

            }
            if (movementDirection.contains("E")) {
                ChangeInX = +1;
            }
            if (movementDirection.contains("W")) {
                ChangeInX = -1;
            }
            while (moveIsOutOfBounds(Square.x, Square.y) == false) {
                Square.x = Square.x + ChangeInX;
                Square.y = Square.y + ChangeInY;
                Squares.add(board.new Coordinate(Square.x, Square.y));
            }

        }
        if (Squares.isEmpty()) {
            Squares = null;
        }

        return Squares;
    }
}
