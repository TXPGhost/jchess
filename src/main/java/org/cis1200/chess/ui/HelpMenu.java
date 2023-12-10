package org.cis1200.chess.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

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
                                <li>You can change the promotion piece with <em>Game -> Set promotion piece</em></li>
                            </ul>

                            <h1>Modifying the board view</h1>

                            <ul>
                                <li>You can flip the board view with <em>View -> Flip board</em> (or by pressing F)</li>
                                <li>If you would like the board to automatically flip to the current player, enable <em>View -> Automatically flip board</em></li>
                                <li>Toggle board coordinates with <em>View -> Show board coordinates</em></li>
                            </ul>

                            <h1>Managing game state</h1>

                            <ul>
                                <li>You can start a new game with <em>Game -> New game</em></li>
                                <li>Go back a move with <em>Game -> Go back a move</em> (or by pressing Left, Down, H, J, A, or S)</li>
                                <li>Go forward a move with <em>Game -> Go forward a move</em> (or by pressing Right, Up, L, K, D, or W)</li>
                                <li>Load an existing game from a file with <em>Game -> Load game</em></li>
                                <li>Save the current game to a file with <em>Game -> Save game</em></li>
                            </ul>

                            <h1>Adjusting the rules</h1>


                            <ul>
                                <li>You can allow editing the past with <em>Rules -> Allow editing the past</em></li>
                                <li>You can adjust clock settings with <em>Rules -> Edit clock settings</em> (these changes only take effect upon starting a new game)</li>
                            </ul>
                        </html>
                                """);
        helpText.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(helpText);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
}
