package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public class Bishop extends Piece {
    public Bishop(final PieceColor color) {
        super(color);
    }

    @Override
    public MoveLegality getLegality(final Move move) {
        final Board board = move.getBoard();
        final Square from = move.getFrom();
        final Square to = move.getTo();

        final int rankDiff = to.getRank().getIndex() - from.getRank().getIndex();
        final int fileDiff = to.getFile().getIndex() - from.getFile().getIndex();

        if (Math.abs(rankDiff) != Math.abs(fileDiff)) {
            return MoveLegality.InaccessibleSquare;
        }

        try {
            if (rankDiff > 0 && fileDiff > 0) {
                Square s = from;
                while (!s.equals(to.offsetBy(-1, -1))) {
                    s = s.offsetBy(1, 1);
                    if (board.getPieceExists(s)) {
                        return MoveLegality.MovementBlocked;
                    }
                }
                return MoveLegality.Legal;
            } else if (rankDiff < 0 && fileDiff > 0) {
                Square s = from;
                while (!s.equals(to.offsetBy(1, -1))) {
                    s = s.offsetBy(-1, 1);
                    if (board.getPieceExists(s)) {
                        return MoveLegality.MovementBlocked;
                    }
                }
                return MoveLegality.Legal;
            } else if (rankDiff > 0 && fileDiff < 0) {
                Square s = from;
                while (!s.equals(to.offsetBy(-1, 1))) {
                    s = s.offsetBy(1, -1);
                    if (board.getPieceExists(s)) {
                        return MoveLegality.MovementBlocked;
                    }
                }
                return MoveLegality.Legal;

            } else if (rankDiff < 0 && fileDiff < 0) {
                Square s = from;
                while (!s.equals(to.offsetBy(1, 1))) {
                    s = s.offsetBy(-1, -1);
                    if (board.getPieceExists(s)) {
                        return MoveLegality.MovementBlocked;
                    }
                }
                return MoveLegality.Legal;
            }

            return MoveLegality.InaccessibleSquare;
        } catch (final IllegalArgumentException e) {
            return MoveLegality.InaccessibleSquare;
        }
    }

    @Override
    public String toString() {
        return "B";
    }

    @Override
    public int getPointValue() {
        return 3;
    }
}
