package org.cis1200.chess;

public class File {
    private int index;

    public File(char file) {
        index = (Character.toUpperCase(file)) - 'A';
        if (index < 0 || index >= 8) {
            throw new IllegalArgumentException("rank must be between 'A' and 'F', inclusive");
        }
    }

    public File offsetBy(int amount) {
        return new File((char) (index + 'A' + amount));
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
}
