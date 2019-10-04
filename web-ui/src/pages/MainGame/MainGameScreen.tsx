import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import {ContractValue, GamePhase, GameState, Position, Suit, Trick} from "./types";
import CardBoard from "../../components/cards/CardBoard";
import BiddingBoard from "../../components/bidding/BiddingBoard";

const CLEAN_TRICK_TIMOUT_MS = 2000;

const makeOtherPlayer = () => ({
  authorisedPlays: new Array(8).fill(true),
  cardsInHand: new Array(8).fill(CardValue.blue_back),
});

const players = {
  [Position.top]: makeOtherPlayer(),
  [Position.right]: makeOtherPlayer(),
  [Position.left]: makeOtherPlayer(),
  [Position.bottom]: {
    authorisedPlays: [true, true, true, false, false, true, true, false],
    cardsInHand: [CardValue.jc, CardValue.jd, CardValue.jh, CardValue.js, CardValue.ac, CardValue.ad, CardValue.ah, CardValue.as],
  }
};

const getNextPlayer = (currentPlayer: Position): Position => {
  if (currentPlayer === Position.bottom) {
    return Position.left;
  }

  if (currentPlayer === Position.left) {
    return Position.top;
  }

  if (currentPlayer === Position.top) {
    return Position.right;
  }

  if (currentPlayer === Position.right) {
    return Position.bottom;
  }
  throw new Error('Unknown position');
};

const emptyTrick: Trick = { top: undefined, left: undefined, right: undefined, bottom: undefined };

const isFull = (trick: Trick): boolean => !!trick.top && !!trick.bottom && !!trick.left && !!trick.right;

export default class MainGameScreen extends React.Component<{}, GameState> {
  state = {
    players,
    currentPlayer: Position.bottom,
    currentTrick: {...emptyTrick},
    currentPhase: GamePhase.bidding,
  };

  componentDidUpdate(): void {
    if (isFull(this.state.currentTrick)) {
      setTimeout(() => {
        this.setState({currentTrick: {...emptyTrick}})
      }, CLEAN_TRICK_TIMOUT_MS)
    }
  }

  playCard = (player: Position, card: CardValue) => {
    this.setState(prevState => {
      const player = prevState.currentPlayer;
      const playerCards = prevState.players[player].cardsInHand;
      playerCards.splice(playerCards.indexOf(card), 1);

      return ({
        currentPlayer: getNextPlayer(player),
        currentTrick: {
          ...this.state.currentTrick,
          [player]: card,
        },
        players: {
          ...prevState.players,
          [player]: {
            authorisedPlays: prevState.players[player].authorisedPlays,
            cardsInHand: playerCards,
          }
        }
      })
    })
  };

  onCardPlayed = (player: Position, card: CardValue) => {
    const cardIndexInHand = this.state.players[player].cardsInHand.indexOf(card);

    if (
        cardIndexInHand === -1 ||
        !this.state.players[player].authorisedPlays[cardIndexInHand] ||
        player !== this.state.currentPlayer
    ) {
      return;
    }

    this.playCard(player, card);
  };

  render() {
    const { players, currentTrick } = this.state;
    return <Container direction="column">
      <HandOfCards
          cards={players[Position.top].cardsInHand}
          rotationDegrees={180}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.top, card)}
      />
      <Container direction="row" justifyContent="space-around" width="100%">
        <HandOfCards
          cards={players[Position.left].cardsInHand}
          rotationDegrees={90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.left, card)}
        />
        {
          this.state.currentPhase === GamePhase.bidding && <BiddingBoard
            contractValues={Object.values(ContractValue)}
            contractSuits={Object.values(Suit)}
          />
        }
        {
          this.state.currentPhase === GamePhase.main && <CardBoard
            left={currentTrick[Position.left]}
            right={currentTrick[Position.right]}
            bottom={currentTrick[Position.bottom]}
            top={currentTrick[Position.top]}
          />
        }
        <HandOfCards
          cards={players[Position.right].cardsInHand}
          rotationDegrees={-90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.right, card)}
        />
      </Container>
      <HandOfCards
        cards={players[Position.bottom].cardsInHand}
        rotationDegrees={0}
        onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.bottom, card)}
        cardsBorderHighlight={players[Position.bottom].authorisedPlays}
      />
    </Container>;
  }
}