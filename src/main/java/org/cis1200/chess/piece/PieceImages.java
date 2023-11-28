package org.cis1200.chess.piece;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PieceImages {
    public final BufferedImage WHITE_PAWN;
    public final BufferedImage WHITE_KNIGHT;
    public final BufferedImage WHITE_BISHOP;
    public final BufferedImage WHITE_ROOK;
    public final BufferedImage WHITE_QUEEN;
    public final BufferedImage WHITE_KING;

    public final BufferedImage BLACK_PAWN;
    public final BufferedImage BLACK_KNIGHT;
    public final BufferedImage BLACK_BISHOP;
    public final BufferedImage BLACK_ROOK;
    public final BufferedImage BLACK_QUEEN;
    public final BufferedImage BLACK_KING;

    public final BufferedImage MOVE_DOT;

    public PieceImages() throws IOException {
        WHITE_PAWN = ImageIO.read(new File("files/white_pawn.png"));
        WHITE_KNIGHT = ImageIO.read(new File("files/white_knight.png"));
        WHITE_BISHOP = ImageIO.read(new File("files/white_bishop.png"));
        WHITE_ROOK = ImageIO.read(new File("files/white_rook.png"));
        WHITE_QUEEN = ImageIO.read(new File("files/white_queen.png"));
        WHITE_KING = ImageIO.read(new File("files/white_king.png"));

        BLACK_PAWN = ImageIO.read(new File("files/black_pawn.png"));
        BLACK_KNIGHT = ImageIO.read(new File("files/black_knight.png"));
        BLACK_BISHOP = ImageIO.read(new File("files/black_bishop.png"));
        BLACK_ROOK = ImageIO.read(new File("files/black_rook.png"));
        BLACK_QUEEN = ImageIO.read(new File("files/black_queen.png"));
        BLACK_KING = ImageIO.read(new File("files/black_king.png"));

        MOVE_DOT = ImageIO.read(new File("files/move_dot.png"));
    }

    public BufferedImage getImage(Piece p) {
        if (p.getClass() == Pawn.class) {
            return switch (p.getColor()) {
                case White -> WHITE_PAWN;
                case Black -> BLACK_PAWN;
            };
        }
        if (p.getClass() == Knight.class) {
            return switch (p.getColor()) {
                case White -> WHITE_KNIGHT;
                case Black -> BLACK_KNIGHT;
            };
        }
        if (p.getClass() == Bishop.class) {
            return switch (p.getColor()) {
                case White -> WHITE_BISHOP;
                case Black -> BLACK_BISHOP;
            };
        }
        if (p.getClass() == Rook.class) {
            return switch (p.getColor()) {
                case White -> WHITE_ROOK;
                case Black -> BLACK_ROOK;
            };
        }
        if (p.getClass() == Queen.class) {
            return switch (p.getColor()) {
                case White -> WHITE_QUEEN;
                case Black -> BLACK_QUEEN;
            };
        }
        if (p.getClass() == King.class) {
            return switch (p.getColor()) {
                case White -> WHITE_KING;
                case Black -> BLACK_KING;
            };
        }

        return null;
    }
}
