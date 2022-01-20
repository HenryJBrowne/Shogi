package Shogigui.pieces;

import Shogigui.Board;

public class Bishop extends Piece {

    public Bishop(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
        this.value=8;
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

            this.value=10;

            if ((this.getX() == destination_x && (Math.abs(destination_y - this.getY()) == 1))
                    || (this.getY() == destination_y && (Math.abs(destination_x - this.getX()) == 1))) {
                return true;
            }
        }
        else{
            this.value=8;
        }

        // if it is trying to move somewhere not in a diagonal line, dont let it

        if (this.getX() == destination_x || this.getY() == destination_y) {
            return false;
        }

        // if increase/decrease in x is not the same as increase/decrease in y

        if (Math.abs(destination_y - this.getY()) != Math.abs(destination_x - this.getX())) {
            return false;
        }

        // make sure their is nothing in the way of the move

        if (nothingBetweenPosAndMoveDest(destination_x, destination_y) == false) {
            return false;
        }

        return true;
    }
}
