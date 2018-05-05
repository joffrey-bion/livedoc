| Name                | Default      | Description |
| ------------------- | ------------ | ----------- |
| `name`              | `null`       | a descriptive name for your API. Used in the default home page.
| `version`           | `null`       | the current version of your API, if relevant
| `packages`          | *required*   | a list of packages that Livedoc should scan for controllers. Also acts as a whitelist for documented types: types outside these packages won't be documented.
| `baseUrl` | current server+context | the base URL that client should target to reach your API
| `playgroundEnabled` | `true`       | allows to send requests directly from Livedoc UI, in order to try the API
| `displayMethodAs`   | `URI`        | displays the operations by their endpoint path (`URI`), their corresponding handler method name (`METHOD`) or their short description (`SUMMARY`)
