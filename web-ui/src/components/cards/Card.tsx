import styled from "styled-components";

type Props = {
  rotationDegrees: number;
  translationX: number;
  translationY: number;
  scale?: number;
}

const applyTransformation = (rotationDegrees: number, translationX: number, translationY: number, scale?: number) => `
  rotate(${rotationDegrees}deg)
  scale(${scale || 1})
  translateX(${translationX}px)
  translateY(${translationY}px)
`;

const Card = styled.img`
  background-color: cornflowerblue;
  width: 100px;
  height: 150px;
  border-radius: 10px;
  transform: ${
    ({rotationDegrees, translationX, translationY, scale}: Props) => applyTransformation(rotationDegrees, translationX, translationY, scale)};
  &:hover {
    transform: ${({translationX, scale}: Props) => applyTransformation(0, translationX, 0,(scale || 1) * 1.2)}
`;

export default Card;