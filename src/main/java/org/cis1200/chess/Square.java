package org.cis1200.chess;

public class Square {
    private Rank rank;
    private File file;

    public Square(Rank rank, File file) {
        this.rank = rank;
        this.file = file;
    }

    /**
     * Constructs a new square from algebraic notation. For example, "e4" would
     * correspond to rank E and file 4.
     */
    public Square(String notation) {
        if (notation.length() != 2) {
            throw new IllegalArgumentException("notation must be a string of length 2");
        }
        this.rank = new Rank(notation.charAt(0) - '1' + 1);
        this.file = new File(notation.charAt(1));
    }

    public Square offsetBy(int rankOffset, int fileOffset) {
        return new Square(rank.offsetBy(rankOffset), file.offsetBy(fileOffset));
    }

    public Rank getRank() {
        return rank;
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (this.getClass() != o.getClass()) {
            return false;
        } else {
            return this.rank.equals(((Square) o).rank) && this.file.equals(((Square) o).file);
        }
    }
}
