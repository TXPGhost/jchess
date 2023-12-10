package org.cis1200.chess;

import java.util.Collection;

import org.cis1200.chess.piece.King;
import org.cis1200.chess.piece.Pawn;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;

public class Move {
    private Board board;

    private Square from;
    private Square to;

    private Piece promotionPiece;

    public Move(final Board board, final Square from, final Square to, final Piece promotionPiece) {
        this.board = board;
        this.from = from;
        this.to = to;
        this.promotionPiece = promotionPiece;

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
            if (move.toString().equals(notation)) {
                this.board = board;
                from = move.getFrom();
                to = move.getTo();
                promotionPiece = move.getPromotionPiece();
                return;
            }
        }
        throw new DeserializeMoveException("move notation was unable to be decoded: " + notation);
    }

    public boolean isPromotion() {
        final Piece p = board.getPiece(from);
        if (p == null) {
            return false;
        }

        if (p.getClass() == Pawn.class) {
            if (p.getColor() == PieceColor.White) {
                if (to.getRank().equals(new Rank(8))) {
                    return true;
                }
            } else {
                if (to.getRank().equals(new Rank(1))) {
                    return true;
                }
            }
        }

        return false;
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

    public Piece getPromotionPiece() {
        return promotionPiece;
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
        if (p.getClass() == King.class
                && Math.abs(to.getFile().getIndex() - from.getFile().getIndex()) == 2) {
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
        String promotion = "";

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

        // Disambiguate moves
        if (p.getClass() != Pawn.class) {
            for (final Move other : board.getPossibleMoves()) {
                if (other.getPiece().getClass() == p.getClass()) {
                    if (to.equals(other.to) && !from.equals(other.from)) {
                        if (from.getFile().equals(other.from.getFile())) {
                            qualifier = from.getRank().toString();
                        } else {
                            qualifier = from.getFile().toString();
                        }
                    }
                }
            }
        }

        // Add promotion
        if (isPromotion()) {
            promotion += "=" + promotionPiece;
        }

        return piece + qualifier + takes + location + promotion + check;
    }
}
