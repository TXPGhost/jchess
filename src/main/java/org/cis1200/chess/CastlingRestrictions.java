package org.cis1200.chess;

public class CastlingRestrictions {
    private boolean whiteKingSide;
    private boolean whiteQueenSide;
    private boolean blackKingSide;
    private boolean blackQueenSide;

    public CastlingRestrictions() {
        whiteKingSide = true;
        whiteQueenSide = true;
        blackKingSide = true;
        blackQueenSide = true;
    }

    public CastlingRestrictions(final CastlingRestrictions original) {
        whiteKingSide = original.whiteKingSide;
        whiteQueenSide = original.whiteQueenSide;
        blackKingSide = original.blackKingSide;
        blackQueenSide = original.blackQueenSide;
    }

    public CastlingRestrictions(final CastlingRestrictions original, final Move move) {
        this(original);

        // Prevent castling if the king moves
        if (move.getFrom().equals(new Square("e1"))) {
            whiteKingSide = false;
            whiteQueenSide = false;
        }
        if (move.getFrom().equals(new Square("e8"))) {
            blackKingSide = false;
            blackQueenSide = false;
        }

        // Prevent castling if the rook moves or is captured
        if (move.getFrom().equals(new Square("a8")) || move.getTo().equals(new Square("a8"))) {
            whiteKingSide = false;
        }
        if (move.getFrom().equals(new Square("a1")) || move.getTo().equals(new Square("a1"))) {
            whiteQueenSide = false;
        }
        if (move.getFrom().equals(new Square("h8")) || move.getTo().equals(new Square("h8"))) {
            blackKingSide = false;
        }
        if (move.getFrom().equals(new Square("h1")) || move.getTo().equals(new Square("h1"))) {
            blackQueenSide = false;
        }
    }

    public boolean canWhiteCastleKingSide() {
        return whiteKingSide;
    }

    public boolean canWhiteCastleQueenSide() {
        return whiteQueenSide;
    }

    public boolean canBlackCastleKingSide() {
        return blackKingSide;
    }

    public boolean canBlackCastleQueenSide() {
        return blackQueenSide;
    }
}
