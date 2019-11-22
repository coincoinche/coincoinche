import React from 'react';
import styled from "styled-components";

type Props = {
  youScore: number,
  themScore: number
}

const ScoresContainer = styled.div`
  width: 200px;
  margin: 15px;
  background-color: #FFFFFF;
  padding: 10px;
  border-style: solid;
  border-radius: 20px;
`

const ScoresComponent = ({ youScore, themScore }: Props) => {
  return (
    <ScoresContainer>
      <b>Points actuels par équipe</b>
      <br />Vous : {youScore}
      <br />Eux : {themScore}
    </ScoresContainer>
  );
}

export default ScoresComponent;
