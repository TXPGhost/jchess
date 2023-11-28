package org.cis1200.chess;

import java.awt.BorderLayout;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class RunChess implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLayout(new BorderLayout());

        frame.add(new Chess());

        final JMenuBar menuBar = new JMenuBar();

        final JMenu menuBarFile = new JMenu("Game");
        menuBarFile.add(new JMenuItem("Save to file"));
        menuBarFile.add(new JMenuItem("Load from file"));
        menuBarFile.add(new JMenuItem("Go back a move"));
        menuBarFile.add(new JMenuItem("Go forward a move"));
        menuBar.add(menuBarFile);

        final JMenu menuBarRules = new JMenu("Rules");
        menuBarRules.add(new JCheckBoxMenuItem("Allow editing the past", false));
        menuBarRules.add(new JMenuItem("Edit clock settings"));
        menuBar.add(menuBarRules);

        final JMenu menuBarView = new JMenu("View");
        menuBarView.add(new JCheckBoxMenuItem("Show notation", true));
        menuBarView.add(new JCheckBoxMenuItem("Show board coordinates", false));
        menuBarView.add(new JMenuItem("Edit board colors"));
        menuBar.add(menuBarView);

        frame.add(menuBar, BorderLayout.NORTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
