import type { LivedocID } from '../model/livedoc';

const GLOBAL: string = '/global';
const APIS: string = '/apis';
const TYPES: string = '/types';

/*
Implementation note: every ID is URL-encoded again here, despite the fact that the server already did.
This is to counter-effect the extra decoding performed by react-router, and more specifically the history library.
This issue is tracked here: https://github.com/ReactTraining/history/issues/505
 */
function workaroundRouterIssue(input: String) {
  return encodeURIComponent(encodeURIComponent(input));
}

export class RouteHelper {

  static globalPageUrl(pageId: LivedocID): string {
    const reencodedId = workaroundRouterIssue(pageId);
    return `${GLOBAL}/${reencodedId}`;
  }

  static apisUrl(): string {
    return APIS;
  }

  static apiUrl(apiId: LivedocID): string {
    const apisUrl = RouteHelper.apisUrl();
    const reencodedId = workaroundRouterIssue(apiId);
    return `${apisUrl}/${reencodedId}`;
  }

  static operationUrl(apiId: LivedocID, operationId: LivedocID): string {
    const apiUrl = RouteHelper.apiUrl(apiId);
    const reencodedId = workaroundRouterIssue(operationId);
    return `${apiUrl}/${reencodedId}`;
  }

  static messageUrl(apiId: LivedocID, messageId: LivedocID): string {
    const apiUrl = RouteHelper.apiUrl(apiId);
    const reencodedId = workaroundRouterIssue(messageId);
    return `${apiUrl}/${reencodedId}`;
  }

  static typesUrl(): string {
    return TYPES;
  }

  static typeUrl(typeId: LivedocID): string {
    const typesUrl = RouteHelper.typesUrl();
    const reencodedId = workaroundRouterIssue(typeId);
    return `${typesUrl}/${reencodedId}`;
  }
}
