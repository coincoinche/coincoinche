import React from 'react';
import Cookies from 'js-cookie';
import { Redirect } from 'react-router-dom';
import './Logout.css';

type State = {
  redirect: boolean;
}

export default class Logout extends React.Component<{}, State> {
  constructor(props:any) {
    super(props);
    this.state = {
      redirect : false,
    }
    this.handleClick = this.handleClick.bind(this);
  }

  handleClick() {
    Cookies.remove('username');
    this.setState({redirect : true})
  }

  render() {
    if (this.state.redirect) {
      return <Redirect to="/" />
    }
    return (
      <div className="logoutContainer">
        <label>Souhaitez-vous vraiment vous déconnecter ?</label>
        <div className="buttonContainer">
          <button className = "button" onClick = {this.handleClick} >
            Oui
          </button>
          <button className="button" onClick = {() => this.setState({redirect : true})} >
            Non
          </button>
        </div>
      </div>
    );
  }
}