import styled from "styled-components";

export default styled.div`
  background-color: darkgreen;
  font-weight: bold;
  color: yellowgreen;
  text-align: center;
  line-height: 50px;
  width: 200px;
  height: 50px;
  border-radius: 50px;
  border: 3px solid black;
  margin: 50px;
  &:hover {
    background-color: yellowgreen;
    color: darkgreen;
  }
`;