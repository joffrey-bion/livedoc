// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { getType } from '../../../../redux/reducer';

export type TypeTemplateProps = {
  typeDoc: ApiObjectDoc,
}

const TypeTemplate = ({typeDoc}: TypeTemplateProps) => {
  if (!typeDoc) {
    return null;
  }
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;
  if (isEnum) {
    return null;
  }
  return <section>
    <h2>Template</h2>
    <pre>
    {JSON.stringify(typeDoc.template, null, 2)}
    </pre>
  </section>
};


export type TypeTemplateOwnProps = {
  match: any,
}

const mapStateToProps = (state: State, {match}: TypeTemplateOwnProps) => ({
  typeDoc: getType(match.params.typeId, state),
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(TypeTemplate);
