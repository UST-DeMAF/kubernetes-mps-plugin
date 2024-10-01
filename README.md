# kubernetes-mps-plugin
The kubernetes-mps-plugin is one of many plugins of the [DeMAF](https://github.com/UST-DeMAF) project.
It is designed to transform [Kubernetes deployment models](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/) into an [EDMM](https://github.com/UST-EDMM) representation.

The plugin only works (without adaptions) in the context of the entire DeMAF application using the [deployment-config](https://github.com/UST-DeMAF/deployment-config).
The documentation for setting up the entire DeMAF application locally is [here](https://github.com/UST-DeMAF/EnPro-Documentation).


## Usage

You can run the application without the [deployment-config](https://github.com/UST-DeMAF/deployment-config) but it will not run as it needs to register itself at the [analysis-manager](https://github.com/UST-DeMAF/analysis-manager).

If you want to boot it locally nevertheless use the following commands.

```shell
./mvnw spring-boot:run
```
or:
```shell
./mvnw package
java -jar target/kubernetes-plugin-0.2.0-SNAPSHOT.jar
```

When running the project locally, ensure the plugin isn't also running in a Docker container to avoid port conflicts.

## Init and Update Submodule
This plugin uses [JetBrains MPS](https://www.jetbrains.com/mps/) to facilitate the model-to-model transformation from Kubernetes to EDMM.
The [matching MPS project](https://github.com/UST-DeMAF/mps-transformation-kubernetes) is located in another git repository and must be added as a submodule (you can also clone via https):

```shell
git submodule add git@github.com:UST-DeMAF/mps-transformation-kubernetes.git mps-transformation-kubernetes
```

To update the MPS application to a new version, execute:
```shell
git submodule update --remote
```

## Kubernetes-Specific Configurations
Differences to other plugins:

1 .env-file: It's possible to read an additional .env file, but this has to be in the same folder as the .yaml file.


## Debugging

If changes are made, the docker container has to be restarted in the [deployment-config](https://github.com/UST-DeMAF/deployment-config) shell, to update the plugin.

```shell
docker-compose pull && docker-compose up --build -d
```