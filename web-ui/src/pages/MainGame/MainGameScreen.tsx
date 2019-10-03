import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import CardBoard from "../../components/cards/CardBoard";
import {GameState, Position, Trick} from "./types";

const CLEAN_TRICK_TIMOUT_MS = 2000;

const makeOtherPlayer = () => ({
  authorisedPlays: [CardValue.blue_back],
  cardsInHand: new Array(8).fill(CardValue.blue_back),
});

const players = {
  [Position.top]: makeOtherPlayer(),
  [Position.right]: makeOtherPlayer(),
  [Position.left]: makeOtherPlayer(),
  [Position.bottom]: {
    authorisedPlays: [CardValue.ac, CardValue.ad, CardValue.ah, CardValue.as, CardValue.jc, CardValue.jd, CardValue.jh, CardValue.js],
    cardsInHand: [CardValue.jc, CardValue.jd, CardValue.jh, CardValue.js, CardValue.jc, CardValue.jd, CardValue.jh, CardValue.js],
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
    if (
        this.state.players[player].cardsInHand.indexOf(card) === -1 ||
        this.state.players[player].authorisedPlays.indexOf(card) === -1 ||
        player !== this.state.currentPlayer
    ) {
      return;
    }

    this.playCard(player, card);
  };

  render() {
    return <Container direction="column">
      <HandOfCards
          cards={this.state.players[Position.top].cardsInHand}
          rotationDegrees={180}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.top, card)}
      />
      <Container direction="row" justifyContent="space-around" width="100%">
        <HandOfCards
          cards={this.state.players[Position.left].cardsInHand}
          rotationDegrees={90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.left, card)}
        />
        <CardBoard
          left={this.state.currentTrick[Position.left]}
          right={this.state.currentTrick[Position.right]}
          bottom={this.state.currentTrick[Position.bottom]}
          top={this.state.currentTrick[Position.top]}
        />
        <HandOfCards
          cards={this.state.players[Position.right].cardsInHand}
          rotationDegrees={-90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.right, card)}
        />
      </Container>
      <HandOfCards
        cards={this.state.players[Position.bottom].cardsInHand}
        rotationDegrees={0}
        onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.bottom, card)}
      />
    </Container>;
  }
}