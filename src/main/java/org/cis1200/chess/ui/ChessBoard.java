package org.cis1200.chess.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.cis1200.chess.Board;
import org.cis1200.chess.File;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Rank;
import org.cis1200.chess.Square;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;
import org.cis1200.chess.piece.PieceImages;

public class ChessBoard extends JPanel {
    private static final Color SQUARE_LIGHT = new Color(195, 163, 113);
    private static final Color SQUARE_DARK = new Color(113, 78, 47);

    private boolean canEditPast;

    private boolean flipped;
    private boolean autoFlipped;

    private List<Board> boards;
    private int currentIndex;

    private PieceImages pieceImages;
    private Square selected;
    private int mouseX;
    private int mouseY;

    private List<MoveListener> moveListeners;

    public ChessBoard() {
        boards = new ArrayList<>();
        boards.add(new Board());
        currentIndex = 0;
        canEditPast = false;
        moveListeners = new ArrayList<>();

        try {
            pieceImages = new PieceImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selected = getHoveredSquare();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Square movingTo = getHoveredSquare();

                // Play the requested move
                if (canEditPast || currentIndex == boards.size() - 1) {
                    Board current = boards.get(currentIndex);
                    if (selected != null && current.getPiece(selected) != null
                            && !movingTo.equals(selected)) {
                        Move move = new Move(current, selected, movingTo);
                        MoveLegality legality = current.getLegality(move);
                        System.out.println(legality);
                        if (legality.isLegal()) {
                            // Truncate the move list, if necessary
                            if (currentIndex != boards.size() - 1) {
                                boards = new ArrayList<>(boards.subList(0, currentIndex + 1));
                            }

                            // Add the new move
                            Board next = current.withMove(move);
                            boards.add(next);
                            currentIndex++;

                            // Check for checkmate
                            if (next.isInCheckmate()) {
                                System.out.println("Checkmate!");
                            }

                            // Call back listeners
                            for (MoveListener listener : moveListeners) {
                                listener.movePlayed(move);
                            }
                        }
                    }
                }

                selected = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_A
                        || key == KeyEvent.VK_S
                        || key == KeyEvent.VK_H || key == KeyEvent.VK_J) {
                    if (e.getModifiers() == KeyEvent.SHIFT_MASK) {
                        goToStart();
                    } else {
                        goBackMove();
                    }
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_D
                        || key == KeyEvent.VK_W || key == KeyEvent.VK_L
                        || key == KeyEvent.VK_K) {
                    if (e.getModifiers() == KeyEvent.SHIFT_MASK) {
                        goToEnd();
                    } else {
                        goForwardMove();
                    }
                } else if (key == KeyEvent.VK_F) {
                    setFlipped(!getFlipped());
                }
            }
        });
    }

    public void goBackMove() {
        currentIndex = Math.max(0, currentIndex - 1);
        repaint();

    }

    public void goForwardMove() {
        currentIndex = Math.min(boards.size() - 1, currentIndex + 1);
        repaint();
    }

    public void goToStart() {
        currentIndex = 0;
        repaint();
    }

    public void goToEnd() {
        currentIndex = boards.size() - 1;
        repaint();
    }

    public void reset() {
        boards = new ArrayList<>();
        boards.add(new Board());
        currentIndex = 0;

        // Call back listeners
        for (MoveListener listener : moveListeners) {
            listener.movePlayed(null);
        }

        repaint();
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
        repaint();
    }

    public void setAutoFlipped(boolean autoFlipped) {
        this.autoFlipped = autoFlipped;
        repaint();
    }

    public boolean getFlipped() {
        if (autoFlipped) {
            return currentIndex % 2 == 1;
        }

        return flipped;
    }

    private Square getHoveredSquare() {
        int r, f;

        if (getFlipped()) {
            r = mouseY * 8 / getHeight();
            f = 7 - mouseX * 8 / getWidth();
        } else {
            r = 7 - mouseY * 8 / getHeight();
            f = mouseX * 8 / getWidth();
        }

        return new Square(new Rank(r + 1), new File((char) (f + 'a')));
    }

    public boolean getCanEditPast() {
        return canEditPast;
    }

    public void setCanEditPast(boolean canEditPast) {
        this.canEditPast = canEditPast;
    }

    public String getGameNotation() {
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

    /**
     * Returns the turn of the plaer based on the current board view.
     */
    public PieceColor getCurrentTurn() {
        return boards.get(currentIndex).getTurn();
    }

    /**
     * Returns the turn of the player based on the most recent board.
     */
    public PieceColor getLatestTurn() {
        return boards.get(currentIndex).getTurn();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        Board current = boards.get(currentIndex);

        int width = getWidth() / 8 + 1;
        int height = getHeight() / 8 + 1;

        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Rank rank = new Rank(r);
                File file = new File(f);
                Square s = new Square(rank, file);

                int x, y;
                if (getFlipped()) {
                    x = (7 - file.getIndex()) * getWidth() / 8;
                    y = rank.getIndex() * getHeight() / 8;
                } else {
                    x = file.getIndex() * getWidth() / 8;
                    y = (7 - rank.getIndex()) * getHeight() / 8;
                }

                if ((rank.getIndex() + file.getIndex()) % 2 == 0) {
                    g.setColor(SQUARE_DARK);
                } else {
                    g.setColor(SQUARE_LIGHT);
                }
                g.fillRect(x, y, width, height);

                if (!s.equals(selected)) {
                    Piece p = current.getPiece(s);
                    if (p != null) {
                        BufferedImage b = pieceImages.getImage(p);
                        g.drawImage(b, x, y, width, height, null);
                    }

                    if (selected != null) {
                        Piece sel = current.getPiece(selected);

                        if (sel != null) {
                            MoveLegality legality = current
                                    .getLegality(
                                            new Move(current, selected, new Square(rank, file)));
                            if (legality == MoveLegality.Legal) {
                                g.drawImage(pieceImages.MOVE_DOT, x, y, width, height, null);
                            }
                        }
                    }

                }
            }
        }

        if (selected != null)

        {
            Piece p = current.getPiece(selected);
            if (p != null) {
                BufferedImage b = pieceImages.getImage(p);
                g.drawImage(b, mouseX - width / 2, mouseY - width / 2, width, height, null);
            }
        }
    }

    public void addMoveListener(MoveListener listener) {
        moveListeners.add(listener);
    }

    public static interface MoveListener {
        public void movePlayed(Move move);
    }

    /**
     * Returns the color of the checkmated player. Will return null if the game is
     * not over yet.
     */
    public PieceColor getWinner() {
        Board latest = boards.get(boards.size() - 1);
        if (latest.isInCheckmate()) {
            return latest.getTurn().opposite();
        }
        return null;
    }
}
