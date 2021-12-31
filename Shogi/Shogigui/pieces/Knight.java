package Shogigui.pieces;

import Shogigui.Board;

public class Knight extends Piece {

    public Knight(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {
        // A knight can move in any L shape forward and can jump over anyone
        // in order to do so. He cannot attack his own pieces.
        // By an L shape, I mean it can move to a square that is 2 squares away
        // horizontally and 1 square away vertically, or 1 square away horizontally
        // and 2 squares away vertically.

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

                if ((Math.abs(destination_x - this.getX()) == 1 && Math.abs(destination_y - this.getY()) == 2&&destination_y<this.getY())||(this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y - 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1) {
                    return true;
                }

                else {
                    return false;
                }
            } else {
                if (((Math.abs(destination_x - this.getX()) == 1 && Math.abs(destination_y - this.getY()) == 2&&destination_y>this.getY())||this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) <= 1)
                        || (this.getY() == destination_y + 1 && (destination_x == this.getX()))
                        || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        // the knight can only move forward
       
        if (this.isBlack() == true) {
            if (destination_y>this.getY()){
                return false;
            }
        }
        else{
            if (destination_y<this.getY()){
                return false;
            }
        }


        // if the knight moves two squares in the y and moves 1 square in the x its a
        // valid move

        if (Math.abs(destination_x - this.getX()) == 1 && Math.abs(destination_y - this.getY()) == 2) {
            return true;
        } 

       
        return false;
    }
}
