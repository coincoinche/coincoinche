import React from 'react';
import styled from "styled-components";
import Title from "../../components/misc/Title";
import {Animated} from "react-animated-css";
import Container from "../../components/utils/Container";
import Button from "../../components/misc/Button";

const TeamTitle = styled.h2`
  color: #c9c9c9;
  font-size: 2em;
`;

const ScoreContainer = styled.div`
  background-color: #1F1F1F;
  border-radius: 30px;
  padding: 10px 30px;
  border: 20px solid #8e5723;
`;

const Text = styled.p`
  color: #c9c9c9
`;

const animationParams = {
  title: {
    type: "bounceInUp" as any,
    duration: 1500,
    delay: 0,
  },
  scoreBoard: {
    type: "fadeIn" as any,
    duration: 500,
    delay: 1500,
  },
  yourTeamTitle: {
    type: "bounceInLeft" as any,
    duration: 1000,
    delay: 2000,
  },
  yourTeamPlayers: {
    type: "fadeIn" as any,
    duration: 1000,
    delay: 3000,
  },
  theirTeamTitle: {
    type: "bounceInRight" as any,
    duration: 1000,
    delay: 2000,
  },
  theirTeamPlayers: {
    type: "fadeIn" as any,
    duration: 1000,
    delay: 3000,
  },
  exitButton: {
    type: "fadeIn" as any,
    duration: 500,
    delay: 4000,
  }
};

type Props = {
  win: boolean;
  yourTeam: {
    points: number;
    players: {
      username: string;
      previousElo: number;
      newElo: number;
    }[];
  };
  theirTeam: {
    points: number;
    players: {
      username: string;
      previousElo: number;
      newElo: number;
    }[];
  }
}

export default ({ win, yourTeam, theirTeam }: Props) => {
  const { title, yourTeamTitle, yourTeamPlayers, theirTeamPlayers, theirTeamTitle, exitButton, scoreBoard } = animationParams;
  return (
    <Container direction="column">
      <Animated
        animationIn={title.type}
        animationInDelay={title.delay}
        animationInDuration={title.duration}
        animationOut="fadeOut"
        isVisible={true}
      >
        {
          win ? <Title fontSize={70}>Gagné!</Title> : <Title fontSize={70}>Perdu...</Title>
        }
      </Animated>

      <Animated
        animationIn={scoreBoard.type}
        animationInDelay={scoreBoard.delay}
        animationInDuration={scoreBoard.duration}
        animationOut="fadeOut"
        isVisible={true}
      >
        <ScoreContainer>
          <Animated
            animationIn={yourTeamTitle.type}
            animationInDelay={yourTeamTitle.delay}
            animationInDuration={yourTeamTitle.duration}
            animationOut="fadeOut"
            isVisible={true}
          >
            <Container direction="row" justifyContent="space-between" minWidth="20em">
              <TeamTitle>Ton équipe</TeamTitle>
              <Text>{yourTeam.points}</Text>
            </Container>
          </Animated>

          <Animated
            animationIn={yourTeamPlayers.type}
            animationInDelay={yourTeamPlayers.delay}
            animationInDuration={yourTeamPlayers.duration}
            animationOut="fadeOut"
            isVisible={true}
          >
            <Container direction="column">
              {
                yourTeam.players.map(({ username, previousElo, newElo }) => (
                  <Container direction="row" justifyContent="space-between" minWidth="20em">
                    <Text>{username}</Text>
                    <Text>{previousElo} -> {newElo}</Text>
                  </Container>
              ))}
            </Container>
          </Animated>

          <Animated
            animationIn={theirTeamTitle.type}
            animationInDelay={theirTeamTitle.delay}
            animationInDuration={theirTeamTitle.duration}
            animationOut="fadeOut"
            isVisible={true}
          >
            <Container direction="row" justifyContent="space-between" minWidth="20em">
              <TeamTitle>Équipe adverse</TeamTitle>
              <Text>{theirTeam.points}</Text>
            </Container>
          </Animated>

          <Animated
            animationIn={theirTeamPlayers.type}
            animationInDelay={theirTeamPlayers.delay}
            animationInDuration={theirTeamPlayers.duration}
            animationOut="fadeOut"
            isVisible={true}
          >
            {
              theirTeam.players.map(({ username, previousElo, newElo }) => (
                <Container direction="row" justifyContent="space-between" minWidth="20em">
                  <Text>{username}</Text>
                  <Text>{previousElo} -> {newElo}</Text>
                </Container>
            ))}
          </Animated>
        </ScoreContainer>
      </Animated>

      <Animated
        animationIn={exitButton.type}
        animationInDelay={exitButton.delay}
        animationInDuration={exitButton.duration}
        animationOut="fadeOut"
        isVisible={true}
      >
        <Button>Retour au menu</Button>
      </Animated>
    </Container>
  )
}