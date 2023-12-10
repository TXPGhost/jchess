package org.cis1200.chess;

public class DeserializeMoveException extends Exception {
    private String reason;

    public DeserializeMoveException(String reason) {
        this.reason = reason;
    }

    public String toString() {
        return reason;
    }
}
