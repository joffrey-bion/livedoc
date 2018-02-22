// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { ApiTypeDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getType } from '../../../redux/livedoc';
import { JsonCard } from '../../shared/cards/JsonCard';

export type TypeExampleProps = {
  typeDoc: ApiTypeDoc,
}

const TypeExamplePresenter = ({typeDoc}: TypeExampleProps) => {
  if (!typeDoc) {
    return null;
  }
  const isEnum = typeDoc.allowedValues && typeDoc.allowedValues.length > 0;
  if (isEnum) {
    return null;
  }
  return <section>
    <h3 style={{marginTop: '1rem'}}>Example</h3>
    <JsonCard jsonObject={typeDoc.template}/>
  </section>;
};

export type TypeExampleOwnProps = {
  typeId: ?LivedocID,
}

const mapStateToProps = (state: State, {typeId}: TypeExampleOwnProps) => ({
  typeDoc: typeId && getType(typeId, state),
});

const mapDispatchToProps = {};

export const TypeExample = connect(mapStateToProps, mapDispatchToProps)(TypeExamplePresenter);
