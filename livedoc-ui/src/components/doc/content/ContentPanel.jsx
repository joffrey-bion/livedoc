// @flow
import * as React from 'react';
import { Route, Switch } from 'react-router-dom';
import ApiDocPanel from './api/ApiDocPanel';
import FlowDocPanel from './flow/FlowDocPanel';
import GlobalDocPanel from './global/GlobalDocPanel';
import TypeDocPanel from './type/TypeDocPanel';

export const ContentPanel = () => (<Switch>
  <Route path="/global" component={GlobalDocPanel}/>
  <Route path="/apis/:apiId" component={ApiDocPanel}/>
  <Route path="/types/:typeId" component={TypeDocPanel}/>
  <Route path="/flows/:flowId" component={FlowDocPanel}/>
  <Route render={(props) => <p>Select an element to see its documentation</p>}/>
</Switch>);
