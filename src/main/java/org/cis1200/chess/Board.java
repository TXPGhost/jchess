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
     * Records who can castle.
     */
    private CastlingRestrictions castlingRestrictions;

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
        castlingRestrictions = new CastlingRestrictions();

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
     * Sets the piece at the given location.
     */
    private void setPiece(Square square, Piece piece) {
        board[square.getRank().getIndex()][square.getFile().getIndex()] = piece;
    }

    /**
     * Takes the piece at the given location, returning it.
     */
    private Piece takePiece(Square square) {
        Piece p = getPiece(square);
        setPiece(square, null);
        return p;
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

        // Perform castling
        CastleSide castleSide = move.getCastleSide();
        if (castleSide == CastleSide.King && turn == PieceColor.White) {
            next.setPiece(new Square("f1"), next.takePiece(new Square("h1")));
        } else if (castleSide == CastleSide.Queen && turn == PieceColor.White) {
            next.setPiece(new Square("d1"), next.takePiece(new Square("a1")));
        } else if (castleSide == CastleSide.King && turn == PieceColor.Black) {
            next.setPiece(new Square("f8"), next.takePiece(new Square("h8")));
        } else if (castleSide == CastleSide.Queen && turn == PieceColor.Black) {
            next.setPiece(new Square("d8"), next.takePiece(new Square("a8")));
        }

        // Perform en passant
        if (!getPieceExists(move.getTo()) && move.getPiece().getClass() == Pawn.class
                && !move.getFrom().getRank().equals(move.getTo().getRank())) {
            next.setPiece(new Square(move.getFrom().getRank(), move.getTo().getFile()), null);
        }

        // Perform promotion
        if (move.getPiece().getClass() == Pawn.class) {
            if (move.getPieceColor().equals(PieceColor.White) && move.getTo().getRank().equals(new Rank(8))) {
                next.setPiece(move.getTo(), new Queen(PieceColor.White));
            } else if (move.getPieceColor().equals(PieceColor.Black) && move.getTo().getRank().equals(new Rank(1))) {
                next.setPiece(move.getTo(), new Queen(PieceColor.Black));
            }
        }

        next.turn = switch (turn) {
            case White -> PieceColor.Black;
            case Black -> PieceColor.White;
        };
        next.lastMove = move;
        next.castlingRestrictions = new CastlingRestrictions(castlingRestrictions, move);

        return next;
    }

    /**
     * Returns a new board where the turns are flipped. Note that this never occurs
     * in normal chess.
     */
    public Board withTurnsFlipped() {
        Board next = new Board(InitialBoardState.Empty);
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Square square = new Square(new Rank(r), new File(f));
                next.setPiece(square, getPiece(square));
            }
        }

        next.turn = switch (turn) {
            case White -> PieceColor.Black;
            case Black -> PieceColor.White;
        };

        next.lastMove = lastMove;

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

        return p.getLegality(move);
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
        if (after.isChecking()) {
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
    public boolean isChecking() {
        Square king = getKingLocation(turn.opposite());
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Square attacker = new Square(new Rank(r), new File(f));
                if (getPieceColor(attacker) == turn) {
                    MoveLegality attackerLegality = getLegalityIgnoreCheck(
                            new Move(this, attacker, king));
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
    public boolean isInCheckmate() {
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
                            if (!isChecking()) {
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

    /**
     * Returns the castling restrictions of the current board.
     */
    public CastlingRestrictions getCastlingRestrictions() {
        return castlingRestrictions;
    }

    /**
     * Returns the material value for the given player based on standard piece point
     * values.
     */
    public int countMaterial(PieceColor color) {
        int material = 0;
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Square s = new Square(new Rank(r), new File(f));
                Piece p = getPiece(s);
                if (p != null && p.getColor() == color) {
                    material += p.getPointValue();
                }
            }
        }
        return material;
    }

    /**
     * Evaluates the current board state, where more positive values are good for
     * white and more negative values are good for black.
     */
    public float eval() {
        if (isInCheckmate()) {
            return switch (turn) {
                case White -> -1000000;
                case Black -> 1000000;
            };
        }
        return countMaterial(PieceColor.White) - countMaterial(PieceColor.Black);
    }
}
