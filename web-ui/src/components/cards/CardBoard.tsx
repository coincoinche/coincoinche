import React from 'react';
import Card from "./Card";
import Container from "../utils/Container";
import {Trick} from "../../pages/MainGame/types";
import cards from "../../assets/cards";

type Props = Trick;

const CardBoard = ({topCard, leftCard, rightCard, bottomCard}: Props) => {
  return (
      <Container direction="column">
        {topCard && <Card
          // @ts-ignore
          src={cards[topCard]}
          rotationDegrees={180}
        />}
        <Container direction="row" justifyContent="space-between">
          {leftCard && <Card
            // @ts-ignore
            src={cards[leftCard]}
            rotationDegrees={90}
          />}
          {rightCard && <Card
            // @ts-ignore
            src={cards[rightCard]}
            rotationDegrees={-90}
          />}
        </Container>
        {bottomCard && <Card
          // @ts-ignore
          src={cards[bottomCard]}
          rotationDegrees={0}
        />}
      </Container>
  );
};

export default CardBoard;
