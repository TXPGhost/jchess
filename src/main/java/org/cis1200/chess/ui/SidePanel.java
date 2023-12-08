package org.cis1200.chess.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
        moves.setPreferredSize(new Dimension(0, 10000));

        clockPanel = new JPanel();
        clockPanel.setLayout(new GridBagLayout());
        {
            clockWhite = new JLabel("10:00", JLabel.CENTER);
            clockBlack = new JLabel("10:00", JLabel.CENTER);

            clockWhite.setFont(new Font("Arial", Font.BOLD, 50));
            clockBlack.setFont(new Font("Arial", Font.BOLD, 50));

            clockWhite.setBackground(Color.WHITE);
            clockBlack.setBackground(Color.BLACK);

            clockWhite.setForeground(Color.BLACK);
            clockBlack.setForeground(Color.WHITE);

            clockWhite.setOpaque(true);
            clockBlack.setOpaque(true);

            GridBagConstraints cw = new GridBagConstraints();
            GridBagConstraints cb = new GridBagConstraints();

            cw.fill = GridBagConstraints.HORIZONTAL;
            cw.anchor = GridBagConstraints.CENTER;
            cw.gridx = 0;
            cw.gridy = 0;
            cw.weightx = 0.5;

            cb.fill = GridBagConstraints.HORIZONTAL;
            cb.anchor = GridBagConstraints.CENTER;
            cb.gridx = 1;
            cb.gridy = 0;
            cb.weightx = 0.5;

            clockPanel.add(clockWhite, cw);
            clockPanel.add(clockBlack, cb);
        }

        add(moveIndicator);
        add(moves);
        add(clockPanel);
    }
}
