package Shogigui.pieces;

import Shogigui.Board;

public class Lance extends Piece {

    public Lance(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
    }

    @Override
    public boolean canBeDropped(int destination_x, int destination_y) {

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null) {

            if (this.isWhite() && destination_y > 7 || this.isBlack() && destination_y < 1 || (this.moveIsOutOfBounds(destination_x, destination_y))) {
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

        // if the peice is captured allow it to be dropped anywhere if empty and can
        // move on next turn

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

        // promoted moves

        if (this.is_promoted() == true) {

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

        // whatever direction it is make sure there is nothing in the way

        if (this.isBlack() == false) {
            int spaces_to_move = Math.abs(destination_y - this.getY());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX(), this.getY() + i);
                if (p != null) {
                    return false;
                }
            }
        } else {
            int spaces_to_move = Math.abs(destination_y - this.getY());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX(), this.getY() - i);
                if (p != null) {
                    return false;
                }
            }
        }

        return true;
    }
}