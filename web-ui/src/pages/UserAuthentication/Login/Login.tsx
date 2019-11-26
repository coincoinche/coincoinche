import React from 'react';
import withWebsocketConnection, { InjectedProps } from "../../../websocket/withWebsocketConnection";
import {MessageType, SocketEndpoint, TopicTemplate, LoggedInMessage, WrongUsernameMessage, WrongPasswordMessage} from "../../../websocket/messages/types";
import { makeLogInMessage } from '../../../websocket/messages/login';
import Cookies from 'js-cookie';
import { Redirect } from 'react-router';
import './Login.css';

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
    if (this.state.authenticated) {
      return <Redirect to="/" />
    }
    return(
        <div className="globalContainer">
        <div style={{"width": "50%"}}>
          <b>ATTENTION !</b> Pour le moment, Coincoinche ne garantit pas la sécurité des mots de passe utilisés sur ce site.
          Par conséquent, veuillez utiliser <b>impérativement</b> un mot de passe unique (<i>i.e.</i> que vous n'utilisez
          nulle part ailleurs) pour ce site. Il s'agit de toute manière d'une bonne pratique de sécurité de manière générale.
        </div>
        <form onSubmit={this.handleSubmit}>
          <div className="loginForm">
            <div className="labelContainer">
              <label className="loginLabel"> Nom d'utilisateur :</label>
              <label className="loginLabel"> Mot de passe : </label>
            </div>
            <div className="inputContainer">
              <input type="text" name="username"/>
              <input type="password" name="password"/>
            </div>
          </div>
          {this.state.wrongUsername && <div>
            <label className="errorMessage"> Cet utilisateur n'existe pas </label>
          </div>}
          {this.state.wrongPassword && <div>
            <label className="errorMessage"> Le mot de passe est invalide </label>
          </div>}
          <div className="submitButton"><input type="submit" value="Se connecter"/></div>
        </form>
        </div>
    )
  }
}

export default withWebsocketConnection<{}> (Login);