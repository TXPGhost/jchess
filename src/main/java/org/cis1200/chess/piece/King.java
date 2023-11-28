package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class King extends Piece {
    public King(PieceColor color) {
        super(color);
    }

    @Override
    public MoveLegality getLegality(Board board, Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();

        int rankDiff = Math.abs(to.getRank().getIndex() - from.getRank().getIndex());
        int fileDiff = Math.abs(from.getFile().getIndex() - to.getFile().getIndex());

        if (rankDiff == 0 && fileDiff == 0) {
            return MoveLegality.InaccessibleSquare;
        } else if (rankDiff <= 1 && fileDiff <= 1) {
            return MoveLegality.Legal;
        }

        return MoveLegality.InaccessibleSquare;
    }

    @Override
    public String toString() {
        return "K";
    }
}
