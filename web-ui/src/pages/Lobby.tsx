import React from 'react';
import MainGameScreen from "./MainGame/MainGameScreen";
// @ts-ignore
import Loader from 'react-loader-spinner';
import withWebsocketConnection, { InjectedProps } from "../websocket/withWebsocketConnection";
import {MessageType, GameStartedMessage, SocketEndpoint, TopicTemplate} from "../websocket/messages/types";
import { Player } from "../game-engine/gameStateTypes";
import { makeJoinLobbyMessage } from "../websocket/messages/lobby";

type State = {
  gameId: string | null;
  username: string;
  users: Player[];
}

type Props = InjectedProps

class Lobby extends React.Component<Props, State> {
  state = {
    gameId: null,
    users: [],
    username: Math.floor(Math.random() * 10000000).toString(),
  };

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOBBY,
      MessageType.GAME_STARTED,
      (msg: GameStartedMessage) => this.setState({ gameId: msg.content.gameId, users: msg.content.users }),
    );
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (!prevProps.socketConnected && this.props.socketConnected) {
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