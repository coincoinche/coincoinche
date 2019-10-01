import React from 'react';
import styled from 'styled-components';
import {Link} from "react-router-dom";
import casual from "../assets/menu/casual.png";
import ranked from "../assets/menu/ranked.png";
import ladder from "../assets/menu/ladder.png";

import logo from '../assets/coincoinche_logo.png';
import Card from "../components/cards/Card";

type State = {};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

export default class MainMenu extends React.Component<{}, State> {
  render() {
    return <Container>
      <img src={logo} className="App-logo" alt="logo" />
      <div>
        <Link to="/game">
          <Card src={casual} className="App-logo" alt="logo" rotationDegrees={-20} translationX={5} translationY={10} />
        </Link>
        <Link to="/game">
          <Card src={ranked} className="App-logo" alt="logo" rotationDegrees={0} translationX={0} translationY={0} />
        </Link>
        <Link to="/ladder">
          <Card src={ladder} className="App-logo" alt="logo" rotationDegrees={20} translationX={-5} translationY={10} />
        </Link>
      </div>
    </Container>;
  }
}
