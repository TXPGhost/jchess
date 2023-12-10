package org.cis1200.chess.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.cis1200.chess.piece.Bishop;
import org.cis1200.chess.piece.Knight;
import org.cis1200.chess.piece.Piece;
import org.cis1200.chess.piece.PieceColor;
import org.cis1200.chess.piece.PieceImages;
import org.cis1200.chess.piece.Queen;
import org.cis1200.chess.piece.Rook;

public class PromotionMenu extends JFrame {
    private final PieceImages pieceImages;

    private final JButton queen;
    private final JButton knight;
    private final JButton bishop;
    private final JButton rook;

    private Piece whitePiece;
    private Piece blackPiece;

    public PromotionMenu(final PieceImages pieceImages) {
        super();

        this.pieceImages = pieceImages;

        getContentPane().setLayout(new GridLayout(2, 2));

        queen = new JButton();
        knight = new JButton();
        bishop = new JButton();
        rook = new JButton();

        whitePiece = new Queen(PieceColor.White);
        blackPiece = new Queen(PieceColor.Black);

        add(queen);
        add(knight);
        add(bishop);
        add(rook);
    }

    public Piece getPromotionPiece(final PieceColor color) {
        if (color == PieceColor.White) {
            return whitePiece;
        } else {
            return blackPiece;
        }
    }

    public void setPromotionPiece(final PieceColor color, final Piece promotionPiece) {
        if (color == PieceColor.White) {
            whitePiece = promotionPiece;
        } else {
            blackPiece = promotionPiece;
        }
    }

    public void updatePromotionPiece(final PieceColor color) {
        setPromotionPiece(color, null);

        queen.setIcon(new ImageIcon(pieceImages.getImage(new Queen(color))));
        knight.setIcon(new ImageIcon(pieceImages.getImage(new Knight(color))));
        bishop.setIcon(new ImageIcon(pieceImages.getImage(new Bishop(color))));
        rook.setIcon(new ImageIcon(pieceImages.getImage(new Rook(color))));

        setTitle("Set " + color + " promotion piece");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        queen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                setPromotionPiece(color, new Queen(color));
                setVisible(false);
            }
        });
        knight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                setPromotionPiece(color, new Knight(color));
                setVisible(false);
            }
        });
        bishop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                setPromotionPiece(color, new Bishop(color));
                setVisible(false);
            }
        });
        rook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                setPromotionPiece(color, new Rook(color));
                setVisible(false);
            }
        });
    }
}
