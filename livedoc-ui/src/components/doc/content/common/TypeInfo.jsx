// @flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import type { LivedocType, TypeElement } from '../../../../model/livedoc';

export type TypeInfoProps = {
  type: LivedocType,
  required: ?boolean,
}

type TypeElementProps = {
  element: TypeElement,
}

const TypeElementLink = ({element}: TypeElementProps) => {
  const text = <code>{element.text}</code>;

  if (element.link) {
    return <Link to={'/types/' + element.link}>{text}</Link>
  } else {
    return text;
  }
};

export const TypeInfo = (props: TypeInfoProps) => {
  const optionalMark = props.required === false ? '?' : '';

  // this if handles old versions of Livedoc
  // TODO remove when unnecessary
  if (props.type.typeElements) {
    return props.type.typeElements.map(e => <TypeElementLink key={e.text} element={e}/>);
  } else {
    return <code>{props.type.oneLineText + optionalMark}</code>;
  }
};

