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
import org.cis1200.chess.ChessGame;
import org.cis1200.chess.Move;
import org.cis1200.chess.MoveLegality;
import org.cis1200.chess.Rank;
import org.cis1200.chess.Square;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceImages;

public class BoardView extends JPanel {
    private static final Color SQUARE_LIGHT = new Color(195, 163, 113);
    private static final Color SQUARE_DARK = new Color(113, 78, 47);

    private final PromotionMenu promotionMenu;

    private boolean canEditPast;

    private boolean flipped;
    private boolean autoFlipped;
    private boolean showCoordinates;

    private ChessGame game;
    private int viewIndex;

    private final PieceImages pieceImages;
    private Square selected;
    private int mouseX;
    private int mouseY;

    private final List<MoveListener> moveListeners;
    private final List<BoardFlipListener> boardFlipListeners;

    public BoardView(
            PieceImages pieceImages, final PromotionMenu promotionMenu, final long whiteTime,
            final long whiteIncrement,
            final long blackTime,
            final long blackIncrement
    ) {
        game = new ChessGame(whiteTime, whiteIncrement, blackTime, blackIncrement);
        viewIndex = 0;
        canEditPast = false;
        showCoordinates = true;

        this.pieceImages = pieceImages;
        this.promotionMenu = promotionMenu;

        moveListeners = new ArrayList<>();
        boardFlipListeners = new ArrayList<>();

        try {
            pieceImages = new PieceImages();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                selected = getHoveredSquare();
                repaint();
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                final Square movingTo = getHoveredSquare();

                // Play the requested move
                if (canEditPast || viewIndex == game.getNumBoards() - 1) {
                    final Board current = game.getBoard(viewIndex);

                    if (selected != null && current.getPiece(selected) != null
                            && !movingTo.equals(selected)) {
                        final Move move = new Move(
                                current, selected, movingTo,
                                promotionMenu.getPromotionPiece(current.getTurn())
                        );
                        if (game.playMoveAtIndex(move, viewIndex)) {
                            viewIndex++;

                            for (final MoveListener listener : moveListeners) {
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
            public void mouseMoved(final MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                repaint();
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                final int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_A
                        || key == KeyEvent.VK_S
                        || key == KeyEvent.VK_H || key == KeyEvent.VK_J) {
                    if (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
                        goToStart();
                    } else {
                        goBackMove();
                    }
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_D
                        || key == KeyEvent.VK_W || key == KeyEvent.VK_L
                        || key == KeyEvent.VK_K) {
                    if (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK) {
                        goToEnd();
                    } else {
                        goForwardMove();
                    }
                } else if (key == KeyEvent.VK_F) {
                    setFlipped(!getFlipped());

                    for (final BoardFlipListener bfl : boardFlipListeners) {
                        bfl.boardFlipped(getFlipped());
                    }
                }
            }
        });
    }

    public void goBackMove() {
        viewIndex = Math.max(0, viewIndex - 1);
        repaint();

    }

    public void goForwardMove() {
        viewIndex = Math.min(game.getNumBoards() - 1, viewIndex + 1);
        repaint();
    }

    public void goToStart() {
        viewIndex = 0;
        repaint();
    }

    public void goToEnd() {
        viewIndex = game.getNumBoards() - 1;
        repaint();
    }

    public void reset(
            final long whiteTime, final long whiteIncrement, final long blackTime,
            final long blackIncrement
    ) {
        game = new ChessGame(whiteTime, whiteIncrement, blackTime, blackIncrement);
        viewIndex = 0;

        // Call back listeners
        for (final MoveListener listener : moveListeners) {
            listener.movePlayed(null);
        }

        repaint();
    }

    public void setGame(final ChessGame game) {
        this.game = game;
        viewIndex = game.getNumBoards() - 1;

        // Call back listeners
        for (final MoveListener listener : moveListeners) {
            listener.movePlayed(null);
        }

        repaint();
    }

    public void setFlipped(final boolean flipped) {
        this.flipped = flipped;
        repaint();
    }

    public void setAutoFlipped(final boolean autoFlipped) {
        this.autoFlipped = autoFlipped;
        repaint();
    }

    public boolean getFlipped() {
        if (autoFlipped) {
            return viewIndex % 2 == 1;
        }

        return flipped;
    }

    public void setShowCoordinates(final boolean showCoordinates) {
        this.showCoordinates = showCoordinates;
        repaint();
    }

    public boolean getShowCoordinates() {
        return showCoordinates;
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

    public void setCanEditPast(final boolean canEditPast) {
        this.canEditPast = canEditPast;
    }

    public ChessGame getGame() {
        return game;
    }

    public Board getCurrentBoard() {
        return game.getCurrentBoard();
    }

    public Board getViewBoard() {
        return game.getBoard(viewIndex);
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;

        final Board current = getViewBoard();

        final int width = getWidth() / 8;
        final int height = getHeight() / 8;

        g.setColor(SQUARE_LIGHT);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int r = 1; r <= 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                final Rank rank = new Rank(r);
                final File file = new File(f);
                final Square s = new Square(rank, file);

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

                // Fill in the missing pixel at the board edges
                final int extraWidth = !getFlipped() ? (file.equals(new File('h')) ? 1 : 0)
                        : (file.equals(new File('a')) ? 1 : 0);
                final int extraHeight = !getFlipped() ? (rank.equals(new Rank(1)) ? 1 : 0)
                        : (rank.equals(new Rank(8)) ? 1 : 0);

                g.fillRect(x, y, width + extraWidth, height + extraHeight);

                if (!s.equals(selected)) {
                    final Piece p = current.getPiece(s);
                    if (p != null) {
                        final BufferedImage b = pieceImages.getImage(p);
                        g.drawImage(b, x, y, width, height, null);
                    }

                    if (selected != null) {
                        final Piece sel = current.getPiece(selected);

                        if (sel != null) {
                            final MoveLegality legality = current
                                    .getLegality(
                                            new Move(
                                                    current, selected, new Square(rank, file),
                                                    promotionMenu
                                                            .getPromotionPiece(current.getTurn())
                                            )
                                    );
                            if (legality == MoveLegality.Legal) {
                                g.drawImage(pieceImages.MOVE_DOT, x, y, width, height, null);
                            }
                        }
                    }
                }

                if (showCoordinates) {
                    if ((rank.getIndex() + file.getIndex()) % 2 == 0) {
                        g.setColor(SQUARE_LIGHT);
                    } else {
                        g.setColor(SQUARE_DARK);
                    }
                    if ((!getFlipped() && s.getRank().equals(new Rank(1)))
                            || (getFlipped() && s.getRank().equals(new Rank(8)))) {
                        g.drawString(s.getFile().toString(), x + 5, y + getHeight() / 8 - 5);
                    }
                    if ((!getFlipped() && s.getFile().equals(new File('h')))
                            || (getFlipped() && s.getFile().equals(new File('a')))) {
                        g.drawString(
                                s.getRank().toString().toUpperCase(), x + getWidth() / 8 - 10,
                                y + 18
                        );
                    }
                }
            }
        }

        if (selected != null)

        {
            final Piece p = current.getPiece(selected);
            if (p != null) {
                final BufferedImage b = pieceImages.getImage(p);
                g.drawImage(b, mouseX - width / 2, mouseY - width / 2, width, height, null);
            }
        }
    }

    public void addMoveListener(final MoveListener listener) {
        moveListeners.add(listener);
    }

    public void addBoardFlipListener(final BoardFlipListener listener) {
        boardFlipListeners.add(listener);
    }

    public static interface MoveListener {
        public void movePlayed(Move move);
    }

    public static interface BoardFlipListener {
        public void boardFlipped(boolean isFlipped);
    }
}
