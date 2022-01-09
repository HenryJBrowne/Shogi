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

        if (this.checking_if_defender == false) { // when checking to see if king is defending piece allow it to move into check

            ArrayList<Board.Square> dangerousSquares;

            if (this.isWhite()) {
                dangerousSquares = board.getBlackAttackingSquares();
            } else {
                dangerousSquares = board.getWhiteAttackingSquares();
            }
            if (dangerousSquares != null) {

                for (Board.Square square : dangerousSquares) {

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

    @Override
    public boolean canMoveToStopCheck(int destination_x, int destination_y) {

        ArrayList<Piece> checkingPieces= (board.whiteIschecked())?board.getPiecesCheckingWhiteKing():board.getPiecesCheckingBlackKing(); 

        // check if king can capture piece, if piece is protected it cannot

        for (Piece p : checkingPieces) {

            if (destination_x == p.getX() && destination_y == p.getY()) {

                return (p.is_protected)? false: true;
            }

        }
        // check if king can move into space to get out of check

        if (this.canMove(destination_x, destination_y)) {

            // make sure it cannot move into a space that is in the path of the piece
            // checking it

            for (Piece p : checkingPieces) {

                // check if piece checking it can move multiple squares if so

                if (p.getClass() != Bishop.class && p.getClass() != Lance.class && p.getClass() != Rook.class) {
                    return true; // ++ fix extendability...
                } else {

                    // make sure it cannot move into a space that is in the path of the piece
                    // checking it

                    String direction = getMoveDirection(p.getX(),p.getY(),this.getX(), this.getY()); // ++ [test] fix efficiancy

                    ArrayList<Board.Square> pMovementRange = p.getMovementRange(direction);

                    if (pMovementRange != null) {

                        for (Board.Square square : pMovementRange) {

                            if (destination_x == square.x && square.y == destination_y) {

                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;

    }

}
