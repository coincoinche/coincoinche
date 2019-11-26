import React from 'react';
import styled from 'styled-components';
import Cookies from 'js-cookie';
import Container from './utils/Container';

const Link = styled.a`
  color : white;
  padding-left : 10px;
`;

const ContainerBis = styled.div`
  position : absolute;
  right : 30px;
  top : 30px;
`;

const Text = styled.p`
  color: white;
`;

type Props = {
  username?: string;
}


export default class LoginToolbarComponent extends React.Component<Props> {
  render() {
    return (
      <ContainerBis>
        {Cookies.get('username') ?
          <Link href="/logout">
            Se déconnecter
          </Link>
        :
          <Container direction="row">
            {
              this.props.username && <Text>
                Connecté en tant que guest
              </Text>
            }
            <Link href="/login">
              S'authentifier
            </Link>
            <Link href="/signup">
              S'inscrire
            </Link>
          </Container>
        }
      </ContainerBis>
    )
  }
}