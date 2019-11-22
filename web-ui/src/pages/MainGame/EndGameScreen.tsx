import React from 'react';
import Title from "../../components/misc/Title";
import styled from "styled-components";
import Container from "../../components/utils/Container";
import Button from "../../components/misc/Button";

const TeamTitle = styled.h2`
  color: #c9c9c9;
  font-size: 2em;
`;

const TeamContainer = styled.div`
  display: block;
  text-align: left;
  min-width: 30em;
`;

const Text = styled.p`
  color: #c9c9c9
`;

export default () => {
  return (
    <Container direction="column">
      <Title>Gagné!</Title>
      <TeamTitle>Ton équipe</TeamTitle>
      <Container direction="column">
        <Container direction="row" justifyContent="space-between" minWidth="20em">
          <Text>Username1</Text>
          <Text>some score</Text>
        </Container>

        <Container direction="row" justifyContent="space-between" minWidth="20em">
          <Text>Username2</Text>
          <Text>some score</Text>
        </Container>
      </Container>
      <TeamTitle>Équipe adverse</TeamTitle>
      <Container direction="row" justifyContent="space-between" minWidth="20em">
        <Text>Username3</Text>
        <Text>some score</Text>
      </Container>
      <Container direction="row" justifyContent="space-between" minWidth="20em">
        <Text>Username4</Text>
        <Text>some score</Text>
      </Container>
      <Button>Retour au menu</Button>
    </Container>
  )
}