import styled from "styled-components";
import {
  getBackgroundColor,
  getBorderColor,
  getHoverBackgroundColor,
  getHoverBorderColor,
  SelectorProps
} from "./selector-style";

export default styled.img`
  width: 30px;
  height: 30px;
  background-color: ${(props: SelectorProps) => getBackgroundColor(props)};
  font-weight: bold;
  text-align: center;
  line-height: 50px;
  min-width: 50px;
  min-height: 50px;
  border: 3px solid ${(props: SelectorProps) => getBorderColor(props)};
  margin: 5px;
  &:hover {
    background-color: ${({disabled}: SelectorProps) => getHoverBackgroundColor(!disabled)};
    border-color: ${({disabled}: SelectorProps) => getHoverBorderColor(!disabled)};
  }
`;