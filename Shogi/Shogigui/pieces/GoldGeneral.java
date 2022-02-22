package Shogigui.pieces;

import Shogigui.Board;

public class GoldGeneral extends Piece {

    public GoldGeneral(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
        this.value=6;
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {

        if (!(this.isLegalMove(destination_x, destination_y))){
            return false;
        }

        // only allow the gold general...

        if (this.isBlack() == true) {

            return ((this.getY() == destination_y + 1 && Math.abs(destination_x - this.getX()) <= 1)
                    || (this.getY() == destination_y - 1 && (destination_x == this.getX()))
                    || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1)?true:false; 
        } else {
            return ((this.getY() == destination_y - 1 && Math.abs(destination_x - this.getX()) <= 1)
                    || (this.getY() == destination_y + 1 && (destination_x == this.getX()))
                    || this.getY() == destination_y && Math.abs(destination_x - this.getX()) == 1)? true:false;
        }
    }
}
