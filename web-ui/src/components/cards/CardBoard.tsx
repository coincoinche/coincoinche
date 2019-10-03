import React from 'react';
import Card from "./Card";
import Container from "../utils/Container";
import {Trick} from "../../pages/MainGame/types";
import cards from "../../assets/cards";

type Props = Trick;

const CardBoard = ({top, left, right, bottom}: Props) => {
  return (
      <Container direction="column">
        {top && <Card
          // @ts-ignore
          src={cards[top]}
          rotationDegrees={180}
        />}
        <Container direction="row" justifyContent="space-between">
          {left && <Card
            // @ts-ignore
            src={cards[left]}
            rotationDegrees={90}
          />}
          {right && <Card
            // @ts-ignore
            src={cards[right]}
            rotationDegrees={-90}
          />}
        </Container>
        {bottom && <Card
          // @ts-ignore
          src={cards[bottom]}
          rotationDegrees={0}
        />}
      </Container>
  );
};

export default CardBoard;
