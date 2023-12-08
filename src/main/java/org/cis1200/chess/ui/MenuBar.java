package org.cis1200.chess.ui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
    public final JMenu game;

    public final JMenuItem newGame;
    public final JMenuItem goBackMove;
    public final JMenuItem goForwardMove;
    public final JMenuItem loadGame;
    public final JMenuItem saveGame;

    public final JMenu rules;

    public final JCheckBoxMenuItem allowEditingPast;
    public final JMenuItem editClockSettings;

    public final JMenu view;

    public final JCheckBoxMenuItem flipBoard;
    public final JCheckBoxMenuItem autoFlipBoard;
    public final JCheckBoxMenuItem showNotation;
    public final JCheckBoxMenuItem showBoardCoordinates;

    public final JMenu help;

    public final JMenuItem showHelp;

    public MenuBar() {
        super();

        // GAME SECTION
        game = new JMenu("Game");
        {
            newGame = new JMenuItem("New game");
            goBackMove = new JMenuItem("Go back a move");
            goForwardMove = new JMenuItem("Go forward a move");
            loadGame = new JMenuItem("Load game . . .");
            saveGame = new JMenuItem("Save game . . .");

            game.add(newGame);
            game.addSeparator();
            game.add(goBackMove);
            game.add(goForwardMove);
            game.addSeparator();
            game.add(loadGame);
            game.add(saveGame);
        }
        add(game);

        // RULES SECTION
        rules = new JMenu("Rules");
        {
            allowEditingPast = new JCheckBoxMenuItem("Allow editing the past");
            editClockSettings = new JMenuItem("Edit clock settings . . .");

            rules.add(allowEditingPast);
            rules.add(editClockSettings);
        }
        add(rules);

        // VIEW SECTION
        view = new JMenu("View");
        {
            flipBoard = new JCheckBoxMenuItem("Flip board");
            autoFlipBoard = new JCheckBoxMenuItem("Automatically flip board");
            showNotation = new JCheckBoxMenuItem("Show notation");
            showBoardCoordinates = new JCheckBoxMenuItem("Show board coordinates");

            view.add(flipBoard);
            view.add(autoFlipBoard);
            view.addSeparator();
            view.add(showNotation);
            view.add(showBoardCoordinates);
        }
        add(view);

        // HELP SECTION
        help = new JMenu("Help");
        {
            showHelp = new JMenuItem("Show help menu . . .");

            help.add(showHelp);
        }
        add(help);
    }
}
