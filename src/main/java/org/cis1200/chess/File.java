package org.cis1200.chess;

public class File {
    private final int index;

    public File(final char file) {
        index = (Character.toLowerCase(file)) - 'a';
        if (index < 0 || index >= 8) {
            throw new IllegalArgumentException("rank must be between 'a' and 'f', inclusive");
        }
    }

    public File offsetBy(final int amount) {
        return new File((char) (index + 'a' + amount));
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
            return this.index == ((File) o).index;
        }
    }

    @Override
    public String toString() {
        return Character.toString((char) (index + 'a'));
    }
}
