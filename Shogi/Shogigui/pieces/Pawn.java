package Shogigui.pieces;

import Shogigui.Board;

// >>> TO DO <<<
//
// MAKE SURE PAWN DROP CANNOT CAUSE INSTANT CHECK MATE... 
//

public class Pawn extends Piece {

    public Pawn(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {
        // A pawn may only move towards the oponent's side of the board.
        // It may only move one space.

        // Do not allow the peice to move outside the board

        if (destination_x > 8) {
            return false;
        }

        // if the peice is captured allow it to be dropped anywhere empty but not
        // somewhere it cannot move or some where in a row containing another pawn
        // +cannot drop into checkmate

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null) {
            if (this.isWhite()) {
                if (destination_y == 8) {
                    return false;
                }
                for (int x = 0; x < 9; x++) {
                    if (board.getPiece(x, destination_y) != null) {
                        if (board.getPiece(x, destination_y).getClass().equals(Pawn.class)
                                && board.getPiece(x, destination_y).isWhite()) {
                            return false;
                        }
                    }
                }
            }

            if (this.isBlack()) {
                if (destination_y == 0) {
                    return false;
                }
                for (int x = 0; x < 9; x++) {
                    if (board.getPiece(x, destination_y) != null) {
                        if (board.getPiece(x, destination_y).getFilePath() == "Pawn.png"
                                && board.getPiece(x, destination_y).isBlack()) {
                            return false;
                        }
                    }
                }
            }
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

        if (this.is_promoted == false) {

            // If it is trying to move somewhere not in a straight line forward, more than 1
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

            // Promoted moves...

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
