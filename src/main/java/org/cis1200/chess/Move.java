package org.cis1200.chess;

import java.util.Collection;

import org.cis1200.chess.piece.Bishop;
import org.cis1200.chess.piece.King;
import org.cis1200.chess.piece.Knight;
import org.cis1200.chess.piece.Pawn;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;
import org.cis1200.chess.piece.Queen;
import org.cis1200.chess.piece.Rook;

public class Move {
    Board board;

    Square from;
    Square to;

    public Move(Board board, Square from, Square to) {
        this.board = board;
        this.from = from;
        this.to = to;

        if (board == null) {
            throw new IllegalArgumentException("board cannot be null");
        }

        if (board.getPiece(from) == null) {
            throw new IllegalArgumentException("from square must contain a piece");
        }
    }

    public Move(Board board, String notation) throws DeserializeMoveException {
        Collection<Move> possibleMoves = board.getPossibleMoves();
        for (Move move : possibleMoves) {
            System.out.println(move.toString() + " ?= " + notation);
            if (move.toString().equals(notation)) {
                this.board = board;
                from = move.getFrom();
                to = move.getTo();
                return;
            }
        }
        throw new DeserializeMoveException();
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    public Board getBoard() {
        return board;
    }

    public Piece getPiece() {
        return board.getPiece(from);
    }

    public PieceColor getPieceColor() {
        return board.getPieceColor(from);
    }

    public CastleSide getCastleSide() {
        if (getPiece().getClass() == King.class) {
            int fileDiff = to.getFile().getIndex() - from.getFile().getIndex();
            if (fileDiff == 2) {
                return CastleSide.King;
            } else if (fileDiff == -2) {
                return CastleSide.Queen;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        // Returns the move in algebraic notation
        Piece p = board.getPiece(from);

        String piece = p.toString();
        String qualifier = "";
        String takes = "";
        String location = to.toString();
        String check = "";

        if (board.getPiece(to) != null) {
            takes = "x";

            if (p.getClass() == Pawn.class) {
                qualifier = from.getFile().toString();
            }
        }

        Board next = new Board(board, this);
        if (next.withTurnsFlipped().isInCheck()) {
            check = "+";

            if (next.isInCheckmate()) {
                check = "#";
            }
        }

        return piece + qualifier + takes + location + check;
    }
}
