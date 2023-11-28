package org.cis1200.chess;

import org.cis1200.chess.piece.Pawn;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;

public class Move {
    Board fromBoard;

    Square from;
    Square to;

    public Move(Board fromBoard, Square from, Square to) {
        this.fromBoard = fromBoard;
        this.from = from;
        this.to = to;

        if (fromBoard == null) {
            throw new IllegalArgumentException("board cannot be null");
        }

        if (fromBoard.getPiece(from) == null) {
            throw new IllegalArgumentException("from square must contain a piece");
        }
    }

    public Square getFrom() {
        return from;
    }

    public void setFrom(Square from) {
        this.from = from;
    }

    public Square getTo() {
        return to;
    }

    public void setTo(Square to) {
        this.to = to;
    }

    @Override
    public String toString() {
        // Returns the move in algebraic notation
        Piece p = fromBoard.getPiece(from);

        String piece = p.toString();
        String qualifier = "";
        String takes = "";
        String location = to.toString();
        String check = "";

        if (fromBoard.getPiece(to) != null) {
            takes = "x";

            if (p.getClass() == Pawn.class) {
                qualifier = from.getFile().toString();
            }
        }

        if (fromBoard.withMove(this).isKingInCheck()) {
            check = "+";
        }

        return piece + qualifier + takes + location + check;
    }
}
