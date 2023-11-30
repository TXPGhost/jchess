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

    public Board getBoard(int index) {
        return boards.get(index);
    }

    public int getNumBoards() {
        return boards.size();
    }

    public boolean playMove(Move move) {
        Board current = getCurrentBoard();
        MoveLegality legality = current.getLegality(move);
        if (legality.isLegal()) {
            boards.add(new Board(current, move));
            return true;
        }
        return false;
    }

    public boolean playMoveAtIndex(Move move, int index) {
        Board current = getBoard(index);
        MoveLegality legality = current.getLegality(move);
        if (legality.isLegal()) {
            if (index != getNumBoards() - 1) {
                boards = new ArrayList<>(boards.subList(0, index));
            }
            boards.add(new Board(current, move));
            return true;
        }
        return false;
    }

    public Result getResult() {
        Board current = getCurrentBoard();
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

    public static ChessGame deserialize(String serialized) throws DeserializeMoveException {
        ChessGame game = new ChessGame();
        String[] moves = serialized.split("[\n\t]");
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
