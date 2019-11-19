import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import CardBoard from "../../components/cards/CardBoard";
import BiddingBoard from "../../components/bidding/BiddingBoard";
import {InjectedProps} from "../../websocket/withWebsocketConnection";
import {MessageType, PlayerBadeEvent, TopicTemplate, MoveType, CardPlayedEvent} from "../../websocket/messages/types";
import {
  GameRoundPhase,
  GameState,
  LegalBiddingMove,
  Position,
  SpecialBidding,
  Player,
  Suit,
  Trick,
  LegalPlayingMove
} from "../../game-engine/gameStateTypes";
import {gameStateInit} from "../../game-engine/gameStateInit";
import {GameStateModifier, applyMessage} from "../../game-engine/gameStateModifiers";
import {inboundGameMessageParser, outboundGameEventConverter} from "../../websocket/messages/game";
import {RouteComponentProps, withRouter} from "react-router";
import {playerIndexFromPosition} from "../../game-engine/playerPositionning";

const UPDATE_TRICK_TIMOUT_MS = 2000;

const hasChanged = (prevTrick: Trick, currTrick: Trick): boolean => {
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
      (jsonEvent: string) => this.onGameFinished(jsonEvent),
    );

    this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
    this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    // TODO replace socket endpoint by Socket endpoint template
    this.props.sendMessage(`/app/game/${this.props.gameId}/player/${this.props.username}/ready`, { message: MessageType.CLIENT_READY })
  }

  onGameFinished = (event: string) => {
    console.log("Game finished! ", event);
    this.props.history.push('/');
  };

  registerCallback = (messageType: MessageType, topic: string): void => {
    this.props.registerOnMessageReceivedCallback(
      topic,
      messageType,
      (jsonMsg: string) => this.applyMessageToState(messageType, jsonMsg),
    );
  };

  shouldComponentUpdate(nextProps: Readonly<InjectedProps>, nexState: Readonly<GameState>): boolean {
    if (this.state.currentPhase !== GameRoundPhase.MAIN) {
      return true;
    }
    return hasChanged(this.state.currentTrick, nexState.currentTrick);
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (this.state.currentPhase === GameRoundPhase.MAIN) {
      setTimeout(() => {
        this.setState(prevState =>
          new GameStateModifier(prevState).updateCurrentTrick(this.state.currentTrick).retrieveNewState()
        )
      }, UPDATE_TRICK_TIMOUT_MS)
    }

    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
      this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    }
  }

  applyMessageToState = (messageType: MessageType, jsonMsg: string) => {
    const msg = inboundGameMessageParser[messageType]!(jsonMsg);
    this.setState(prevState => applyMessage(msg, prevState))
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
    console.log("updating legal cards to play");
    let currentTrick: Trick | null;
    if (this.state.currentPhase === GameRoundPhase.MAIN) {
      legalCardsToPlay = this.state.cards[this.props.username].map(
        (card: CardValue) =>
          (this.state.legalMoves as LegalPlayingMove[]).map((m: LegalPlayingMove) => m.card).includes(card)
      );
      currentTrick = this.state.currentTrick;
    }

    const placeholderCards = new Array(8).fill(CardValue.blue_back);

    return <Container direction="column">
      <HandOfCards
          cards={placeholderCards}
          rotationDegrees={180}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.top, card)}
      />
      <Container direction="row" justifyContent="space-around" width="100%">
        <HandOfCards
          cards={placeholderCards}
          rotationDegrees={90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.left, card)}
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
          currentPhase === GameRoundPhase.MAIN && <CardBoard
            left={currentTrick!.cards[this.state.usernamesByPosition[Position.left]]}
            right={currentTrick!.cards[this.state.usernamesByPosition[Position.right]]}
            bottom={currentTrick!.cards[this.state.usernamesByPosition[Position.bottom]]}
            top={currentTrick!.cards[this.state.usernamesByPosition[Position.top]]}
          />
        }
        <HandOfCards
          cards={placeholderCards}
          rotationDegrees={-90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.right, card)}
        />
      </Container>
      <HandOfCards
        cards={cardsInHand}
        rotationDegrees={0}
        onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.bottom, card)}
        cardsBorderHighlight={legalCardsToPlay}
      />
    </Container>;
  }
}

export default withRouter(MainGameScreen);