package org.cis1200.chess;

import java.util.Collection;

import org.cis1200.chess.piece.King;
import org.cis1200.chess.piece.Pawn;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;

public class Move {
    Board board;

    Square from;
    Square to;

    public Move(final Board board, final Square from, final Square to) {
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

    public Move(final Board board, final String notation) throws DeserializeMoveException {
        final Collection<Move> possibleMoves = board.getPossibleMoves();
        for (final Move move : possibleMoves) {
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
            final int fileDiff = to.getFile().getIndex() - from.getFile().getIndex();
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
        final Piece p = board.getPiece(from);

        // Check for castling
        if (p.getClass() == King.class && Math.abs(to.getFile().getIndex() - from.getFile().getIndex()) == 2) {
            if (to.getFile().equals(new File('g'))) {
                return "O-O";
            } else if (to.getFile().equals(new File('c'))) {
                return "O-O-O";
            }
        }

        final String piece = p.toString();
        String qualifier = "";
        String takes = "";
        final String location = to.toString();
        String check = "";

        if (board.getPiece(to) != null) {
            takes = "x";

            if (p.getClass() == Pawn.class) {
                qualifier = from.getFile().toString();
            }
        }

        final Board next = new Board(board, this);
        if (next.isInCheck()) {
            check = "+";

            if (next.isInCheckmate()) {
                check = "#";
            }
        }

        if (p.getClass() != Pawn.class) {
            // Disambiguate file
            boolean disambiguateFile = false;
            for (char f = 'a'; f <= 'h'; f++) {
                Square s = new Square(from.getRank(), new File(f));
                if (!s.equals(from)) {
                    Piece o = board.getPiece(s);
                    if (o != null) {
                        if (o.getClass() == p.getClass()) {
                            if (board.getLegality(new Move(board, s, to)).isLegal()) {
                                disambiguateFile = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (disambiguateFile) {
                qualifier = from.getFile().toString();
            } else {
                // Disambiguate rank
                boolean disambiguateRank = false;
                for (int r = 1; r <= 8; r++) {
                    Square s = new Square(new Rank(r), from.getFile());
                    if (!s.equals(from)) {
                        Piece o = board.getPiece(s);
                        if (o != null) {
                            if (o.getClass() == p.getClass()) {
                                if (board.getLegality(new Move(board, s, to)).isLegal()) {
                                    disambiguateRank = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (disambiguateRank) {
                    qualifier = from.getRank().toString();
                }
            }
        }

        return piece + qualifier + takes + location + check;
    }
}
