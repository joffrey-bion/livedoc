// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import type { ApiObjectDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { getType } from '../../../redux/livedoc';
import { RouteHelper } from '../../../routing/routeHelpler';
import { ContentHeader } from '../../shared/content/ContentHeader';
import { ComplexTypeDetails } from './complex/ComplexTypeDetails';
import { EnumTypeDetails } from './enum/EnumTypeDetails';

export type TypeDocPanelProps = {
  typeDoc: ?ApiObjectDoc,
}

const TypeDocContent = ({isEnum, typeDoc}) => {
  if (isEnum) {
    return <EnumTypeDetails typeDoc={typeDoc}/>;
  } else {
    return <ComplexTypeDetails typeDoc={typeDoc}/>;
  }
};

const TypeDocPanelPresenter = ({typeDoc}: TypeDocPanelProps) => {
  if (!typeDoc) {
    return <Redirect to={RouteHelper.typesUrl()}/>;
  }
  const isEnum = typeDoc.allowedvalues && typeDoc.allowedvalues.length > 0;

  return <section>
    <ContentHeader title={typeDoc.name} description={typeDoc.description} stage={typeDoc.stage}/>
    <TypeDocContent isEnum={isEnum} typeDoc={typeDoc}/>
  </section>;
};

export type TypeDocPanelOwnProps = {
  typeId: ?LivedocID,
}

const mapStateToProps = (state: State, {typeId}: TypeDocPanelOwnProps) => ({
  typeDoc: typeId && getType(typeId, state),
});

const mapDispatchToProps = {};

export const TypeDocPanel = connect(mapStateToProps, mapDispatchToProps)(TypeDocPanelPresenter);
