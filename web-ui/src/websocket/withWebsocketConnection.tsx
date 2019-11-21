import * as React from "react";
import { MessageType, SocketMessage } from "./messages/types";
import { API_BASE_URL } from '../constants';

const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

export type InjectedProps = {
  sendMessage: (socketEndpoint: string, message: SocketMessage) => void;
  registerOnMessageReceivedCallback: (topic: string, messageType: MessageType, callback: MessageCallback) => void;
  socketConnected: boolean;
  retryTimeMs: number;
  subscribe: (topic: string) => void;
}

type WrappedComponentProps<BaseProps> = BaseProps & InjectedProps;

type RawSocketMessage = { body: string };

type MessageCallback = (message: SocketMessage & any) => void;

type State = {
  callbacks: {
    [topic: string]: {
      [k in MessageType]?: MessageCallback;
    };
  }
  socketConnected: boolean;
  connexionFailures: number;
  retryTimeMs: number;
}

function withWebsocketConnection<BaseProps>(WrappedComponent: React.ComponentType<WrappedComponentProps<BaseProps>>) {
  return class extends React.Component<BaseProps, State> {
    stompClient: any = null;
    state = {
      callbacks: {},
      socketConnected: false,
      connexionFailures: 0,
      retryTimeMs: 0,
    };

    componentDidMount(): void {
      this.connect();
    }

    onMessageReceived = (topic: string, message: RawSocketMessage) => {
      const parsedMessage: SocketMessage = JSON.parse(message.body);
      console.log("onMessageReceived", parsedMessage);
      // @ts-ignore
      if (!!this.state.callbacks[topic] && !!this.state.callbacks[topic][parsedMessage.message]) {
        console.log("running message callback");
        // @ts-ignore
        this.state.callbacks[topic][parsedMessage.message](parsedMessage);
      }
    };

    registerCallback = (
      topic: string,
      messageType: MessageType,
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
      let retryTimeMs: number;
      this.setState(({connexionFailures}) => {
        retryTimeMs = new Date().getTime() + this.getRetryTimeoutMs(connexionFailures + 1);
        return {
          connexionFailures: connexionFailures + 1,
          retryTimeMs,
        }
      }, () => {
        setTimeout(this.connect, this.getRetryTimeoutMs(this.state.connexionFailures))
      });
    };

    connect = () => {
      this.stompClient = Stomp.over(new SockJS(`${API_BASE_URL}/ws`));
      this.stompClient.connect({}, this.onConnected, this.onError);
    };

    onConnected = () => {
      this.setState({socketConnected: true});
    };

    getRetryTimeoutMs = (connectionFailures: number) => {
      const timoutIncrementMsPerFailure = 2000;
      return connectionFailures * timoutIncrementMsPerFailure;
    };

    subscribe = (topic: string) => {
      this.stompClient.subscribe(
        topic,
        (message: RawSocketMessage) => this.onMessageReceived(topic, message)
      )
    };

    sendMessage = (socketEndpoint: string, message: SocketMessage) => {
      if (this.stompClient) {
        this.stompClient.send(socketEndpoint, {}, JSON.stringify(message));
      }
    };

    render() {
      return <WrappedComponent
        socketConnected={this.state.socketConnected}
        retryTimeMs={this.state.retryTimeMs}
        sendMessage={this.sendMessage}
        registerOnMessageReceivedCallback={this.registerCallback}
        subscribe={this.subscribe}
        {...this.props}
      />
    }
  }
}

export default withWebsocketConnection;