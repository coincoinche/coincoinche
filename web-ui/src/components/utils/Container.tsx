import styled from "styled-components";

type Props = {
  direction: string;
  alignItems?: string;
  justifyContent?: string;
  width?: string;
  minWidth?: string;
  backgroundColor?: string;
  borderRadius?: string;
  padding?: string;
}

export default styled.div`
  display: flex;
  flex-direction: ${({direction}: Props) => direction};
  align-items: ${({alignItems}: Props) => alignItems || 'center'};
  justify-content: ${({justifyContent}: Props) => justifyContent || 'center'};
  width: ${({width}: Props) => width || ''};
  min-width: ${({minWidth}: Props) => minWidth || ''};
  background-color: ${({backgroundColor}: Props) => backgroundColor || ''};
  border-radius: ${({borderRadius}: Props) => borderRadius || ''};
  padding: ${({padding}: Props) => padding || ''};
`;
