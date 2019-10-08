import styled from "styled-components";
import {getBackgroundColor, getColor, getHoverBackgroundColor, getHoverColor, SelectorProps} from "./selector-style";

type Props = SelectorProps & {
  minWidth?: string;
}

export default styled.div`
  background-color: ${(props: Props) => getBackgroundColor(props)};
  font-weight: bold;
  color: ${(props: Props) => getColor(props)};
  text-align: center;
  line-height: 50px;
  min-width: ${({minWidth}: Props) => minWidth || '50px'};
  min-height: 50px;
  border-radius: 50px;
  border: 3px solid ${({disabled}: Props) => disabled ? 'darkgray' : 'black'};
  margin: 5px;
  &:hover {
    background-color: ${({selectedByOpponent, disabled}: Props) => getHoverBackgroundColor(!selectedByOpponent && !disabled)};
    color: ${({selectedByOpponent, disabled}: Props) => getHoverColor(!selectedByOpponent && !disabled)};
  }
`;