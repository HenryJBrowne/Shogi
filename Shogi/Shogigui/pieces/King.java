package Shogigui.pieces;

import Shogigui.Board;

public class King extends Piece {

    public King(int x, int y, boolean is_white, String file_path, Board board,
            Boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {
        // A king can move one square up, right, left, or down, or
        // diagonally, but he can never put himself in danger of an oposing
        // piece attacking him on the next turn. He cannot attack his own pieces.

        // Do not allow the peice to move outside the board

        if (destination_x > 8) {
            return false;
        }

        // if the peice is captured allow it to be dropped anywhere if empty

        if (this.is_captured == true && board.getPiece(destination_x, destination_y) == null) {
            return true;
        }

        // If there is a piece at the destination, and it is our own, dont let us move
        // there

        Piece possiblePiece = board.getPiece(destination_x, destination_y);

        if (possiblePiece != null) {
            if (possiblePiece.isWhite() && this.isWhite()) {
                return false;
            }
            if (possiblePiece.isBlack() && this.isBlack()) {
                return false;
            }
        }

        // only allow the king to move 1 space in a

        if (Math.abs(destination_x - this.getX()) > 1 || Math.abs(destination_y - this.getY()) > 1) {
            return false;
        }

        return true;

    }
}
