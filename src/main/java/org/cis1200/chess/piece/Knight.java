package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class Knight extends Piece {
    public Knight(PieceColor color) {
        super(color);
    }

    @Override
    public MoveLegality getLegality(Move move) {
        Board board = move.getBoard();
        Square from = move.getFrom();
        Square to = move.getTo();

        int rankDiff = Math.abs(to.getRank().getIndex() - from.getRank().getIndex());
        int fileDiff = Math.abs(from.getFile().getIndex() - to.getFile().getIndex());

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
}
