package org.cis1200.chess.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class HelpMenu extends JFrame {
    public final JLabel helpText;

    public HelpMenu() {
        super("Help");

        helpText = new JLabel();
        helpText.setText(
                """
                        <html>
                            <h1>Interacting with the game board</h1>

                            <ul>
                                <li>Click and drag with the mouse to move pieces</li>
                                <li>While dragging, dots will appear to show the possible moves</li>
                                <li>If you are playing with a local opponent, make sure to only move on your own turn</li>
                            </ul>

                            <h1>Modifying the board view</h2>

                            <ul>
                                <li>You can flip the board view with <em>View -> Flip Board</em> (or by pressing F)</li>
                                <li>If you would like the board to automatically flip to the current player, enable <em>View -> Automatically Flip Board</em></li>
                            </ul>
                        </html>
                                """);
        add(helpText);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
}
