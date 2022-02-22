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

    /**
     * The piece constructor is used to initilize a piece object on the shogi board
     * 
     * @param x           The location of the piece on the grids x axis
     * @param y           The location of the piece on the grids y axis
     * @param is_white    True if the piece is white / belonging to the player
     *                    playing as white pieces, false if not
     * @param file_path   The file path used to retrieve the image of the piece
     * @param board       The board instance the piece resides within
     * @param is_promoted True if the piece is promoted, false if not
     */
    public Piece(int x, int y, boolean is_white, String file_path, Board board, boolean is_promoted) {
        this.is_white = is_white;
        this.x = x;
        this.y = y;
        this.file_path = file_path;
        this.board = board;
        this.is_promoted = is_promoted;
    }

    /**
     * The moveIsOutOfBounds method is used to check if a specified position within
     * the board window grid is not within the playing board
     * 
     * @param destination_x The location of the position on the grids x axis
     * @param destination_y The location of the position on the grids y axis
     * @return True if the position resides within the playing board, false if not
     */
    public boolean moveIsOutOfBounds(int destination_x, int destination_y) {

        if (destination_x > board.getROWS() - 1 || destination_x < 0 || destination_y > board.getCOLS() - 1
                || destination_y < 0) {
            return true;
        }
        return false;

    }

    /**
     * The moveIsOnTopOfOwnPiece method is used to check if a specified position on
     * the board grid contains a piece of the same colour as a piece
     * 
     * @param destination_x The location of the position on the grids x axis
     * @param destination_y The location of the position on the grids y axis
     * @return True if a piece of the same colour as this piece resides in the
     *         position, false if not
     */
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

    /**
     * The nothingBetweenPosAndMoveDest method is used to check if a piece resides
     * in the squares between this piece and a specified position on the board grid
     * 
     * @param destination_x The location of the position on the grids x axis
     * @param destination_y The location of the position on the grids y axis
     * @return True if a piece resides in the squares between this piece and the
     *         position, false if not
     */
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

    /**
     * The canMove method is used to determin if a piece can legally move to a
     * specified position on the board grid
     * 
     * @param destination_x The location of the position on the grids x axis of the
     *                      possible move
     * @param destination_y The location of the position on the grids y axis of the
     *                      possible move
     * @return True if the piece can legally move to the position, false if not
     */
    public boolean canMove(int destination_x, int destination_y) {
        return false;
    }

    /**
     * The canBeDropped method is used to determin if a piece can be legally dropped
     * to a specified position on the board grid
     * 
     * @param destination_x The location of the position on the grids x axis of the
     *                      possible drop
     * @param destination_y The location of the position on the grids y axis of the
     *                      possible drop
     * @return True if the piece can legally be dropped to the position false if not
     */
    public boolean canBeDropped(int destination_x, int destination_y) {

        // allow drop anywhere if not on piece unless in check then only allow drop to
        // block check

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null
                && this.moveIsOutOfBounds(destination_x, destination_y) == false) {

            return true;
        }
        return false;
    }

    /**
     * The isLegalMove method checks if a piece moving to a specificed position would result in an illegal moves taking place
      * @param destination_x The location of the position on the grids x axis of the
     *                       possible illegal move
     * @param destination_y The location of the position on the grids y axis of the
     *                      possible illegal move
     * @return True if the move is legal, false if not 
     */
    public boolean isLegalMove(int destination_x, int destination_y){
        
        // do not allow the peice to move outside the board

        if (this.moveIsOutOfBounds(destination_x, destination_y)) {
            return false;
        }

        // if there is a piece at the destination, and it is our own, dont let us move
        // there

        if (this.moveIsOnTopOfOwnPiece(destination_x, destination_y)) {
            return false;
        }

        // dont allow the king to move into space that puts it in check

        if (this.moveChecksOwnKing(destination_x, destination_y)) {
            return false;
        }
        return true;
    }

    /**
     * The moveChecksOwnKing method is used to check if a piece moving to a
     * specified position results in the king of its own colour being directly
     * threatened / checked
     * 
     * @param destination_x The location of the position on the grids x axis of the
     *                      possible move
     * @param destination_y The location of the position on the grids y axis of the
     *                      possible move
     * @return True if the piece moving to the position results in check of the same
     *         colour king, false if not
     */
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

    /**
     * The canMoveToStopCheck method is used to check if a piece moving to a
     * specified position results in the king of the same colour no longer being
     * directly threatened by an enemy piece / checked
     * 
     * @param destination_x The location of the position on the grids x axis of the
     *                      possible move
     * @param destination_y The location of the position on the grids y axis of the
     *                      possible move
     * @return True if the pieces movement results in that colour player no longer
     *         being in check, false if not
     */
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

    /**
     * The canBeDroppedToStopCheck method is used to check if a piece being dropped
     * in a specified position results in the king of the same colour no longer
     * being
     * directly threatened by an enemy piece / checked
     * 
     * @param destination_x The location of the position on the grids x axis of the
     *                      possible drop
     * @param destination_y The location of the position on the grids y axis of the
     *                      possible drop
     * @return True if the pieces drop results in that colour player no longer
     *         being in check, false if not
     */
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

    /**
     * The getMoveDirection static method is used to determin what direction the
     * movement is from one to position to another respectively (position one to
     * position two)
     * 
     * @param position_x    The location of position one on the grid in the x axis
     * @param position_y    The location of position one on the grid in the y axis
     * @param destination_x The location of position two on the grid in the x axis
     * @param destination_y The location of position two on the grid in the x axis
     * @return A string representing the compas direction of the movement postion
     *         one to position two
     */
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

    /**
     * The getMovementRange method is used to retrieve all the possible LEGAL moves
     * from a pieces current position on the board grid
     * 
     * @return An array list of squares (within the board grid) where a piece can
     *         LEGALLY move to, null if the piece has no possible moves
     */
    public ArrayList<Board.Square> getMovementRange() {

        ArrayList<Board.Square> movementRange = new ArrayList<Board.Square>();

        for (int x = 0; x < board.getROWS(); x++) {
            for (int y = 0; y < board.getCOLS(); y++) {

                if (this.canMove(x, y)) {

                    movementRange.add(board.new Square(x, y));
                }
            }
        }
        if (movementRange.isEmpty()) {
            movementRange = null;
        }

        return movementRange;
    }

    /**
     * The getSquaresInDirection method is used to retreive all the squares from a
     * piece position to the end of the playing board grid in a specified direction
     * 
     * @param Direction The direction to move from the pieces position to retrieve
     *                  board grid squares
     * @return ArrayList of board grid squares that reside in the board from a
     *         pieces position to the end of the board grid in the specified
     *         direction, null if no squares reside in this space
     */
    public ArrayList<Square> getSquaresInDirection(String Direction) {

        int ChangeInX = 0;
        int ChangeInY = 0;

        ArrayList<Board.Square> Squares = new ArrayList<Board.Square>();

        if (Direction.contains("N")) {
            ChangeInY = -1;

        }
        if (Direction.contains("S")) {
            ChangeInY = +1;

        }
        if (Direction.contains("E")) {
            ChangeInX = +1;
        }
        if (Direction.contains("W")) {
            ChangeInX = -1;
        }

        Square Square = board.new Square(this.getX(), this.getY());

        while (moveIsOutOfBounds(Square.x, Square.y) == false) {
            Square.x = Square.x + ChangeInX;
            Square.y = Square.y + ChangeInY;
            Squares.add(board.new Square(Square.x, Square.y));
        }

        if (Squares.isEmpty()) {
            Squares = null;
        }

        return Squares;
    }

    /**
     * The getMovementRangeFrom method is used to retrieve all the possible LEGAL
     * moves of a piece from a specified position
     * 
     * @param newX The location of the set position for a piece on the board grid in
     *             the x axis -to retrieve movement range from
     * @param newY The location of the set position for a piece on the board grid in
     *             the y axis -to retrieve movement range from
     * @return ArrayList of board grid squares where a piece can legally move to
     *         from a specified position, null if piece cannot move from this
     *         position
     */
    public ArrayList<Board.Square> getMovementRangeFrom(int newX, int newY) {

        int prevX = this.getX();
        int prevY = this.getY();

        this.setX(newX);
        this.setY(newY);

        ArrayList<Board.Square> movementRange = new ArrayList<Board.Square>();

        for (int x = 0; x < board.getROWS(); x++) {
            for (int y = 0; y < board.getCOLS(); y++) {

                if (this.canMove(x, y)) {

                    movementRange.add(board.new Square(x, y));
                }
            }
        }
        if (movementRange.isEmpty()) {
            movementRange = null;
        }

        this.setX(prevX);
        this.setY(prevY);

        return movementRange;
    }

    /**
     * The getPossibleDisplacementRange method is used to retrieve board grid
     * squares where a piece can legally move to from a specified position in a set
     * direction
     * 
     * @param xpos              The location of the set position for a piece on the
     *                          board grid in the x axis -to retrieve movement range
     *                          from
     * @param ypos              The location of the set position for a piece on the
     *                          board grid in the y axis -to retrieve movement range
     *                          from
     * @param movementDirection Method only retrieves squares that the piece can
     *                          legally move to on the board grid (from the set
     *                          position) if the move is are in direction
     * @return ArrayList of board grid squares where a piece can legally move to
     *         from a specified position in set direction, null if piece cannot move
     *         from this position
     */
    public ArrayList<Board.Square> getPossibleDisplacementRange(int xpos, int ypos, String movementDirection) {

        ArrayList<Board.Square> movementRange = new ArrayList<Board.Square>();

        for (int x = 0; x < board.getROWS(); x++) {
            for (int y = 0; y < board.getCOLS(); y++) {

                if (this.canMove(x, y) && getMoveDirection(this.getX(), this.getY(), x, y).equals(movementDirection)) {

                    movementRange.add(board.new Square(x, y));
                }
            }
        }
        if (movementRange.isEmpty()) {
            movementRange = null;
        }

        return movementRange;
    }

    /**
     * The getMovementRange method is used to retrieve all possible squares in a
     * pieces (xray) movement range set direction from a specified position on the
     * board grid; the move DOES NOT have to be a LEGAL move, THIS METHOD DISREGARDS
     * PIECES IN THE WAY OF THE MOVE (checks for xray moves)
     * 
     * @param xpos              The location of the set position for a piece on the
     *                          board grid in the x axis -to retrieve movement range
     *                          from
     * @param ypos              The location of the set position for a piece on the
     *                          board grid in the y axis -to retrieve movement range
     *                          from
     * @param movementDirection Method only retrieves squaresin the pieces movement
     *                          range on the board grid (from the set position) if
     *                          the move is are in direction
     * @return ArrayList of board grid squares in a pieces movement range
     *         from a specified position in set direction, null if piece cannot move
     *         from this position
     */
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

    /**
     * getFilePath accessor method
     * 
     * @return file_path used to retrieve piece image
     */
    public String getFilePath() {
        return file_path;
    }

    /**
     * setFilePath set method
     * 
     * @param path set the file_path used to retrieve piece image
     */
    public void setFilePath(String path) {
        this.file_path = path;
    }

    /**
     * isWhite accessor method
     * 
     * @return True if piece is of white colour, false if not
     */
    public boolean isWhite() {
        return is_white;
    }

    /**
     * isBlack accessor method
     * 
     * @return True if piece is of black colour, false if not
     */
    public boolean isBlack() {
        return !is_white;
    }

    /**
     * changeColour method used to invert the colour of a piece (ie black piece to
     * white piece)
     */
    public void changeColour() {

        is_white = (this.isWhite()) ? false : true;

    }

    /**
     * setX set method
     * 
     * @param x location of a piece position in the board grid window on the x
     *          axis
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * setX set method
     * 
     * @param y location of a piece position in the board grid window on the y
     *          axis
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * getX accessor method
     * 
     * @return location of a piece position in the board grid window on the x axis
     */
    public int getX() {
        return x;
    }

    /**
     * getY accessor method
     * 
     * @return location of a piece position in the board grid window on the y axis
     */
    public int getY() {
        return y;
    }

    /**
     * is_captured accessor method
     * 
     * @return True if piece is captured, false if not
     */
    public boolean is_captured() {
        return is_captured;
    }

    /**
     * captured set method
     * 
     * @param is_captured True if piece is captured, false if not
     */
    public void captured(boolean is_captured) {
        this.is_captured = is_captured;
    }

    /**
     * is_promoted accessor method
     * 
     * @return True if piece is promoted, false if not
     */
    public boolean is_promoted() {
        return is_promoted;
    }

    /**
     * promote set method
     * 
     * @param is_promoted True if piece is promoted, false if not
     */
    public void promote(boolean is_promoted) {
        this.is_promoted = is_promoted;
    }

    /**
     * is_protected accessor method retrieve piece protected status (ie protected if
     * it resides in a same colour pieces movement range)
     * 
     * @return True if piece is protected, false if not
     */
    public boolean is_protected() {
        return is_protected;
    }

    /**
     * set_protected set method, set piece status to protected (ie the piece resides
     * in a same colour pieces movement range) / unprotected
     * 
     * @param is_protected True if piece is protected, false if not
     */
    public void set_protected(boolean is_protected) {
        this.is_protected = is_protected;
    }

    /**
     * checking_if_defender accessor method retreives this checking_if_defender (when
     * checking_if_defender is true it allows the piece to move ontop of its own
     * colour pieces to check if an own colour piece was captured would this piece
     * be able to move to defend the position)
     * 
     * @return True if piece is being checked if it is a defender, false if not
     */
    public boolean checking_if_defender() {
        return checking_if_defender;
    }

     /**
     * set_checking_if_defender set method set this checking_if_defender (when
     * checking_if_defender is true it allows the piece to move ontop of its own
     * colour pieces to check if an own colour piece was captured would this piece
     * be able to move to defend the position)
     * 
     * @param checking_if_defender True if piece is being checked if it is a defender, false if not
     */
    public void set_checking_if_defender(boolean checking_if_defender) {
        this.checking_if_defender = checking_if_defender;
    }

    /**
     * is_blocking_check accessor method, used to check if piece is blocking
     * opposing check (ie it resides in squares between movement range of opposing
     * piece and same colour king within that range)
     * 
     * @return True if piece is blocking check, false if not
     */
    public boolean is_blocking_check() {
        return is_blocking_check;
    }

    /**
     * set_is_blocking_check set method, used to set pieces is blocking check status
     * (ie it resides in squares between movement range of opposing piece and same
     * colour king within that range)
     * 
     * @param is_blocking_check True if piece is blocking check, false if not
     */
    public void set_is_blocking_check(boolean is_blocking_check) {
        this.is_blocking_check = is_blocking_check;
    }

    /**
     * is_blocking_check_from set method, if the piece is blocking check status is
     * true this method sets what piece it is blocking an opposing check from (ie
     * this piece resides in squares between movement range of piece this method
     * sets and same colour king within that range)
     * 
     * @param Piece The piece this piece is blocking an opposing check from
     */
    public void is_blocking_check_from(Piece Piece) {
        this.is_blocking_check_from = Piece;
    }

    /**
     * get_is_blocking_check_from accessor method, if the piece is blocking check
     * status is
     * true this method retrieves what piece it is blocking an opposing check from
     * (ie
     * this piece resides in squares between movement range of the piece this method
     * retrieves and same colour king within that range)
     * 
     * @return The piece this piece is blocking an opposing check from
     */
    public Piece get_is_blocking_check_from() {
        return is_blocking_check_from;
    }

    /**
     * getAttackers accessor method
     * 
     * @return Array list of pieces that can move to capture this piece
     */
    public ArrayList<Piece> getAttackers() {
        return Attackers;
    }

    /**
     * getDefenders accessor method
     * 
     * @return Array list of pieces that can move to defend this piece
     */
    public ArrayList<Piece> getDefenders() {
        return Defenders;
    }

    /**
     * addAttackers method adds opposing pieces that can capture this piece to
     * attackers list
     * 
     * @param Attacker piece that can capture this piece
     */
    public void addAttackers(Piece Attacker) {
        this.Attackers.add(Attacker);
    }

    /**
     * addDefenders method adds pieces of the same colour that can defend this piece
     * to defenders list
     * 
     * @param Defenders piece that can defend this piece
     */
    public void addDefenders(Piece Defender) {
        this.Defenders.add(Defender);
    }

    /**
     * setAttackers set method, sets this attackers list to a list of all the
     * opposing pieces that can capture this piece
     * 
     * @param Attackers Array list of opposing pieces that can capture this piece
     */
    public void setAttackers(ArrayList<Piece> Attackers) {
        this.Attackers = Attackers;
    }

    /**
     * setDefenders set method, sets this defenders list to a list of all the same
     * colour pieces that can defend this piece
     * 
     * @param Defenders Array list of opposing pieces that can capture this piece
     */
    public void setDefenders(ArrayList<Piece> Defenders) {
        this.Defenders = Defenders;
    }

    /**
     * clone method is used to create exact copy of an object (ie copy of Piece
     * instance / object)
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * getValue accessor method
     * 
     * @return the generalized value indicating a pieces importance based on how
     *         effective each piece is within shogi
     */
    public int getValue() {
        return value;
    }

    /**
     * resetCaptureWith set method, sets this captureWith to empty array (if this
     * piece is capturable captureWith is used to indicate what opposing piece
     * should capture
     * this piece to result in best position)
     */
    public void resetCaptureWith() {
        captureWith = new ArrayList<Piece>();
    }

    /**
     * addCaptureWith method is used to add a opposing piece to this CaptureWith if
     * this piece is captureable (if this piece is capturable captureWith is used to
     * indicate what opposing piece / pieces should capture this piece to result in
     * best position)
     * 
     * @param piece Piece to add to this captureWith array
     */
    public void addCaptureWith(Piece piece) {
        this.captureWith.add(piece);
    }

    /**
     * getCaptureWith accessor method is used to retreive this captureWith arraylist
     * (if this piece is capturable captureWith is used to
     * indicate what opposing piece / pieces should capture this piece to result in
     * best position)
     * 
     * @return Array list indicating what opposing piece / pieces should capture
     *         this piece to result in best position)
     */
    public ArrayList<Piece> getCaptureWith() {
        return captureWith;
    }

    /**
     * setBoard set method, is used to set the board in which this piece resides
     * within
     * 
     * @param board_ the board in which this piece resides within
     */
    public void setBoard(Board board_) {
        this.board = board_;
    }
}
