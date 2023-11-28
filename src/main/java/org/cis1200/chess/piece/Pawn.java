package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class Pawn extends Piece {
    public Pawn(PieceColor color) {
        super(color);
    }

    @Override
    public MoveLegality getLegality(Board board, Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();

        int fileDiff = Math.abs(from.getFile().getIndex() - to.getFile().getIndex());
        int rankDiff = to.getRank().getIndex() - from.getRank().getIndex();

        try {

            // Check white moves
            if (getColor() == PieceColor.White && rankDiff > 0) {
                if (fileDiff == 1 && rankDiff == 1) {
                    if (board.getPieceExists(to)) {
                        return MoveLegality.Legal;
                    } else {
                        return MoveLegality.PawnNoPieceToCapture;
                    }
                } else if (fileDiff == 0) {
                    boolean blocked = board.getPieceExists(from.offsetBy(1, 0));
                    if (blocked) {
                        return MoveLegality.MovementBlocked;
                    } else if (rankDiff == 1) {
                        return MoveLegality.Legal;
                    } else if (rankDiff == 2 && from.getRank().getIndex() == 1) {
                        blocked = board.getPieceExists(from.offsetBy(2, 0));
                        if (blocked) {
                            return MoveLegality.MovementBlocked;
                        } else {
                            return MoveLegality.Legal;
                        }
                    }
                }
            }

            // Check black moves
            else if (getColor() == PieceColor.Black && rankDiff < 0) {
                if (fileDiff == 1 && rankDiff == -1) {
                    if (board.getPieceExists(to)) {
                        return MoveLegality.Legal;
                    } else {
                        return MoveLegality.PawnNoPieceToCapture;
                    }
                } else if (fileDiff == 0) {
                    boolean blocked = board.getPieceExists(from.offsetBy(-1, 0));
                    if (blocked) {
                        return MoveLegality.MovementBlocked;
                    } else if (rankDiff == -1) {
                        return MoveLegality.Legal;
                    } else if (rankDiff == -2 && from.getRank().getIndex() == 6) {
                        blocked = board.getPieceExists(from.offsetBy(-2, 0));
                        if (blocked) {
                            return MoveLegality.MovementBlocked;
                        } else {
                            return MoveLegality.Legal;
                        }
                    }
                }
            }

            // Otherwise, the move is illegal
            return MoveLegality.InaccessibleSquare;
        } catch (IllegalArgumentException e) {
            // If we get here, the pawn is trying to move off the board
            return MoveLegality.InaccessibleSquare;
        }

        // if (fileDiff <= 1) {
        // if (getColor() == PieceColor.WHITE && rankDiff == 1) {
        // return true;
        // } else if (getColor() == PieceColor.BLACK && rankDiff == -1) {
        // return true;
        // }
        // }
        // if (fileDiff == 0) {
        // if (canDoubleMoveWhite && getColor() == PieceColor.WHITE && rankDiff == 2) {
        // return true;
        // } else if (canDoubleMoveBlack && getColor() == PieceColor.BLACK && rankDiff
        // == -2) {
        // return true;
        // }
        // }
        // return false;

        // int fileDiff = Math.abs(from.getFile().getIndex() - to.getFile().getIndex());

        // if (fileDiff == 1) {

        // }
    }

    @Override
    public String toString() {
        return "";
    }
}
