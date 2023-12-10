package org.cis1200.chess.ui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClockSettings extends JFrame {
    public final String[] timeStrings;
    public final long[] timeValues;

    public final String[] incrementStrings;
    public final long[] incrementValues;

    public final JPanel white;
    public final JPanel black;

    public final JComboBox<String> whiteTime;
    public final JComboBox<String> blackTime;

    public final JComboBox<String> whiteIncrement;
    public final JComboBox<String> blackIncrement;

    public final JButton confirm;

    public ClockSettings() {
        super("Edit Clock Settings");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        final long SECONDS = 1000;
        final long MINUTES = SECONDS * 60;

        timeStrings = new String[] { "0:30", "1:00", "3:00", "5:00", "10:00", "15:00", "30:00" };
        timeValues = new long[] { 30 * SECONDS, 1 * MINUTES, 3 * MINUTES, 5 * MINUTES, 10 * MINUTES,
            30 * MINUTES };

        incrementStrings = new String[] { "None", "0:01", "0:02", "0:05", "0:10", "0:30" };
        incrementValues = new long[] { 0, 1 * SECONDS, 2 * SECONDS, 5 * SECONDS, 10 * SECONDS,
            30 * SECONDS };

        white = new JPanel();
        black = new JPanel();

        whiteTime = new JComboBox<>(timeStrings);
        whiteTime.setSelectedIndex(4);
        blackTime = new JComboBox<>(timeStrings);
        blackTime.setSelectedIndex(4);

        whiteIncrement = new JComboBox<>(incrementStrings);
        whiteIncrement.setSelectedIndex(0);
        blackIncrement = new JComboBox<>(incrementStrings);
        blackIncrement.setSelectedIndex(0);

        white.add(new JLabel("White Time: "));
        white.add(whiteTime);
        black.add(new JLabel("Black Time: "));
        black.add(blackTime);

        white.add(new JLabel("White Increment: "));
        white.add(whiteIncrement);
        black.add(new JLabel("Black Increment: "));
        black.add(blackIncrement);

        confirm = new JButton("Confirm");
        confirm.setAlignmentX(0.5f);

        final JLabel warning = new JLabel("Changes will take effect upon starting a new game");
        warning.setAlignmentX(0.5f);

        add(warning);
        add(white);
        add(black);
        add(confirm);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public long getWhiteTime() {
        return timeValues[whiteTime.getSelectedIndex()];
    }

    public long getBlackTime() {
        return timeValues[blackTime.getSelectedIndex()];
    }

    public long getWhiteIncrement() {
        return incrementValues[whiteIncrement.getSelectedIndex()];
    }

    public long getBlackIncrement() {
        return incrementValues[blackIncrement.getSelectedIndex()];
    }
}
