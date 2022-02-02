package Shogigui.pieces;

import Shogigui.Board;

public class Rook extends Piece {

    public Rook(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
        this.value=10;
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

            this.value=12;

            if (this.getX() != destination_x && this.getY() != destination_y
                    && (Math.abs(destination_y - this.getY()) == 1) && (Math.abs(destination_x - this.getX()) == 1)) {
                return true;
            }
        }
        else{
            this.value=10;
        }

        // if it is trying to move somewhere not in a straight line, dont let it

        if (this.getX() != destination_x && this.getY() != destination_y) {
            return false;
        }

        // make sure their is nothing in the way of the move

        if (nothingBetweenPosAndMoveDest(destination_x,destination_y)==false){
            return false;
        }

        return true;
    }
}
