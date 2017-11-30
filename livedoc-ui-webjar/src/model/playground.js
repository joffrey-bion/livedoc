import type { ApiVerb } from "./livedoc";

export type RequestInfo = {
  url: string,
  method: ApiVerb,
  headers: {
    accept: string,
    contentType: string,
  },
  body: string,
}
