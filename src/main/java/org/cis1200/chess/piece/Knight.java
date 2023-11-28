package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class Knight extends Piece {
    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public char getNotationChar() {
        return 'N';
    }

    @Override
    public MoveLegality getLegality(Board board, Square from, Square to) {
        int rankDiff = Math.abs(to.getRank().getIndex() - from.getRank().getIndex());
        int fileDiff = Math.abs(from.getFile().getIndex() - to.getFile().getIndex());

        if ((rankDiff == 1 && fileDiff == 2) || (rankDiff == 2 && fileDiff == 1)) {
            return MoveLegality.Legal;
        } else {
            return MoveLegality.InaccessibleSquare;
        }
    }
}
