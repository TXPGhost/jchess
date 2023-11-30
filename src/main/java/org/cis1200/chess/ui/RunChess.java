package org.cis1200.chess.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cis1200.chess.ChessGame;
import org.cis1200.chess.DeserializeMoveException;
import org.cis1200.chess.Move;

public class RunChess implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLayout(new BorderLayout());

        BoardView boardView = new BoardView();
        frame.add(boardView, BorderLayout.WEST);

        final JMenuBar menuBar = new JMenuBar();

        final JMenu menuBarFile = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardView.reset();
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
                boardView.goBackMove();
            }
        });

        goForwardMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardView.goForwardMove();
            }
        });

        menuBarFile.addSeparator();

        JMenuItem loadGame = new JMenuItem("Load game . . .");
        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("CHESS file", "chess"));
                if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String fileName = file.getName();
                    if (!fileName.substring(fileName.length() - 6, fileName.length()).equals(".chess")) {
                        file = new File(fileName + ".chess");
                    }
                    try {
                        String serialized = new String(Files.readAllBytes(Paths.get(file.getPath())));
                        boardView.setGame(ChessGame.deserialize(serialized));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DeserializeMoveException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        menuBarFile.add(loadGame);

        JMenuItem saveGame = new JMenuItem("Save game . . .");
        saveGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File("game.chess"));
                fc.setFileFilter(new FileNameExtensionFilter("CHESS file", "chess"));
                if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String fileName = file.getName();
                    if (!fileName.substring(fileName.length() - 6, fileName.length()).equals(".chess")) {
                        file = new File(fileName + ".chess");
                    }
                    try {
                        FileWriter writer = new FileWriter(file);
                        writer.write(boardView.getGame().serialize());
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        menuBarFile.add(saveGame);

        menuBar.add(menuBarFile);

        final JMenu menuBarRules = new JMenu("Rules");

        JCheckBoxMenuItem allowEditingPast = new JCheckBoxMenuItem("Allow editing the past", false);
        allowEditingPast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardView.setCanEditPast(allowEditingPast.getState());
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
                boardView.setAutoFlipped(autoFlipBoard.getState());
                flipBoard.setState(boardView.getFlipped());
                flipBoard.setEnabled(!autoFlipBoard.getState());
            }
        });
        flipBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardView.setAutoFlipped(false);
                boardView.setFlipped(flipBoard.getState());
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

        boardView.addMoveListener(new BoardView.MoveListener() {
            @Override
            public void movePlayed(Move move) {
                moves.setText(boardView.getGame().toString());

                ChessGame.Result result = boardView.getGame().getResult();

                if (result == ChessGame.Result.Undecided) {
                    moveIndicator.setText(switch (boardView.getCurrentBoard().getTurn()) {
                        case White -> "White";
                        case Black -> "Black";
                    } + " to move.");
                } else {
                    moveIndicator.setText(switch (result) {
                        case WhiteWinsByCheckmate -> "White wins by checkmate.";
                        case BlackWinsByCheckmate -> "Black wins by checkmate.";
                        case DrawByRepetition -> "Draw by repetition.";
                        case DrawByStalemate -> "Draw by stalemate.";
                        case Undecided -> throw new IllegalStateException();
                    });
                }

                flipBoard.setState(boardView.getFlipped());
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
                boardView.setPreferredSize(
                        new Dimension(boardView.getHeight(), boardView.getHeight()));
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
