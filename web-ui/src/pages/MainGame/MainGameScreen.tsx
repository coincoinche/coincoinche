import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import CardBoard from "../../components/cards/CardBoard";
import BiddingBoard from "../../components/bidding/BiddingBoard";
import {InjectedProps} from "../../websocket/withWebsocketConnection";
import {
  MessageType,
  PlayerBadeEvent,
  TopicTemplate,
  MoveType,
  CardPlayedEvent,
  GameFinishedEvent
} from "../../websocket/messages/types";
import {
  GameRoundPhase,
  GameState,
  LegalBiddingMove,
  Position,
  SpecialBidding,
  Player,
  Suit,
  Trick,
  LegalPlayingMove,
} from "../../game-engine/gameStateTypes";
import {gameStateInit} from "../../game-engine/gameStateInit";
import {GameStateModifier, applyMessage, showPreviousTrick} from "../../game-engine/gameStateModifiers";
import {inboundGameMessageParser, outboundGameEventConverter} from "../../websocket/messages/game";
import {RouteComponentProps, withRouter} from "react-router";
import {playerIndexFromPosition} from "../../game-engine/playerPositionning";
import ContractComponent from '../../components/ContractComponent';
import ScoresComponent from '../../components/ScoresComponent';

const SHOW_PREVIOUS_TRICK_TIMEOUT_MS = 2500;

const hasTrickChanged = (prevTrick: Trick, currTrick: Trick): boolean => {
  let i = 0;
  for (const username in prevTrick.cards) {
    if (Object.prototype.hasOwnProperty.call(prevTrick.cards, username)) {
      if (!Object.prototype.hasOwnProperty.call(currTrick.cards, username)) {
        return true;
      }
      if (prevTrick.cards[username] !== currTrick.cards[username]) {
        return true;
      }
      i++;
    }
  }
  let j = 0;
  for (const username in currTrick.cards) {
    if (Object.prototype.hasOwnProperty.call(currTrick.cards, username)) {
      if (!Object.prototype.hasOwnProperty.call(prevTrick.cards, username)) {
        return true;
      }
      if (currTrick.cards[username] !== prevTrick.cards[username]) {
        return true;
      }
      j++;
    }
  }
  return i !== j;
}

type Props = RouteComponentProps & InjectedProps & {
  gameId: string;
  username: string;
  users: Player[];
}

type State = GameState;

const getGameTopic = (gameId: string, username: string) => TopicTemplate.GAME
  .replace('{gameId}', gameId)
  .replace('{username}', username);

const getBroadcastGameTopic = (gameId: string) => TopicTemplate.GAME_BROADCAST
  .replace('{gameId}', gameId);

class MainGameScreen extends React.Component<Props, State> {
  state: State = gameStateInit(this.props.users, this.props.users.map(u => u.username).indexOf(this.props.username));

  componentDidMount(): void {
    const playerTopic = getGameTopic(this.props.gameId, this.props.username);
    const broadcastTopic = getBroadcastGameTopic(this.props.gameId);

    this.registerCallback(MessageType.NEW_STATE, playerTopic);
    this.registerCallback(MessageType.MOVE_BID, broadcastTopic);
    this.registerCallback(MessageType.MOVE_CARD, broadcastTopic);
    this.registerCallback(MessageType.GAME_FINISHED, playerTopic);

    this.props.registerOnMessageReceivedCallback(
      playerTopic,
      MessageType.GAME_FINISHED,
      (event: GameFinishedEvent) => this.onGameFinished(event),
    );

    this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
    this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    // TODO replace socket endpoint by Socket endpoint template
    this.props.sendMessage(`/app/game/${this.props.gameId}/player/${this.props.username}/ready`, { message: MessageType.CLIENT_READY })
  }

  onGameFinished = (event: GameFinishedEvent) => {
    console.log("Game finished! ", event);

    const { win, yourTeamPoints, otherTeamPoints, eloUpdate } = event.content;
    const bottomUsername = this.state.usernamesByPosition[Position.bottom];
    const topUsername = this.state.usernamesByPosition[Position.top];
    const leftUsername = this.state.usernamesByPosition[Position.left];
    const rightUsername = this.state.usernamesByPosition[Position.right];
    const endGameScreenProps = {
      win,
      yourTeam: {
        points: yourTeamPoints,
        players: [{
          username: bottomUsername,
          previousElo: this.getRating(bottomUsername),
          newElo: eloUpdate[bottomUsername],
        }, {
          username: topUsername,
          previousElo: this.getRating(topUsername),
          newElo: eloUpdate[topUsername],
        }],
      },
      theirTeam: {
        points: otherTeamPoints,
        players: [{
          username: leftUsername,
          previousElo: this.getRating(leftUsername),
          newElo: eloUpdate[leftUsername],
        }, {
          username: rightUsername,
          previousElo: this.getRating(rightUsername),
          newElo: eloUpdate[rightUsername],
        }],
      }
    };

    this.props.history.push("/game/end", endGameScreenProps);
  };

  registerCallback = (messageType: MessageType, topic: string): void => {
    this.props.registerOnMessageReceivedCallback(
      topic,
      messageType,
      (jsonMsg: string) => this.applyMessageToState(messageType, jsonMsg),
    );
  };

  shouldComponentUpdate(nextProps: Readonly<InjectedProps>, nexState: Readonly<GameState>): boolean {
    if (this.state.currentPhase === GameRoundPhase.BIDDING) {
      return true;
    }
    // main phase
    return hasTrickChanged(this.state.currentTrick, nexState.currentTrick)
      || this.state.showPreviousTrick !== nexState.showPreviousTrick;
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
      this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    }
  }

  applyMessageToState = (messageType: MessageType, jsonMsg: string) => {
    const msg = inboundGameMessageParser[messageType]!(jsonMsg);
    this.setState(prevState => applyMessage(msg, prevState));
    // if trick is empty, let previous trick so that players see it before it's removed
    if (this.state.currentTrick.no > 0 && Object.entries(this.state.currentTrick.cards).length === 0) {
      this.setState(prevState => showPreviousTrick(true, prevState));
      setTimeout(() => { this.setState(prevState => showPreviousTrick(false, prevState)); }, SHOW_PREVIOUS_TRICK_TIMEOUT_MS);
    }
  };

  onCardPlayed = (player: Position, card: CardValue) => {
    console.log("card was played, sending event to server...");
    if (
        this.state.currentPhase !== GameRoundPhase.MAIN ||
        !this.state.cards[this.props.username].includes(card) ||
        !(this.state.legalMoves as LegalPlayingMove[])
          .map((m: LegalPlayingMove) => m.card).includes(card) ||
        player !== this.state.currentPlayer
    ) {
      console.log("aborting");
      return;
    }
    const event: CardPlayedEvent = {
      message: MessageType.MOVE_CARD,
      playerIndex: playerIndexFromPosition(Position.bottom, this.state.usernamesByPosition, this.state.users.map(u => u.username)),
      move: { card }
    }
    const msg = outboundGameEventConverter[MessageType.MOVE_CARD](event);
    this.props.sendMessage(
      `/app/game/${this.props.gameId}/player/${this.props.username}/move`, msg
    );

    this.setState(prevState =>
      new GameStateModifier(prevState)
        .setLegalMoves([])
        .retrieveNewState()
    );
  };

  onContractPicked = (biddingMove: LegalBiddingMove) => {
    console.log("contract was picked, sending event to server");
    const event: PlayerBadeEvent = {
      type: MessageType.MOVE_BID,
      playerIndex: playerIndexFromPosition(Position.bottom, this.state.usernamesByPosition, this.state.users.map(u => u.username)),
      move: biddingMove
    };
    const msg = outboundGameEventConverter[MessageType.MOVE_BID](event);

    this.props.sendMessage(
      `/app/game/${this.props.gameId}/player/${this.props.username}/move`, msg
    );

  };

  getRating = (username: string) => this.state.users.filter((p: Player) => p.username === username)[0].rating;

  render() {
    let cardsInHand: CardValue[] = [];
    if (this.state.cards) {
      cardsInHand = this.state.cards[this.props.username] || [];
    }
    const currentPhase = this.state.currentPhase;

    let authorisedContractValues: number[] = [];
    let authorisedContractSuits: Suit[] = [];
    let authorisedSpecialBiddings: SpecialBidding[] = [];
    if (this.state.currentPhase === GameRoundPhase.BIDDING) {
      // @ts-ignore
      this.state.legalMoves.forEach((move: LegalBiddingMove) => {
        if (move.moveType === MoveType.SPECIAL_BIDDING) {
          authorisedSpecialBiddings.push(move.bidding);
          return;
        }
        // contract: every suit is legal
        authorisedContractSuits = [Suit.SPADES, Suit.HEARTS, Suit.CLUBS, Suit.DIAMONDS];
        authorisedContractValues.push(move.contract.value);
      });
    }

    let legalCardsToPlay: boolean[] = [];
    let currentTrick: Trick | null = null;
    if (this.state.currentPhase === GameRoundPhase.MAIN) {
      legalCardsToPlay = this.state.cards[this.props.username].map(
        (card: CardValue) =>
          (this.state.legalMoves as LegalPlayingMove[]).map((m: LegalPlayingMove) => m.card).includes(card)
      );
      currentTrick = this.state.currentTrick;
    }
    let previousTrick: Trick | null = null;
    if (this.state.currentPhase === GameRoundPhase.MAIN && this.state.previousTrick) {
      previousTrick = this.state.previousTrick;
    }

    const topUsername: string = this.state.usernamesByPosition[Position.top];
    const bottomUsername: string = this.state.usernamesByPosition[Position.bottom];
    const leftUsername: string = this.state.usernamesByPosition[Position.left];
    const rightUsername: string = this.state.usernamesByPosition[Position.right];
    let contractComponent = <ContractComponent
      owner={null}
      suit={null}
      value={null}
      multiplier={1}
    />
    if (this.state.highestBidding) {
      contractComponent = <ContractComponent
        owner={this.state.highestBidding.owner || null}
        suit={this.state.highestBidding.suit}
        value={this.state.highestBidding.value}
        multiplier={this.state.multiplier}
      />;
    }
    return <Container direction="column">
      <Container direction="row" justifyContent="space-around" width="80%">
      {contractComponent}
      <HandOfCards
          cards={this.state.cards[topUsername] || []}
          rotationDegrees={180}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.top, card)}
          playerName={topUsername}
          playerRating={this.getRating(topUsername)}
          currentPlayer={this.state.currentPlayer === Position.top}
      />
      <ScoresComponent youScore={this.state.scores.you} themScore={this.state.scores.them} />
      </Container>
      <Container direction="row" justifyContent="space-around" width="80%">
        <HandOfCards
          cards={this.state.cards[leftUsername] || []}
          rotationDegrees={90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.left, card)}
          playerName={leftUsername}
          playerRating={this.getRating(leftUsername)}
          currentPlayer={this.state.currentPlayer === Position.left}
        />
        {
          currentPhase === GameRoundPhase.BIDDING && <BiddingBoard
            authorisedContractValues={authorisedContractValues}
            authorisedSpecialBiddings={authorisedSpecialBiddings}
            authorisedContractSuits={authorisedContractSuits}
            onContractPicked={this.onContractPicked}
          />
        }
        {
          currentPhase === GameRoundPhase.MAIN && !this.state.showPreviousTrick && <CardBoard
            left={currentTrick!.cards[this.state.usernamesByPosition[Position.left]]}
            right={currentTrick!.cards[this.state.usernamesByPosition[Position.right]]}
            bottom={currentTrick!.cards[this.state.usernamesByPosition[Position.bottom]]}
            top={currentTrick!.cards[this.state.usernamesByPosition[Position.top]]}
          />
        }
        {
          currentPhase === GameRoundPhase.MAIN && this.state.showPreviousTrick && <CardBoard
            left={previousTrick!.cards[this.state.usernamesByPosition[Position.left]]}
            right={previousTrick!.cards[this.state.usernamesByPosition[Position.right]]}
            bottom={previousTrick!.cards[this.state.usernamesByPosition[Position.bottom]]}
            top={previousTrick!.cards[this.state.usernamesByPosition[Position.top]]}
        />
        }
        <HandOfCards
          cards={this.state.cards[rightUsername] || []}
          rotationDegrees={-90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.right, card)}
          playerName={rightUsername}
          playerRating={this.getRating(rightUsername)}
          currentPlayer={this.state.currentPlayer === Position.right}
        />
      </Container>
      <HandOfCards
        cards={cardsInHand}
        rotationDegrees={0}
        onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.bottom, card)}
        cardsBorderHighlight={legalCardsToPlay}
        playerName={bottomUsername}
        playerRating={this.getRating(bottomUsername)}
        currentPlayer={this.state.currentPlayer === Position.bottom}
      />
    </Container>;
  }
}

export default withRouter(MainGameScreen);