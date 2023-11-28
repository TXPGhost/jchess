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
     * correspond to 'a'..='h' and ranks 0..=7 correspond to 1..=8. Indices are
     * defined in the Rank and File classes.
     */
    private Piece[][] board;

    /**
     * The move that was played to achieve this board state.
     */
    private Move lastMove;

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
    public Board withMove(Move move) {
        Board next = new Board(InitialBoardState.Empty);
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Square square = new Square(new Rank(r), new File(f));
                if (square.equals(move.getFrom())) {
                    next.setPiece(square, null);
                } else if (square.equals(move.getTo())) {
                    next.setPiece(square, getPiece(move.getFrom()));
                } else {
                    next.setPiece(square, getPiece(square));
                }
            }
        }

        next.turn = switch (turn) {
            case White -> PieceColor.Black;
            case Black -> PieceColor.White;
        };

        next.lastMove = move;

        return next;
    }

    /**
     * Returns the move that was played to achieve this board state.
     */
    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Returns the legality of the given move, ignoring checks.
     */
    private MoveLegality getLegalityIgnoreCheck(Move move) {
        Piece p = getPiece(move.getFrom());

        // Make sure the piece we are trying to move exists
        if (p == null) {
            return MoveLegality.NoSuchPiece;
        }

        // Make sure it is the right player's turn
        if (p.getColor() != turn) {
            return MoveLegality.WrongTurn;
        }

        // Make srue we are not trying to capture our own piece
        if (p.getColor() == getPieceColor(move.getTo())) {
            return MoveLegality.SameSideCapture;
        }

        return p.getLegality(this, move);
    }

    /**
     * Returns the legality of the given move.
     */
    public MoveLegality getLegality(Move move) {
        MoveLegality legality = getLegalityIgnoreCheck(move);

        if (!legality.isLegal()) {
            return legality;
        }

        // Make sure the king would remain out of check
        Board after = withMove(move);
        if (after.isKingInCheck()) {
            return MoveLegality.WouldBeInCheck;
        }

        return MoveLegality.Legal;
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
            for (char f = 'a'; f <= 'h'; f++) {
                Square s = new Square(new Rank(r), new File(f));
                Piece p = getPiece(s);
                if (p != null && p.getColor() == color && p.getClass() == King.class) {
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Returns true if the player whose turn it is is checking the other player's
     * king.
     */
    public boolean isKingInCheck() {
        Square king = getKingLocation(turn.opposite());
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Square attacker = new Square(new Rank(r), new File(f));
                if (getPieceColor(attacker) == turn) {
                    MoveLegality attackerLegality = getLegalityIgnoreCheck(new Move(this, attacker, king));
                    if (attackerLegality == MoveLegality.Legal) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns true if the player whose turn it is is in checkmate.
     */
    public boolean isPlayerInCheckmate() {
        for (int rFrom = 1; rFrom <= 8; rFrom++) {
            for (char fFrom = 'a'; fFrom <= 'h'; fFrom++) {
                Square from = new Square(new Rank(rFrom), new File(fFrom));
                if (getPieceColor(from) != turn) {
                    continue;
                }

                for (int rTo = 1; rTo <= 8; rTo++) {
                    for (char fTo = 'a'; fTo <= 'h'; fTo++) {
                        Square to = new Square(new Rank(rTo), new File(fTo));

                        MoveLegality legality = getLegality(new Move(this, from, to));
                        if (legality == MoveLegality.Legal) {
                            if (!isKingInCheck()) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Returns the color of the current player's turn.
     */
    public PieceColor getTurn() {
        return turn;
    }
}
