import React from 'react';
import HandOfCards from "../../components/HandOfCards";
import cards from '../../assets/cards';

export default class MainGameScreen extends React.Component {
  render() {
    return <div>
      <HandOfCards cards={[cards.jc]} />
      <HandOfCards cards={[cards.jc, cards.jd]} />
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh]} />
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js]} />
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js, cards.js]} />
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js, cards.js, cards.js]} />
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js, cards.js, cards.js, cards.js]} />
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js, cards.js, cards.js, cards.js, cards.js]} />
    </div>;
  }
}