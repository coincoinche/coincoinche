import React from 'react';
import styled from 'styled-components';
import logo from "../assets/cards/AC.png";

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
    return <div>
      <Rectangle src={logo} className="App-logo" alt="logo" rotationDegrees={-30} />
      <Rectangle src={logo} className="App-logo" alt="logo" rotationDegrees={0} />
      <Rectangle src={logo} className="App-logo" alt="logo" rotationDegrees={30} />
    </div>;
  }
}
