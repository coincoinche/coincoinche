import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import cards from '../../assets/cards';
import Container from "../../components/utils/Container";
import CardBoard from "../../components/cards/CardBoard";

export default class MainGameScreen extends React.Component {
  render() {
    return <Container direction="column">
      <HandOfCards cards={[cards.red_back, cards.red_back, cards.red_back, cards.red_back]} rotationDegrees={180} scale={0.8} />
      <Container direction="row" justifyContent="space-between">
        <HandOfCards cards={[cards.red_back, cards.red_back, cards.red_back, cards.red_back]} rotationDegrees={90}  scale={0.8} />
        <CardBoard
          leftCard={cards.jc}
          rightCard={cards.jd}
          bottomCard={cards.jh}
          topCard={cards.js}
        />
        <HandOfCards cards={[cards.red_back, cards.red_back, cards.red_back, cards.red_back]} rotationDegrees={-90} scale={0.8} />
      </Container>
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js]} rotationDegrees={0} />
    </Container>;
  }
}