// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { ApiObjectDoc } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getType } from '../../../redux/livedoc';
import { JsonCard } from '../../cards/JsonCard';

export type TypeExampleProps = {
  typeDoc: ApiObjectDoc,
}

const TypeExample = ({typeDoc}: TypeExampleProps) => {
  if (!typeDoc) {
    return null;
  }
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;
  if (isEnum) {
    return null;
  }
  return <section>
    <h3 style={{marginTop: '1rem'}}>Example</h3>
    <JsonCard jsonObject={typeDoc.template}/>
  </section>;
};

export type TypeTemplateOwnProps = {
  match: any,
}

const mapStateToProps = (state: State, {match}: TypeTemplateOwnProps) => ({
  typeDoc: getType(match.params.typeId, state),
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(TypeExample);
