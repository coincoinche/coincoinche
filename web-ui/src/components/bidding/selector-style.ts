export type SelectorProps = {
  selectedByPlayer?: boolean;
  selectedByOpponent?: boolean;
  disabled?: boolean;
}

export const getBackgroundColor = ({selectedByOpponent, disabled}: SelectorProps) =>
    selectedByOpponent ? 'coral' :
        disabled ? 'gray' : 'darkgreen';

export const getColor = ({selectedByOpponent, disabled}: SelectorProps) =>
    selectedByOpponent ? 'darkred' :
        disabled ? 'darkgray' : 'yellowgreen';

export const getHoverBackgroundColor = (selectable: boolean) =>
    selectable ? 'yellowgreen' : '';

export const getHoverColor = (selectable: boolean) =>
    selectable ? 'darkgreen' : '';