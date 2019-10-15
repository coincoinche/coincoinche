import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import {
  AuthorisedBidding,
  ContractValue,
  isAuthorisedSpecialBidding} from "./types";
import CardBoard from "../../components/cards/CardBoard";
import BiddingBoard from "../../components/bidding/BiddingBoard";
import {InjectedProps} from "../../websocket/withWebsocketConnection";
import {
  EventType,
  MoveType,
  PlayerBadeEvent,
  TopicTemplate,
} from "../../websocket/events/types";
import {
  ContractBiddingMove,
  GamePhase,
  GameState,
  LegalBiddingMove, Position, SpecialBidding,
  SpecialBiddingMove, Suit, Trick
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
      (jsonEvent: string) => this.updateState(EventType.ROUND_STARTED, jsonEvent),
    );

    this.props.registerOnMessageReceivedCallback(
      getGameTopic(this.props.gameId, this.props.username),
      EventType.TURN_STARTED,
      (jsonEvent: string) => this.updateState(EventType.TURN_STARTED, jsonEvent),
    );

    this.props.registerOnMessageReceivedCallback(
      getBroadcastGameTopic(this.props.gameId),
      EventType.PLAYER_BADE,
      (jsonEvent: string) => this.updateState(EventType.PLAYER_BADE, jsonEvent),
    );

    this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
    this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    // TODO replace socket endpoint by Socket endpoint template
    this.props.sendMessage(`/app/game/${this.props.gameId}/player/${this.props.username}/ready`, { type: EventType.CLIENT_READY })
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (this.state.currentPhase === GamePhase.main && isFull(this.state.currentTrick)) {
      setTimeout(() => {
        this.setState(prevState => new GameStateModifier(prevState).setCurrentTrick({}).retrieveNewState())
      }, CLEAN_TRICK_TIMOUT_MS)
    }

    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
      this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    }
  }

  updateState = (eventType: EventType, jsonEvent: string) => {
    const event = inboundGameEventParser[eventType]!(jsonEvent);
    this.setState(prevState => applyEvent(event, prevState))
  };

  // TODO update player cards using created event CardPlayed, then applyEvent;
  updatePlayerCards = (newCards: CardValue[]) => {
    // this.setState(prevState => ({
    //   players: {
    //     ...prevState.players,
    //     [Position.bottom]: {
    //       ...prevState.players[Position.bottom],
    //       cardsInHand: newCards,
    //     }
    //   }
    // }))
  };
  playCard = (player: Position, card: CardValue) => {
    // const playerCards = this.state.players[player].cardsInHand;
    // playerCards.splice(playerCards.indexOf(card), 1);
    // this.updatePlayerCards(playerCards);
    //
    // this.setState(prevState => {
    //   const player = prevState.currentPlayer;
    //
    //   return ({
    //     currentTrick: {
    //       ...prevState.currentTrick,
    //       [player]: card,
    //     },
    //   })
    // });
    //
    // this.rotateCurrentPlayer();
  };
  onCardPlayed = (player: Position, card: CardValue) => {
    // const cardIndexInHand = this.state.players[player].cardsInHand.indexOf(card);
    //
    // if (
    //     cardIndexInHand === -1 ||
    //     !this.state.players[player].legalMoves[cardIndexInHand] ||
    //     player !== this.state.currentPlayer
    // ) {
    //   return;
    // }
    //
    // this.playCard(player, card);
  };

  // TODO use moveType here instead of AuthorisedBidding.
  onContractPicked = (contract: AuthorisedBidding) => {
    const event: PlayerBadeEvent = isAuthorisedSpecialBidding(contract) ? {
      type: EventType.PLAYER_BADE,
      moveType: MoveType.SPECIAL_BIDDING,
      bidding: contract.special,
    } : {
      type: EventType.PLAYER_BADE,
      moveType: MoveType.CONTRACT_BIDDING,
      value: contract.value,
      suit: contract.suit,
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
    if (this.state.currentPhase === GamePhase.bidding) {
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
    if (this.state.currentPhase === GamePhase.main) {
      legalCardsToPlay = this.state.legalMoves;
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
          currentPhase === GamePhase.bidding && <BiddingBoard
            authorisedContractValues={authorisedContractValues}
            authorisedSpecialBiddings={authorisedSpecialBiddings}
            authorisedContractSuits={authorisedContractSuits}
            onContractPicked={this.onContractPicked}
            lastContract={lastBiddingContract!}
          />
        }
        {
          currentPhase === GamePhase.main && <CardBoard
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