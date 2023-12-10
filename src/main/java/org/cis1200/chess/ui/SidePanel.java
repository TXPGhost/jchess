package org.cis1200.chess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.cis1200.chess.ChessGame;
import org.cis1200.chess.ChessGame.Result;
import org.cis1200.chess.piece.PieceColor;

public class SidePanel extends JPanel {
    public final JLabel moveIndicator;
    public final JTextArea moves;

    public final JPanel clockPanel;
    public final JLabel clockWhite;
    public final JLabel clockBlack;

    public SidePanel() {
        super(new BorderLayout());

        moveIndicator = new JLabel();
        moveIndicator.setHorizontalAlignment(JLabel.CENTER);
        moveIndicator.setFont(new Font("Arial", Font.BOLD, 30));
        moveIndicator.setOpaque(true);

        moves = new JTextArea();
        moves.setEditable(false);
        moves.setLineWrap(true);
        moves.setWrapStyleWord(true);
        moves.setPreferredSize(new Dimension(0, 10000));
        moves.setBackground(new Color(0.95f, 0.95f, 0.95f));

        clockPanel = new JPanel();
        clockPanel.setLayout(new GridBagLayout());
        clockPanel.setBackground(Color.BLACK);
        {
            clockWhite = new JLabel("10:00", JLabel.CENTER);
            clockBlack = new JLabel("10:00", JLabel.CENTER);

            clockWhite.setFont(new Font("Arial", Font.BOLD, 70));
            clockBlack.setFont(new Font("Arial", Font.BOLD, 70));

            clockWhite.setBackground(Color.WHITE);
            clockBlack.setBackground(Color.BLACK);

            clockWhite.setForeground(Color.BLACK);
            clockBlack.setForeground(Color.WHITE);

            clockWhite.setOpaque(true);
            clockBlack.setOpaque(true);

            GridBagConstraints cw = new GridBagConstraints();
            GridBagConstraints cb = new GridBagConstraints();

            cw.fill = GridBagConstraints.HORIZONTAL;
            cw.anchor = GridBagConstraints.CENTER;
            cw.gridx = 0;
            cw.gridy = 0;
            cw.weightx = 0.5;

            cb.fill = GridBagConstraints.HORIZONTAL;
            cb.anchor = GridBagConstraints.CENTER;
            cb.gridx = 1;
            cb.gridy = 0;
            cb.weightx = 0.5;

            clockPanel.add(clockWhite, cw);
            clockPanel.add(clockBlack, cb);
        }

        add(moveIndicator, BorderLayout.NORTH);
        add(moves, BorderLayout.CENTER);
        add(clockPanel, BorderLayout.SOUTH);
    }

    private static String formatClock(long clock) {
        if (clock < 0) {
            return "0.00";
        } else if (clock >= 60 * 1000) {
            long minutes = clock / (60 * 1000);
            long seconds = (clock / 1000) - minutes * 60;
            if (seconds < 10) {
                return minutes + ":0" + seconds;
            } else {
                return minutes + ":" + seconds;
            }
        } else if (clock >= 30 * 1000) {
            return Long.toString(clock / 1000);
        } else {
            long seconds = clock / 1000;
            long hundredths = (clock % 1000) / 10;
            if (hundredths == 0) {
                return seconds + ".00";
            } else if (hundredths < 10) {
                return seconds + ".0" + hundredths;
            } else {
                return seconds + "." + hundredths;
            }
        }
    }

    public void updateWhiteClock(long clock) {
        clockWhite.setText(formatClock(clock));
    }

    public void updateBlackClock(long clock) {
        clockBlack.setText(formatClock(clock));
    }

    public void updateMoveIndicator(Result result, PieceColor turn) {
        if (result == ChessGame.Result.Undecided) {
            moveIndicator.setText(switch (turn) {
                case White -> "White";
                case Black -> "Black";
            } + " to move.");
            moveIndicator.setBackground(switch (turn) {
                case White -> Color.WHITE;
                case Black -> Color.BLACK;
            });
            moveIndicator.setForeground(switch (turn) {
                case White -> Color.BLACK;
                case Black -> Color.WHITE;
            });
        } else {
            moveIndicator.setText(switch (result) {
                case WhiteWinsByCheckmate -> "White wins by checkmate.";
                case BlackWinsByCheckmate -> "Black wins by checkmate.";
                case WhiteWinsOnTime -> "White wins on time.";
                case BlackWinsOnTime -> "Black wins on time.";
                case DrawByRepetition -> "Draw by repetition.";
                case DrawByStalemate -> "Draw by stalemate.";
                case Undecided -> throw new IllegalStateException();
            });
        }
    }
}
