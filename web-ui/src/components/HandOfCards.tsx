import React from 'react';
import styled from "styled-components";
import Card from "./cards/Card";

type Props = {
  cards: NodeRequire[];
}

const Container = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  margin: 50px;
`;

const ROTATION_MAX_DEGREE_ANGLE = 20;

const HandOfCards = ({cards}: Props) => {
  const rotationStep = cards.length > 1 ? 2 * ROTATION_MAX_DEGREE_ANGLE / (cards.length - 1) : 0;
  const yTranslationMax = 15 * cards.length;
  const yTranslationStep = 2 * yTranslationMax / (cards.length - 1);
  return (
      <Container>
        {
          cards.map((card, index) => {
            return (
                <Card
                    // @ts-ignore
                    src={card}
                    alt="card"
                    rotationDegrees={cards.length > 1 ? -1 * ROTATION_MAX_DEGREE_ANGLE + index * rotationStep : 0}
                    translationX = {((cards.length / 2) - index) * 5 * cards.length}
                    translationY = {Math.round(Math.abs(-1 * yTranslationMax + index * yTranslationStep))}
                />
            )
          })
        }
      </Container>
  );
};

export default HandOfCards;
