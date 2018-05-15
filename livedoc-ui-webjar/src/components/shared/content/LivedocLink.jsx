//@flow
import * as React from 'react';
import { Link } from 'react-router-dom';
import { RouteHelper } from '../../../routing/routeHelper';

const API: RegExp = /livedoc:\/\/api\/([^/]+)/;

const OPERATION: RegExp = /livedoc:\/\/api\/([^/]+)\/([^/]+)/;

const TYPE: RegExp = /livedoc:\/\/type\/([^/]+)/;

export type LivedocLinkProps = {
  url: string;
  [string]: any;
}

export const LivedocLink = ({url, ...otherProps}: LivedocLinkProps) => <Link to={getRoute(url)} {...otherProps}/>;

function getRoute(url: string): ?string {
  const operation = OPERATION.exec(url);
  if (operation != null && operation.length > 1) {
    const apiId = operation[1];
    const operationId = operation[2];
    return RouteHelper.operationUrl(apiId, operationId);
  }

  const api = API.exec(url);
  if (api != null && api.length > 1) {
    const apiId = api[1];
    return RouteHelper.apiUrl(apiId);
  }

  const type = TYPE.exec(url);
  if (type != null && type.length > 1) {
    const typeId = type[1];
    return RouteHelper.typeUrl(typeId);
  }

  return null;
}
