import React from 'react';
import withWebsocketConnection, { InjectedProps } from "../../../websocket/withWebsocketConnection";
import {EventType, SocketEndpoint, TopicTemplate} from "../../../websocket/events/types";
import MainMenu from "../../MainMenu/MainMenu";
import { makeLogInMessage } from '../../../websocket/events/login';

type Props = InjectedProps

type State = {
  wrongPassword: boolean;
  redirect: boolean;
  username: string | null;
}

class Login extends React.Component<Props, State> {
  constructor(props:any) {
    super(props);
    this.state = {
      wrongPassword : false,
      redirect : false,
      username : null,
    }
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOGIN,
      EventType.WRONG_ID,
      () => this.setState({ wrongPassword : true }),
    );
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOGIN,
      EventType.CONNECTED,
      () => this.setState({ redirect : true }),
    )
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(TopicTemplate.LOGIN);
    }
  }

  handleSubmit(event:any) {
    this.props.sendMessage(SocketEndpoint.USER_LOGIN, makeLogInMessage('connected'));
  }

  render() {
    if (this.state.redirect) {
      return <MainMenu />
    }
    return(
        <form onSubmit={this.handleSubmit}>
            <div><label> User Name : <input type="text" name="username"/> </label></div>
            <div><label> Password: <input type="password" name="password"/> </label></div>
            <div><input type="submit" value="Sign In"/></div>
        </form>
    )
  }
}

export default withWebsocketConnection<{}> (Login);