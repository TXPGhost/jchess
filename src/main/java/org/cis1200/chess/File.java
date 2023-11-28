package org.cis1200.chess;

public class File {
    private int index;

    public File(char file) {
        index = (Character.toLowerCase(file)) - 'a';
        if (index < 0 || index >= 8) {
            throw new IllegalArgumentException("rank must be between 'a' and 'f', inclusive");
        }
    }

    public File offsetBy(int amount) {
        return new File((char) (index + 'a' + amount));
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
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
