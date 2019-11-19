import React from 'react';
import styled from "styled-components";
import ValueSelector from "./ValueSelector";
import Container from "../utils/Container";
import SuitSelector from "./SuitSelector";
import {LegalBiddingMove, SpecialBidding, Suit} from "../../game-engine/gameStateTypes";
import { MoveType } from '../../websocket/messages/types';

const getDisplayedValue = (value: number): string => {
  if (value === 250) {
    return 'CAPOT';
  }

  if (value === 500) {
    return 'GENERALE';
  }

  return value.toString();
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

type Props = {
  authorisedContractValues: number[];
  authorisedSpecialBiddings: SpecialBidding[];
  authorisedContractSuits: Suit[];
  onContractPicked: (contract: LegalBiddingMove) => void;
};

type State = {
  selectedValue: number | null;
  selectedSuit: Suit | null;
  selectedSpecialBidding: SpecialBidding | null;
}

export default class BiddingBoard extends React.Component<Props, State> {
  state = {
    selectedValue: null,
    selectedSuit: null,
    selectedSpecialBidding: null,
  };

  onValueClicked = (value: number) => {
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


  componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>) {
    const { selectedSuit, selectedValue, selectedSpecialBidding } = this.state;
    const {
      selectedSuit: prevSelectedSuit,
      selectedValue: prevSelectedValue,
      selectedSpecialBidding: prevSelectedSpecialBidding
    } = prevState;

    if (prevSelectedSpecialBidding === null && selectedSpecialBidding !== null) {
      this.props.onContractPicked({
        moveType: MoveType.SPECIAL_BIDDING,
        bidding: selectedSpecialBidding!,
      });
      this.setState({
        selectedValue: null,
        selectedSuit: null,
        selectedSpecialBidding : null,
      });
    }

    if ((prevSelectedSuit === null || prevSelectedValue === null) && (selectedSuit !== null && selectedValue !== null)) {
      this.props.onContractPicked({
        moveType: MoveType.CONTRACT_BIDDING,
        contract: {
          value: selectedValue!,
          suit: selectedSuit!
        }
      });
      this.setState({
        selectedValue: null,
        selectedSuit: null,
        selectedSpecialBidding : null,
      });
    }
  }

  render() {
    const { authorisedContractSuits, authorisedContractValues, authorisedSpecialBiddings } = this.props;

    const contractValues = [80, 90, 100, 110, 120, 130, 140, 150, 160, 250, 500];
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
                  const disabled = !authorisedContractValues.includes(value);
                  const onClick = disabled ? () => {} : () => this.onValueClicked(value);
                  const minWidth = [250, 500].includes(value) ? '85px' : undefined;

                  return (
                    <ValueSelector
                      minWidth={minWidth}
                      key={value}
                      disabled={disabled}
                      onClick={onClick}
                      selectedByPlayer={this.state.selectedValue === value}
                    >
                      {getDisplayedValue(value)}
                    </ValueSelector>
                  )
                })
            }
          </ValuesGroup>
          <SuitGroup>
            {
              contractSuits.map(suit => {
                const disabled = !authorisedContractSuits.includes(suit);
                const onClick = disabled ? () => {} : () => this.onSuitClicked(suit);

                return (
                  <SuitSelector
                    src={require(`../../assets/suits/${suit}.png`)}
                    key={suit}
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
                const disabled = !authorisedSpecialBiddings.includes(bidding);
                const onClick = disabled ? () => {} : () => this.onSpecialBiddingClicked(bidding);

                return (
                  <ValueSelector
                    minWidth="90px"
                    key={bidding}
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
