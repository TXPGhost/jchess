package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class Knight extends Piece {
    public Knight(final PieceColor color) {
        super(color);
    }

    @Override
    public MoveLegality getLegality(final Move move) {
        final Square from = move.getFrom();
        final Square to = move.getTo();

        final int rankDiff = Math.abs(to.getRank().getIndex() - from.getRank().getIndex());
        final int fileDiff = Math.abs(from.getFile().getIndex() - to.getFile().getIndex());

        if ((rankDiff == 1 && fileDiff == 2) || (rankDiff == 2 && fileDiff == 1)) {
            return MoveLegality.Legal;
        } else {
            return MoveLegality.InaccessibleSquare;
        }
    }

    @Override
    public String toString() {
        return "N";
    }

    @Override
    public int getPointValue() {
        return 3;
    }
}
