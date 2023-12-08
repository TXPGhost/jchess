package org.cis1200.chess;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private List<Board> boards;

    public ChessGame() {
        boards = new ArrayList<>();
        boards.add(new Board());
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

    public boolean playMove(final Move move) {
        final Board current = getCurrentBoard();
        final MoveLegality legality = current.getLegality(move);
        if (legality.isLegal()) {
            boards.add(new Board(current, move));
            return true;
        }
        return false;
    }

    public boolean playMoveAtIndex(final Move move, final int index) {
        final Board current = getBoard(index);
        final MoveLegality legality = current.getLegality(move);
        if (legality.isLegal()) {
            if (index != getNumBoards() - 1) {
                boards = new ArrayList<>(boards.subList(0, index + 1));
            }
            boards.add(new Board(current, move));
            return true;
        }
        return false;
    }

    public Result getResult() {
        final Board current = getCurrentBoard();
        if (current.isInCheckmate()) {
            return switch (current.getTurn()) {
                case White -> Result.BlackWinsByCheckmate;
                case Black -> Result.WhiteWinsByCheckmate;
            };
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
        String serialized = "";
        for (int i = 1; i < boards.size(); i++) {
            if (i % 2 == 1) {
                serialized += ((i + 1) / 2) + ". " + boards.get(i).getLastMove();
            } else {
                serialized += "\t" + boards.get(i).getLastMove() + "\n";
            }
        }
        return serialized;
    }

    public static ChessGame deserialize(final String serialized) throws DeserializeMoveException {
        final ChessGame game = new ChessGame();
        final String[] moves = serialized.split("[\n\t]");
        for (String move : moves) {
            move = move.substring(move.indexOf(' ') + 1);
            game.playMove(new Move(game.getCurrentBoard(), move));
        }
        return game;
    }

    public static enum Result {
        Undecided,
        WhiteWinsByCheckmate,
        BlackWinsByCheckmate,
        DrawByStalemate,
        DrawByRepetition,
    }
}
