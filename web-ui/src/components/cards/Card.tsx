import styled from "styled-components";

type Props = {
  rotationDegrees: number;
  translationX?: number;
  translationY?: number;
  scale?: number;
}

const getTransformationCSS = (rotationDegrees: number, translationX?: number, translationY?: number, scale?: number) => `
  rotate(${rotationDegrees}deg)
  scale(${scale || 1})
  translateX(${translationX || 0}px)
  translateY(${translationY || 0}px)
`;

const Card = styled.img`
  background-color: cornflowerblue;
  width: 100px;
  height: 150px;
  border-radius: 10px;
  transform: ${
    ({rotationDegrees, translationX, translationY, scale}: Props) => getTransformationCSS(rotationDegrees, translationX, translationY, scale)};
  &:hover {
    transform: ${({translationX, scale}: Props) => getTransformationCSS(0, translationX, 0,(scale || 1) * 1.2)}
`;

export default Card;