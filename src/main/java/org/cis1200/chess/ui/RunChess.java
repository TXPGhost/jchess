package org.cis1200.chess.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.cis1200.chess.Move;
import org.cis1200.chess.piece.PieceColor;

public class RunChess implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLayout(new BorderLayout());

        ChessBoard chessBoard = new ChessBoard();
        frame.add(chessBoard, BorderLayout.WEST);

        final JMenuBar menuBar = new JMenuBar();

        final JMenu menuBarFile = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard.reset();
            }
        });
        menuBarFile.add(newGame);

        menuBarFile.addSeparator();

        JMenuItem goBackMove = new JMenuItem("Go back a move");
        JMenuItem goForwardMove = new JMenuItem("Go forward a move");
        menuBarFile.add(goBackMove);
        menuBarFile.add(goForwardMove);

        goBackMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard.goBackMove();
            }
        });

        goForwardMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard.goForwardMove();
            }
        });

        menuBarFile.addSeparator();

        menuBarFile.add(new JMenuItem("Load game . . ."));
        menuBarFile.add(new JMenuItem("Save game . . ."));

        menuBar.add(menuBarFile);

        final JMenu menuBarRules = new JMenu("Rules");

        JCheckBoxMenuItem allowEditingPast = new JCheckBoxMenuItem("Allow editing the past", false);
        allowEditingPast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard.setCanEditPast(allowEditingPast.getState());
            }
        });
        menuBarRules.add(allowEditingPast);

        menuBarRules.addSeparator();

        menuBarRules.add(new JMenuItem("Edit clock settings . . ."));
        menuBar.add(menuBarRules);

        final JMenu menuBarView = new JMenu("View");

        JCheckBoxMenuItem flipBoard = new JCheckBoxMenuItem("Flip board", false);
        menuBarView.add(flipBoard);

        JCheckBoxMenuItem autoFlipBoard = new JCheckBoxMenuItem("Automatically flip board", false);
        menuBarView.add(autoFlipBoard);

        autoFlipBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard.setAutoFlipped(autoFlipBoard.getState());
                flipBoard.setState(chessBoard.getFlipped());
                flipBoard.setEnabled(!autoFlipBoard.getState());
            }
        });
        flipBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chessBoard.setAutoFlipped(false);
                chessBoard.setFlipped(flipBoard.getState());
                autoFlipBoard.setState(false);
            }
        });

        menuBarView.addSeparator();

        menuBarView.add(new JCheckBoxMenuItem("Show notation", true));
        menuBarView.add(new JCheckBoxMenuItem("Show board coordinates", false));

        menuBarView.addSeparator();

        menuBarView.add(new JMenuItem("Edit board colors . . ."));
        menuBar.add(menuBarView);

        frame.add(menuBar, BorderLayout.NORTH);

        final JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        final JLabel moveIndicator = new JLabel("White to move.");
        moveIndicator.setAlignmentX(0.5f);
        moveIndicator.setFont(new Font("Arial", Font.BOLD, 20));
        sidePanel.add(moveIndicator);

        final JTextArea moves = new JTextArea();
        moves.setEditable(false);
        moves.setLineWrap(true);
        moves.setWrapStyleWord(true);
        sidePanel.add(moves);

        chessBoard.addMoveListener(new ChessBoard.MoveListener() {
            @Override
            public void movePlayed(Move move) {
                moves.setText(chessBoard.getGameNotation());

                PieceColor winner = chessBoard.getWinner();

                if (winner == null) {
                    moveIndicator.setText(switch (chessBoard.getLatestTurn()) {
                        case White -> "White";
                        case Black -> "Black";
                    } + " to move.");
                } else {
                    moveIndicator.setText(switch (winner) {
                        case White -> "White";
                        case Black -> "Black";
                    } + " wins by checkmate.");
                }

                flipBoard.setState(chessBoard.getFlipped());
            }
        });

        final JPanel clockFrame = new JPanel();
        clockFrame.setLayout(new BorderLayout());

        final JLabel clockWhite = new JLabel("10:00", JLabel.CENTER);
        final JLabel clockBlack = new JLabel("10:00", JLabel.CENTER);
        clockFrame.add(clockWhite, BorderLayout.WEST);
        clockFrame.add(clockBlack, BorderLayout.EAST);

        sidePanel.add(clockFrame);

        frame.add(sidePanel);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                chessBoard.setPreferredSize(
                        new Dimension(chessBoard.getHeight(), chessBoard.getHeight())
                );
            }
        });

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 450));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
