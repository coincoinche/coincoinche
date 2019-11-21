import React from 'react';
import styled from "styled-components";
import Card from "./Card";
import cardsImages, {CardValue} from '../../assets/cards';

type Props = {
  cards: CardValue[];
  rotationDegrees: number;
  scale?: number;
  onCardPlayed: (card: CardValue) => void;
  cardsBorderHighlight?: boolean[];
  playerName: string;
  playerRating: number;
  currentPlayer: boolean;
}

type ContainerProps = {
  rotationDegrees: number;
}

type PlayerRatingProps = {
  rotationDegrees: number;
  currentPlayer: boolean;
}

const ColumnContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  transform: ${({rotationDegrees}: ContainerProps) => `rotate(${rotationDegrees}deg)`};
`

const PlayerRating = styled.div`
  margin: 15px;
  background-color: #CC3329;
  padding: 10px;
  padding-left: 20px;
  padding-right: 20px;
  border-style: solid;
  border-radius: 20px;
  transform: ${({rotationDegrees}: PlayerRatingProps) => `rotate(${rotationDegrees}deg)`};
  ${
    ({currentPlayer}: PlayerRatingProps) => currentPlayer && `
      box-shadow:
        0 0 30px 15px rgb(255, 255, 255, 0.5),
        0 0 50px 30px rgb(255, 254, 173, 0.7),
        0 0 70px 45px rgb(255, 254, 173, 0.9);
    `
  }
`

const Container = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
`;

const ROTATION_MAX_DEGREE_ANGLE = 20;

const HandOfCards = ({cards, rotationDegrees, scale, onCardPlayed, cardsBorderHighlight, playerName, playerRating, currentPlayer}: Props) => {
  const rotationStep = cards.length > 1 ? 2 * ROTATION_MAX_DEGREE_ANGLE / (cards.length - 1) : 0;
  const yTranslationMax = 15 * cards.length;
  const yTranslationStep = 2 * yTranslationMax / (cards.length - 1);
  return (
      <ColumnContainer rotationDegrees={rotationDegrees}>
      <PlayerRating rotationDegrees={-rotationDegrees} currentPlayer={currentPlayer}>
          <b>{playerName}</b>
          <br/>{playerRating}
      </PlayerRating>
      <Container>
        {
          cards.map((card, index) => {
            return (
                <Card
                    // @ts-ignore
                    src={cardsImages[card]}
                    alt="card"
                    rotationDegrees={cards.length > 1 ? -1 * ROTATION_MAX_DEGREE_ANGLE + index * rotationStep : 0}
                    translationX = {((cards.length / 2) - index) * 5 * cards.length}
                    translationY = {Math.round(Math.abs(-1 * yTranslationMax + index * yTranslationStep))}
                    scale={scale}
                    onClick={() => onCardPlayed(card)}
                    highlightBorder={cardsBorderHighlight ? cardsBorderHighlight[index] : false}
                    key={index}
                />
            )
          })
        }
      </Container>
      </ColumnContainer>
  );
};

export default HandOfCards;
