=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: xanrob_
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays

  The state of the game board includes a 2D array of `Piece` objects, each of
  which either corresponds to a dynamically-dispatched subtype of `Piece`
  (i.e. `Pawn`, `Rook`, etc.), or `null`. Accessing the array is completely
  encapsulated by the `Board` class, and the indices used are `Square` objects
  which have a corresponding `Rank` and `File`.

  2. File I/O

  The game stores moves in short algebraic chess notation (i.e. `e4`, `Ke2`),
  and provides the ability to save and load games with a file chooser dialog.
  Any errors that are encountered parsing the file are reported back to the
  user in the form of an error dialog. The file also stores the time remaining
  on each player's clock, and everything else about the game state (check,
  etc.) is recreated by reading the move sequence. The sequence of moves is
  also validaded to make sure they correspond to legal moves.

  3. Inheritance and Subtyping

  All chess pieces inherit from the abstract class `Piece`, which defines
  the abstract method `getLegality(Move move)`, which determines whether
  a given move is legal, and if not, reports back the reason. This is
  overridden by each piece to define their unique moveset. In addition to
  this, `Piece` provides shared color functionality and a default constructor,
  because this behavior is shared across all pieces, and also provides a shared
  override implementation of `equals`.

  4. Complex Game Logic

  All parts of the standard chess ruleset are implemented. This includes the
  unique moveset for each piece, double pawn move from the starting square,
  en passant, promotion (with choice), all castling rules, checks, checkmate,
  stalemate, threefold repetition, and the fifty move rule. Short algebraic
  notation is also implemented, which is nontrivial because it has lots of
  exceptions and does not store complete information for each move.



===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  `ui/RunChess.java`

    Extends `JFrame`, Combines various UI components together and shows them
    in a single window. Handles the majority of user input via `ActionListeners`
    and calls corresponding methods on the other UI components.

  `ui/BoardView.java`

    Extends `JPanel`, draws the current state of the chess board to the screen
    (has a `ChessGame` instance variable). Also handles user input for the
    board including dragging pieces and keyboard shortcuts for going back and
    forth in time. Also has the ability to reset the game, flip the board, and
    many other features. Overrides `paintComponent` to draw the board. Also
    provides `MoveListener` and `BoardFlipListener` classes to add callbacks
    for when these events happen. Maintains a square shape to avoid stretching
    or shrinking the board.

  `ui/MenuBar.java`

    Extends `JMenuBar`, menu bar UI component with `Game`, `Rules`, `View`, and
    `Help` sections. This is where game properties are configured. The help
    window is also accessed here.

  `ui/SidePanel.java`

    Extends `JPanel`, rests to the right of the `BoardView`, shows crucial
    information such as whose turn it is, the sequence of moves, and the
    players' clocks.

  `ui/HelpMenu.java`

    Extends `JFrame`, help menu popup that is accessed from the `MenuBar`.
    Simply consists of a `JLabel` with HTML contents. Is shown and hidden by
    calling `setVisible` from `RunChess`.

  `ui/PromotionMenu.java`

    Extends `JFrame`, popup window to choose which piece to promote to when
    a pawn reaches the end. Choices are queen, rook, bishop, and knight. The
    frame has 4 buttons with images for each piece, updated to match the color
    of the player who is choosing. The window automatically closes once a
    choice is made.

  `ui/ClockSettings.java`

    Extends `JFrame`, popup window to choose the clock settings for the next
    game. This includes base time and time increment for both the white and
    black players (having separate options allows for "time odds" games).

  `ChessGame.java`

    Manages the state of a chess game. Stores a list of `Board` objects which
    correspond to the game "history". Also stores additional information such
    as the 50-move rule countdown, the time at which the last move was played
    (used for the clock), both players' clocks, and both players' per-move time
    increments. Also has lots of useful methods such as `playMove` and
    `getResult` (to see if the game has ended, and if so, why), and many others.
    Finally, this class also provides `serialize` and `deserialize` abilities,
    which saves and loads the game state to/from a string, respectively, which
    allows for human-readable file IO.

  `DeserializeMoveException`

    Exception that is thrown when there is an error deserializing a move or
    the chess game. Has a `message` field which is used to provide context
    for why the exception was thrown.

  `Baord.java`

    Represents the state of a single chess "board", a.k.a. a moment in time
    of the chess game. Has a `Piece[][] board` which stores the pieces on
    the board, as well as recording the last move that was played, whose turn
    it is, and the `CastlingRestrictions` that are in place (if, for example, a
    player moved their king and can no longer castle). This class also provides
    several constructors, including one that takes in an `InitialBoardState`
    that is either `Empty` or `StandardChess`, in which the initial starting
    position is created. Also provides a copy constructor and a constructor that
    takes in a `Board` and a `Move`, which creates a new board with that move
    having been played. Finally, the class also provides methods such as
    `getLegality`, which returns whether or not a given move is legal, as well
    as `getPossibleMoves`, which returns a collection of all possible moves
    from that position. It also provides methods to see if the player is in
    check, checkmate, or stalemate (and much more).

  `InitialBoardState.java`

    Simple enum used by `Board`, which has two variants, `StandardChess` and
    `Empty`.

  `Square.java`

    Encapsulates the idea of a "square" on the chess board (i.e. "f4").
    Has two instance variables, a `Rank` and a `File`. Provides two
    constructors, one which directly takes in the rank and file and produces
    a square, and another which parses a string. This class is used heavily
    to identify squares on the board and is used to index into the 2D array.
    It also overrides `equals` because this class is used a lot in comparisons.

  `Rank.java` and `File.java`

    These classes correspond to ranks and files on the chess board,
    respectively. Together, they comprise a `Square`, but they are also useful
    on their own. `Rank` is constructed with an integer (1..8), and `File` is
    constructed with a character ('a'..'f'). Internally, they use an `index`
    integer, which corresponds to an index in the 2D array, but on the interface
    level, they use these higher-level abstractions. Both classes also override
    `equals`, and provide a `getIndex` method to directly access the underlying
    index.

  `Move.java`

    Corresponds to a move on the chess board, including a starting "from"
    square, and an ending "to" square. Also stores the board on which the move
    is being played (this is cheap because of GC), which is necessary because
    moves are only legal depending on the context. Finally, also stores the
    piece which is being promoted to (if promotion is taking place), which will
    be `null` most of the time, but otherwise will either be a `Queen`, `Rook`,
    `Bishop`, or `Knight` instance. Finally, overrides `toString`, specifically
    to represent the move in algebraic notation (i.e. `Qh5`).

  `MoveLegality.java`

    An enum that stores the possible "legalities" of a given move. In addition
    to being simply "legal", a move can be a legal castle (king or queen side),
    or illegal for a variety of reasons (such as moving the wrong color piece,
    movement being blocked, moving into check, and many others). Also overrides
    `toString` to print a user-friendly message describing why the move is
    illegal.

  `Result.java`

    An enum that stores the possible results of a chess game (such as undecided,
    checkmate, stalemate, etc.). Also overrides `toString` to print a
    user-friendly message, which is displayed in the side panel. Generated by
    `getResult` in `ChessGame`.

  `CastlingRestrictions.java`

    Keeps track of which players can castle and how. Specifically, remembers for
    both white and black whether or not they can castle kingside and queenside
    (for a total of 4 boolean fields). There are three constructors, one which
    takes no arguments and sets all 4 fields to `true`, one which is a copy
    constructor, and finally, one which takes in a `CastlingRestrictions` and
    a `Move`, flipping any instance variables to `false` if castling becomes
    disallowed. Each `Board` stores its own `CastlingRestrictions` instance
    and new ones are constructed accordingly as the game progresses.

  `CastleSide.java`

    Enum with two variants, `King` and `Queen`. Corresponds to castling "sides"
    on the board, either short or long.

  `piece/Piece.java`

    Abstract class which represents a "piece" on the chess board. Stores a
    `PieceColor` which is either white or black and proviedes a constructor
    which initializes this field. Also provides a `getColor` method and
    overrides `equals`, checking to see that the color is the same in addition
    to the normal `equals` checks. Finally, provides two abstract methods,
    `getLegality`, which takes in a move and returns its legality, and
    `getPointValue`, which returns the standard point value of the given piece.

  `piece/Pawn.java` and `piece/Knight.java` and `piece/Bishop.java` and
  `piece/Rook.java` and `piece/Queen.java` and `piece/King.java`

    These classes all extend `Piece` and override `getLegality` to perform
    all the checks necessary to see if a move is legal. Also override `toString`
    to give the piece in algebraic notation (i.e. "N" for `Knight`), and
    `getPointValue` (i.e. 9 for `Queen`).

  `piece/PieceColor.java`

    Enum with two variants, `Black` and `White`, corresponding to the two colors
    in chess. Also provides a helpful `opposite` method, which flips the color,
    and a `toString` implementation.

  `piece/PieceImages.java`

    This class stores a `BufferedImage` for each of the piece-color
    combinations. These files are stored in PNG format in the `files/`
    directory. The class also provides a `getImage` method which takes in a
    `Piece` and returns its corresponding image.



- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?




- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used
  while implementing your game.

  https://commons.wikimedia.org/wiki/Category:SVG_chess_pieces
  https://www.tutorialspoint.com/swingexamples/show_error_message_dialog.htm
  https://en.wikipedia.org/wiki/Fifty-move_rule
