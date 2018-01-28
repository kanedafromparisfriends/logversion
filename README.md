# logversion

This is a sample application used for my kubernetes introduction courses

## Dev, Builds and Run

### run locally
in order to make it run locally 

````
mvn package -Dexec.executable=java -Dexec.args="-classpath %classpath io.github.kandefromparis.versiondisplay.Server" org.codehaus.mojo:exec-maven-plugin:exec
````

### run in docker

````
mvn package docker:build docker:start docker:watch
````


## docker images released


https://hub.docker.com/r/kanedafromparis/logversion/

## 

## Curl & jq Tips


### to get cheers
```
curl http://127.0.0.1:8080/api/1.0/version/info
```

### run in kubernetes

```
kubectl run logversion --image=kanedafromparis/logversion:0.1
kubectl scale deploy/logversion --replicas=5
```

in an other terminal
kubetail -l run=logversion

```
kubectl edit deploy/kanedafromparis/logversion
```

### remember
docker rm $(docker ps -aq) 