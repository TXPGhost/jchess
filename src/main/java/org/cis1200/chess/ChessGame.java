package org.cis1200.chess;

import java.util.ArrayList;
import java.util.List;

import org.cis1200.chess.piece.PieceColor;

public class ChessGame {
    private List<Board> boards;

    private long lastMoveTime;

    private long whiteClock;
    private long blackClock;

    private final long whiteIncrement;
    private final long blackIncrement;

    public ChessGame(
            final long whiteTime, final long whiteIncrement, final long blackTime,
            final long blackIncrement
    ) {
        boards = new ArrayList<>();
        boards.add(new Board());

        lastMoveTime = System.currentTimeMillis();

        whiteClock = whiteTime;
        blackClock = blackTime;
        this.whiteIncrement = whiteIncrement;
        this.blackIncrement = blackIncrement;
    }

    public Board getCurrentBoard() {
        return boards.get(boards.size() - 1);
    }

    public Board getBoard(final int index) {
        return boards.get(index);
    }

    public int getNumBoards() {
        return boards.size();
    }

    public long getWhiteClock() {
        if (boards.size() == 1) {
            return whiteClock;
        } else if (getResult() == Result.Undecided
                && getCurrentBoard().getTurn() == PieceColor.White) {
            return whiteClock - (System.currentTimeMillis() - lastMoveTime);
        } else {
            return whiteClock;
        }
    }

    public long getBlackClock() {
        if (getResult() == Result.Undecided && getCurrentBoard().getTurn() == PieceColor.Black) {
            return blackClock - (System.currentTimeMillis() - lastMoveTime);
        } else {
            return blackClock;
        }
    }

    public void setWhiteClock(final int whiteClock) {
        this.whiteClock = whiteClock;
    }

    public void setBlackClock(final int blackClock) {
        this.blackClock = blackClock;
    }

    public boolean playMove(final Move move) {
        final Board current = getCurrentBoard();
        final MoveLegality legality = current.getLegality(move);
        if (legality.isLegal()) {
            if (boards.size() != 1 && getResult() == Result.Undecided
                    && getCurrentBoard().getTurn() == PieceColor.White) {
                whiteClock += whiteIncrement;
                whiteClock -= (System.currentTimeMillis() - lastMoveTime);
            } else if (boards.size() != 1 && getResult() == Result.Undecided) {
                blackClock += blackIncrement;
                blackClock -= (System.currentTimeMillis() - lastMoveTime);
            }
            lastMoveTime = System.currentTimeMillis();
            boards.add(new Board(current, move));
            return true;
        }
        return false;
    }

    public boolean playMoveAtIndex(final Move move, final int index) {
        final Board current = getBoard(index);
        final MoveLegality legality = current.getLegality(move);
        if (legality.isLegal()) {
            if (boards.size() != 1 && getResult() == Result.Undecided
                    && getCurrentBoard().getTurn() == PieceColor.White) {
                whiteClock += whiteIncrement;
                whiteClock -= (System.currentTimeMillis() - lastMoveTime);
            } else if (boards.size() != 1 && getResult() == Result.Undecided) {
                blackClock += blackIncrement;
                blackClock -= (System.currentTimeMillis() - lastMoveTime);
            }
            lastMoveTime = System.currentTimeMillis();
            if (index != getNumBoards() - 1) {
                boards = new ArrayList<>(boards.subList(0, index + 1));
            }
            boards.add(new Board(current, move));
            return true;
        }
        return false;
    }

    public Result getResult() {
        // Check for game ending on time
        if (whiteClock < 0) {
            return Result.BlackWinsOnTime;
        }
        if (blackClock < 0) {
            return Result.WhiteWinsOnTime;
        }

        final Board current = getCurrentBoard();

        // Check for checkmate
        if (current.isInCheckmate()) {
            return switch (current.getTurn()) {
                case White -> Result.BlackWinsByCheckmate;
                case Black -> Result.WhiteWinsByCheckmate;
            };
        }

        // Check for stalemate
        if (current.isInStalemate()) {
            return Result.DrawByStalemate;
        }

        return Result.Undecided;
    }

    @Override
    public String toString() {
        String moveHistory = "";
        for (int i = 1; i < boards.size(); i++) {
            if (moveHistory != "") {
                moveHistory += "  ";
                if (i % 2 == 1) {
                    moveHistory += " ";
                }
            }
            if (i % 2 == 1) {
                moveHistory += ((i + 1) / 2) + ". ";
            }
            moveHistory += boards.get(i).getLastMove();
        }
        return moveHistory;
    }

    public String serialize() {
        String serialized = "White Clock:\t" + getWhiteClock() + "\nBlack Clock:\t"
                + getBlackClock() + "\n";
        for (int i = 1; i < boards.size(); i++) {
            if (i % 2 == 1) {
                serialized += ((i + 1) / 2) + ". " + boards.get(i).getLastMove();
            } else {
                serialized += "\t" + boards.get(i).getLastMove() + "\n";
            }
        }
        return serialized;
    }

    public static ChessGame deserialize(String serialized) throws DeserializeMoveException {
        String whiteClockStr, blackClockStr;
        try {
            serialized = serialized.substring(serialized.indexOf('\t') + 1);
            whiteClockStr = serialized.substring(0, serialized.indexOf('\n'));
            serialized = serialized.substring(serialized.indexOf('\t') + 1);
            blackClockStr = serialized.substring(0, serialized.indexOf('\n'));
            serialized = serialized.substring(serialized.indexOf('\n') + 1);
        } catch (final Exception e) {
            throw new DeserializeMoveException("unable to read clock information from file");
        }

        int whiteClock, blackClock;
        try {
            whiteClock = Integer.parseInt(whiteClockStr);
            blackClock = Integer.parseInt(blackClockStr);
        } catch (final Exception e) {
            throw new DeserializeMoveException("clock time was not an integer");
        }

        final ChessGame game = new ChessGame(Long.MAX_VALUE, 0, Long.MAX_VALUE, 0);
        final String[] moves = serialized.split("[\n\t]");
        for (String move : moves) {
            if (move.equals("")) {
                continue;
            }
            move = move.substring(move.indexOf(' ') + 1);
            game.playMove(new Move(game.getCurrentBoard(), move));
        }
        game.whiteClock = whiteClock;
        game.blackClock = blackClock;
        return game;
    }
}
