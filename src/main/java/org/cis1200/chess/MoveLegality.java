package org.cis1200.chess;

public enum MoveLegality {
    Legal(true, "Legal move."),
    LegalCastleKingSide(true, "Legal move (castling kingside)."),
    LegalCastleQueenSide(true, "Legal move (castling queenside)."),
    WrongTurn(false, "You cannot move this piece."),
    NoSuchPiece(false, "You must select a piece to move."),
    PawnNoPieceToCapture(false, "You must capture a piece to move diagonally."),
    MovementBlocked(false, "Movement is blocked by another piece."),
    SameSideCapture(false, "Cannot capture your own piece."),
    InaccessibleSquare(false, "This square is inaccessible."),
    InCheck(false, "You are in check."),
    WouldBeInCheck(false, "You would be in check.");

    private boolean isLegal;
    private String message;

    private MoveLegality(boolean isLegal, String message) {
        this.isLegal = isLegal;
        this.message = message;
    }

    public boolean isLegal() {
        return isLegal;
    }

    public String toString() {
        return message;
    }
}
