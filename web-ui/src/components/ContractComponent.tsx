import React from 'react';
import styled from "styled-components";
import { Suit } from '../game-engine/gameStateTypes';

type Props = {
  owner: string | null,
  value: number | null,
  suit: Suit | null,
  multiplier: number
}

const emojiFromSuit = (suit: string | null): string => {
  switch (suit) {
    case "s":
      return "♠";
    case "h":
      return "♥";
    case "c":
      return "♣";
    case "d":
      return "♦";
    default:
      return "x";
  }
}

const ContractContainer = styled.div`
    width: 200px;
    margin: 15px;
    background-color: #FFFFFF;
    padding: 10px;
    border-style: solid;
    border-radius: 20px;
`

const ContractComponent = ({ owner, value, suit, multiplier }: Props) => {
  return (
    <ContractContainer>
      <b>Contrat actuel</b>
      <br />
      {
        value && `
                ${value}${emojiFromSuit(suit)}
                par ${owner}
                `
      }
      {
        value !== null || "-"
      }
      <br />Multiplicateur : {multiplier}
    </ContractContainer>
  );
};

export default ContractComponent;