package Shogigui.pieces;

import Shogigui.Board;

public class Lance extends Piece {

    public Lance(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
        this.value=3;
    }

    @Override
    public boolean canBeDropped(int destination_x, int destination_y) {

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null) {

            if (this.isWhite() && destination_y > 7 || this.isBlack() && destination_y < 1
                    || (this.moveIsOutOfBounds(destination_x, destination_y))) {
                return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {

        // do not allow the peice to move outside the board

        if (this.moveIsOutOfBounds(destination_x, destination_y)) {
            return false;
        }

        // if there is a piece at the destination, and it is our own, dont let us move
        // there

        if (this.moveIsOnTopOfOwnPiece(destination_x, destination_y)) {
            return false;
        }

        // dont allow piece to move if puts us in check

        if (this.moveChecksOwnKing(destination_x, destination_y)) {
            return false;
        }

        // promoted moves

        if (this.is_promoted() == true) {

            this.value=6;

            if (this.isBlack() == true) {

                return ((this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y - 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1)? true:false; 
            } else {
                return ((this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y + 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1)? true:false;
            }
        }
        else{
            this.value=3;
        }

        // if it is trying to move somewhere not in a straight line forward, dont let it

        if (this.getX() != destination_x) {
            return false;
        }

        if (this.isBlack() == true) {

            if (this.getY() < destination_y) {
                return false;
            }
        } else {
            if (this.getY() > destination_y) {
                return false;
            }
        }

        // make sure their is nothing in the way of the move

        if (nothingBetweenPosAndMoveDest(destination_x, destination_y) == false) {
            return false;
        }

        return true;
    }
}