import React from 'react';
import styled from 'styled-components';
import Cookies from 'js-cookie';

const Link = styled.a`
  color : white;
  padding-left : 10px;
`;
const Container = styled.a`
  position : absolute;
  right : 30px;
  top : 30px;
`

export default class LoginToolbarComponent extends React.Component {
  render() {
    return (
      <Container>
        {Cookies.get('username') ?
          <Link href="/logout">
            Se déconnecter
          </Link>
        :
          <div>
            <Link href="/login">
              S'authentifier
            </Link>
            <Link href="/signup">
              S'inscrire
            </Link>
          </div>
        }
      </Container>
    )
  }
}