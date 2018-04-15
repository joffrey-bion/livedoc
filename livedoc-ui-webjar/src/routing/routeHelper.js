import type { LivedocID } from '../model/livedoc';

const GLOBAL: string = '/global';
const APIS: string = '/apis';
const TYPES: string = '/types';
const FLOWS: string = '/flows';

export class RouteHelper {

  static globalPageUrl(pageId: LivedocID): string {
    return `${GLOBAL}/${pageId}`;
  }

  static apisUrl(): string {
    return APIS;
  }

  static apiUrl(apiId: LivedocID): string {
    const apisUrl = RouteHelper.apisUrl();
    return `${apisUrl}/${apiId}`;
  }

  static operationUrl(apiId: LivedocID, methodId: LivedocID): string {
    const apiUrl = RouteHelper.apiUrl(apiId);
    return `${apiUrl}/${methodId}`;
  }

  static typesUrl(): string {
    return TYPES;
  }

  static typeUrl(typeId: LivedocID): string {
    const typesUrl = RouteHelper.typesUrl();
    return `${typesUrl}/${typeId}`;
  }

  static flowsUrl(): string {
    return FLOWS;
  }

  static flowOperationUrl(methodId: LivedocID): string {
    const flowsUrl = RouteHelper.flowsUrl();
    return `${flowsUrl}/${methodId}`;
  }
}