// @flow
import * as React from 'react';
import { Route, Switch } from 'react-router-dom';
import ApiDocPanel from './api/ApiDocPanel';
import FlowDocPanel from './flow/FlowDocPanel';
import TypeDocPanel from './type/TypeDocPanel';

type ContentPanelProps = {}

export const ContentPanel = (props: ContentPanelProps) => (<Switch>
  <Route path="/apis/:apiId" component={ApiDocPanel}/>
  <Route path="/types/:typeId" component={TypeDocPanel}/>
  <Route path="/flows/:flowId" component={FlowDocPanel}/>
  <Route render={(props) => <p>Select an element to see its documentation</p>}/>
</Switch>);
