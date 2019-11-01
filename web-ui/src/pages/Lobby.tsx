import React from 'react';
import MainGameScreen from "./MainGame/MainGameScreen";
// @ts-ignore
import Loader from 'react-loader-spinner';
import withWebsocketConnection, { InjectedProps } from "../websocket/withWebsocketConnection";
import {EventType, GameStartedEvent, SocketEndpoint, TopicTemplate} from "../websocket/events/types";
import { makeJoinLobbyMessage } from "../websocket/events/lobby";

type State = {
  gameId: string | null;
  username: string;
  usernames: string[] | null;
}

type Props = InjectedProps

class Lobby extends React.Component<Props, State> {
  state = {
    gameId: null,
    usernames: null,
    username: Math.floor(Math.random() * 10000000).toString(),
  };

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOBBY,
      EventType.GAME_STARTED,
      ({ gameId, usernames }: GameStartedEvent) => this.setState({ gameId, usernames }),
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
    const { gameId, username, usernames } = this.state;

    if (!!gameId) {
      return <MainGameScreen username={username} gameId={gameId!} {...this.props} usernames={usernames!} />
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