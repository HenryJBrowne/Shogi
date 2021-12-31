package Shogigui.pieces;

import Shogigui.Board;

public class Rook extends Piece {

    public Rook(int x, int y, boolean is_white, String file_path, Board board,
            boolean is_promoted) {
        super(x, y, is_white, file_path, board, is_promoted);
    }

    @Override
    public boolean canMove(int destination_x, int destination_y) {
        // A rook can move as many squares as he wants either forward,
        // backward, or sideways without jumping any pieces. He cannot attack
        // his own pieces.

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

        // promoted moves...

        if (this.is_promoted() == true) {

            if (this.getX() != destination_x && this.getY() != destination_y
                    && (Math.abs(destination_y - this.getY()) == 1) && (Math.abs(destination_x - this.getX()) == 1)) {
                return true;
            }
        }

        // If it is trying to move somewhere not in a straight line, dont let it

        if (this.getX() != destination_x && this.getY() != destination_y) {
            return false;
        }

        // Find out what direction we're trying to move

        String direction = "";

        if (destination_y > this.getY()) {
            direction = "south";
        }
        if (destination_y < this.getY()) {
            direction = "north";
        }
        if (destination_x > this.getX()) {
            direction = "east";
        }
        if (destination_x < this.getX()) {
            direction = "west";
        }

        // Whatever direction it is make sure there is nothing in the way

        if (direction.equals("south")) {
            int spaces_to_move = Math.abs(destination_y - this.getY());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX(), this.getY() + i);
                if (p != null) {
                    return false;
                }
            }
        }
        if (direction.equals("north")) {
            int spaces_to_move = Math.abs(destination_y - this.getY());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX(), this.getY() - i);
                if (p != null) {
                    return false;
                }
            }
        }
        if (direction.equals("west")) {
            int spaces_to_move = Math.abs(destination_x - this.getX());
            for (int i = 1; i < spaces_to_move; i++) {
                Piece p = board.getPiece(this.getX() - i, this.getY());
                if (p != null) {
                    return false;
                }
            }
        }
        if (direction.equals("east")) {
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
