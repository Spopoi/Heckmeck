# Heckmeck

This is a java implemetation of the game:
Heckmeck am bratwurm.

---
## How to play

For more details, see [Official page](https://www.zoch-verlag.com/zoch_en/categories/family-games/heckmeck-am-bratwurmeck-601125200-en.html?wse=1).


HeckMeck features 15 tiles numbered 21 to 36, displaying varying worm quantities, and 8 dice with numbers 1-5 and a worm image on the sixth side.

The goal is to accumulate the most worms on tiles by the end of the game.
Set up by arranging all tiles number/worm side up in the center, ordered by size.

Gameplay begins by rolling dice until either the number matches a playable tile or the player goes bust.
When selecting dice, consider the remaining tiles and opponents' collections. Calculate points based on face value and worms (each worth 5 points).
Keep only one set of numbers per roll and re-roll unused dice. Players continue until they meet their goal or run out of options.

If a player fails to match a tile or lacks a worm in chosen dice, they bust. The highest value tile in the center is turned over and out of play.
Players collect faced-up tiles, stealing the top one from opponents if the rolled number matches. Exact matches are required.
If the rolled number falls between two available tiles, players can pick up the lower one.

When no tiles remain in the center, players add up worm counts on their tiles. The player with the most worms wins; focus on worm counts, not tile values.
  
***
# Features:
- Command Line Interface
- Graphical User Interface (with Swing)
- Multiplayer functionality
- Game is flexible on User Interface 

---
# Prerequisites & Tools

- **Java Version**: 18
- **IDE**: IntelliJ IDEA 2022.2.5
- **Build Automation Tool**: Gradle 7.5.1
- **Version Control System**: Git + GitHub
- **Continuous Integration**: CircleCI
- **JSON Management**: gson 2.10

---
# Testing

The project uses the following testing frameworks:

- **Test Framework**: JUnit 4
- **Mocking Framework**: Mockito 2.22


---
# Structure:
Project structure is defined like this:
- main
  - java
    - CLI
    - GUI
    - Heckmeck
    - TCP
    - Utils

| Package  | Description                             |
|----------|-----------------------------------------|
| CLI      | Command Line Interface + CLI launcher   |
| GUI      | Graphical User Interface + GUI launcher |
| Heckmeck | The GAme itself and its elements        |
| TCP      | Manages multiplayer functionalities     |
| Utils    | Utility module                          |


---
# How to run:
## Command Line Interface:

```
./gradlew run 
```
## Graphic Interface:
```
./gradlew runGUI
```
## Docker container (CLI version):
If you have docker installed, just run this command:

```
docker run -it --rm dew54/heckmeck:cli
```
For containerized GUI version please contact the authors.

---
# Authors
- Kevin Marzio - [marzio92@gmail.com](mailto:marzio92@gmail.com)
- Davide Panarella - [davidepanarella.ts@gmail.com](mailto:davidepanarella.ts@gmail.com)
- Davide Vidmar - [dvdvdm96@gmail.com](mailto:dvdvdm96@gmail.com)


## Repo Url
https://github.com/Spopoi/Heckmeck.git
