import React from 'react';
import Card from "./Card";
import Container from "../utils/Container";

type Props = {
  topCard?: NodeRequire;
  leftCard?: NodeRequire;
  rightCard?: NodeRequire;
  bottomCard?: NodeRequire;
}

const CardBoard = ({topCard, leftCard, rightCard, bottomCard}: Props) => {
  return (
      <Container direction="column">
        {topCard && <Card
          // @ts-ignore
          src={topCard}
          rotationDegrees={180}
        />}
        <Container direction="row" justifyContent="space-between">
          {leftCard && <Card
            // @ts-ignore
            src={leftCard}
            rotationDegrees={90}
          />}
          {rightCard && <Card
            // @ts-ignore
            src={rightCard}
            rotationDegrees={-90}
          />}
        </Container>
        {bottomCard && <Card
          // @ts-ignore
          src={bottomCard}
          rotationDegrees={0}
        />}
      </Container>
  );
};

export default CardBoard;
