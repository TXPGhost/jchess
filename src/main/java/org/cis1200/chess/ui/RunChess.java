package org.cis1200.chess.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cis1200.chess.ChessGame;
import org.cis1200.chess.DeserializeMoveException;
import org.cis1200.chess.Move;
import org.cis1200.chess.ui.BoardView.BoardFlipListener;

public class RunChess implements Runnable {
    public void run() {
        // Initialize the main game frame
        final JFrame frame = new JFrame("Chess");
        frame.setLayout(new BorderLayout());

        // Initialize the sub-components
        final BoardView boardView = new BoardView();
        final HelpMenu helpMenu = new HelpMenu();
        final MenuBar menuBar = new MenuBar();
        final SidePanel sidePanel = new SidePanel();

        // Add action listeners
        {
            menuBar.newGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    boardView.reset();
                }
            });
            menuBar.goBackMove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    boardView.goBackMove();
                }
            });
            menuBar.goForwardMove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    boardView.goForwardMove();
                }
            });
            menuBar.loadGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    final JFileChooser fc = new JFileChooser();
                    fc.setFileFilter(new FileNameExtensionFilter("CHESS file", "chess"));
                    if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        final String fileName = file.getName();
                        if (!fileName.substring(fileName.length() - 6, fileName.length())
                                .equals(".chess")) {
                            file = new File(fileName + ".chess");
                        }
                        try {
                            final String serialized = new String(
                                    Files.readAllBytes(Paths.get(file.getPath())));
                            boardView.setGame(ChessGame.deserialize(serialized));
                        } catch (final IOException e) {
                            e.printStackTrace();
                        } catch (final DeserializeMoveException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            menuBar.saveGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    final JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File("game.chess"));
                    fc.setFileFilter(new FileNameExtensionFilter("CHESS file", "chess"));
                    if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        final String fileName = file.getName();
                        if (!fileName.substring(fileName.length() - 6, fileName.length())
                                .equals(".chess")) {
                            file = new File(fileName + ".chess");
                        }
                        try {
                            final FileWriter writer = new FileWriter(file);
                            writer.write(boardView.getGame().serialize());
                            writer.flush();
                            writer.close();
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            menuBar.allowEditingPast.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    boardView.setCanEditPast(menuBar.allowEditingPast.getState());
                }
            });
            menuBar.autoFlipBoard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    boardView.setAutoFlipped(menuBar.autoFlipBoard.getState());
                    menuBar.flipBoard.setState(boardView.getFlipped());
                    menuBar.flipBoard.setEnabled(!menuBar.autoFlipBoard.getState());
                }
            });
            menuBar.flipBoard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    boardView.setAutoFlipped(false);
                    boardView.setFlipped(menuBar.flipBoard.getState());
                    menuBar.autoFlipBoard.setState(false);
                }
            });
            menuBar.showHelp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    helpMenu.setVisible(true);
                }
            });

            boardView.addBoardFlipListener(new BoardFlipListener() {
                @Override
                public void boardFlipped(boolean isFlipped) {
                    menuBar.flipBoard.setState(isFlipped);
                }
            });

            boardView.addMoveListener(new BoardView.MoveListener() {
                @Override
                public void movePlayed(final Move move) {
                    sidePanel.moves.setText(boardView.getGame().toString());

                    final ChessGame.Result result = boardView.getGame().getResult();

                    if (result == ChessGame.Result.Undecided) {
                        sidePanel.moveIndicator.setText(switch (boardView.getCurrentBoard().getTurn()) {
                            case White -> "White";
                            case Black -> "Black";
                        } + " to move.");
                    } else {
                        sidePanel.moveIndicator.setText(switch (result) {
                            case WhiteWinsByCheckmate -> "White wins by checkmate.";
                            case BlackWinsByCheckmate -> "Black wins by checkmate.";
                            case DrawByRepetition -> "Draw by repetition.";
                            case DrawByStalemate -> "Draw by stalemate.";
                            case Undecided -> throw new IllegalStateException();
                        });
                    }

                    menuBar.flipBoard.setState(boardView.getFlipped());
                }
            });
        }

        // Add the sub-compoonents to the main game frame
        frame.add(boardView, BorderLayout.WEST);
        frame.add(menuBar, BorderLayout.NORTH);
        frame.add(sidePanel);

        // Make sure the board remains square
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                boardView.setPreferredSize(
                        new Dimension(boardView.getHeight(), boardView.getHeight()));
            }
        });

        // Configure and show the main game frame
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 450));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
