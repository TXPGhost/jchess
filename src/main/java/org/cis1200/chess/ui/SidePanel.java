package org.cis1200.chess.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SidePanel extends JPanel {
    public final JLabel moveIndicator;
    public final JTextArea moves;

    public final JPanel clockPanel;
    public final JLabel clockWhite;
    public final JLabel clockBlack;

    public SidePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        moveIndicator = new JLabel("White to move.");
        moveIndicator.setAlignmentX(0.5f);
        moveIndicator.setFont(new Font("Arial", Font.BOLD, 20));

        moves = new JTextArea();
        moves.setEditable(false);
        moves.setLineWrap(true);
        moves.setWrapStyleWord(true);

        clockPanel = new JPanel();
        clockPanel.setLayout(new BorderLayout());
        {
            clockWhite = new JLabel("10:00", JLabel.CENTER);
            clockBlack = new JLabel("10:00", JLabel.CENTER);

            clockPanel.add(clockWhite);
            clockPanel.add(clockBlack);
        }

        add(moveIndicator);
        add(moves);
        add(clockPanel);
    }
}
