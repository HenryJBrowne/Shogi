package Shogigui.pieces;

import Shogigui.Board;

public class Rook extends Piece {

    public Rook(int x, int y, boolean is_white, String file_path, Board board,
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

        // promoted moves

        if (this.is_promoted() == true) {

            if (this.getX() != destination_x && this.getY() != destination_y
                    && (Math.abs(destination_y - this.getY()) == 1) && (Math.abs(destination_x - this.getX()) == 1)) {
                return true;
            }
        }

        // if it is trying to move somewhere not in a straight line, dont let it

        if (this.getX() != destination_x && this.getY() != destination_y) {
            return false;
        }

        // find out what direction we're trying to move

        String direction = this.getMoveDirection(destination_x, destination_y);  

        // whatever direction it is make sure there is nothing in the way       //++make more efficiant...

        if (direction.equals("S")) {
            int spaces_to_move = Math.abs(destination_y - this.getY());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX(), this.getY() + i);
                if (p != null) {
                    return false;
                }
            }
        }
        if (direction.equals("N")) {
            int spaces_to_move = Math.abs(destination_y - this.getY());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX(), this.getY() - i);
                if (p != null) {
                    return false;
                }
            }
        }
        if (direction.equals("W")) {
            int spaces_to_move = Math.abs(destination_x - this.getX());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX() - i, this.getY());
                if (p != null) {
                    return false;
                }
            }
        }
        if (direction.equals("E")) {
            int spaces_to_move = Math.abs(destination_x - this.getX());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX() + i, this.getY());
                if (p != null) {
                    return false;
                }
            }
        }

        return true;
    }
}
