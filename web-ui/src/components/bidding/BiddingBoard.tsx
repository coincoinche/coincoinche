import React from 'react';
import styled from "styled-components";
import ValueSelector from "./ValueSelector";
import {Contract, ContractValue, SpecialBidding, Suit} from "../../pages/MainGame/types";
import Container from "../utils/Container";
import SuitSelector from "./SuitSelector";

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

type Props = {
  contractValues: ContractValue[];
  contractSuits: Suit[];
  specialBiddings: SpecialBidding[];
  onContractPicked: (contract: Contract) => void;
};

type State = {
  selectedValue: ContractValue | null;
  selectedSuit: Suit | null;
}

export default class BiddingBoard extends React.Component<Props, State> {
  state = {
    selectedValue: null,
    selectedSuit: null,
  };

  onValueClicked = (value: ContractValue) => {
    this.setState({selectedValue: value});
  };

  onSuitClicked = (suit: Suit) => {
    this.setState({selectedSuit: suit});
  };

  componentDidUpdate() {
    const { selectedSuit, selectedValue } = this.state;
    if (selectedSuit !== null && selectedValue !== null) {
      this.props.onContractPicked({
        value: selectedValue!,
        suit: selectedSuit!,
      });
    }
  }

  render() {
    const { contractValues, contractSuits, specialBiddings } = this.props;
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
                    .map((value, index) => (
                        <ValueSelector
                            key={value}
                            selectedByOpponent={index===3}
                            disabled={index<3}
                            onClick={index<=3 ? () => {} : () => this.onValueClicked(value)}
                            selectedByPlayer={this.state.selectedValue === value}
                        >
                          {value}
                        </ValueSelector>
                    ))
              }
              {
                contractValues.includes(ContractValue.CAPOT) && (
                    <ValueSelector
                        minWidth="85px"
                        onClick={() => this.onValueClicked(ContractValue.CAPOT)}
                        selectedByPlayer={this.state.selectedValue === ContractValue.CAPOT}
                    >
                      {ContractValue.CAPOT}
                    </ValueSelector>
                )
              }
              {
                contractValues.includes(ContractValue.GENERALE) && (
                    <ValueSelector
                        minWidth="85px"
                        onClick={() => this.onValueClicked(ContractValue.GENERALE)}
                        selectedByPlayer={this.state.selectedValue === ContractValue.GENERALE}
                    >
                      {ContractValue.GENERALE}
                    </ValueSelector>
                )
              }
            </ValuesGroup>
            <SuitGroup>
              {
                contractSuits.map((suit, index) => (
                    <SuitSelector
                        src={require(`../../assets/${suit}.png`)}
                        key={suit}
                        selectedByOpponent={index===2}
                        onClick={() => this.onSuitClicked(suit)}
                        selectedByPlayer={this.state.selectedSuit === suit}
                    />
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
    )
  }
}
