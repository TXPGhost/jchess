package org.cis1200.chess;

import org.cis1200.chess.piece.King;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;
import org.cis1200.chess.piece.Rook;

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

        final Piece piece = move.getPiece();
        if (piece.getClass() == King.class) {
            if (piece.getColor() == PieceColor.White) {
                this.whiteKingSide = false;
                this.whiteQueenSide = false;
            } else {
                this.blackKingSide = false;
                this.blackQueenSide = false;
            }
        } else if (piece.getClass() == Rook.class) {
            if (piece.getColor() == PieceColor.White) {
                if (move.from == new Square("h1")) {
                    this.whiteKingSide = false;
                } else if (move.from == new Square("a1")) {
                    this.whiteQueenSide = false;
                }
            } else {
                if (move.from == new Square("h8")) {
                    this.blackKingSide = false;
                } else if (move.from == new Square("a8")) {
                    this.blackQueenSide = false;
                }
            }
        }

        if (move.getTo().equals(new Square("a8"))) {
            this.whiteQueenSide = false;
        }
        if (move.getTo().equals(new Square("a1"))) {
            this.whiteKingSide = false;
        }
        if (move.getTo().equals(new Square("h8"))) {
            this.blackKingSide = false;
        }
        if (move.getTo().equals(new Square("h1"))) {
            this.blackKingSide = false;
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
