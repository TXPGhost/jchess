package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class King extends Piece {
    public King(PieceColor color) {
        super(color);
    }

    @Override
    public char getNotationChar() {
        return 'K';
    }

    @Override
    public MoveLegality getLegality(Board board, Square from, Square to) {
        int rankDiff = Math.abs(to.getRank().getIndex() - from.getRank().getIndex());
        int fileDiff = Math.abs(from.getFile().getIndex() - to.getFile().getIndex());

        if (rankDiff == 0 && fileDiff == 0) {
            return MoveLegality.InaccessibleSquare;
        } else if (rankDiff <= 1 && fileDiff <= 1) {
            return MoveLegality.Legal;
        }

        return MoveLegality.InaccessibleSquare;
    }
}
