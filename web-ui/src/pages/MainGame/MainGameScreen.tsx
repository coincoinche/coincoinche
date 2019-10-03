import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import CardBoard from "../../components/cards/CardBoard";
import {GameState, Position} from "./types";

const makeOtherPlayer = () => ({
  authorisedPlays: [],
  cardsInHand: new Array(8).fill(CardValue.blue_back),
});

const players = {
  top: makeOtherPlayer(),
  right: makeOtherPlayer(),
  left: makeOtherPlayer(),
  bottom: {
    authorisedPlays: [],
    cardsInHand: [CardValue.ac, CardValue.ad, CardValue.ah, CardValue.as, CardValue.jc, CardValue.jd, CardValue.jh, CardValue.js],
  }
};

export default class MainGameScreen extends React.Component<{}, GameState> {
  state = {
    players,
    currentPlayer: Position.bottom,
    currentTrick: { topCard: undefined, leftCard: undefined, rightCard: undefined, bottomCard: undefined },
  };

  render() {
    return <Container direction="column">
      <HandOfCards cards={this.state.players.top.cardsInHand} rotationDegrees={180} scale={0.8} />
      <Container direction="row" justifyContent="space-between">
        <HandOfCards cards={this.state.players.left.cardsInHand} rotationDegrees={90}  scale={0.8} />
        <CardBoard
          leftCard={this.state.currentTrick.leftCard}
          rightCard={this.state.currentTrick.rightCard}
          bottomCard={this.state.currentTrick.bottomCard}
          topCard={this.state.currentTrick.topCard}
        />
        <HandOfCards cards={this.state.players.right.cardsInHand} rotationDegrees={-90} scale={0.8} />
      </Container>
      <HandOfCards cards={this.state.players.bottom.cardsInHand} rotationDegrees={0} />
    </Container>;
  }
}