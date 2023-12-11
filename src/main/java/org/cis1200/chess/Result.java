package org.cis1200.chess;

public enum Result {
    Undecided,
    WhiteWinsByCheckmate,
    BlackWinsByCheckmate,
    DrawByStalemate,
    DrawByRepetition,
    DrawByFiftyMoveRule,
    WhiteWinsOnTime,
    BlackWinsOnTime;

    public String toString() {
        return switch (this) {
            case WhiteWinsByCheckmate -> "White wins by checkmate.";
            case BlackWinsByCheckmate -> "Black wins by checkmate.";
            case WhiteWinsOnTime -> "White wins on time.";
            case BlackWinsOnTime -> "Black wins on time.";
            case DrawByRepetition -> "Draw by repetition.";
            case DrawByStalemate -> "Draw by stalemate.";
            case DrawByFiftyMoveRule -> "Draw by fifty move rule.";
            case Undecided -> "Currently undecided.";
        };
    }
}
