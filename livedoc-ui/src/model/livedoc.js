// @flow
export type Authenticated = {
  auth: ApiAuthDoc,
};

export type LivedocID = string;

export type Identified = {
  livedocId: LivedocID,
};

export type Named = {
  name: string,
};

export type Versioned = {
  supportedversions: ApiVersionDoc,
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
  type: string,
  roles: Array<string>,
  testusers: {[key: string]: string},
  scheme: string,
  testtokens: Array<string>
}

export type ApiBodyObjectDoc = Identified & {
  type: LivedocType,
  template: any
}

export type ApiChangelogsDoc = Identified & {
  changelogs: Array<ApiChangelogDoc>,
}

export type ApiChangelogDoc = Identified & {
  version: string,
  changes: Array<string>,
}

export type ApiDoc = Identified & Named & Versioned & Authenticated & {
  description: string,
  visibility: ApiVisibility,
  stage: ApiStage,
  group: string,
  methods: Array<ApiMethodDoc>,
};

export type ApiErrorDoc = Identified & {
  code: string,
  description: string
}

export type ApiFlowDoc = Named & {
  description: string,
  preconditions: Array<string>,
  steps: Array<ApiFlowStepDoc>,
  methods: Array<ApiMethodDoc>,
  group: string,
}

export type ApiFlowStepDoc = Identified & {
  apimethodid: string,
  apimethoddoc: ApiMethodDoc,
}

export type ApiGlobalDoc = Identified & {
  sections: Array<ApiGlobalSectionDoc>,
  changelogset: ApiChangelogsDoc,
  migrationset: ApiMigrationsDoc,
}

export type ApiGlobalSectionDoc = Identified & {
  title: string,
  paragraphs: Array<string>,
}

export type ApiHeaderDoc = Identified & Named & {
  description: string,
  allowedvalues: Array<string>,
}

export type ApiMethodDoc = Identified & LivedocHints & Versioned & {
  id: string,
  path: Array<string>,
  method: string,
  description: string,
  summary: string,
  verb: Array<ApiVerb>,
  produces: Array<string>,
  consumes: Array<string>,
  headers: Array<ApiHeaderDoc>,
  pathparameters: Array<ApiParamDoc>,
  queryparameters: Array<ApiParamDoc>,
  bodyobject: ApiBodyObjectDoc,
  response: ApiResponseObjectDoc,
  responsestatuscode: string,
  visibility: ApiVisibility,
  stage: ApiStage,
  apierrors: Array<ApiErrorDoc>,
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

export type ApiObjectDoc = Identified & Named & LivedocHints & Versioned & {
  description: string,
  visibility: ApiVisibility,
  stage: ApiStage,
  group: string,
  fields: Array<ApiObjectFieldDoc>,
  auth: ApiAuthDoc,
  allowedvalues: ?Array<string>,
  template: any,
};

export type ApiObjectFieldDoc = Identified & Named & LivedocHints & Versioned & {
  description: string,
  type: LivedocType,
  format: Array<string>,
  allowedvalues: Array<string>,
};

export type ApiParamDoc = LivedocHints & Named & {
  description: string,
  type: LivedocType,
  format: string,
  allowedvalues: Array<string>,
  required: 'true' | 'false' | 'undefined',
  defaultValue: string,
}

export type ApiResponseObjectDoc = Identified & {
  type: LivedocType,
}

export type ApiStage = '' | 'PRE_ALPHA' | 'ALPHA' | 'BETA' | 'RC' | 'GA' | 'DEPRECATED';

export type ApiVerb = 'GET' | 'POST' | 'PATCH' | 'PUT' | 'DELETE' | 'HEAD' | 'OPTIONS' | 'TRACE' | 'UNDEFINED';

export type ApiVersionDoc = {
  since: string,
  until: string,
};

export type ApiVisibility = '' | 'PRIVATE' | 'PUBLIC';

export type LivedocHints = {
  jsondocerrors: Array<string>,
  jsondocwarnings: Array<string>,
  jsondochints: Array<string>,
};

export type LivedocType = {
  oneLineText: string,
  typeElements: Array<TypeElement>,
};

export type TypeElement = {
  text: string,
  link: string,
};

export type MethodDisplay = 'URI' | 'SUMMARY' | 'METHOD';
