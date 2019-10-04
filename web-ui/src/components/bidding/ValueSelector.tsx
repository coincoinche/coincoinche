import styled from "styled-components";

type Props = {
  minWidth?: string;
}

export default styled.div`
  background-color: darkgreen;
  font-weight: bold;
  color: yellowgreen;
  text-align: center;
  line-height: 50px;
  min-width: ${({minWidth}: Props) => minWidth || '50px'};
  min-height: 50px;
  border-radius: 50px;
  border: 3px solid black;
  margin: 5px;
  &:hover {
    background-color: yellowgreen;
    color: darkgreen;
  }
`;