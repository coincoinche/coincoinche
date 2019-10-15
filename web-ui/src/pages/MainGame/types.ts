import {SpecialBidding, Suit} from "../../game-engine/gameStateTypes";

/**
 * START Authorised bidding formats returned by game engine
 */
export type AuthorisedSpecialBidding = {
  special: SpecialBidding
};
export type AuthorisedContractBidding = {
  value: ContractValue,
  suit: Suit
};
export type AuthorisedBidding = AuthorisedSpecialBidding | AuthorisedContractBidding;
export const isAuthorisedSpecialBidding =
  (authorisedBidding: AuthorisedBidding): authorisedBidding is AuthorisedSpecialBidding =>
    !!(authorisedBidding as AuthorisedSpecialBidding).special;
/**
 * END Authorised bidding formats returned by game engine
 */

export enum ContractValue {
  EIGHTY = '80',
  NINETY = '90',
  HUNDRED = '100',
  HUNDRED_TEN = '110',
  HUNDRED_TWENTY = '120',
  HUNDRED_THIRTY = '130',
  HUNDRED_FOURTY = '140',
  HUNDRED_FIFTY = '150',
  HUNDRED_SIXTY = '160',
  CAPOT = '250',
  GENERALE = '500',
}

export const getDisplayedValue = (value: ContractValue) => {
  if (value === ContractValue.CAPOT) {
    return 'CAPOT';
  }

  if (value === ContractValue.GENERALE) {
    return 'GENERALE';
  }

  return value;
};
