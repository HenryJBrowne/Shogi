package Shogigui.pieces;

import Shogigui.Board;

public class SilverGeneral extends Piece {

    public SilverGeneral(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {
        // A Silver General can move ...
        //
        //

        // Do not allow the peice to move outside the board

        if (destination_x > 8) {
            return false;
        }

        // if the peice is captured allow it to be dropped anywhere if empty

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null) {
            return true;
        }

        // If there is a piece at the destination, and it is our own, dont let us move
        // there ++...

        if (this.is_checking == false) {

            Piece possiblePiece = board.getPiece(destination_x, destination_y);

            if (possiblePiece != null) {
                if (possiblePiece.isWhite() && this.isWhite()) {
                    return false;
                }
                if (possiblePiece.isBlack() && this.isBlack()) {
                    return false;
                }
            }
        }

        // promoted moves

        if (this.is_promoted() == true) {

            if (this.isBlack() == true) {

                if (((this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) == 1))
                        && (this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y - 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1) {
                    return true;
                }

                else {
                    return false;
                }
            } else {
                if (((this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) == 1))
                        && (this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y + 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        // Only allow the silver general to move if it moves 1 forward in the y and the
        // change in the x is 0 or 1 or if it moves 1 back and the change in the x is
        // equal to 1

        if (this.isBlack() == true) {

            if ((this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                    || (this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) == 1)) {
                return true;
            }

        } else {
            if ((this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) <= 1)
                    || (this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) == 1)) {
                return true;
            }
        }

        return false;
    }
}
