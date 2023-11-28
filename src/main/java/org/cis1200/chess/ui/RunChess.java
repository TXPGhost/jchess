package org.cis1200.chess.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class RunChess implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLayout(new BorderLayout());

        ChessBoard chessBoard = new ChessBoard();
        frame.add(chessBoard);

        final JMenuBar menuBar = new JMenuBar();

        final JMenu menuBarFile = new JMenu("Game");
        menuBarFile.add(new JMenuItem("Save to file"));
        menuBarFile.add(new JMenuItem("Load from file"));
        menuBarFile.add(new JMenuItem("Go back a move"));
        menuBarFile.add(new JMenuItem("Go forward a move"));
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

        menuBarRules.add(new JMenuItem("Edit clock settings"));
        menuBar.add(menuBarRules);

        final JMenu menuBarView = new JMenu("View");
        menuBarView.add(new JCheckBoxMenuItem("Show notation", true));
        menuBarView.add(new JCheckBoxMenuItem("Show board coordinates", false));
        menuBarView.add(new JMenuItem("Edit board colors"));
        menuBar.add(menuBarView);

        frame.add(menuBar, BorderLayout.NORTH);

        final JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(500, 900));

        final JLabel moveIndicator = new JLabel("White to move.");
        moveIndicator.setAlignmentX(0.5f);
        sidePanel.add(moveIndicator);

        final JTextArea moves = new JTextArea();
        moves.setEditable(false);
        moves.setLineWrap(true);
        moves.setWrapStyleWord(true);
        sidePanel.add(moves);

        chessBoard.addMoveListener(new ChessBoard.MoveListener() {
            @Override
            public void movePlayed(Move move) {
                moves.setText(chessBoard.getMoveHistory());
                moveIndicator.setText(switch (chessBoard.getTurn()) {
                    case White -> "White";
                    case Black -> "Black";
                } + " to move.");
            }
        });

        frame.add(sidePanel, BorderLayout.EAST);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
