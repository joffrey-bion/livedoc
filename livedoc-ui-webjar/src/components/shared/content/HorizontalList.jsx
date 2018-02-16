// @flow
export type HorizontalListProps = {
  items: any[],
  delimiter?: string,
}

export const HorizontalList = ({items, delimiter = ', '}: HorizontalListProps) => {
  let result = [];
  for (let e of items) {
    result.push(e);
    result.push(delimiter);
  }
  if (result.length > 0) {
    result.pop();
  }
  return result;
};
