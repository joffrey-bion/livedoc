// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { ApiDocPanel } from './api/ApiDocPanel';
import { TypeDocPanel } from './type/TypeDocPanel';
import { FlowDocPanel } from './flow/FlowDocPanel';
import type { ApiDoc, ApiFlowDoc, ApiObjectDoc, LivedocID } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { actions, getSelectedApi, getSelectedFlow, getSelectedType } from '../../../redux/reducer';

type ContentPanelProps = {
  selectedApi: ApiDoc,
  selectedType: ApiObjectDoc,
  selectedFlow: ApiFlowDoc,
  onTypeClick: (id: LivedocID) => void,
}

const ContentPanel = ({selectedApi, selectedType, selectedFlow, onTypeClick}: ContentPanelProps) => {
  if (selectedApi) {
    return <ApiDocPanel apiDoc={selectedApi} onMethodSelect={id => console.log(id)} onTypeClick={onTypeClick}/>;
  }
  if (selectedType) {
    return <TypeDocPanel typeDoc={selectedType} onTypeClick={onTypeClick}/>;
  }
  if (selectedFlow) {
    return <FlowDocPanel flowDoc={selectedFlow} onMethodSelect={id => console.log(id)} onTypeClick={onTypeClick}/>;
  }
  return <p>Select an element to see its documentation</p>;
};

const mapStateToProps = (state: State) => ({
  selectedApi: getSelectedApi(state),
  selectedType: getSelectedType(state),
  selectedFlow: getSelectedFlow(state),
});

const mapDispatchToProps = {
  onTypeClick: actions.selectElement,
};

export default connect(mapStateToProps, mapDispatchToProps)(ContentPanel);
