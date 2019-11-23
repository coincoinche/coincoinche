import React from 'react';
import withWebsocketConnection, { InjectedProps } from "../../../websocket/withWebsocketConnection";
import {MessageType, SocketEndpoint, TopicTemplate, LoggedInMessage, WrongUsernameMessage, WrongPasswordMessage} from "../../../websocket/messages/types";
import { makeLogInMessage } from '../../../websocket/messages/login';
import Cookies from 'js-cookie';

type Props = InjectedProps

type State = {
  wrongUsername: boolean;
  wrongPassword: boolean;
  authenticated: boolean;
  username: string;
}

class Login extends React.Component<Props, State> {
  constructor(props:any) {
    super(props);
    this.state = {
      wrongUsername : false,
      wrongPassword : false,
      authenticated : false,
      username : '',
    }
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOGIN,
      MessageType.WRONG_USERNAME,
      (msg: WrongUsernameMessage) => {
        this.setState({
          authenticated : false,
          wrongUsername : true,
          wrongPassword : false,
        })
      },
    );
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOGIN,
      MessageType.WRONG_PASSWORD,
      (msg:WrongPasswordMessage) => {
        this.setState({
          authenticated : false,
          wrongUsername : false,
          wrongPassword : true,
        })
      },
    );
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.LOGIN,
      MessageType.LOGGED_IN,
      (msg: LoggedInMessage) => {
        Cookies.set('username', this.state.username);
        this.setState({
          authenticated : true,
          wrongUsername : false,
          wrongPassword : false,
        });
      },
    )
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(TopicTemplate.LOGIN);
    }
  }

  handleSubmit(event:any) {
    event.preventDefault();
    this.props.sendMessage(SocketEndpoint.USER_LOGIN, makeLogInMessage(event.target[0].value, event.target[1].value));
    this.setState({username:event.target[0].value});
  }

  render() {
    return(
        <form onSubmit={this.handleSubmit}>
            <div><label> User Name : <input type="text" name="username"/> </label></div>
            <div><label> Password: <input type="password" name="password"/> </label></div>
            {this.state.wrongUsername && <div>
              <label style={{color:'red'}}> This user doesn't exist </label>
            </div>}
            {this.state.wrongPassword && <div>
              <label style={{color:'red'}}> These username and password don't match </label>
            </div>}
            {this.state.authenticated && <div>
              <label style={{color:'red'}}> Connected ! </label>
            </div>}
            <div><input type="submit" value="Sign In"/></div>
        </form>
    )
  }
}

export default withWebsocketConnection<{}> (Login);