# Transactional Key Value Store Android App

An app that implements the following spec using an in-memory data store with the limitation of not using any third-party libraries.

Download the APK [here](https://github.com/MichaelM97/Transactional-Key-Value-Store/releases/tag/1.0.0).

```
SET <key> <value> // store the value for key
GET <key>         // return the current value for key
DELETE <key>      // remove the entry for key
COUNT <value>     // return the number of keys that have the given value
BEGIN             // start a new transaction
COMMIT            // complete the current transaction
ROLLBACK          // revert to state prior to BEGIN call
```

## Setup

### Ensure that you build the app using Java 11

`Android Studio -> Settings/Preferences -> Build, Execution, Deployment -> Build Tools -> Gradle -> Set "Gradle JDK" to '11'`

## Module structure

* `:app` - Code that launches the app & integration tests
* `:data` - The in-memory data source
* `:repository` - Repositories used for interacting with data sources
* `:feature:cli:ui` - View layer for the CLI feature (composables, viewmodels, etc.)
* `:feature:cli:domain` - Domain layer for the CLI feature (usecases)
* `:core:ui` - View layer utilities
* `:core:di` - Dependency injection utilities
* `:core:test` - Unit test utilities, also exposes all required unit test dependencies 
