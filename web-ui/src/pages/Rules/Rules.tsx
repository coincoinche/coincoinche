import React from 'react';
import Title from "../../components/misc/Title";
import styled from "styled-components";
import BiddingBoard from "../../components/bidding/BiddingBoard";
import {SpecialBidding, Suit} from "../../game-engine/gameStateTypes";
import Container from "../../components/utils/Container";
import cardsImages, {CardValue} from "../../assets/cards";
import Card from "../../components/cards/Card";

const Text = styled.p`
  color: #c9c9c9;
  font-weight: bold;
`;

const Paragraph = styled.div`
  text-align: left;
  padding: 0px 50px;
`;

const BiddingBoardContainer = styled.div`
  max-width: 350px;
  max-height: 500px;
`;

const CardContainer = styled.div`
  padding: 10px;
`;

const RowOfCards = ({ cards }: {cards: string[]}) => (
  <Container direction="row">
    {
      cards.map(cardValue => (
        <CardContainer>
          <Card
            rotationDegrees={0}
            // @ts-ignore
            src={cardsImages[CardValue[cardValue]]}
            alt={cardValue}
          />
        </CardContainer>
      ))
    }
  </Container>
);

const Rules: React.FC = () => {
  return (
    <Container direction="column">
      <Title fontSize={70}>Règles du jeu</Title>
      <Title fontSize={40}>La coinche en bref</Title>
      <Paragraph>
        <Text>La coinche est un jeu comprenant 2 équipes de 2 joueurs. Les joueurs de la même équipe sont face à face.</Text>
        <Text>L'équipe gagnante est la première équipe arrivant à 1000 points.</Text>
        <Text>Une partie est constituée de différents rounds. Après chaque round, l'équipe gagnante marque des points.</Text>
        <Text>Chaque round est constitué de 2 phases: la phase d'enchère et la phase de plis.</Text>
        <Text>
          Les joueurs ont leurs cartes dès le début de la phase d'enchère,
          et doivent parier sur le nombre de points que leur equipe peut faire avec leur main actuelle.
          Les joueurs enchérissent à tour de role jusqu'à ce que plus personne ne veuille enchérir,
          on dit alors que l'équipe gagnante est l'équipe qui prend.
        </Text>
        <Text>
          Ensuite vient la phase de jeu, les joueurs jouent à tour de rôle les cartes une par une.
          Quand chaque joueur a joué une carte, on dit que le pli est terminé, et un nouveau pli commence.
          Le joueur ayant la plus forte carte remporte le pli, c'est ce joueur qui commence à jouer au pli suivant.
          Le premier joueur à jouer dans un pli décide de la couleur de ce pli,
          c'est à dire que les joueurs suivant sont obligés de jouer la même couleur de carte pour ce pli!
        </Text>
        <Text>A la fin du round, si l'équipe qui a pris remporte son contrat, elle marque le nombre de points correspondant, sinon c'est l'équipe adverse qui marque des points.</Text>
      </Paragraph>
      <Title fontSize={40}>L'atout et l'ordre des cartes</Title>
      <Text>L'ordre des cartes est le suivant, l'as étant la carte la plus forte: </Text>
      <RowOfCards cards={['7h', '8h', '9h', 'jh', 'qh', 'kh', 'th', 'ah']} />
      <Paragraph>
        <Text>Lors de la phase d'enchère, vous serez presentés avec les choix suivants: </Text>
      </Paragraph>
      <BiddingBoardContainer>
        <BiddingBoard
          authorisedContractValues={[80, 90, 100, 110, 120, 130, 140, 150, 160, 250, 500]}
          authorisedSpecialBiddings={[SpecialBidding.COINCHE, SpecialBidding.PASS, SpecialBidding.SURCOINCHE]}
          authorisedContractSuits={[Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS, Suit.SPADES]}
          onContractPicked={() => {}}
        />
      </BiddingBoardContainer>
      <Paragraph>
        <Text>
          Pour enchérir, il suffit de selectionner une couleur et une valeur, sinon passer.
          Nous verrons le détail des valeurs plus tard.
          L'important, c'est que la couleur du contrat qui remporte les enchères sera la couleur de l'atout!
        </Text>
        <Text>
          L'atout est une couleur speciale pour le round qui est toujours plus forte que les autres couleurs.
          L'ordre des cartes à l'atout est également légèrement différent, le voici:
        </Text>
      </Paragraph>
      <RowOfCards cards={['7h', '8h', 'qh', 'kh', 'th', 'ah', '9h', 'jh']} />
      <Title fontSize={40}>Mais à quoi sert donc l'atout?</Title>
      <Paragraph>
        <Text>En effet, à quoi sert donc l'atout, à part à compliquer les règles?</Text>
        <Text>
          Il y a un détail que nous avons omis précédemment: que ce passe-t-il lorsqu'un joueur ne peut pas jouer la couleur demandée lors d'un pli?
        </Text>
      </Paragraph>
    </Container>
  );
};

export default Rules;