// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiObjectDoc } from '../../../../model/livedoc';
import type { State } from '../../../../model/state';
import { getType } from '../../../../redux/livedoc';
import { ContentHeader } from '../ContentHeader';
import { ComplexTypeDetails } from './complex/ComplexTypeDetails';
import { EnumTypeDetails } from './enum/EnumTypeDetails';

export type TypeDocPanelProps = {
  typeDoc: ?ApiObjectDoc,
}

const TypeDocContent = ({isEnum, typeDoc}) => {
  if (isEnum) {
    return <EnumTypeDetails typeDoc={typeDoc}/>;
  } else {
    return <ComplexTypeDetails typeDoc={typeDoc}/>
  }
};

const TypeDocPanel = ({typeDoc}: TypeDocPanelProps) => {
  if (!typeDoc) {
    return <Redirect to="/types"/>;
  }
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;

  return <section>
    <ContentHeader title={typeDoc.name} description={typeDoc.description}/>
    <TypeDocContent isEnum={isEnum} typeDoc={typeDoc}/>
  </section>
};

export type TypeDocPanelOwnProps = {
  match: any,
}

const mapStateToProps = (state: State, {match}: TypeDocPanelOwnProps) => ({
  typeDoc: getType(match.params.typeId, state),
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(TypeDocPanel);
