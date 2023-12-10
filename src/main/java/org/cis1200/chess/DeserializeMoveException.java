package org.cis1200.chess;

public class DeserializeMoveException extends Exception {
    private final String reason;

    public DeserializeMoveException(final String reason) {
        this.reason = reason;
    }

    public String toString() {
        return reason;
    }
}
