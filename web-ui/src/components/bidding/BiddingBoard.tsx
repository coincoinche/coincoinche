import React from 'react';
import styled from "styled-components";
import ValueSelector from "./ValueSelector";
import {ContractValue, SpecialBidding, Suit} from "../../pages/MainGame/types";
import Container from "../utils/Container";
import SuitSelector from "./SuitSelector";

type Props = {
  contractValues: ContractValue[];
  contractSuits: Suit[];
  specialBiddings: SpecialBidding[];
};

const ValuesGroup = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  width: 100%
`;

const SuitGroup = styled.div`
  display: flex;
  flex-direction: column;
`;

const Separator = styled.div`
  width: 100%;
  height: 2px;
  background-color: black;
  margin: 5px;
`;

const BiddingBoard = ({ contractValues, contractSuits, specialBiddings }: Props) => {
  return (
      <Container
          direction="column"
          backgroundColor="darkgreen"
          borderRadius="50px"
          minWidth="300px"
          padding="20px"
      >
        <Container
            direction="row"
        >
          <ValuesGroup>
            {
              contractValues
                  .filter(value => ![ContractValue.CAPOT, ContractValue.GENERALE].includes(value))
                  .map(value => (
                      <ValueSelector key={value}>
                        {value}
                      </ValueSelector>
                  ))
            }
            {
              contractValues.includes(ContractValue.CAPOT) && (
                  <ValueSelector minWidth="85px">
                    {ContractValue.CAPOT}
                  </ValueSelector>
              )
            }
            {
              contractValues.includes(ContractValue.GENERALE) && (
                  <ValueSelector minWidth="85px">
                    {ContractValue.GENERALE}
                  </ValueSelector>
              )
            }
          </ValuesGroup>
          <SuitGroup>
            {
              contractSuits.map(suit => (
                  <SuitSelector src={require(`../../assets/${suit}.png`)} key={suit} />
              ))
            }
          </SuitGroup>
        </Container>
        <Separator />
        <Container direction="row">
          {
            specialBiddings.map(bidding => (
                <ValueSelector key={bidding} minWidth="90px" >
                  {bidding}
                </ValueSelector>
            ))
          }
        </Container>
      </Container>
  );
};

export default BiddingBoard;
