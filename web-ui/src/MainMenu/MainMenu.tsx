import React from 'react';
import styled from 'styled-components';
import {Link} from "react-router-dom";
import casual from "../assets/menu/casual.png";
import ranked from "../assets/menu/ranked.png";
import ladder from "../assets/menu/ladder.png";

import logo from '../assets/coincoinche_logo.png';
import TestComponent from "../TestComponent";
import TestWebSocket from "../websocket/TestWebSocket";

type State = {};

type RectangleProps = {
  rotationDegrees: number;
}

const applyTransformation = (rotationDegrees: number, scale: number) => `
  rotate(${rotationDegrees}deg)
  translateY(${Math.abs(rotationDegrees)}px)
  translateX(${rotationDegrees}px)
  scale(${scale})
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const Rectangle = styled.img`
  background-color: cornflowerblue;
  width: 100px;
  height: 150px;
  border-radius: 10px;
  transform: ${
    ({rotationDegrees}: RectangleProps) => applyTransformation(rotationDegrees, 1)
  };
  &:hover {
    transform: ${({rotationDegrees}: RectangleProps) => applyTransformation(rotationDegrees, 1.2)
  }
`;

export default class MainMenu extends React.Component<{}, State> {
  render() {
    return <Container>
      <img src={logo} className="App-logo" alt="logo" />
      {/*<TestComponent />*/}
      {/*<TestWebSocket />*/}
      <div>
        <Link to="/game">
          <Rectangle src={casual} className="App-logo" alt="logo" rotationDegrees={-20} />
        </Link>
        <Link to="/game">
          <Rectangle src={ranked} className="App-logo" alt="logo" rotationDegrees={0} />
        </Link>
        <Link to="/ladder">
          <Rectangle src={ladder} className="App-logo" alt="logo" rotationDegrees={20} />
        </Link>
      </div>
    </Container>;

  }
}
