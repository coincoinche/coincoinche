export type SelectorProps = {
  selectedByPlayer?: boolean;
  selectedByOpponent?: boolean;
  disabled?: boolean;
}

export const getBackgroundColor = ({selectedByOpponent, disabled, selectedByPlayer}: SelectorProps) =>
  selectedByPlayer ? 'yellowgreen':
    selectedByOpponent ? 'coral' :
      disabled ? 'gray' : 'darkgreen';

export const getColor = ({selectedByOpponent, disabled, selectedByPlayer}: SelectorProps) =>
  selectedByPlayer ? 'darkgreen':
    selectedByOpponent ? 'darkred' :
      disabled ? 'darkgray' : 'yellowgreen';

export const getBorderColor = ({selectedByOpponent, disabled}: SelectorProps) =>
    selectedByOpponent ? 'darkred' :
      disabled ? 'darkgray' : 'black';

export const getHoverBackgroundColor = (selectable: boolean) =>
  selectable ? 'yellowgreen' : '';

export const getHoverColor = (selectable: boolean) =>
  selectable ? 'darkgreen' : '';

export const getHoverBorderColor = (selectable: boolean) =>
  selectable ? 'black' : '';