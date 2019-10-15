import styled from "styled-components";
import {
  getBackgroundColor,
  getBorderColor,
  getColor,
  getHoverBackgroundColor, getHoverBorderColor,
  getHoverColor,
  SelectorProps
} from "./selector-style";

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
  border: 3px solid ${(props: Props) => getBorderColor(props)};
  margin: 5px;
  &:hover {
    background-color: ${({disabled}: Props) => getHoverBackgroundColor(!disabled)};
    color: ${({disabled}: Props) => getHoverColor(!disabled)};
    border-color: ${({disabled}: Props) => getHoverBorderColor(!disabled)};
  }
`;