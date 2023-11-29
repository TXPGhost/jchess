package org.cis1200.chess.piece;

import org.cis1200.chess.Board;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Square;

public abstract class Piece {
    private PieceColor color;

    /**
     * Creates a new piece with the given color.
     */
    public Piece(PieceColor color) {
        this.color = color;
    }

    /**
     * Returns the team color of the given piece.
     */
    public PieceColor getColor() {
        return this.color;
    }

    /**
     * Returns the legality of a potential move given the board state. This method
     * should be called on the from piece and verify every applicable chess rule. It
     * should also assume that it is the correct player's turn and that the to
     * piece, if it exists,
     * is not the same colors as the from piece.
     */
    public abstract MoveLegality getLegality(Move move);
}
