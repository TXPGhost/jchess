package org.cis1200.chess;

public class Rank {
    private final int index;

    public Rank(final int rank) {
        index = rank - 1;
        if (index < 0 || index >= 8) {
            throw new IllegalArgumentException("rank must be between 1 and 8, inclusive");
        }
    }

    public Rank offsetBy(final int amount) {
        return new Rank(index + 1 + amount);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (this.getClass() != o.getClass()) {
            return false;
        } else {
            return this.index == ((Rank) o).index;
        }
    }

    @Override
    public String toString() {
        return Integer.toString(index + 1);
    }
}
