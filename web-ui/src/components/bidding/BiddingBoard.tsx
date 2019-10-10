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
  authorisedContractValues: ContractValue[];
  authorisedSpecialBiddings: SpecialBidding[];
  authorisedContractSuits: Suit[];
  lastContract?: Contract;
  onContractPicked: (contract: Contract) => void;
};

type State = {
  selectedValue: ContractValue | null;
  selectedSuit: Suit | null;
  selectedSpecialBidding: SpecialBidding | null;
}

export default class BiddingBoard extends React.Component<Props, State> {
  state = {
    selectedValue: null,
    selectedSuit: null,
    selectedSpecialBidding: null,
  };

  onValueClicked = (value: ContractValue) => {
    this.setState({
      selectedValue: value,
      selectedSpecialBidding: null,
    });
  };

  onSuitClicked = (suit: Suit) => {
    this.setState({
      selectedSuit: suit,
      selectedSpecialBidding: null,
    });
  };

  onSpecialBiddingClicked = (bidding: SpecialBidding) => {
    this.setState({
      selectedValue: null,
      selectedSuit: null,
      selectedSpecialBidding : bidding
    });
  };


  componentDidUpdate() {
    const { selectedSuit, selectedValue, selectedSpecialBidding } = this.state;

    if (selectedSpecialBidding !== null) {
      this.props.onContractPicked({
        value: (this.props.lastContract || {}).value!,
        suit: (this.props.lastContract || {}).suit!,
        specialBidding: selectedSpecialBidding!,
      });
    }

    if (selectedSuit !== null && selectedValue !== null) {
      this.props.onContractPicked({
        value: selectedValue!,
        suit: selectedSuit!,
        specialBidding: (this.props.lastContract || {}).specialBidding,
      });
    }
  }

  render() {
    const { authorisedContractValues, authorisedSpecialBiddings, authorisedContractSuits, lastContract } = this.props;
    const contractValues = Object.values(ContractValue);
    const contractSuits = Object.values(Suit);
    const specialBiddings = Object.values(SpecialBidding);

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
                .map(value => {
                  const disabled = !authorisedContractValues.includes(value) && value !== (lastContract || {}).value;
                  const onClick = disabled || value === (lastContract || {}).value ? () => {} : () => this.onValueClicked(value);
                  const minWidth = [ContractValue.CAPOT, ContractValue.GENERALE].includes(value) ? '85px' : undefined;

                  return (
                    <ValueSelector
                      minWidth={minWidth}
                      key={value}
                      selectedByOpponent={(lastContract || {}).value === value}
                      disabled={disabled}
                      onClick={onClick}
                      selectedByPlayer={this.state.selectedValue === value}
                    >
                      {value}
                    </ValueSelector>
                  )
                })
            }
          </ValuesGroup>
          <SuitGroup>
            {
              contractSuits.map(suit => {
                const disabled = !authorisedContractSuits.includes(suit) && suit !== (lastContract || {}).suit;
                const onClick = disabled ? () => {} : () => this.onSuitClicked(suit);

                return (
                  <SuitSelector
                    src={require(`../../assets/suits/${suit}.png`)}
                    key={suit}
                    selectedByOpponent={(lastContract || {}).suit === suit}
                    onClick={onClick}
                    selectedByPlayer={this.state.selectedSuit === suit}
                    disabled={disabled}
                  />
                )
              })
            }
          </SuitGroup>
        </Container>
        <Separator />
        <Container direction="row">
          {
            specialBiddings
              .map(bidding => {
                const disabled = !authorisedSpecialBiddings.includes(bidding) && bidding !== (lastContract || {}).specialBidding;
                const onClick = disabled || bidding === (lastContract || {}).specialBidding ? () => {} : () => this.onSpecialBiddingClicked(bidding);

                return (
                  <ValueSelector
                    minWidth="90px"
                    key={bidding}
                    selectedByOpponent={(lastContract || {}).specialBidding === bidding}
                    disabled={disabled}
                    onClick={onClick}
                    selectedByPlayer={this.state.selectedSpecialBidding === bidding}
                  >
                    {bidding}
                  </ValueSelector>
                )
              })
          }
        </Container>
      </Container>
    )
  }
}
