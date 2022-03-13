# Clowre OpenAPI Document Server

A simple program that merges multiple OpenAPI/Swagger schema files, and serves the merged result. This project was born
to solve a very specific use-case where we at Clowre needed to see the API docs of all our microservices at one place.

However, this project also aims to be a generic solution for OpenAPI Schema merging.

## Running

By default, the server tries to resolve configuration from a Spring Cloud Config Server, so every external configuration
must specify this property explicitly:

```yaml
micronaut:
  config-client:
    enabled: false
```

The recommended way to run this server is through Docker. This server serves merged OpenAPI Schemas at
`http://host:port/docs.json` and `http://host:port/docs.yaml`.

### With external configuration

```shell
docker run --rm -it --name=docserver -p 8080:8080 \
  -v "$(pwd)/_examples/external-configuration.yaml:/config.yaml" \
  -e MICRONAUT_CONFIG_FILES=/config.yaml \
  clowre/docserver:latest
```

### Using Spring Cloud Config Server

If you wish to configure the docserver using a Cloud Config instance, it is required that `CONFIG_SERVER_URI` points to
your configuration server. The docserver's application ID is `clowre-docserver`, so make sure that the configuration can
be resolved by this name.

```shell
docker run --rm -it --name=docserver -p 8080:8080 \
  -e CONFIG_SERVER_URI=http://your-config-server:8888 \
  -e MICRONAUT_ENVIRONMENTS=cloud \
  clowre/docserver:latest
```

## Configuration

### Swagger Sources

This is the only configuration that is required to be present to run a bare-minimum version of this server. This
configuration is specified by the `swagger:` key in the configuration YAML. Here's a sample configuration that merges a
couple of schemas.

```yaml
swagger:
  ## Will be used to generate the merged OpenAPI file's 'info' section.
  info:
    ## Title of the merged OpenAPI schema. Required.
    title: A basic combined API YAML
    description: |-
      ## What it is
      An example of clowre/docserver's schema merge functionality.

      ## What it merges
      The swagger Pet Store and BoxKnight API schemas.

  ## List of OpenAPI schemas to be merged.
  sources:
    ## A friendly name of this resource to be used in logging - required.
    - name: Pet Store

      ## String to be used as a prefix of API paths - required
      path-prefix: pet-store

      ## Location of OpenAPI schema JSON/YAML - required
      address: https://redocly.github.io/redoc/openapi.yaml

      ## Version of schema - V2 or V3 - required
      version: V3
```

More configuration examples can be found in `_examples` directory of this repository.

### Views

The docserver can also serve off Swagger and ReDoc views out of the box. This functionality can be enabled by putting
the following in your configuration:

```yaml
views:
  enable-redoc: true
  enable-swagger: true
```

Once enabled, the views can be accessed at `http://host:port/views/redoc` and `http://host:port/views/swagger`.

---

## Project usability status

This project is in **alpha** state right now, and is not meant to be used in production. However, this will keep on
evolving and will see a production release as we at Clowre enhance it.

## Contributions welcome

We're happy to take contributions in form of issues, feature requests, documentation, and code!