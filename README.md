# zone-health

[![Build Status](https://travis-ci.org/hmrc/zone-health.svg?branch=master)](https://travis-ci.org/hmrc/zone-health) [ ![Download](https://api.bintray.com/packages/hmrc/releases/zone-health/images/download.svg) ](https://bintray.com/hmrc/releases/zone-health/_latestVersion)

## Functionality
This microservice has a health check endpoint that will perform the check(s) defined below.
1. Insert into and read from a local mongo instance to ensure that connectivity works
1. Optionally call the health check endpoint on another instance of the service. This can be useful to test the connectivity from one zone to another.

## Testing
The unit tests can be run without any other dependencies. The integration tests require a running mono instance.
For convenience, when running locally it's possible to uncomment the code in HealthRepositorySpec in order start a docker container running mongo. It's commented out as there is no support for starting docker containers within Jenkins. Alternatively run `docker-compose up` within the it/resources folder to start a container manually.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
