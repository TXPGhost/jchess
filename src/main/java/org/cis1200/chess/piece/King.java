package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.CastlingRestrictions;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class King extends Piece {
    public King(PieceColor color) {
        super(color);
    }

    @Override
    public MoveLegality getLegality(Move move) {
        Board board = move.getBoard();
        Square from = move.getFrom();
        Square to = move.getTo();

        int rankDiff = Math.abs(to.getRank().getIndex() - from.getRank().getIndex());
        int fileDiff = Math.abs(to.getFile().getIndex() - from.getFile().getIndex());

        if (rankDiff == 0 && fileDiff == 0) {
            return MoveLegality.InaccessibleSquare;
        } else if (rankDiff <= 1 && fileDiff <= 1) {
            return MoveLegality.Legal;
        }

        // Check for castling
        if (fileDiff == 2 && rankDiff == 0) {
            int signedFileDiff = to.getFile().getIndex() - from.getFile().getIndex();
            CastlingRestrictions restrictions = board.getCastlingRestrictions();

            if (signedFileDiff == 2) {
                if (move.getPieceColor() == PieceColor.White
                        && restrictions.canWhiteCastleKingSide()) {
                    if (board.getPiece(new Square("f1")) == null
                            && board.getPiece(new Square("g1")) == null) {
                        return MoveLegality.LegalCastleKingSide;
                    }
                } else if (move.getPieceColor() == PieceColor.Black
                        && restrictions.canBlackCastleKingSide()) {
                    if (board.getPiece(new Square("f8")) == null
                            && board.getPiece(new Square("g8")) == null) {
                        return MoveLegality.LegalCastleKingSide;
                    }
                }
            } else if (signedFileDiff == -2) {
                if (move.getPieceColor() == PieceColor.White
                        && restrictions.canWhiteCastleQueenSide()) {
                    if (board.getPiece(new Square("b1")) == null
                            && board.getPiece(new Square("c1")) == null
                            && board.getPiece(new Square("d1")) == null) {
                        return MoveLegality.LegalCastleKingSide;
                    }
                } else if (move.getPieceColor() == PieceColor.Black
                        && restrictions.canBlackCastleQueenSide()) {
                    if (board.getPiece(new Square("b8")) == null
                            && board.getPiece(new Square("c8")) == null
                            && board.getPiece(new Square("d8")) == null) {
                        return MoveLegality.LegalCastleKingSide;
                    }
                }
            }
        }

        return MoveLegality.InaccessibleSquare;
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public int getPointValue() {
        return 0;
    }
}
