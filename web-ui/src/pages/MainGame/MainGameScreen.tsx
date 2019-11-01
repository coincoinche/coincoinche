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

const CLEAN_TRICK_TIMOUT_MS = 2000;

const isFull = (trick: Trick): boolean => !!trick.top && !!trick.bottom && !!trick.left && !!trick.right;

type Props = InjectedProps & {
  gameId: string;
  username: string;
}

type State = GameState;

const getGameTopic = (gameId: string, username: string) => TopicTemplate.GAME
  .replace('{gameId}', gameId)
  .replace('{username}', username);

const getBroadcastGameTopic = (gameId: string) => TopicTemplate.GAME_BROADCAST
  .replace('{gameId}', gameId);

export default class MainGameScreen extends React.Component<Props, State> {
  state: State = gameStateInit();

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      getGameTopic(this.props.gameId, this.props.username),
      EventType.ROUND_STARTED,
      (jsonEvent: string) => this.applyEventToState(EventType.ROUND_STARTED, jsonEvent),
    );

    [getGameTopic(this.props.gameId, this.props.username), getBroadcastGameTopic(this.props.gameId)]
      .forEach((topic: string) =>
        this.props.registerOnMessageReceivedCallback(
          topic,
          EventType.ROUND_PHASE_STARTED,
          (jsonEvent: string) => this.applyEventToState(EventType.ROUND_PHASE_STARTED, jsonEvent),
        )
      );

    this.props.registerOnMessageReceivedCallback(
      getGameTopic(this.props.gameId, this.props.username),
      EventType.TURN_STARTED,
      (jsonEvent: string) => this.applyEventToState(EventType.TURN_STARTED, jsonEvent),
    );

    this.props.registerOnMessageReceivedCallback(
      getBroadcastGameTopic(this.props.gameId),
      EventType.PLAYER_BADE,
      (jsonEvent: string) => this.applyEventToState(EventType.PLAYER_BADE, jsonEvent),
    );

    this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
    this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    // TODO replace socket endpoint by Socket endpoint template
    this.props.sendMessage(`/app/game/${this.props.gameId}/player/${this.props.username}/ready`, { type: EventType.CLIENT_READY })
  }

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
        !this.state.legalMoves.includes(card) ||
        player !== this.state.currentPlayer
    ) {
      return;
    }

    this.setState(prevState =>
      new GameStateModifier(prevState)
        .playCardFromHand(card)
        .addCardToCurrentTrick(player, card)
        .rotateCurrentPlayer()
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
      authorisedContractValues = this.state.legalMoves
        .filter(move => move.moveType === MoveType.CONTRACT_BIDDING)
        .map(move => (move as ContractBiddingMove).value);
      authorisedContractSuits = this.state.legalMoves
        .filter(move => move.moveType === MoveType.CONTRACT_BIDDING)
        .map(move => (move as ContractBiddingMove).suit);
      authorisedSpecialBiddings = this.state.legalMoves
        .filter(move => move.moveType === MoveType.SPECIAL_BIDDING)
        .map(move => (move as SpecialBiddingMove).bidding);

      lastBiddingContract = this.state.lastBiddingContract;
    }

    let legalCardsToPlay: boolean[] = [];
    let currentTrick: Trick;
    if (this.state.currentPhase === GameRoundPhase.MAIN) {
      legalCardsToPlay = this.state.cardsInHand.map(
        (cardsInHand: CardValue) => (this.state.legalMoves as CardValue[]).includes(cardsInHand)
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