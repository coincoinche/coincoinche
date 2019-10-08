import * as React from "react";
import {HelloMessage, MESSAGE_TYPE, SOCKET_ENDPOINT, SocketMessage, TOPIC} from "./types";
import './TestWebSocket.css';

const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

type State = {
  messages: SocketMessage[]
};

class TestWebSocket extends React.Component<{}, State> {
  stompClient: any = null;
  helloMessage: HelloMessage = {
    type: MESSAGE_TYPE.HELLO,
    content: 'Hello from a client',
    from: 'Client'
  };
  joinLobbyMessage: SocketMessage = {
    type: MESSAGE_TYPE.JOIN_LOBBY,
  };
  state = {
    messages: []
  };

  componentDidMount(): void {
    this.connect();
  }

  onMessageReceived = (helloMessage: { body: string, type: string }) => {
    const parsedMessage: SocketMessage = JSON.parse(helloMessage.body);
    console.log(helloMessage.type);
    if (helloMessage.type === MESSAGE_TYPE.GAME_START) {
      console.log('GAME STARTED');
    }
    this.setState((prevState: State) => ({ messages: [...prevState.messages, parsedMessage] }))
  };

  onError = (error: any) => {
    console.error("onError", error);
  };

  connect = () => {
    const socketClient = new SockJS('http://localhost:8080/ws');
    this.stompClient = Stomp.over(socketClient);
    this.stompClient.connect({}, this.onConnected, this.onError);
  };

  onConnected = () => {
    // Subscribing to the greetings topic where the server sends the response
    this.stompClient.subscribe(TOPIC.GREETINGS, this.onMessageReceived);
    this.stompClient.subscribe(TOPIC.LOBBY, this.onMessageReceived);
  };

  sendMessage = (helloMessage: SocketMessage) => {
    if (this.stompClient) {
      this.stompClient.send(SOCKET_ENDPOINT.JOIN_LOBBY, {}, JSON.stringify(helloMessage));
      this.setState((prevState: State) => ({ messages: [...prevState.messages, helloMessage] }))
    }
  };

  render() {
    return (
        <div>
          <h3>Messages</h3>
          {
            this.state.messages.map((helloMessage: HelloMessage) => (
              <div id='chat'>
                <strong>From:</strong>
                <div>{helloMessage.from}</div>
                <strong>Content:</strong>
                <div>{helloMessage.content}</div>
              </div>
            ))
          }
          <button onClick={() => this.sendMessage(this.joinLobbyMessage)} >Send Message</button>
        </div>
    );
  }
}

export default TestWebSocket;