# zone-health

[![Brought to you by Telemetry Team](https://img.shields.io/badge/MDTP-Telemetry-40D9C0?style=flat&labelColor=000000&logo=gov.uk)](https://confluence.tools.tax.service.gov.uk/display/TEL/Telemetry)

## Functionality
This microservice has a health check endpoint that will perform the check(s) defined below.
1. Insert into and read from a local mongo instance to ensure that connectivity works
1. Optionally call the health check endpoint on another instance of the service. This can be useful to test the connectivity from one zone to another.

## Testing

```shell
$ cd zone-health/it/test/resources
docker-compose up -d
[+] Running 2/2
 ✔ Network resources_default    Created
 ✔ Container resources-mongo-1  Started

$ cd ../../..
$ sbt clean test 
... output omitted ...
[info] Run completed in 2 seconds, 418 milliseconds.
[info] Total number of tests run: 10
[info] Suites: completed 3, aborted 0
[info] Tests: succeeded 10, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 7 s, completed 21 May 2025, 11:31:08
```

The unit tests can be run without any other dependencies. The integration tests require a running mono instance.
For convenience, when running locally it's possible to uncomment the code in HealthRepositorySpec in order to start a
Docker container running mongo. It's commented out as there is no support for starting docker containers within Jenkins.
Alternatively run `docker-compose up` within the `it/test/resources` folder to start a container manually.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
