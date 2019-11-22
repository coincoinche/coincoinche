import React from 'react';
import MainGameScreen from "./MainGame/MainGameScreen";
// @ts-ignore
import Loader from 'react-loader-spinner';
import withWebsocketConnection, { InjectedProps } from "../websocket/withWebsocketConnection";
import {MessageType, GameStartedMessage, SocketEndpoint, TopicTemplate} from "../websocket/messages/types";
import { Player } from "../game-engine/gameStateTypes";
import {makeJoinLobbyMessage, makeQuitLobbyMessage} from "../websocket/messages/lobby";
import styled from "styled-components";
import {RouteComponentProps, withRouter} from "react-router";

type State = {
  gameId: string | null;
  username: string;
  users: Player[];
  socketConnectionRetryTimeoutMs: number;
}

type Props = InjectedProps & RouteComponentProps;

const ConnectionStatus = styled.p`
  font-family: arial,helvetica,verdana;
  font-size: 18pt;
  color: #c9c9c9;
`;

const ExitLobbyButton = styled.div`
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

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
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

  onQuitLobbyButtonClicked = () => {
    this.props.sendMessage(SocketEndpoint.QUIT_LOBBY, makeQuitLobbyMessage(this.state.username));
    this.props.history.push('/');
  };

  render() {
    const { gameId, username, users } = this.state;

    if (!!gameId) {
      return <MainGameScreen username={username} gameId={gameId!} {...this.props} users={users!} />
    }

    let title;
    if (this.props.socketConnected) {
      title = (
        <ConnectionStatus>
          La connexion a réussie, nous devons maintenant attendre les autres joueurs!
        </ConnectionStatus>
      )
    } else if (this.props.retryTimeMs > new Date().getTime()) {
      title = (
        <ConnectionStatus>
          La connexion a echouée,
          mais on réessaye dans {Math.round(this.state.socketConnectionRetryTimeoutMs / 1000)} secondes!
        </ConnectionStatus>
      )
    } else {
      title = (
        <ConnectionStatus>Connexion en cours</ConnectionStatus>
      )
    }

    return (
      <Container>
        {title}
        <Loader
          type="ThreeDots"
          color="#c9c9c9"
          height={100}
          width={100}
        />
        <ExitLobbyButton onClick={this.onQuitLobbyButtonClicked}>Revenir au menu</ExitLobbyButton>
      </Container>
    )
  }
}

export default withWebsocketConnection<{}>(withRouter(Lobby));