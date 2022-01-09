package Shogigui.pieces;

import Shogigui.Board;

import java.util.ArrayList;

public class King extends Piece {

    public King(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
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

        // only allow the king to move 1 space

        if (Math.abs(destination_x - this.getX()) > 1 || Math.abs(destination_y - this.getY()) > 1) {
            return false;
        }

        // dont allow the king to move into space that puts it in check

        if (this.is_checking == false) {    // when checking to see if king is defending piece allow it to move into check

            ArrayList<Board.Coordinate> dangerousSquares;

            if (this.isWhite()) {
                dangerousSquares = board.getBlackAttackingSquares();
            } else {
                dangerousSquares = board.getWhiteAttackingSquares();
            }
            if (dangerousSquares != null) {

                for (Board.Coordinate square : dangerousSquares) {

                    if (destination_x == square.x && destination_y == square.y
                            || (board.getPiece(destination_x, destination_y) != null
                                    && board.getPiece(destination_x, destination_y).is_protected)) {

                        return false;
                    }

                }
            }
        }
            return true;

        
    }
    // @Override
    // public boolean canMoveToStopCheck(int destination_x, int destination_y) { }

}
