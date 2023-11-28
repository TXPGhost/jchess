package org.cis1200.chess.piece;

public enum PieceColor {
    Black(),
    White();

    public PieceColor opposite() {
        return switch (this) {
            case Black -> PieceColor.White;
            case White -> PieceColor.Black;
        };
    }
}
