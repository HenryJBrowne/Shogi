package Shogigui.pieces;

import Shogigui.Board;

public class GoldGeneral extends Piece {

    public GoldGeneral(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
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

        // only allow the gold general...

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
}
