import React from 'react';
import HandOfCards from "../../components/HandOfCards";
import cards from '../../assets/cards';
import styled from "styled-components";

type ContainerProps = {
  direction: 'row' | 'column';
}

const Container = styled.div`
  display: flex;
  flex-direction: ${({direction}: ContainerProps) => direction};
  align-items: center;
  justify-content: center;
  margin: 50px;
`;

export default class MainGameScreen extends React.Component {
  render() {
    return <Container direction="column">
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js]} rotationDegrees={180} scale={0.8} />
      <Container direction="row">
        <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js]} rotationDegrees={90}  scale={0.8} />
        <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js]} rotationDegrees={-90} scale={0.8} />
      </Container>
      <HandOfCards cards={[cards.jc, cards.jd, cards.jh, cards.js]} rotationDegrees={0} />
    </Container>;
  }
}