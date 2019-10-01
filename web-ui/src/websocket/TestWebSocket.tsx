import * as React from "react";
import {SocketMessage, MESSAGE_TYPE, SOCKET_ENDPOINT, TOPIC} from "./types";
import './TestWebSocket.css';

const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

type State = {
  messages: SocketMessage[]
};

class TestWebSocket extends React.Component<{}, State> {
  stompClient: any = null;
  message: SocketMessage = {
    type: MESSAGE_TYPE.HELLO,
    content: 'Hello from a client',
    from: 'Client'
  };
  state = {
    messages: []
  };

  componentDidMount(): void {
    this.connect();
  }

  onMessageReceived = (message: { body: string }) => {
    const parsedMessage: SocketMessage = JSON.parse(message.body);
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
  };

  sendMessage = (message: SocketMessage) => {
    if (this.stompClient) {
      this.stompClient.send(SOCKET_ENDPOINT.HELLO, {}, JSON.stringify(message));
      this.setState((prevState: State) => ({ messages: [...prevState.messages, message] }))
    }
  };

  render() {
    return (
        <div>
          <h3>Messages</h3>
          {
            this.state.messages.map((message: SocketMessage) => (
              <div id='chat'>
                <strong>From:</strong>
                <div>{message.from}</div>
                <strong>Content:</strong>
                <div>{message.content}</div>
              </div>
            ))
          }
          <button onClick={() => this.sendMessage(this.message)} >Send Message</button>
        </div>
    );
  }
}

export default TestWebSocket;