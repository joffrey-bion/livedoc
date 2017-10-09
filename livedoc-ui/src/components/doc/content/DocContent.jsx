// @flow
import * as React from 'react';
import { connect } from 'react-redux';
import { ApiDocPanel } from './api/ApiDocPanel';
import { ApiTypePanel } from './type/ApiTypePanel';
import { ApiFlowPanel } from './flow/ApiFlowPanel';
import type { ApiDoc, ApiFlowDoc, ApiObjectDoc } from '../../../model/livedoc';
import type { State } from '../../../model/state';
import { actions, getSelectedApi, getSelectedType, getSelectedFlow } from '../../../redux/reducer';

type Props = {
  selectedApi: ApiDoc,
  selectedType: ApiObjectDoc,
  selectedFlow: ApiFlowDoc,
  onSelect: (id: string) => void,
}

const DocContent = ({selectedApi, selectedType, selectedFlow, onSelect}: Props) => {
  if (selectedApi) {
    return <ApiDocPanel apiDoc={selectedApi} onSelect={onSelect}/>;
  }
  if (selectedType) {
    return <ApiTypePanel typeDoc={selectedType} onSelect={onSelect}/>;
  }
  if (selectedFlow) {
    return <ApiFlowPanel flowDoc={selectedFlow} onSelect={onSelect}/>;
  }
  return <p>Select an element to see its documentation</p>;
};

const mapStateToProps = (state: State) => ({
  selectedApi: getSelectedApi(state),
  selectedType: getSelectedType(state),
  selectedFlow: getSelectedFlow(state),
});

const mapDispatchToProps = {
  onSelect: actions.selectElement,
};

export default connect(mapStateToProps, mapDispatchToProps)(DocContent);
