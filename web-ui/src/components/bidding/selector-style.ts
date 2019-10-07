export type SelectorProps = {
  selectedByPlayer?: boolean;
  selectedByOpponent?: boolean;
  disabled?: boolean;
}

export const getBackgroundColor = ({selectedByOpponent, disabled, selectedByPlayer}: SelectorProps) =>
  disabled ? 'gray' :
    selectedByPlayer ? 'yellowgreen':
      selectedByOpponent ? 'coral' : 'darkgreen';

export const getColor = ({selectedByOpponent, disabled, selectedByPlayer}: SelectorProps) =>
  disabled ? 'darkgray' :
    selectedByPlayer ? 'darkgreen':
      selectedByOpponent ? 'darkred' : 'yellowgreen';

export const getHoverBackgroundColor = (selectable: boolean) =>
  selectable ? 'yellowgreen' : '';

export const getHoverColor = (selectable: boolean) =>
  selectable ? 'darkgreen' : '';