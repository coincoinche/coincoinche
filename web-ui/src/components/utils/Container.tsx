import styled from "styled-components";

type Props = {
  direction: string;
  alignItems?: string;
  justifyContent?: string;
}

export default styled.div`
  display: flex;
  flex-direction: ${({direction}: Props) => direction};
  align-items: ${({alignItems}: Props) => alignItems || 'center'};
  justify-content: ${({justifyContent}: Props) => justifyContent || 'center'};
`;
