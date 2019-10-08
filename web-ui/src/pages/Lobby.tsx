import React from 'react';
import MainGameScreen from "./MainGame/MainGameScreen";

// @ts-ignore
import Loader from 'react-loader-spinner';

type State = {
  hasFoundGame: boolean;
}

class Lobby extends React.Component<{}, State> {
  state = {
    hasFoundGame: false,
  };

  componentDidMount(): void {
    setTimeout(() => this.setState({ hasFoundGame: true }), 2000);
  }

  render() {
    const { hasFoundGame } = this.state;

    if ( hasFoundGame ) {
      return <MainGameScreen />
    }

    return (
      <Loader
        type="BallTriangle"
        color="#00BFFF"
        height={100}
        width={100}
      />
    )
  }
}

export default Lobby;