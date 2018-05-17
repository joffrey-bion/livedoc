// @flow
export type LivedocID = string;

export type Identified = {
  livedocId: LivedocID,
};

export type Named = {
  name: string,
};

export type Versioned = {
  supportedVersions: ApiVersionDoc,
};

export type Secured = {
  auth: ApiAuthDoc,
};

export type Staged = {
  stage: ?ApiStage,
};

export type Livedoc = {
  livedocInfo: LivedocMetaData,
  apiInfo: ApiMetaData,
  apis: Array<Group<ApiDoc>>,
  types: Array<Group<ApiTypeDoc>>,
  global: ApiGlobalDoc,
  playgroundEnabled: boolean,
  displayMethodAs: MethodDisplay,
};

export type ApiMetaData = {
  version: string,
  baseUrl: string,
}

export type LivedocMetaData = {
  version: string,
  buildDate: string,
}

export type Group<T> = {
  groupName: string,
  elements: Array<T>,
}

export type ApiAuthDoc = {
  type: ApiAuthType,
  roles: Array<string>,
  testUsers: {[key: string]: string},
  scheme: ?string,
  testTokens: Array<string>,
}

export type ApiAuthType = 'NONE' | 'BASIC_AUTH' | 'TOKEN' ;

export type ApiRequestBodyDoc = {
  type: LivedocType,
  template: any,
}

export type ApiDoc = Identified & Named & Versioned & Secured & Staged & {
  description: string,
  group: string,
  operations: Array<ApiOperationDoc>,
  messages: Array<AsyncMessageDoc>,
};

export type ApiErrorDoc = {
  code: string,
  description: string
}

export type ApiGlobalDoc = {
  homePageId: LivedocID,
  pages: Array<GlobalDocPage>,
}

export type GlobalDocPage = Identified & {
  title: string,
  content: string,
}

export type ApiHeaderDoc = Named & {
  description: string,
  type: HeaderFilterType,
  values: Array<string>,
  defaultValue: ?string,
}

export type HeaderFilterType = 'REQUIRED_MATCHING' | 'OPTIONAL' | 'FORBIDDEN' | 'DIFFERENT';

export type ApiOperationDoc = Identified & LivedocHints & Versioned & Staged & {
  id: string,
  paths: Array<string>,
  name: string,
  description: string,
  summary: string,
  verbs: Array<ApiVerb>,
  produces: Array<string>,
  consumes: Array<string>,
  headers: Array<ApiHeaderDoc>,
  pathParameters: Array<ApiParamDoc>,
  queryParameters: Array<ApiParamDoc>,
  requestBody: ?ApiRequestBodyDoc,
  responseBodyType: LivedocType,
  responseStatusCode: string,
  apiErrors: Array<ApiErrorDoc>,
  auth: ApiAuthDoc,
}

export type AsyncMessageDoc = Identified & LivedocHints & Versioned & Staged & Secured & {
  name: string,
  summary: string,
  description: string,
  command: AsyncCommand,
  destinations: Array<string>,
  destinationVariables: Array<ApiParamDoc>,
  headers: Array<ApiHeaderDoc>,
  payloadType: LivedocType,
  triggeredMessages: Array<AsyncMessageDoc>,
}

export type AsyncCommand = 'SEND' | 'SUBSCRIBE';

export type ApiTypeDoc = Identified & Named & LivedocHints & Versioned & Staged & {
  description: string,
  group: string,
  fields: Array<ApiPropertyDoc>,
  auth: ApiAuthDoc,
  allowedValues: ?Array<string>,
  template: any,
};

export type ApiPropertyDoc = Named & LivedocHints & Versioned & {
  description: string,
  type: LivedocType,
  format: Array<string>,
  allowedValues: Array<string>,
};

export type ApiParamDoc = LivedocHints & Named & {
  description: string,
  type: LivedocType,
  format: string,
  allowedValues: Array<string>,
  required: 'true' | 'false' | 'undefined',
  defaultValue: string,
}

export type ApiStage = 'PRE-ALPHA' | 'ALPHA' | 'BETA' | 'RC' | 'GA' | 'DEPRECATED';

export type ApiVerb = 'GET' | 'POST' | 'PATCH' | 'PUT' | 'DELETE' | 'HEAD' | 'OPTIONS' | 'TRACE';

export type ApiVersionDoc = {
  since: string,
  until: string,
};

export type LivedocHints = {
  livedocErrors: Array<string>,
  livedocWarnings: Array<string>,
  livedocHints: Array<string>,
};

export type LivedocType = {
  oneLineText: string,
  typeElements: Array<TypeElement>,
};

export type TypeElement = {
  text: string,
  livedocId: ?LivedocID,
};

export type MethodDisplay = 'URI' | 'SUMMARY' | 'METHOD';
