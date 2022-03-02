package Shogigui;

import Shogigui.Board.Square;
import Shogigui.pieces.*;
import java.io.*;
import java.util.*;
import Shogigui.pieces.Piece;

public class AssistedFeatures {

    private final String board_square_images_file_path = "images" + File.separator + "board" + File.separator
            + "assistedSquares" + File.separator;
    private final String good_square_file_path = board_square_images_file_path + "good_square.png";
    private final String great_square_file_path = board_square_images_file_path + "great_square.png";
    private final String amazing_square_file_path = board_square_images_file_path + "amazing_square.png";
    private final String bad_square_file_path = board_square_images_file_path + "bad_square.png";
    private final String checkmate_square_file_path = board_square_images_file_path + "checkmate_square.png";
    private final String opposing_checkmate_square_file_path = board_square_images_file_path
            + "opposing_checkmate_square.png";
    private final String drop_square_file_path = board_square_images_file_path + "drop_square.png";

    private final String board_images_file_path = "images" + File.separator + "board" + File.separator;
    private final String default_tutorial = board_images_file_path + "controls.png";
    private final String white_pieces_file_path = board_images_file_path + "white_pieces" + File.separator;
    private final String black_pieces_file_path = board_images_file_path + "black_pieces" + File.separator;
    private final String tutorial_white_pieces_file_path = white_pieces_file_path + "Tutorial" + File.separator;
    private final String tutorial_black_pieces_file_path = black_pieces_file_path + "Tutorial" + File.separator;
    private final String promoted_tutorial_white_pieces_file_path = white_pieces_file_path + "Tutorial" + File.separator
            + "Promoted" + File.separator;
    private final String promoted_tutorial_black_pieces_file_path = black_pieces_file_path + "Tutorial" + File.separator
            + "Promoted" + File.separator;

    private ArrayList<ImageFactory> Hints_Images;
    private ArrayList<ImageFactory> Arrow_Images;
    private ImageFactory Tutorial_Image;
    public Board board;

    private ArrayList<Square> getOutOfCheckMoveHints;
    private ArrayList<Square> getOutOfCheckDropHints;

    private ArrayList<Piece> capturablePieces;

    private ArrayList<Arrow> arrows;
    private ArrayList<Arrow> getOutOfCheckArrows;
    private ArrayList<Arrow> checkmateArrows;

    private ArrayList<Square> checkMateMoves = null;

    /**
     * The AssistedFeatures constuctor method creates a new assisted features object
     * respective the board
     * 
     * @param board The board AssistedFeatures object can be used to generate
     *              assisted features for
     */
    public AssistedFeatures(Board board) {
        this.board = board;
    }

    /**
     * The generateAssistedImages generates an array list of images that can contain
     * hints (via coloured squares and arrow) and tutorials that can asssist the
     * user on a given board
     * 
     * @return Array list of images that if displayed on the board specified in this
     *         instance assist the user via move hints and tutorials
     */
    public ArrayList<ImageFactory> generateAssistedImages() {

        Hints_Images = new ArrayList<ImageFactory>();
        Arrow_Images = new ArrayList<ImageFactory>();

        Tutorial_Image = new ImageFactory(default_tutorial, board.getSquare_Width() * 9, board.getSquare_Width() * 2.6);

        Tutorial_Image = (!(board.checkForCheckMate() || board.whiteIschecked() || board.blackIschecked()
                || board.PromotionButtonOn()))
                        ? new ImageFactory(default_tutorial, board.getSquare_Width() * 9, board.getSquare_Width() * 2.6)
                        : null;

        if (board.getActivePiece() != null) {

            // set tutorial image if a piece is clicked to image corresponding to piece (if
            // tutorials are on)

            if (board.TutorialOn() && board.checkForCheckMate() == false && board.PromotionButtonOn() == false) {

                String tutorial_file_path = default_tutorial;

                if (/* PlayerIsWhite && */board.getActivePiece().isWhite()) {
                    tutorial_file_path = (!board.getActivePiece().is_promoted())
                            ? tutorial_white_pieces_file_path + board.getActivePiece().getFilePath()
                            : promoted_tutorial_white_pieces_file_path + board.getActivePiece().getFilePath();
                } else if ((/* PlayerIsWhite==false && */board.getActivePiece().isBlack())) {
                    tutorial_file_path = (!board.getActivePiece().is_promoted())
                            ? tutorial_black_pieces_file_path + board.getActivePiece().getFilePath()
                            : promoted_tutorial_black_pieces_file_path + board.getActivePiece().getFilePath();
                }

                Tutorial_Image = new ImageFactory(tutorial_file_path, board.getSquare_Width() * 9,
                        board.getSquare_Width() * 2.6);
            }
        }

        // add to hints array get out of check hints (if player hints are turned on and
        // player is checked)

        if (board.hintsON() && board.checkForCheckMate() == false) {

            // (if a king is checked) add get out of check hints

            updateCaptureablePieces();

            if ((board.whiteIschecked() && board.PlayerIsWhite())
                    || (board.blackIschecked() && board.PlayerIsWhite() == false)) {

                updateGetOutOfCheckHints();

                if (getOutOfCheckMoveHints != null) {

                    for (Square moveSquare : getOutOfCheckMoveHints) {

                        if (board.getPiece(moveSquare.x, moveSquare.y) != null) {
                            Hints_Images.add(new ImageFactory(great_square_file_path,
                                    board.getSquare_Width() * moveSquare.x, board.getSquare_Width() * moveSquare.y));
                        } else {
                            Hints_Images.add(new ImageFactory(good_square_file_path,
                                    board.getSquare_Width() * moveSquare.x, board.getSquare_Width() * moveSquare.y));
                        }
                    }
                    for (Arrow arrow : getOutOfCheckArrows) {
                        Arrow_Images.add(new ImageFactory(arrow.getFilePath(),
                                board.getSquare_Width() * arrow.getX(), board.getSquare_Width() * arrow.getY()));
                    }

                }
                if (getOutOfCheckDropHints != null) {

                    for (Square dropSquare : getOutOfCheckDropHints) {
                        Hints_Images.add(new ImageFactory(drop_square_file_path,
                                board.getSquare_Width() * dropSquare.x, board.getSquare_Width() * dropSquare.y));
                    }
                }
            }
            // (if king is not checked)
            else {

                // add capturable pieces hints
                if (capturablePieces != null) {
                    for (Piece captureablePiece : capturablePieces) {

                        if (((board.PlayerIsWhite() && captureablePiece.isWhite() == false)
                                || (board.PlayerIsWhite() == false && captureablePiece.isWhite() == true))) {

                            Hints_Images.add(new ImageFactory(great_square_file_path,
                                    board.getSquare_Width() * captureablePiece.getX(),
                                    board.getSquare_Width() * captureablePiece.getY()));
                        }

                        if (((board.PlayerIsWhite() && captureablePiece.isWhite())
                                || (board.PlayerIsWhite() == false && captureablePiece.isWhite() == false))) {
                            Hints_Images.add(new ImageFactory(bad_square_file_path,
                                    board.getSquare_Width() * captureablePiece.getX(),
                                    board.getSquare_Width() * captureablePiece.getY()));
                        }
                    }
                    if (arrows != null) {
                        for (Arrow arrow : arrows) {

                            Arrow_Images.add(new ImageFactory(arrow.getFilePath(),
                                    board.getSquare_Width() * arrow.getX(), board.getSquare_Width() * arrow.getY()));
                        }
                    }
                }

                // add to hints array all good, great and amazing possible moves when a piece is
                // clicked (if not checked and hints setting is turned on)

                if (board.getActivePiece() != null && ((board.getActivePiece().isWhite() && board.PlayerIsWhite())
                        || (board.getActivePiece().isBlack() && board.PlayerIsWhite() == false)
                                && calcSafeMovesAndDrops(board.getActivePiece()) != null)
                        && board.hintsON() && board.checkForCheckMate() == false) {

                    for (Square square : calcSafeMovesAndDrops(board.getActivePiece())) {

                        Hints_Images
                                .add(new ImageFactory(good_square_file_path, board.getSquare_Width() * square.x,
                                        board.getSquare_Width() * square.y));
                    }

                    if (calcGreatMovesAndDrops(board.getActivePiece()) != null) {
                        for (Square square : calcGreatMovesAndDrops(board.getActivePiece())) {

                            Hints_Images
                                    .add(new ImageFactory(great_square_file_path, board.getSquare_Width() * square.x,
                                            board.getSquare_Width() * square.y));
                        }
                    }
                    if (calcAmazingMovesAndDrops(board.getActivePiece()) != null) {
                        for (Square square : calcAmazingMovesAndDrops(board.getActivePiece())) {

                            Hints_Images
                                    .add(new ImageFactory(amazing_square_file_path, board.getSquare_Width() * square.x,
                                            board.getSquare_Width() * square.y));
                        }
                    }
                }

                // add to hints array possible checkmate hints

                boolean possibleCheckMate = (board.PlayerIsWhite()) ? checkForCheckMateMoves(board.getWhitePieces())
                        : checkForCheckMateMoves(board.getBlackPieces());

                if (possibleCheckMate && checkMateMoves != null) {
                    for (Square checkMateSquare : checkMateMoves) {
                        Hints_Images.add(new ImageFactory(checkmate_square_file_path,
                                board.getSquare_Width() * checkMateSquare.x,
                                board.getSquare_Width() * checkMateSquare.y));
                    }
                    for (Arrow arrow : checkmateArrows) {
                        Arrow_Images.add(new ImageFactory(arrow.getFilePath(),
                                board.getSquare_Width() * arrow.getX(), board.getSquare_Width() * arrow.getY()));
                    }
                }

                // add to hints arry possible opposing checkmate hints

                boolean opposingPossibleCheckMate = (board.PlayerIsWhite())
                        ? checkForCheckMateMoves(board.getBlackPieces())
                        : checkForCheckMateMoves(board.getWhitePieces());

                if (opposingPossibleCheckMate && checkMateMoves != null) {
                    for (Square checkMateSquare : checkMateMoves) {
                        Hints_Images.add(new ImageFactory(opposing_checkmate_square_file_path,
                                board.getSquare_Width() * checkMateSquare.x,
                                board.getSquare_Width() * checkMateSquare.y));
                    }
                    for (Arrow arrow : checkmateArrows) {
                        arrow.setCol("black");
                        Arrow_Images.add(new ImageFactory(arrow.getFilePath(),
                                board.getSquare_Width() * arrow.getX(), board.getSquare_Width() * arrow.getY()));
                    }
                }
            }
        }

        ArrayList<ImageFactory> assisted_Images = Hints_Images;
        assisted_Images.addAll(Arrow_Images);
        assisted_Images.add(Tutorial_Image);

        return assisted_Images;
    }

    /**
     * The updateCaptureablePieces method iterates through all the pieces in the
     * game (within the All_Pieces array) to calculate if a piece is capturable: if
     * the capture of the piece results in giving the player an advanced position;
     * the capturablePieces array list is update accordingly
     */
    void updateCaptureablePieces() { // [BUG] doesnt show capturable pieces at start of game // ++ make more
        // efficiant and optimize??

        capturablePieces = new ArrayList<Piece>(); // ++ test xray defeners and attackers exchanges with piece values

        // ++ remove duplicates

        // check for capturable pieces, add to list and update Jlabel text accordingly

        for (Piece piece : board.getAllPieces()) {

            calcCaptureablePiece(piece);

        }

        if (capturablePieces.isEmpty()) {
            capturablePieces = null;
        } else {
            updateCaptureableArrowHints();
        }
    }

    /**
     * The calcCaptureablePiece method is used to calculate if a piece is
     * capturable: if the capture of the piece results in giving the player an
     * advanced position; the capturablePieces array list is update accordingly
     * 
     * @param piece The piece the method checks if it is capturable
     */
    public void calcCaptureablePiece(Piece piece) {

        // check if piece is unprotected and can be captured

        piece.resetCaptureWith();

        if (piece.getAttackers().size() > 0 && piece.is_protected() == false) {

            capturablePieces.add(piece);

            return;
        }

        // check if lower value piece can capture highter value piece (sacrafice)

        if ((piece.getAttackers().size() > 0)) {

            for (Piece attacker : piece.getAttackers()) {

                if (attacker.getValue() < piece.getValue()) {

                    capturablePieces.add(piece);

                    piece.addCaptureWith(attacker);

                    return;
                }

            }

        }

        // calculate capturable pieces depending on exchange value calculated from the
        // exchange resultant if all the pieces attackers capture the piece and all its
        // following defenders

        int exchangeValue = -1;

        // test
        piece.setExchangeValue(0);

        // calc xray attackersDefenders && xray Defenders

        ArrayList<Piece> Defenders = piece.getDefenders();
        ArrayList<Piece> Attackers = piece.getAttackers();

        ArrayList<Piece> exchangeDefenders = getXrayDefenders(piece);
        ArrayList<Piece> exchangeAttackers = getXrayAttackers(piece);

        exchangeDefenders.addAll(Defenders);
        exchangeAttackers.addAll(Attackers);

        if (exchangeAttackers != null && exchangeAttackers.size() > 0 && exchangeDefenders != null
                && exchangeDefenders.size() > 0 && exchangeAttackers.size() >= exchangeDefenders.size() + 1) {

            exchangeValue = piece.getValue();
            for (int x = 0; x < exchangeDefenders.size() - 1; x = x + 1) { // ++order list lowest values first??
                exchangeValue = exchangeValue - exchangeAttackers.get(x).getValue();
            }
            for (Piece defender : exchangeDefenders) {
                exchangeValue = exchangeValue + defender.getValue();

            }
        }

        // updatePiecesAttackersAndDefenders();

        piece.setExchangeValue(exchangeValue);

        if (exchangeValue >= 0) {
            capturablePieces.add(piece);
            return;
        }

    }

    /**
     * The getXrayDefenders method is used to determin if a defending piece of a
     * specified piece has pieces on the same axis that can move to defend this
     * piece if the defending piece was exchanged
     * 
     * @param piece The piece the method checks for xray defenders / defenders that
     *              can defend the piece if the current defender is exchanged
     * @return Array list of xray defenders
     */
    public ArrayList<Piece> getXrayDefenders(Piece piece) {

        ArrayList<Piece> xRayDefenders = new ArrayList<Piece>();

        for (Piece defender : piece.getDefenders()) {

            ArrayList<Square> axis = piece.getSquaresInDirection(
                    Piece.getMoveDirection(piece.getX(), piece.getY(), defender.getX(), defender.getY()));

            if (axis != null && board.getSquare(defender.getX(), defender.getY()) != null
                    && board.getSquare(defender.getX(), defender.getY()).isContainedWithin(axis)) {

                // for each square tending towards the outbounds squares of the board grid in
                // the direction of the piece towards its defender, if the square contains a
                // piece and the piece boolean negates the statement: is of opposing colour and
                // the piece is not in its movement range, end the loop, if not add to array and
                // continue iterating through squares tending towards squares out of the board
                // grid boundaries

                for (Square square : axis) {

                    Piece possibleDefender = board.getPiece(square.x, square.y);

                    if (possibleDefender != null && possibleDefender != defender
                            && possibleDefender.isRangedAttacker()
                            && ((possibleDefender.isBlack() && piece.isBlack())
                                    || (possibleDefender.isWhite() && piece.isWhite()))) {

                        if (possibleDefender.getRangedAttackDirections().contains(Piece.getMoveDirection(
                                possibleDefender.getX(), possibleDefender.getY(), piece.getX(), piece.getY()))) {

                            xRayDefenders.add(possibleDefender);

                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return xRayDefenders;
    }

    /**
     * The getXrayAttackers method is used to determin if a attacking piece of a
     * specified piece has pieces on the same axis that can move to attack this
     * piece if the attacking piece was exchanged
     * 
     * @param piece The piece the method checks for xray attackers / attackers that
     *              can attack the piece if the current attacker is exchanged
     * @return Array list of xray defenders
     */
    public ArrayList<Piece> getXrayAttackers(Piece piece) {

        ArrayList<Piece> getXrayAttackers = new ArrayList<Piece>();

        for (Piece attacker : piece.getAttackers()) {

            ArrayList<Square> axis = piece.getSquaresInDirection(
                    Piece.getMoveDirection(piece.getX(), piece.getY(), attacker.getX(), attacker.getY()));

            if (axis != null && board.getSquare(attacker.getX(), attacker.getY()) != null
                    && board.getSquare(attacker.getX(), attacker.getY()).isContainedWithin(axis)) {

                // for each square tending towards the outbounds squares of the board grid in
                // the direction of the piece towards its attacker, if the square contains a
                // piece and the piece boolean negates the statement: is of same colour and the
                // piece is not its movement range, end the loop, if not add to array and
                // continue iterating through squares tending towards squares out of the board
                // grid boundaries

                for (Square square : axis) {

                    Piece possibleAttacker = board.getPiece(square.x, square.y);

                    if (possibleAttacker != null && possibleAttacker != attacker
                            && possibleAttacker.isRangedAttacker()
                            && ((possibleAttacker.isWhite() && piece.isBlack())
                                    || (possibleAttacker.isBlack() && piece.isWhite()))) {

                        if (possibleAttacker.getRangedAttackDirections().contains(Piece.getMoveDirection(
                                possibleAttacker.getX(), possibleAttacker.getY(), piece.getX(), piece.getY()))) {

                            getXrayAttackers.add(possibleAttacker);

                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return getXrayAttackers;
    }

    /**
     * The updateCaptureableArrowHints method iterates through all the captureable
     * pieces in the game (within the capturablePieces array) calcualtes the arrows
     * positions and directions that indicate where the user should move pieces to
     * capture enemy capturable pieces (also contained within the captureable pieces
     * array)
     */
    public void updateCaptureableArrowHints() {

        arrows = new ArrayList<Arrow>();

        for (Piece capturablePiece : capturablePieces) {

            if ((capturablePiece.isBlack() && board.PlayerIsWhite())
                    || (capturablePiece.isWhite() && board.PlayerIsWhite() == false)) {
                        
                // add arrows to pieces toward possible opposing piece capture

                if (capturablePiece.getCaptureWith().isEmpty() == false) {

                    for (Piece attacker : capturablePiece.getCaptureWith()) {

                        arrows.add(new Arrow(Piece.getMoveDirection(attacker.getX(), attacker.getY(),
                                capturablePiece.getX(), capturablePiece.getY()), attacker.getX(), attacker.getY(),
                                "green"));
                    }
                } else {
                    for (Piece attacker : capturablePiece.getAttackers()) {

                        arrows.add(new Arrow(Piece.getMoveDirection(attacker.getX(), attacker.getY(),
                                capturablePiece.getX(), capturablePiece.getY()), attacker.getX(), attacker.getY(),
                                "green"));
                    }
                }
            }
        }

    }

    /**
     * The updateGetOutOfCheckHints method is used to calculate good moves that
     * result in the player no longer being check, the array lists containing the
     * squares and the arrows to the squares (indicating the move) are updated
     * accordingly
     */
    public void updateGetOutOfCheckHints() { // ++ optimize

        getOutOfCheckMoveHints = new ArrayList<Square>();
        getOutOfCheckDropHints = new ArrayList<Square>();

        getOutOfCheckArrows = new ArrayList<Arrow>();

        int index = 0;

        if (board.getOutOfcheckMoves() != null) {

            for (Square square : board.getOutOfcheckMoves()) {

                Piece piece = board.getOutOfcheckMovePiece().get(index);

                if (piece.getClass() == King.class && piece.canMove(square.x, square.y)) {
                    getOutOfCheckMoveHints.add(square);
                }

                else if (moveIsSafe(piece, square.x, square.y)) {
                    getOutOfCheckMoveHints.add(square);

                } else if (board.getPiece(square.x, square.y) != null
                        && capturablePieces.contains(board.getPiece(square.x, square.y))) {
                    getOutOfCheckMoveHints.add(square);
                }

                if (getOutOfCheckMoveHints.contains(square) && (board.getPiece(square.x, square.y) != null)) {// test
                    getOutOfCheckArrows
                            .add(new Arrow(Piece.getMoveDirection(piece.getX(), piece.getY(), square.x, square.y),
                                    piece.getX(), piece.getY(), "green"));
                } else if (getOutOfCheckMoveHints.contains(square)) {
                    getOutOfCheckArrows
                            .add(new Arrow(Piece.getMoveDirection(piece.getX(), piece.getY(), square.x, square.y),
                                    piece.getX(), piece.getY(), "black"));
                }

                index = index + 1;
            }
        }

        index = 0;

        if (board.getOutOfCheckDrops() != null) {

            for (Square square : board.getOutOfCheckDrops()) {

                Piece piece = board.getOutOfCheckDropPiece().get(index);

                if (moveIsSafe(piece, square.x, square.y)) {
                    getOutOfCheckDropHints.add(square);
                }
                index = index + 1;
            }
        }
    }

    /**
     * The calcSafeSquares method is used to calculate all the squares a piece could
     * move to that a piece could move that if captured on the square would not
     * result in the opposing colour players advantage
     * 
     * @param piece The piece the method checks for all moves are the squares the
     *              piece could move to safe
     * @return Array list of square that the piece could move to without giving the
     *         opposing colour player an advantage
     */
    public ArrayList<Square> calcSafeMovesAndDrops(Piece piece) {

        ArrayList<Square> safeSquares = new ArrayList<Square>();

        if (piece.getMovementRange() != null) {

            for (Square square : piece.getMovementRange()) {

                if (moveIsSafe(piece, square.x, square.y)) {
                    safeSquares.add(square);
                }
            }
        }

        return safeSquares;
    }

    /**
     * The moveIsSafe method is used to check if the piece was captured on the
     * square would it result in the opposing colour players advantage, if so the
     * square is not safe
     * 
     * @param piece The piece the method checks if it was on / moved to the
     *              specified square would it be safe
     * @param x     The location of the square position on the grids x axis
     * @param y     The location of the square position on the grids y axis
     * @return True of the piece could move to specified square without giving the
     *         opposing colour player an advantage (the piece moving to the square
     *         is a safe move), false if not
     */
    public boolean moveIsSafe(Piece piece, int x, int y) {

        if (board.getPiece(x, y) != null && board.getPiece(x, y).getCaptureWith() != null) {
            if (board.getPiece(x, y).getCaptureWith().contains(piece) == false) {
                return false;
            }
        }

        int prevX = piece.getX();
        int prevY = piece.getY();
        boolean rtrn;
        ArrayList<Piece> prevCapturablePieces = capturablePieces;
        capturablePieces = new ArrayList<Piece>();

        piece.setX(x);
        piece.setY(y);

        board.updateBoardStatus();

        calcCaptureablePiece(piece);

        if (capturablePieces != null && capturablePieces.contains(piece)) {
            rtrn = false;
        } else {
            rtrn = true;
        }
        piece.setX(prevX);
        piece.setY(prevY);

        board.updateBoardStatus();

        capturablePieces = prevCapturablePieces;

        return rtrn;
    }

    /**
     * The calcGreatMovesAndDrops method is used to calculate the squares that if a
     * specified piece move to (legally), it would result the player who made the
     * moves advantage (the pieces move threatens another piece and if the piece was
     * captured on the square it moves to it would not result in the opposing player
     * advantage)
     * 
     * @param piece The piece the method checks if it was on / moved to the
     *              specified square would it result the player who makes the moves
     *              advantage
     * @return True if the players who makes the move / drop gains an advantage
     *         after making the move, false if not
     */
    public ArrayList<Square> calcGreatMovesAndDrops(Piece piece) {

        ArrayList<Square> greatMoveSquares = new ArrayList<Square>();

        if (piece.getMovementRange() != null) {

            for (Square square : piece.getMovementRange()) {

                if (moveThreatensPiece(piece, square.x, square.y) != null
                        && moveThreatensPiece(piece, square.x, square.y).isEmpty() == false
                        && moveIsSafe(piece, square.x, square.y)) {
                    greatMoveSquares.add(square);
                }
            }
        }
        return greatMoveSquares;
    }

    /**
     * The calcAmazingMovesAndDrops calculates amazing moves; the squares that if a
     * specified piece move to (legally) it would result the player
     * who made the moves signficant advantage (the pieces move threatens two piece
     * or more and if the piece was captured on the square it moves to it would not
     * result in the opposing player advantage). By threatening two or more pieces;
     * if a square / squares are returned, if the piece moved to these square it
     * would result in a capture.
     * 
     * @param piece The piece the method checks if it was on / moved to the
     *              specified square would it result the player who makes the moves
     *              signficant advantage
     * @return True if the players who makes the move / drop gains an signficant
     *         advantage after making the move, false if not
     */
    public ArrayList<Square> calcAmazingMovesAndDrops(Piece piece) {

        ArrayList<Square> amazingMoveSquares = new ArrayList<Square>();

        if (piece.getMovementRange() != null) {

            for (Square square : piece.getMovementRange()) {

                ArrayList<Piece> threatenedPieces = moveThreatensPiece(piece, square.x, square.y);

                if (threatenedPieces != null && threatenedPieces.size() > 1 && moveIsSafe(piece, square.x, square.y)) {
                    amazingMoveSquares.add(square);
                }
            }
        }
        return amazingMoveSquares;
    }

    // ++update return and name?

    /**
     * The moveThreatensPiece method is used to check if a piece moved to a
     * specified location would it result in an opposing piece becoming capturable
     * by a piece (if a certain piece now captured the opposing would it result in
     * the player who moved to capture advancing in position)
     * 
     * @param piece The piece the method checks if it moved to a specified location
     *              would it threaten an opposing piece
     * @param x     The location of the square position on the grids x axis
     * @param y     The location of the square position on the grids y axis
     * @return True if the piece moving to the specified position would result in an
     *         enemy piece being threatened, false if not
     */
    public ArrayList<Piece> moveThreatensPiece(Piece piece, int x, int y) {

        ArrayList<Piece> threatenedPiece = new ArrayList<Piece>();

        ArrayList<Piece> prevCapturablePieces = capturablePieces;
        capturablePieces = new ArrayList<Piece>();
        int prevX = piece.getX();
        int prevY = piece.getY();
        piece.setX(x);
        piece.setY(y);

        board.updateBoardStatus();

        updateCaptureablePieces();

        if (capturablePieces != null) {

            if (prevCapturablePieces != null) {
                capturablePieces.removeAll(prevCapturablePieces);
            }

            for (Piece capturablePiece : capturablePieces) {

                if ((capturablePiece.isWhite() && piece.isBlack()) || (capturablePiece.isBlack() && piece.isWhite())) {

                    threatenedPiece.add(capturablePiece);
                }
            }

        }
        piece.setX(prevX);
        piece.setY(prevY);

        board.updateBoardStatus();

        capturablePieces = prevCapturablePieces;

        return threatenedPiece;
    }

    /**
     * The checkForCheckMatesMoves iterates through an array list of pieces and
     * checks / calculates for each pieces move in the pieces movement range wether
     * the move would result in checkmate
     * 
     * @param pieces Array list of pieces that the method iterates through and
     *               calculates for each piece if it can move to checkmate
     * @return true if their is a possible checkmate move within the movement range
     *         of the pieces, false if not
     */
    public boolean checkForCheckMateMoves(ArrayList<Piece> pieces) { // ++ add promotion checkmates //++ if piece
        // blocking checkmate moves is it checkmate?4
        // ++ TEST

        checkMateMoves = new ArrayList<Square>();
        checkmateArrows = new ArrayList<Arrow>(); // check if piece that blocks checkmate can be taken resulting in next
        // move checkmate

        if (board.getOpposingKing(pieces).getMovementRange() == null
                || board.getOpposingKing(pieces).getMovementRange().size() <= 4) { // check if a move can block more
                                                                                   // than 4
            // king moves and check for check mate?

            for (Piece piece : pieces) {

                if (piece.getMovementRange() != null) {

                    for (Square possibleMove : piece.getMovementRange()) {

                        if (piece.isCheckMateMove(possibleMove.x, possibleMove.y)
                                && !(piece.getClass() == Pawn.class && piece.is_captured())) { // check pawn is not
                            // being dropped into
                            // checkmate ++ fix
                            // extendability
                            checkMateMoves.add(possibleMove);
                            checkmateArrows.add(new Arrow(
                                    Piece.getMoveDirection(piece.getX(), piece.getY(), possibleMove.x, possibleMove.y),
                                    piece.getX(), piece.getY(), "gold")); // change to gold arrow
                        }
                    }
                }
            }
        }

        if (checkMateMoves.isEmpty()) {
            checkMateMoves = null;
            return false;
        } else {
            return true;
        }
    }
}
