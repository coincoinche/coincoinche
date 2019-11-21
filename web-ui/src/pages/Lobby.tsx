import React from 'react';
import MainGameScreen from "./MainGame/MainGameScreen";
// @ts-ignore
import Loader from 'react-loader-spinner';
import withWebsocketConnection, { InjectedProps } from "../websocket/withWebsocketConnection";
import {MessageType, GameStartedMessage, SocketEndpoint, TopicTemplate} from "../websocket/messages/types";
import { Player } from "../game-engine/gameStateTypes";
import { makeJoinLobbyMessage } from "../websocket/messages/lobby";
import styled from "styled-components";

type State = {
  gameId: string | null;
  username: string;
  users: Player[];
  socketConnectionRetryTimeoutMs: number;
}

type Props = InjectedProps

const ConnectionStatus = styled.p`
  font-family: arial,helvetica,verdana;
  font-size: 18pt;
  color: #c9c9c9;
`;

class Lobby extends React.Component<Props, State> {
  retryTimeoutUpdaterFunctionId = 0;
  state = {
    gameId: null,
    users: [],
    username: Math.floor(Math.random() * 10000000).toString(),
    socketConnectionRetryTimeoutMs: 0,
  };

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOBBY,
      MessageType.GAME_STARTED,
      (msg: GameStartedMessage) => this.setState({ gameId: msg.content.gameId, users: msg.content.users }),
    );
    this.retryTimeoutUpdaterFunctionId = setInterval(() =>
      this.setState({
        socketConnectionRetryTimeoutMs: this.props.retryTimeMs - new Date().getTime(),
      }), 1000)
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (!prevProps.socketConnected && this.props.socketConnected) {
      clearInterval(this.retryTimeoutUpdaterFunctionId);
      this.props.subscribe(TopicTemplate.LOBBY);
      // TODO ask user to input a username
      this.props.sendMessage(SocketEndpoint.JOIN_LOBBY, makeJoinLobbyMessage(this.state.username));
    }
  }

  render() {
    const { gameId, username, users } = this.state;

    if (!!gameId) {
      return <MainGameScreen username={username} gameId={gameId!} {...this.props} users={users!} />
    }

    let title;
    if (this.props.socketConnected) {
      title = (
        <ConnectionStatus>
          Successfully connected to server, waiting for other players
        </ConnectionStatus>
      )
    } else if (this.props.retryTimeMs > new Date().getTime()) {
      title = (
        <ConnectionStatus>
          Connexion failed, retrying in {Math.round(this.state.socketConnectionRetryTimeoutMs / 1000)}
        </ConnectionStatus>
      )
    } else {
      title = (
        <ConnectionStatus>Connecting to server</ConnectionStatus>
      )
    }

    return (
      <div>
        {title}
        <Loader
          type="ThreeDots"
          color="#c9c9c9"
          height={100}
          width={100}
        />
      </div>
    )
  }
}

export default withWebsocketConnection<{}>(Lobby);