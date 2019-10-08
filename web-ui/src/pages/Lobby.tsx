import React from 'react';
import MainGameScreen from "./MainGame/MainGameScreen";
// @ts-ignore
import Loader from 'react-loader-spinner';
import withWebsocketConnection, {InjectedProps} from "../websocket/withWebsocketConnection";
import {MESSAGE_TYPE, SOCKET_ENDPOINT, TOPIC} from "../websocket/types";
import {joinLobbyMessage} from "../websocket/socket-messages";

type State = {
  hasFoundGame: boolean;
}

type Props = InjectedProps

class Lobby extends React.Component<Props, State> {
  state = {
    hasFoundGame: false,
  };

  componentDidUpdate(prevProps: Readonly<InjectedProps>, prevState: Readonly<State>, snapshot?: any): void {
    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.sendMessage(SOCKET_ENDPOINT.JOIN_LOBBY, joinLobbyMessage);
      this.props.registerOnMessageReceivedCallback(
        TOPIC.LOBBY,
        MESSAGE_TYPE.GAME_START,
        () => this.setState({ hasFoundGame: true }),
      );
    }
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

export default withWebsocketConnection<{}>(Lobby);