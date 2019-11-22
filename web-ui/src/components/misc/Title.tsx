import styled from "styled-components";

type Props = {
  fontSize: number;
}

export default styled.div`
  color: #FFE136;
  text-shadow: ${({ fontSize }: Props) => {
    const shaddow = Math.round(fontSize / 20);
    return `-${shaddow}px 0 black, 0 ${shaddow}px black, ${shaddow}px 0 black, 0 -${shaddow}px black`;
  }};
  font-family: 'Cookie',cursive;
  font-size: ${({ fontSize }: Props) => fontSize}pt;
  padding-bottom: 20px;
`;