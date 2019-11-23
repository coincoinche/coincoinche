import React from 'react';
import withWebsocketConnection, { InjectedProps } from "../../../websocket/withWebsocketConnection";
import {MessageType, SocketEndpoint, TopicTemplate, UserExistsMessage, } from "../../../websocket/messages/types";
import Cookies from 'js-cookie';
import { makeSignUpMessage } from "../../../websocket/messages/signup";
import { Redirect } from 'react-router';
import LoginToolbarComponent from "../../../components/LoginToolbarComponent";

type Props = InjectedProps

type State = {
  wrongPassword : boolean;
  userExists: boolean;
  userCreated: boolean;
  username: string;
}

class SignUp extends React.Component<Props, State> {
  constructor(props:any) {
    super(props);
    this.state = {
      wrongPassword : false,
      userExists : false,
      userCreated : false,
      username : ''
    }
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.SIGNUP,
      MessageType.USER_EXISTS,
      (msg: UserExistsMessage) => {
        this.setState({
          wrongPassword : false,
          userExists : true,
          userCreated : false,
        })
      },
    );
    this.props.registerOnMessageReceivedCallback(
      TopicTemplate.SIGNUP,
      MessageType.USER_CREATED,
      (msg: UserExistsMessage) => {
        Cookies.set('username', this.state.username)
        this.setState({
          wrongPassword : false,
          userExists : false,
          userCreated : true,
        })
      },
    );
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(TopicTemplate.SIGNUP);
    }
  }

  handleSubmit(event:any) {
    event.preventDefault();
    if (event.target[1].value != event.target[2].value) {
      this.setState({
        wrongPassword : true,
        userExists : false,
        userCreated : false,
      })
      return
    }
    this.props.sendMessage(SocketEndpoint.USER_SIGNUP, makeSignUpMessage(event.target[0].value, event.target[1].value));
    this.setState({username:event.target[0].value});
  }

  render() {
    if (this.state.userCreated) {
      return <Redirect to="/" />
    }
    return(
      <div>
        <LoginToolbarComponent />
        <form onSubmit={this.handleSubmit}>
          <div className="loginForm">
            <div className="labelContainer">
              <label className="loginLabel"> Nom d'utilisateur :</label>
              <label className="loginLabel"> Mot de passe : </label>
              <label className="loginLabel"> Retapez votre mot de passe : </label>
            </div>
            <div className="inputContainer">
              <input type="text" name="username"/>
              <input type="password" name="password"/>
              <input type="password" name="password"/>
            </div>
          </div>
          {this.state.userExists && <div>
            <label className="errorMessage"> Ce nom d'utilisateur est déjà pris </label>
          </div>}
          {this.state.wrongPassword && <div>
            <label className="errorMessage"> Les mots de passe sont différents </label>
          </div>}
          <div className="submitButton"><input type="submit" value="S'inscrire"/></div>
        </form>
      </div>
    )
  }
}


export default withWebsocketConnection<{}> (SignUp);