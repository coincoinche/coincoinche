import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import CardBoard from "../../components/cards/CardBoard";
import BiddingBoard from "../../components/bidding/BiddingBoard";
import {InjectedProps} from "../../websocket/withWebsocketConnection";
import {EventType, MoveType, PlayerBadeEvent, TopicTemplate,} from "../../websocket/events/types";
import {
  ContractBiddingMove,
  ContractValue,
  GameRoundPhase,
  GameState,
  LegalBiddingMove,
  Position,
  SpecialBidding,
  SpecialBiddingMove,
  Suit,
  Trick
} from "../../game-engine/gameStateTypes";
import {gameStateInit} from "../../game-engine/gameStateInit";
import {applyEvent, GameStateModifier} from "../../game-engine/gameStateModifiers";
import {inboundGameEventParser, outboundGameEventConverter} from "../../websocket/events/game";
import {withRouter} from "react-router";
import {RouteComponentProps} from "react-router";

const CLEAN_TRICK_TIMOUT_MS = 2000;

const isFull = (trick: Trick): boolean => !!trick.top && !!trick.bottom && !!trick.left && !!trick.right;

type Props = RouteComponentProps & InjectedProps & {
  gameId: string;
  username: string;
  usernames: string[];
}

type State = GameState;

const getGameTopic = (gameId: string, username: string) => TopicTemplate.GAME
  .replace('{gameId}', gameId)
  .replace('{username}', username);

const getBroadcastGameTopic = (gameId: string) => TopicTemplate.GAME_BROADCAST
  .replace('{gameId}', gameId);

class MainGameScreen extends React.Component<Props, State> {
  state: State = gameStateInit(this.props.usernames, this.props.usernames.indexOf(this.props.username));

  componentDidMount(): void {
    const playerTopic = getGameTopic(this.props.gameId, this.props.username);
    const broadcastTopic = getBroadcastGameTopic(this.props.gameId);

    this.registerCallback(EventType.ROUND_STARTED, playerTopic);
    this.registerCallback(EventType.ROUND_PHASE_STARTED, playerTopic);
    this.registerCallback(EventType.ROUND_PHASE_STARTED, broadcastTopic);
    this.registerCallback(EventType.BIDDING_TURN_STARTED, playerTopic);
    this.registerCallback(EventType.PLAYING_TURN_STARTED, playerTopic);
    this.registerCallback(EventType.PLAYER_BADE, broadcastTopic);
    this.registerCallback(EventType.CARD_PLAYED, broadcastTopic);
    this.registerCallback(EventType.GAME_FINISHED, playerTopic);

    this.props.registerOnMessageReceivedCallback(
      playerTopic,
      EventType.GAME_FINISHED,
      (jsonEvent: string) => this.onGameFinished(jsonEvent),
    );

    this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
    this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    // TODO replace socket endpoint by Socket endpoint template
    this.props.sendMessage(`/app/game/${this.props.gameId}/player/${this.props.username}/ready`, { type: EventType.CLIENT_READY })
  }

  onGameFinished = (event: string) => {
    console.log("Game finished! ", event);
    this.props.history.push('/');
  };

  registerCallback = (eventType: EventType, topic: string): void => {
    this.props.registerOnMessageReceivedCallback(
      topic,
      eventType,
      (jsonEvent: string) => this.applyEventToState(eventType, jsonEvent),
    );
  };

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (this.state.currentPhase === GameRoundPhase.MAIN && isFull(this.state.currentTrick)) {
      setTimeout(() => {
        this.setState(prevState => new GameStateModifier(prevState).resetCurrentTrick().retrieveNewState())
      }, CLEAN_TRICK_TIMOUT_MS)
    }

    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
      this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    }
  }

  applyEventToState = (eventType: EventType, jsonEvent: string) => {
    const event = inboundGameEventParser[eventType]!(jsonEvent);
    this.setState(prevState => applyEvent(event, prevState))
  };

  onCardPlayed = (player: Position, card: CardValue) => {
    if (
        this.state.currentPhase !== GameRoundPhase.MAIN ||
        !this.state.cardsInHand.includes(card) ||
        !this.state.legalPlayingMoves.includes(card) ||
        player !== this.state.currentPlayer
    ) {
      return;
    }

    this.props.sendMessage(
      `/app/game/${this.props.gameId}/player/${this.props.username}/play`,
      outboundGameEventConverter[EventType.CARD_PLAYED]({
        type: EventType.CARD_PLAYED,
        card,
      })
    );

    this.setState(prevState =>
      new GameStateModifier(prevState)
        .playCardFromHand(card)
        .setLegalPlayingMoves([])
        .retrieveNewState()
    );
  };

  onContractPicked = (biddingMove: LegalBiddingMove) => {
    const event: PlayerBadeEvent = {
      type: EventType.PLAYER_BADE,
      ...biddingMove
    };

    this.props.sendMessage(
      `/app/game/${this.props.gameId}/player/${this.props.username}/bid`,
      outboundGameEventConverter[EventType.PLAYER_BADE](event)
    );
    this.setState(prevState => applyEvent(event, prevState))
  };

  render() {
    const { cardsInHand, currentPhase } = this.state;

    let authorisedContractValues: ContractValue[] = [];
    let authorisedContractSuits: Suit[] = [];
    let authorisedSpecialBiddings: SpecialBidding[] = [];
    let lastBiddingContract: Partial<LegalBiddingMove>;
    if (this.state.currentPhase === GameRoundPhase.BIDDING) {
      authorisedContractValues = this.state.legalBiddingMoves
        .filter(move => move.moveType === MoveType.CONTRACT_BIDDING)
        .map(move => (move as ContractBiddingMove).value);
      authorisedContractSuits = this.state.legalBiddingMoves
        .filter(move => move.moveType === MoveType.CONTRACT_BIDDING)
        .map(move => (move as ContractBiddingMove).suit);
      authorisedSpecialBiddings = this.state.legalBiddingMoves
        .filter(move => move.moveType === MoveType.SPECIAL_BIDDING)
        .map(move => (move as SpecialBiddingMove).bidding);

      lastBiddingContract = this.state.lastBiddingContract;
    }

    let legalCardsToPlay: boolean[] = [];
    let currentTrick: Trick;
    if (this.state.currentPhase === GameRoundPhase.MAIN) {
      legalCardsToPlay = this.state.cardsInHand.map(
        (cardsInHand: CardValue) => (this.state.legalPlayingMoves as CardValue[]).includes(cardsInHand)
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
            lastContract={lastBiddingContract!}
          />
        }
        {
          currentPhase === GameRoundPhase.MAIN && <CardBoard
            left={currentTrick![Position.left]}
            right={currentTrick![Position.right]}
            bottom={currentTrick![Position.bottom]}
            top={currentTrick![Position.top]}
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