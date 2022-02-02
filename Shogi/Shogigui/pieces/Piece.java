package Shogigui.pieces;

import java.util.ArrayList;

import Shogigui.Board;
import Shogigui.Board.Square;

public class Piece implements Cloneable {
    private int x;
    private int y;
    private boolean is_white;
    private String file_path;

    Board board;
    boolean is_captured = false;
    boolean is_promoted;
    boolean is_protected;
    boolean checking_if_defender;
    boolean is_blocking_check;
    Piece is_blocking_check_from;
    ArrayList<Piece> Attackers;
    ArrayList<Piece> Defenders;

    ArrayList<Piece> captureWith;

    int value;

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

        if (this.checking_if_defender == false) {

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

    public boolean nothingBetweenPosAndMoveDest(int destination_x, int destination_y) {

        ArrayList<Board.Square> squaresBetweenPosAndDest = board.getSquaresBetween(this.getX(), this.getY(),
                destination_x, destination_y);

        if (squaresBetweenPosAndDest != null) {

            for (Square square : squaresBetweenPosAndDest) {

                if (board.getPiece(square.x, square.y) != null) {
                    return false;
                }

            }
        }
        return true;
    }

    public boolean canMove(int destination_x, int destination_y) {
        return false;
    }

    public boolean canBeDropped(int destination_x, int destination_y) {

        // allow drop anywhere if not on piece unless in check then only allow drop to
        // block check

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null
                && this.moveIsOutOfBounds(destination_x, destination_y) == false) {

            return true;
        }
        return false;
    }

    public boolean moveChecksOwnKing(int destination_x, int destination_y) {

        ArrayList<Board.Square> SquaresBetweenPieceAndKing = null;

        Piece king = (this.isWhite()) ? board.getKing(board.getWhitePieces()) : board.getKing(board.getBlackPieces());

        if (this.is_blocking_check_from != null) {

            SquaresBetweenPieceAndKing = board.getSquaresBetween(this.is_blocking_check_from.getX(),
                    this.is_blocking_check_from.getY(),
                    king.getX(), king.getY());
        }

        if (this.is_blocking_check) {

            // allow piece to move into squares between king and threatening piece, and
            // allow it to capture the piece it is blocking check from

            if (SquaresBetweenPieceAndKing != null
                    && SquaresBetweenPieceAndKing.isEmpty() == false & this.getClass() != King.class) {

                for (Board.Square Square : SquaresBetweenPieceAndKing) {

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

    public boolean canMoveToStopCheck(int destination_x, int destination_y) { 

        ArrayList<Board.Square> squares;

        // check if piece can move into space blocking check

        Piece king = (board.whiteIschecked()) ? board.getKing(board.getWhitePieces())
                : board.getKing(board.getBlackPieces());

        ArrayList<Piece> checkingPieces = (board.whiteIschecked()) ? board.getPiecesCheckingWhiteKing()
                : board.getPiecesCheckingBlackKing();

        squares = board.getSquaresThatBlockCheck(king);

        if (squares != null) {

            for (Square square : squares) {

                if (destination_x == square.x && destination_y == square.y && this.is_captured == false) {
                    return true;
                }
            }
        }

        // check if piece can capture the opposing piece putting king in check
        // if they're two pieces checking king (double check) the piece cannot capture a
        // checking piece to get stop check (this wouldnt stop check)

        if (checkingPieces.size() == 1) {

            if (destination_x == checkingPieces.get(0).getX() && destination_y == checkingPieces.get(0).getY()) {
                return true;
            }
        }

        return false;
    }

    public boolean canBeDroppedToStopCheck(int destination_x, int destination_y) {

        ArrayList<Board.Square> squaresBetweenKingAndAttacker = new ArrayList<Board.Square>();

        // check if piece can be dropped into space blocking check

        Piece king = (board.whiteIschecked()) ? board.getKing(board.getWhitePieces())
                : board.getKing(board.getBlackPieces());

        if (board.getSquaresThatBlockCheck(king) != null) {

            squaresBetweenKingAndAttacker = board.getSquaresThatBlockCheck(king);
        }

        if (squaresBetweenKingAndAttacker != null) {

            for (Square square : squaresBetweenKingAndAttacker) {

                if (destination_x == square.x && destination_y == square.y) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getMoveDirection(int position_x, int position_y, int destination_x, int destination_y) {

        String direction = "";

        if (destination_y > position_y) {
            direction = "S";
        }
        if (destination_y < position_y) {
            direction = "N";
        }
        if (destination_x > position_x) {
            direction = "E";
        }
        if (destination_x < position_x) {
            direction = "W";
        }
        if (destination_x < position_x && destination_y < position_y) {
            direction = "NW";
        }
        if (destination_x > position_x && destination_y < position_y) {
            direction = "NE";
        }
        if (destination_x > position_x && destination_y > position_y) {
            direction = "SE";
        }
        if (destination_x < position_x && destination_y > position_y) {
            direction = "SW";
        }
        return direction;
    }

    // get movement range from current position for possible moves

    public ArrayList<Board.Square> getMovementRange(){

        ArrayList<Board.Square> movementRange= new ArrayList<Board.Square>();

        for (int x = 0; x < board.getROWS(); x++) {
            for (int y = 0; y < board.getCOLS(); y++) {

                if (this.canMove(x, y)){

                    movementRange.add(board.new Square(x, y));
                }
            }
        }
        if (movementRange.isEmpty()){
            movementRange=null;
        }

        return movementRange;
    }

    // get movement range from a set position for possible moves 
    public ArrayList<Board.Square> getMovementRangeFrom(int newX, int newY){

        int prevX= this.getX();
        int prevY= this.getY();

        this.setX(newX);
        this.setY(newY);

        ArrayList<Board.Square> movementRange= new ArrayList<Board.Square>();

        for (int x = 0; x < board.getROWS(); x++) {
            for (int y = 0; y < board.getCOLS(); y++) {

                if (this.canMove(x, y)){

                    movementRange.add(board.new Square(x, y));
                }
            }
        }
        if (movementRange.isEmpty()){
            movementRange=null;
        }

        this.setX(prevX);
        this.setY(prevY);

        return movementRange;
    }


    // get movement range in one direction (for possible moves)

    public ArrayList<Board.Square> getPossibleDisplacementRange(int xpos, int ypos, String movementDirection) {

        ArrayList<Board.Square> movementRange= new ArrayList<Board.Square>();

        for (int x = 0; x < board.getROWS(); x++) {
            for (int y = 0; y < board.getCOLS(); y++) {

                if (this.canMove(x, y) && getMoveDirection(this.getX(), this.getY(), x, y).equals(movementDirection)){

                    movementRange.add(board.new Square(x, y));
                }
            }
        }
        if (movementRange.isEmpty()){
            movementRange=null;
        }

        return movementRange;
    }


    // get movement range in one direction (xray)

    public ArrayList<Board.Square> getMovementRange(int xpos, int ypos, String movementDirection) {

        int ChangeInX = 0;
        int ChangeInY = 0;

        Square Square = board.new Square(xpos, ypos);

        ArrayList<Board.Square> Squares = new ArrayList<Board.Square>();

        if (((movementDirection == "N" || movementDirection == "E" || movementDirection == "S" // ++ [fix
                                                                                               // extendability...]

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
                Squares.add(board.new Square(Square.x, Square.y));
            }

        }

        if (Squares.isEmpty()) {
            Squares = null;
        }

        return Squares;
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

    public void changeColour() {

        is_white = (this.isWhite()) ? false : true;

    }

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

    public boolean checking_if_defender() {
        return checking_if_defender;
    }

    public void set_checking_if_defender(boolean checking_if_defender) {
        this.checking_if_defender = checking_if_defender;
    }

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
        this.Attackers = Attackers;
    }

    public void setDefenders(ArrayList<Piece> Defenders) {
        this.Defenders = Defenders;
    }
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    public int getValue(){
        return value;
    }
    public void resetCaptureWith(){
        captureWith= new ArrayList<Piece>();
    }
    public void addCaptureWith(Piece piece){
        this.captureWith.add(piece);
    }
    public ArrayList<Piece> getCaptureWith(){
        return captureWith;
    }

    public boolean canbedropped(int clicked_Column, int clicked_Row) {
        return false;
    }
    public void setBoard(Board board_){
        this.board=board_;
    }
}
