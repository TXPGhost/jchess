package org.cis1200.chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
    public Board(final InitialBoardState initialBoardState) {
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

    public Board(final Board b, final Move move) {
        this(InitialBoardState.Empty);

        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                final Square square = new Square(new Rank(r), new File(f));
                if (square.equals(move.getFrom())) {
                    setPiece(square, null);
                } else if (square.equals(move.getTo())) {
                    setPiece(square, b.getPiece(move.getFrom()));
                } else {
                    setPiece(square, b.getPiece(square));
                }

            }
        }

        // Perform castling
        final CastleSide castleSide = move.getCastleSide();
        if (castleSide == CastleSide.King && b.turn == PieceColor.White) {
            setPiece(new Square("f1"), takePiece(new Square("h1")));
        } else if (castleSide == CastleSide.Queen && b.turn == PieceColor.White) {
            setPiece(new Square("d1"), takePiece(new Square("a1")));
        } else if (castleSide == CastleSide.King && b.turn == PieceColor.Black) {
            setPiece(new Square("f8"), takePiece(new Square("h8")));
        } else if (castleSide == CastleSide.Queen && b.turn == PieceColor.Black) {
            setPiece(new Square("d8"), takePiece(new Square("a8")));
        }

        // Perform en passant
        if (!b.getPieceExists(move.getTo()) && move.getPiece().getClass() == Pawn.class
                && !move.getFrom().getRank().equals(move.getTo().getRank())) {
            setPiece(new Square(move.getFrom().getRank(), move.getTo().getFile()), null);
        }

        // Perform promotion
        if (move.getPiece().getClass() == Pawn.class) {
            if (move.getPieceColor().equals(PieceColor.White)
                    && move.getTo().getRank().equals(new Rank(8))) {
                setPiece(move.getTo(), move.getPromotionPiece());
            } else if (move.getPieceColor().equals(PieceColor.Black)
                    && move.getTo().getRank().equals(new Rank(1))) {
                setPiece(move.getTo(), move.getPromotionPiece());
            }
        }

        turn = switch (b.turn) {
            case White -> PieceColor.Black;
            case Black -> PieceColor.White;
        };

        lastMove = move;
        castlingRestrictions = new CastlingRestrictions(b.castlingRestrictions, move);
    }

    public Board(final Board b) {
        this(InitialBoardState.Empty);

        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                final Square square = new Square(new Rank(r), new File(f));
                setPiece(square, b.getPiece(square));
            }
        }
    }

    public Board(final Board b, final PieceColor turn) {
        this(b);
        this.turn = turn;
    }

    /**
     * Returns a 2d array of the squares in the board
     */
    public Piece[][] getSquares() {
        return board;
    }

    /**
     * Returns the piece at the given square. May be null, which corresponds to no
     * piece.
     */
    public Piece getPiece(final Square square) {
        return board[square.getRank().getIndex()][square.getFile().getIndex()];
    }

    /**
     * Sets the piece at the given location.
     */
    public void setPiece(final Square square, final Piece piece) {
        board[square.getRank().getIndex()][square.getFile().getIndex()] = piece;
    }

    /**
     * Takes the piece at the given location, returning it.
     */
    public Piece takePiece(final Square square) {
        final Piece p = getPiece(square);
        setPiece(square, null);
        return p;
    }

    /**
     * Returns a new board where the turns are flipped. Note that this never occurs
     * in normal chess.
     */
    public Board withTurnsFlipped() {
        final Board next = new Board(InitialBoardState.Empty);
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                final Square square = new Square(new Rank(r), new File(f));
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
    private MoveLegality getLegalityIgnoreCheck(final Move move) {
        final Piece p = getPiece(move.getFrom());

        // Make sure the piece is promoting
        if (move.isPromotion() && move.getPromotionPiece() == null) {
            return MoveLegality.MustPromote;
        }

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
    public MoveLegality getLegality(final Move move) {
        final MoveLegality legality = getLegalityIgnoreCheck(move);

        if (!legality.isLegal()) {
            return legality;
        }

        // Make sure the king would remain out of check
        final Board after = new Board(new Board(this, move), this.turn);
        if (after.isInCheck()) {
            return MoveLegality.WouldBeInCheck;
        }

        // Make sure the king does not castle in check
        if (Math.abs(move.getFrom().getFile().getIndex() - move.getTo().getFile().getIndex()) == 2) {
            if (isInCheck()) {
                return MoveLegality.CastlingInCheck;
            }
        }

        return MoveLegality.Legal;
    }

    /**
     * Returns true if there is a piece at the given square and false otherwise.
     */
    public boolean getPieceExists(final Square s) {
        return getPiece(s) != null;
    }

    /**
     * Returns the color of the piece at the given square, and null if there is no
     * piece.
     */
    public PieceColor getPieceColor(final Square s) {
        final Piece p = getPiece(s);
        if (p == null) {
            return null;
        }
        return p.getColor();
    }

    /**
     * Returns the location of the king with the given color.
     */
    public Square getKingLocation(final PieceColor color) {
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                final Square s = new Square(new Rank(r), new File(f));
                final Piece p = getPiece(s);
                if (p != null && p.getColor() == color && p.getClass() == King.class) {
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Returns a collection of all possible non-king moves from this position.
     */
    public Collection<Move> getPossibleMoves() {
        return getPossibleMovesWithBlacklist(Collections.emptyList());
    }

    /**
     * Returns a collection of all possible moves from this position with a
     * blacklist of piece types.
     */
    public Collection<Move> getPossibleMovesWithBlacklist(Collection<Class<? extends Piece>> blacklist) {
        final Collection<Move> possibleMoves = new ArrayList<>();
        for (int rFrom = 1; rFrom <= 8; rFrom++) {
            for (char fFrom = 'a'; fFrom <= 'h'; fFrom++) {
                final Square from = new Square(new Rank(rFrom), new File(fFrom));
                if (!turn.equals(getPieceColor(from))) {
                    continue;
                }
                if (blacklist.contains(getPiece(from).getClass())) {
                    continue;
                }
                for (int rTo = 1; rTo <= 8; rTo++) {
                    for (char fTo = 'a'; fTo <= 'h'; fTo++) {
                        final Square to = new Square(new Rank(rTo), new File(fTo));

                        final Move move = new Move(this, from, to, null);
                        if (move.isPromotion()) {
                            final Move moveQ = new Move(this, from, to, new Queen(turn));
                            final Move moveK = new Move(this, from, to, new Knight(turn));
                            final Move moveR = new Move(this, from, to, new Rook(turn));
                            final Move moveB = new Move(this, from, to, new Bishop(turn));

                            if (getLegality(moveQ).isLegal()) {
                                possibleMoves.add(moveQ);
                            }
                            if (getLegality(moveK).isLegal()) {
                                possibleMoves.add(moveK);
                            }
                            if (getLegality(moveR).isLegal()) {
                                possibleMoves.add(moveR);
                            }
                            if (getLegality(moveB).isLegal()) {
                                possibleMoves.add(moveB);
                            }
                        } else {
                            if (getLegality(move).isLegal()) {
                                possibleMoves.add(move);
                            }
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    /**
     * Returns true if the player whose turn it is is in check.
     */
    public boolean isInCheck() {
        final Square king = getKingLocation(turn);
        if (king == null) {
            return false;
        }

        final Board flipped = new Board(this, turn.opposite());
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                final Square attacker = new Square(new Rank(r), new File(f));
                if (getPieceColor(attacker) == turn.opposite()) {
                    if (flipped.getLegalityIgnoreCheck(
                            new Move(this, attacker, king, null)) == MoveLegality.Legal) {
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
        return getPossibleMoves().isEmpty() && isInCheck();
    }

    /**
     * Returns true if the player whose turn it is is in stalemate.
     */
    public boolean isInStalemate() {
        return getPossibleMoves().isEmpty() && !isInCheck();
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
    public int countMaterial(final PieceColor color) {
        int material = 0;
        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                final Square s = new Square(new Rank(r), new File(f));
                final Piece p = getPiece(s);
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

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (this.getClass() != o.getClass()) {
            return false;
        } else {
            for (int r = 1; r <= 8; r++) {
                for (char f = 'a'; f <= 'h'; f++) {
                    Square s = new Square(new Rank(r), new File(f));
                    Piece a = getPiece(s);
                    Piece b = ((Board) o).getPiece(s);
                    if (a == null && b == null) {
                        continue;
                    } else if ((a == null && b != null) || (a != null && b == null)) {
                        return false;
                    } else if (!getPiece(s).equals(((Board) o).getPiece(s))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
