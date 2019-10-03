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
}

type ContainerProps = {
  rotationDegrees: number;
}

const Container = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  transform: ${({rotationDegrees}: ContainerProps) => `rotate(${rotationDegrees}deg)`};
`;

const ROTATION_MAX_DEGREE_ANGLE = 20;

const HandOfCards = ({cards, rotationDegrees, scale, onCardPlayed, cardsBorderHighlight}: Props) => {
  const rotationStep = cards.length > 1 ? 2 * ROTATION_MAX_DEGREE_ANGLE / (cards.length - 1) : 0;
  const yTranslationMax = 15 * cards.length;
  const yTranslationStep = 2 * yTranslationMax / (cards.length - 1);
  return (
      <Container rotationDegrees={rotationDegrees}>
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
                />
            )
          })
        }
      </Container>
  );
};

export default HandOfCards;
