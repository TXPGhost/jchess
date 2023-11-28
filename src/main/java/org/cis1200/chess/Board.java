package org.cis1200.chess;

import org.cis1200.chess.piece.Bishop;
import org.cis1200.chess.piece.King;
import org.cis1200.chess.piece.Knight;
import org.cis1200.chess.piece.Pawn;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;
import org.cis1200.chess.piece.Queen;
import org.cis1200.chess.piece.Rook;

public class Board {
    /**
     * A 2d array of pieces representing the chess board. Each entry is either a
     * Piece instance or null. The format is board[file][rank] where: files 0..=7
     * correspond to 'A'..='H' and ranks 0..=7 correspond to 1..=8. Indices are
     * defined in the Rank and File classes.
     */
    private Piece[][] board;

    /**
     * A piece color corresponding to the current player's turn
     */
    private PieceColor turn;

    /**
     * Constructs a new board with the standard chess board layout.
     */
    public Board() {
        this(InitialBoardState.StandardChess);
    }

    /**
     * Constructs a new board with the given initial state.
     */
    public Board(InitialBoardState initialBoardState) {
        board = new Piece[8][8];
        turn = PieceColor.White;

        if (initialBoardState == InitialBoardState.Empty) {
            return;
        }

        // Initialize first rank with white pieces
        board[0][0] = new Rook(PieceColor.White);
        board[0][1] = new Knight(PieceColor.White);
        board[0][2] = new Bishop(PieceColor.White);
        board[0][3] = new Queen(PieceColor.White);
        board[0][4] = new King(PieceColor.White);
        board[0][5] = new Bishop(PieceColor.White);
        board[0][6] = new Knight(PieceColor.White);
        board[0][7] = new Rook(PieceColor.White);

        // Initialize second rank with white pawns
        for (int f = 0; f < 8; f++) {
            board[1][f] = new Pawn(PieceColor.White);
        }

        // Initialize seventh rank with black pawns
        for (int f = 0; f < 8; f++) {
            board[6][f] = new Pawn(PieceColor.Black);
        }

        // Initialize eighth rank with black pieces
        board[7][0] = new Rook(PieceColor.Black);
        board[7][1] = new Knight(PieceColor.Black);
        board[7][2] = new Bishop(PieceColor.Black);
        board[7][3] = new Queen(PieceColor.Black);
        board[7][4] = new King(PieceColor.Black);
        board[7][5] = new Bishop(PieceColor.Black);
        board[7][6] = new Knight(PieceColor.Black);
        board[7][7] = new Rook(PieceColor.Black);
    }

    /**
     * Returns the piece at the given square. May be null, which corresponds to no
     * piece.
     */
    public Piece getPiece(Square square) {
        return board[square.getRank().getIndex()][square.getFile().getIndex()];
    }

    /**
     * Returns the piece at the given rank and file indices.
     */
    public Piece getPieceRaw(int rank, int file) {
        return board[rank][file];
    }

    /**
     * Sets the piece at the given location.
     */
    private void setPiece(Square square, Piece piece) {
        board[square.getRank().getIndex()][square.getFile().getIndex()] = piece;
    }

    /**
     * Returns a new board with the given piece moved to a new position. Does not
     * verify if the move is a legal move.
     */
    public Board withMove(Square from, Square to, MoveContext context) {
        Board next = new Board(InitialBoardState.Empty);
        for (int r = 1; r <= 8; r++) {
            for (char f = 'A'; f <= 'H'; f++) {
                Square square = new Square(new Rank(r), new File(f));
                if (square.equals(from)) {
                    next.setPiece(square, null);
                } else if (square.equals(to)) {
                    next.setPiece(square, getPiece(from));
                } else {
                    next.setPiece(square, getPiece(square));
                }
            }
        }

        next.turn = switch (turn) {
            case White -> PieceColor.Black;
            case Black -> PieceColor.White;
        };

        return next;
    }

    /**
     * Returns a new board with the given piece moved to a new position. Assumes
     * normal move context. That is, the move is not en passant, castling, or
     * promotion. Does not verify if the move is a legal move.
     */
    public Board withMove(Square from, Square to) {
        return withMove(from, to, MoveContext.Normal);
    }

    /**
     * Returns the legality of the given move.
     */
    public MoveLegality getLegality(Square from, Square to) {
        Piece p = getPiece(from);

        if (p == null) {
            return MoveLegality.NoSuchPiece;
        }

        if (p.getColor() != turn) {
            return MoveLegality.WrongTurn;
        }

        if (p.getColor() == getPieceColor(to)) {
            return MoveLegality.SameSideCapture;
        }

        return p.getLegality(this, from, to);
    }

    /**
     * Returns true if there is a piece at the given square and false otherwise.
     */
    public boolean getPieceExists(Square s) {
        return getPiece(s) != null;
    }

    /**
     * Returns the color of the piece at the given square, and null if there is no
     * piece.
     */
    public PieceColor getPieceColor(Square s) {
        Piece p = getPiece(s);
        if (p == null) {
            return null;
        }
        return p.getColor();
    }

    /**
     * Returns the location of the king with the given color.
     */
    public Square getKingLocation(PieceColor color) {
        for (int r = 1; r <= 8; r++) {
            for (char f = 'A'; f <= 'H'; f++) {
                Square s = new Square(new Rank(r), new File(f));
                Piece p = getPiece(s);
                if (p.getColor() == color && p.getClass() == King.class) {
                    return s;
                }
            }
        }
        return null;
    }
}
