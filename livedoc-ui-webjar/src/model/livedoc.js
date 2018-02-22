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

export type Scoped = {
  visibility: ?ApiVisibility,
};

export type Secured = {
  auth: ApiAuthDoc,
};

export type Staged = {
  stage: ?ApiStage,
};

export type Livedoc = {
  version: string,
  basePath: string,
  apis: {[groupName: string]: $ReadOnlyArray<ApiDoc>},
  objects: {[groupName: string]: $ReadOnlyArray<ApiObjectDoc>},
  flows: {[groupName: string]: $ReadOnlyArray<ApiFlowDoc>},
  global: ApiGlobalDoc,
  playgroundEnabled: boolean,
  displayMethodAs: MethodDisplay,
};

export type ApiAuthDoc = {
  type: ApiAuthType,
  roles: Array<string>,
  testUsers: {[key: string]: string},
  scheme: ?string,
  testTokens: Array<string>,
}

export type ApiAuthType = 'NONE' | 'BASIC_AUTH' | 'TOKEN' ;

export type ApiRequestBodyDoc = Identified & {
  type: LivedocType,
  template: any,
}

export type ApiChangelogsDoc = Identified & {
  changelogs: Array<ApiChangelogDoc>,
}

export type ApiChangelogDoc = Identified & {
  version: string,
  changes: Array<string>,
}

export type ApiDoc = Identified & Named & Versioned & Secured & Scoped & Staged & {
  description: string,
  group: string,
  methods: Array<ApiMethodDoc>,
};

export type ApiErrorDoc = Identified & {
  code: string,
  description: string
}

export type ApiFlowDoc = Identified & Named & {
  description: string,
  preconditions: Array<string>,
  steps: Array<ApiFlowStepDoc>,
  methods: Array<ApiMethodDoc>,
  group: string,
}

export type ApiFlowStepDoc = Identified & {
  apiMethodId: string,
  apiMethodDoc: ApiMethodDoc,
}

export type ApiGlobalDoc = Identified & {
  sections: Array<ApiGlobalSectionDoc>,
  changelogSet: ApiChangelogsDoc,
  migrationSet: ApiMigrationsDoc,
}

export type ApiGlobalSectionDoc = Identified & {
  title: string,
  paragraphs: Array<string>,
}

export type ApiHeaderDoc = Identified & Named & {
  description: string,
  type: HeaderFilterType,
  values: Array<string>,
  defaultValue: ?string,
}

export type HeaderFilterType = 'REQUIRED_MATCHING' | 'OPTIONAL' | 'FORBIDDEN' | 'DIFFERENT';

export type ApiMethodDoc = Identified & LivedocHints & Versioned & Scoped & Staged & {
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

export type ApiMigrationsDoc = Identified & {
  migrations: Array<ApiMigrationDoc>,
}

export type ApiMigrationDoc = Identified & {
  fromVersion: string,
  toVersion: string,
  steps: Array<string>,
}

export type ApiObjectDoc = Identified & Named & LivedocHints & Versioned & Scoped & Staged & {
  description: string,
  group: string,
  fields: Array<ApiObjectFieldDoc>,
  auth: ApiAuthDoc,
  allowedValues: ?Array<string>,
  template: any,
};

export type ApiObjectFieldDoc = Identified & Named & LivedocHints & Versioned & {
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

export type ApiVisibility = 'PRIVATE' | 'PUBLIC';

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
