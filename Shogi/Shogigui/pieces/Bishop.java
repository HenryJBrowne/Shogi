package Shogigui.pieces;

import Shogigui.Board;

/**
 * The Bishop class is used to generate Bishop Piece objects within the program: 
 * with all the properties corresponding to this piece
 * 
 * @Author Henry Browne – 37733273
 */
public class Bishop extends Piece {

    public Bishop(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
        this.value=8;
        this.rangedAttackDirections.add("NE");this.rangedAttackDirections.add("NW");this.rangedAttackDirections.add("SE");this.rangedAttackDirections.add("SW");
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {

        if (!(this.isLegalMove(destination_x, destination_y))){
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
