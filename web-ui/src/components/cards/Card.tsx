import styled from "styled-components";

type Props = {
  rotationDegrees: number;
  translationX?: number;
  translationY?: number;
  scale?: number;
  disableHoverTransformation?: boolean;
  highlightBorder?: boolean;
}

const getTransformationCSS = (rotationDegrees: number, translationX?: number, translationY?: number, scale?: number) => `
  rotate(${rotationDegrees}deg)
  scale(${scale || 1})
  translateX(${translationX || 0}px)
  translateY(${translationY || 0}px)
`;

const Card = styled.img`
  width: 100px;
  height: 150px;
  border-radius: 10px;
  border: ${({highlightBorder}: Props) => highlightBorder ? '3px solid red' : ''};
  transform: ${
    ({rotationDegrees, translationX, translationY, scale}: Props) => getTransformationCSS(rotationDegrees, translationX, translationY, scale)};
  &:hover {
    transform: ${({translationX, scale, disableHoverTransformation}: Props) => !disableHoverTransformation && getTransformationCSS(0, translationX, 0,(scale || 1) * 1.2)}
`;

export default Card;