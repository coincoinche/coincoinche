import * as React from "react";
import { MESSAGE_TYPE, SOCKET_ENDPOINT, SocketMessage, TOPIC } from "./types";

const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

export type InjectedProps = {
  sendMessage: (socketEndpoint: SOCKET_ENDPOINT, message: SocketMessage) => void;
  registerOnMessageReceivedCallback: (topic: TOPIC, messageType: MESSAGE_TYPE, callback: MessageCallback) => void;
  socketConnected: boolean;
}

type WrappedComponentProps<BaseProps> = BaseProps & InjectedProps;

type RawSocketMessage = { body: string };

type MessageCallback = (message: SocketMessage) => void;

type State = {
  callbacks: {
    [k in TOPIC]?: {
      [k in MESSAGE_TYPE]?: MessageCallback;
    };
  }
  socketConnected: boolean;
}

function withWebsocketConnection<BaseProps>(WrappedComponent: React.ComponentType<WrappedComponentProps<BaseProps>>) {
  return class extends React.Component<BaseProps, State> {
    stompClient: any = null;
    state = {
      callbacks: {},
      socketConnected: false,
    };

    componentDidMount(): void {
      this.connect();
    }

    onMessageReceived = (topic: TOPIC, message: RawSocketMessage) => {
      const parsedMessage: SocketMessage = JSON.parse(message.body);
      // @ts-ignore
      if (!!this.state.callbacks[topic] && !!this.state.callbacks[topic][parsedMessage.type]) {
        // @ts-ignore
        this.state.callbacks[topic][parsedMessage.type](parsedMessage);
      }
    };

    registerCallback = (
      topic: TOPIC,
      messageType: MESSAGE_TYPE,
      callback: MessageCallback,
    ) => {
      this.setState(prevState => ({
        callbacks: {
          ...prevState.callbacks,
          [topic]: {
            ...prevState.callbacks[topic],
            [messageType]: callback,
          },

        }
      }))
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
      Object.values(TOPIC).forEach(
        topic =>
          this.stompClient.subscribe(
            topic,
            (message: RawSocketMessage) => this.onMessageReceived(topic, message)
          )
      );
      this.setState({socketConnected: true});
    };

    sendMessage = (socketEndpoint: SOCKET_ENDPOINT, message: SocketMessage) => {
      if (this.stompClient) {
        this.stompClient.send(socketEndpoint, {}, JSON.stringify(message));
      }
    };

    render() {
      return <WrappedComponent socketConnected={this.state.socketConnected} sendMessage={this.sendMessage} registerOnMessageReceivedCallback={this.registerCallback} {...this.props} />
    }
  }
}

export default withWebsocketConnection;