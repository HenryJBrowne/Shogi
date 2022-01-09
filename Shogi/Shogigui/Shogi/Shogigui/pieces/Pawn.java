package Shogigui.pieces;

import Shogigui.Board;

// ++
// [MAKE SURE PAWN DROP CANNOT CAUSE INSTANT CHECK MATE... 
// AND PAWN CANNOT BE DROPPED IN SAME COLUMN AS A UN PROMOTED PAWN]
// ++

public class Pawn extends Piece {

    public Pawn(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
    }

    @Override
    public boolean canBeDropped(int destination_x, int destination_y) {

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null) {

            if (this.isWhite() && destination_y > 7 || this.isBlack() && destination_y < 1 || (this.moveIsOutOfBounds(destination_x, destination_y))) {
                return false;
            }

            for (int y = 0; y < board.getROWS(); y++) {
                if (board.getPiece(destination_x, y) != null) {
                    if (board.getPiece(destination_x, y).getClass().equals(Pawn.class)
                            && (board.getPiece(destination_x, y).isWhite() && this.isWhite()
                                    || board.getPiece(destination_x, y).isBlack() && this.isBlack())) {
                        return false;
                    }
                }
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

        // if the peice is captured allow it to be dropped anywhere if empty and can
        // move on next turn (Pawns cannot be dropped on same column as players own
        // pawns)

        if (this.canBeDropped(destination_x, destination_y)) {
            return true;
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

        if (this.is_promoted == false) {

            // if it is trying to move somewhere not in a straight line forward, more than 1
            // space forward or backwards dont let it

            if (this.getX() != destination_x) {
                return false;
            }

            if (this.isBlack() == true) {

                if (this.getY() > destination_y + 1 || this.getY() < destination_y) {
                    return false;
                }
            } else {
                if (this.getY() < destination_y - 1 || this.getY() > destination_y) {
                    return false;
                }
            }
        } else {

            // promoted moves

            if (this.isBlack() == true) {

                if ((this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y - 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1) {
                    return true;
                }

                else {
                    return false;
                }
            } else {
                if ((this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y + 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

}
