import styled from "styled-components";

export default styled.img`
  width: 30px;
  height: 30px;
  background-color: darkgreen;
  font-weight: bold;
  text-align: center;
  line-height: 50px;
  min-width: 50px;
  min-height: 50px;
  border: 3px solid black;
  margin: 5px;
  &:hover {
    background-color: yellowgreen;
    color: darkgreen;
  }
`;