import React from 'react';
import styled from "styled-components";
import ValueSelector from "./ValueSelector";
import {ContractValue, Suit} from "../../pages/MainGame/types";
import Container from "../utils/Container";
import SuitSelector from "./SuitSelector";

type Props = {
  contractValues: ContractValue[];
  contractSuits: Suit[];
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

const BiddingBoard = ({ contractValues, contractSuits }: Props) => {
  return (
      <Container
          direction="row"
          backgroundColor="darkgreen"
          borderRadius="50px"
          minWidth="300px"
          padding="20px"
      >
        <ValuesGroup>
          {
            contractValues
                .filter(value => ![ContractValue.CAPOT, ContractValue.GENERALE].includes(value))
                .map(value => (
                  <ValueSelector>
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
                <SuitSelector src={require(`../../assets/${suit}.png`)}/>
            ))
          }
        </SuitGroup>
      </Container>
  );
};

export default BiddingBoard;
