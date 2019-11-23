import React from 'react';
import styled from 'styled-components';
import Cookies from 'js-cookie';

const Link = styled.a`
  position : absolute;
  right : 30px;
  top : 30px;
  color : white;
`;

export default class LoginToolbarComponent extends React.Component {
  render() {
    return (
      <div>
        {Cookies.get('username') ?
          <Link href="/logout">
            Se déconnecter
          </Link>
        :
          <Link href="/login">
            S'authentifier
          </Link>
        }
      </div>
    )
  }
}