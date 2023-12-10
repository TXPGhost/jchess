package org.cis1200.chess;

public class Square {
    private final Rank rank;
    private final File file;

    public Square(final Rank rank, final File file) {
        this.rank = rank;
        this.file = file;
    }

    /**
     * Constructs a new square from algebraic notation. For example, "e4" would
     * correspond to rank e and file 4.
     */
    public Square(final String notation) {
        if (notation.length() != 2) {
            throw new IllegalArgumentException("notation must be a string of length 2");
        }
        this.rank = new Rank(notation.charAt(1) - '1' + 1);
        this.file = new File(notation.charAt(0));
    }

    public Square offsetBy(final int rankOffset, final int fileOffset) {
        return new Square(rank.offsetBy(rankOffset), file.offsetBy(fileOffset));
    }

    public Rank getRank() {
        return rank;
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return rank.equals(((Square) o).rank) && file.equals(((Square) o).file);
        }
    }

    @Override
    public String toString() {
        return file.toString() + rank.toString();
    }
}
