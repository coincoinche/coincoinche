# Coincoinche engine

The `com.coincoinche.engine` package contains the coinche game engine. Here is the structure of the folder:

```
com.coincoinche.engine
├── .             -> classes for game, round, states, and moves
├── cards         -> classes related to cards
├── contracts     -> classes for contracts (decided during bidding phase)
├── game          -> abstractions around game with rotating players
├── serialization -> JSON serialization of moves and states
└── teams         -> classes related to players and teams
```

## Game logic

A coinche game is a sequence of rounds. A round is made of two phases: the bidding phase and the playing phase. Two teams of two players take part in the game. A team wins a round depending on whether the contract was successful. A team wins the game when it exceeds 1000 points.

To make a move, a player must create a move and apply it on the game. The move must be legal (one can get the legal moves at any time). This move changes the state of the game.

## Usage of the game engine

### Step 1: create players, teams, and the game

```java
Player p1 = new Player("p1");
Player p2 = new Player("p2");
Player p3 = new Player("p3");
Player p4 = new Player("p4");

Team t1 = new Team(p1, p3);
Team t2 = new Team(p2, p4);

CoincheGame game = new CoincheGame(t1, t2);
```

### Step 2: get information from the game

```java
// state of the current round
game.getCurrentRound().getState();
// current player of the current round
game.getCurrentRound().getCurrentPlayer();
// legal moves of the current round
game.getCurrentRound().getLegalMoves();
// teams
game.getRedTeam();
game.getBlueTeam();
// etc.
```

### Step 3: apply a legal move to the game

```java
// you can create the move from a JSON string
Move move = MoveFactory.createMove(jsonMove);
// get the move's result
GameResult<Team> result = move.applyOnGame(game);
```

If the move isn't legal, then an `IllegalMoveException` is raised.

### Step 4: check if the round and/or the game is finished

```java
result.isFinished();
game.isNewRound();
```

If the game isn't finished, go back to step 3 and repeat until the game is finished. Else, proceed to step 5.

### Step 5: get the winner and loser and their points

```java
Team winner = result.getWinnerTeam();
Team loser = result.getLoserTeam();
Map<Team, Integer> points = result.getTeamsPoints();
```
