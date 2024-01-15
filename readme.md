Heckmeck

This is a java implemetation of the game:
Heckmeck am bratwurm.

https://www.uplay.it/it/boardgame-heckmeck-am-bratwurmeck.html

Structure:
Project strucure is defined like this:
- main
  - java
    - CLI
    - GUI
    - Heckmeck
    - TCP
    - Utils

Core of the game is located inside Heckmeck:

- Heckmeck
  - Components          # Main items that the game relys on
    - BoardTiles        # Tiles that lay down on the board
    - Dice              # Dice, a collection of Die objects
    - Die               # One single die, method for rolling die implemented
    - Player            # Player class that stores score, name etc.
    - StackOfTiles      # Tiles that each player accumulates
    - Tile              # One single tile that contain info on the tile Number and his value
    - TilesCollection   # Interface for tiles COllections
  - Game                # Game itself with 'play()' method
  - IOHandler           # Interface for user interactions
  - Rules               # Helper for managing rules

IOHandler is the interface with which the game interacts to inbteract with players. Game is flexible to the User Interface implementation.
Implementations of IOHandler are:
- CLIIOHandler # Interface for managing command line interface
- GUIIOHandler # Interface for managing graphic user interface
- TCPIOHandler # Interface for managing TCP interface (multiplayer)

Command Line Interface (HECKMECK.CLI)
Loctaed in
- main
  - java
    - CLI
      - CLIIOHandler  # Interface
      - HeckmeckCLI   # Launcher that manages menu and runs Game and IOHandler

Graphic user Interface (HECKMECK.GUI)
- main
  - java
    - GUI
      - GUIIOHandler  # Interface
      - HeckmeckGUI   # Launcher that manages menu and runs Game and IOHandler

Multiplayer (HECKMECK.TCP)
- main
  - java
  - TCP
    - Client
      - Client          # Client that reads incoming message and interprets commands from Server
      - MessageHandler  # Handler that uses User Interface to perform the action required by the server
    - Server
      - GameServer      # Where the game actually is hosted
      - ClientHandler   # For each connected client, an handler for managing communication between client and server
    - TCPIOHandler      # Interface
    - HeckmeckGUI       # Launcher that manages menu and runs Game and IOHandler