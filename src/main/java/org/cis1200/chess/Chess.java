package org.cis1200.chess;

import java.awt.Color;
import java.awt.Dimension;
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

import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceImages;

public class Chess extends JPanel {
    private static final Color SQUARE_LIGHT = new Color(255, 255, 255);
    private static final Color SQUARE_DARK = new Color(200, 150, 100);
    private static final Color SQUARE_SELECTED = new Color(255, 255, 0);

    private List<Board> boards;
    private int currentIndex;

    private PieceImages pieceImages;
    private Square selected;
    private int mouseX;
    private int mouseY;

    public Chess() {
        boards = new ArrayList<Board>();
        boards.add(new Board());
        currentIndex = 0;

        try {
            pieceImages = new PieceImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(900, 900));
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

                // Play the move if the latest board is in play
                if (currentIndex == boards.size() - 1) {
                    Board current = boards.get(currentIndex);
                    if (selected != null && current.getPiece(selected) != null
                            && !movingTo.equals(selected)) {
                        MoveLegality legality = current.getLegality(selected, movingTo);
                        System.out.println(legality);
                        if (legality.isLegal()) {
                            boards.add(current.withMove(selected, movingTo));
                            currentIndex++;
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
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    currentIndex = Math.max(0, currentIndex - 1);

                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    currentIndex = Math.min(boards.size() - 1, currentIndex + 1);
                    repaint();
                }
            }
        });
    }

    private Square getHoveredSquare() {
        int r = 7 - mouseY * 8 / getHeight();
        int f = mouseX * 8 / getWidth();

        return new Square(new Rank(r + 1), new File((char) (f + 'A')));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        Board current = boards.get(currentIndex);

        int width = getWidth() / 8 + 1;
        int height = getHeight() / 8 + 1;

        for (int r = 0; r < 8; r++) {
            for (int f = 0; f < 8; f++) {
                int x = f * getWidth() / 8;
                int y = (7 - r) * getHeight() / 8;

                boolean isSelected = false;
                if (selected != null) {
                    boolean sameRank = selected.getRank().getIndex() == r;
                    boolean sameFile = selected.getFile().getIndex() == f;
                    isSelected = sameRank && sameFile;
                }
                if (isSelected) {
                    g.setColor(SQUARE_SELECTED);
                    g.fillRect(x, y, width, height);
                } else {
                    if ((r + f) % 2 == 0) {
                        g.setColor(SQUARE_DARK);
                    } else {
                        g.setColor(SQUARE_LIGHT);
                    }
                    g.fillRect(x, y, width, height);

                    Piece p = current.getPieceRaw(r, f);
                    if (p != null) {
                        BufferedImage b = pieceImages.getImage(p);
                        g.drawImage(b, x, y, width, height, null);
                    }

                    if (selected != null) {
                        Piece sel = current.getPiece(selected);
                        Rank rank = new Rank(r + 1);
                        File file = new File((char) (f + 'A'));

                        if (sel != null) {
                            MoveLegality legality = current.getLegality(selected, new Square(rank, file));
                            if (legality == MoveLegality.Legal) {
                                g.drawImage(pieceImages.MOVE_DOT, x, y, width, height, null);
                            }

                        }
                    }
                }
            }
        }

        if (selected != null) {
            Piece p = current.getPiece(selected);
            if (p != null) {
                BufferedImage b = pieceImages.getImage(p);
                g.drawImage(b, mouseX - width / 2, mouseY - width / 2, width, height, null);
            }
        }
    }
}
