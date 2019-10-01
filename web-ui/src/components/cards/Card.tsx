import styled from "styled-components";

type Props = {
  rotationDegrees: number;
  translationX: number,
  translationY: number,
}

const applyTransformation = (rotationDegrees: number, translationX: number, translationY: number, scale: number) => `
  rotate(${rotationDegrees}deg)
  scale(${scale})
  translateX(${translationX}px)
  translateY(${translationY}px)
`;

const Card = styled.img`
  background-color: cornflowerblue;
  width: 100px;
  height: 150px;
  border-radius: 10px;
  transform: ${
    ({rotationDegrees, translationX, translationY}: Props) => applyTransformation(rotationDegrees, translationX, translationY, 1)};
  &:hover {
    transform: ${({translationX}: Props) => applyTransformation(0, translationX, 0,1.2)}
`;

export default Card;