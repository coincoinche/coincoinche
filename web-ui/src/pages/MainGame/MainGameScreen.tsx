import React from 'react';
import HandOfCards from "../../components/cards/HandOfCards";
import {CardValue} from '../../assets/cards';
import Container from "../../components/utils/Container";
import {
  AuthorisedBidding,
  AuthorisedContractBidding,
  AuthorisedSpecialBidding,
  ContractValue, fromLetter,
  GamePhase,
  GameState,
  isAuthorisedContractBidding,
  isAuthorisedSpecialBidding,
  Position,
  Suit,
  Trick
} from "./types";
import CardBoard from "../../components/cards/CardBoard";
import BiddingBoard from "../../components/bidding/BiddingBoard";
import {InjectedProps} from "../../websocket/withWebsocketConnection";
import {MESSAGE_TYPE, TOPIC_TEMPLATE} from "../../websocket/types";

const CLEAN_TRICK_TIMOUT_MS = 2000;

const makeOtherPlayer = () => ({
  legalMoves: new Array(8).fill(true),
  cardsInHand: new Array(8).fill(CardValue.blue_back),
  authorisedContractValues: [],
  authorisedSpecialBiddings: [],
  authorisedContractSuits: [],
});

const players = {
  [Position.top]: makeOtherPlayer(),
  [Position.right]: makeOtherPlayer(),
  [Position.left]: makeOtherPlayer(),
  [Position.bottom]: {
    legalMoves: [],
    cardsInHand: [CardValue.jc, CardValue.jd, CardValue.jh, CardValue.js, CardValue.ac, CardValue.ad, CardValue.ah, CardValue.as],
    authorisedContractValues: [],
    authorisedSpecialBiddings: [],
    authorisedContractSuits: [],
  }
};

const getNextPlayer = (currentPlayer: Position): Position => {
  if (currentPlayer === Position.bottom) {
    return Position.left;
  }

  if (currentPlayer === Position.left) {
    return Position.top;
  }

  if (currentPlayer === Position.top) {
    return Position.right;
  }

  if (currentPlayer === Position.right) {
    return Position.bottom;
  }
  throw new Error('Unknown position');
};

const emptyTrick: Trick = { top: undefined, left: undefined, right: undefined, bottom: undefined };

const isFull = (trick: Trick): boolean => !!trick.top && !!trick.bottom && !!trick.left && !!trick.right;

type Props = InjectedProps & {
  gameId: string;
  username: string;
}

type State = GameState & {
  lastBiddingContract?: AuthorisedBidding
}

const getGameTopic = (gameId: string, username: string) => TOPIC_TEMPLATE.GAME
  .replace('{gameId}', gameId)
  .replace('{username}', username);

const getBroadcastGameTopic = (gameId: string) => TOPIC_TEMPLATE.GAME_BROADCAST
  .replace('{gameId}', gameId);

export default class MainGameScreen extends React.Component<Props, State> {
  state: State = {
    players,
    currentPlayer: Position.bottom,
    currentTrick: {...emptyTrick},
    currentPhase: GamePhase.bidding,
    contract: null,
  };

  componentDidMount(): void {
    this.props.registerOnMessageReceivedCallback(
      getGameTopic(this.props.gameId, this.props.username),
      MESSAGE_TYPE.ROUND_STARTED,
      ({ playerCards }) => this.updatePlayerCards(playerCards),
    );

    this.props.registerOnMessageReceivedCallback(
      getGameTopic(this.props.gameId, this.props.username),
      MESSAGE_TYPE.TURN_STARTED,
      ({ legalMoves }) => {
        const parsedlegalMoves = legalMoves.map((play: string) => JSON.parse(play));
        const authorisedContractBiddings = parsedlegalMoves
          .filter((play: AuthorisedBidding) => isAuthorisedContractBidding(play));
        const authorisedContractValues = authorisedContractBiddings.map((play: AuthorisedContractBidding) => play.value.toString());
        const authorisedContractSuits = authorisedContractBiddings.map((play: AuthorisedContractBidding) => fromLetter(play.suit));
        const authorisedSpecialBiddings = parsedlegalMoves
          .filter((play: AuthorisedBidding) => isAuthorisedSpecialBidding(play))
          .map((play: AuthorisedSpecialBidding) => play.special);

        this.setState(prevState => ({
          players: {
            ...prevState.players,
            [Position.bottom]: {
              ...prevState.players[Position.bottom],
              authorisedContractValues,
              authorisedSpecialBiddings,
              authorisedContractSuits,
            }
          }
        }))
      },
    );

    this.props.registerOnMessageReceivedCallback(
      getBroadcastGameTopic(this.props.gameId),
      MESSAGE_TYPE.PLAYER_BADE,
      (contract: AuthorisedBidding) => {
        if (isAuthorisedSpecialBidding(contract)) {
          this.setState(({
            lastBiddingContract: {
              special: contract.special,
            }
          }));
        } else {
          this.setState(({
            lastBiddingContract: {
              value: contract.value.toString() as ContractValue,
              suit: contract.suit.toLowerCase() as Suit,
            }
          }));
        }


        this.rotateCurrentPlayer();
      },
    );

    this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
    this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    // TODO replace socket endpoint by Socket endpoint template
    this.props.sendMessage(`/app/game/${this.props.gameId}/player/${this.props.username}/ready`, { type: MESSAGE_TYPE.CLIENT_READY })
  }

  componentDidUpdate(prevProps: Readonly<InjectedProps>): void {
    if (isFull(this.state.currentTrick)) {
      setTimeout(() => {
        this.setState({currentTrick: {...emptyTrick}})
      }, CLEAN_TRICK_TIMOUT_MS)
    }

    if (!prevProps.socketConnected && this.props.socketConnected) {
      this.props.subscribe(getGameTopic(this.props.gameId, this.props.username));
      this.props.subscribe(getBroadcastGameTopic(this.props.gameId));
    }
  }

  updatePlayerCards = (newCards: CardValue[]) => {
    this.setState(prevState => ({
      players: {
        ...prevState.players,
        [Position.bottom]: {
          ...prevState.players[Position.bottom],
          cardsInHand: newCards,
        }
      }
    }))
  };

  rotateCurrentPlayer = () => this.setState(prevState => ({
    currentPlayer: getNextPlayer(prevState.currentPlayer),
  }));

  playCard = (player: Position, card: CardValue) => {
    const playerCards = this.state.players[player].cardsInHand;
    playerCards.splice(playerCards.indexOf(card), 1);
    this.updatePlayerCards(playerCards);

    this.setState(prevState => {
      const player = prevState.currentPlayer;

      return ({
        currentTrick: {
          ...prevState.currentTrick,
          [player]: card,
        },
      })
    });

    this.rotateCurrentPlayer();
  };

  onCardPlayed = (player: Position, card: CardValue) => {
    const cardIndexInHand = this.state.players[player].cardsInHand.indexOf(card);

    if (
        cardIndexInHand === -1 ||
        !this.state.players[player].legalMoves[cardIndexInHand] ||
        player !== this.state.currentPlayer
    ) {
      return;
    }

    this.playCard(player, card);
  };

  onContractPicked = (contract: AuthorisedBidding) => {
    const message = isAuthorisedSpecialBidding(contract) ? {
      type: MESSAGE_TYPE.PLAYER_BADE,
      special: contract.special,
    } : {
      type: MESSAGE_TYPE.PLAYER_BADE,
      value: parseInt(contract.value),
      suit: contract.suit.toUpperCase(),
    };
    this.props.sendMessage(`/app/game/${this.props.gameId}/player/${this.props.username}/bid`, message);
    this.setState(prevState => ({
      players: {
        ...prevState.players,
        [Position.bottom]: {
          ...prevState.players[Position.bottom],
          authorisedContractValues: [],
          authorisedSpecialBiddings: [],
          authorisedContractSuits: [],
        }
      }
    }))
  };

  render() {
    const { players, currentTrick, lastBiddingContract } = this.state;
    let lastContract;
    if (lastBiddingContract) {
      if (isAuthorisedSpecialBidding(lastBiddingContract)) {
        lastContract = {
          special: lastBiddingContract!.special,
        }
      } else {
        lastContract = {
          value: lastBiddingContract!.value,
          suit: lastBiddingContract!.suit,
        };
      }
    }
    return <Container direction="column">
      <HandOfCards
          cards={players[Position.top].cardsInHand}
          rotationDegrees={180}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.top, card)}
      />
      <Container direction="row" justifyContent="space-around" width="100%">
        <HandOfCards
          cards={players[Position.left].cardsInHand}
          rotationDegrees={90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.left, card)}
        />
        {
          this.state.currentPhase === GamePhase.bidding && <BiddingBoard
            authorisedContractValues={players[Position.bottom].authorisedContractValues}
            authorisedSpecialBiddings={players[Position.bottom].authorisedSpecialBiddings}
            authorisedContractSuits={players[Position.bottom].authorisedContractSuits}
            onContractPicked={this.onContractPicked}
            lastContract={lastContract}
          />
        }
        {
          this.state.currentPhase === GamePhase.main && <CardBoard
            left={currentTrick[Position.left]}
            right={currentTrick[Position.right]}
            bottom={currentTrick[Position.bottom]}
            top={currentTrick[Position.top]}
          />
        }
        <HandOfCards
          cards={players[Position.right].cardsInHand}
          rotationDegrees={-90}
          scale={0.8}
          onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.right, card)}
        />
      </Container>
      <HandOfCards
        cards={players[Position.bottom].cardsInHand}
        rotationDegrees={0}
        onCardPlayed={(card: CardValue) => this.onCardPlayed(Position.bottom, card)}
        cardsBorderHighlight={players[Position.bottom].legalMoves}
      />
    </Container>;
  }
}