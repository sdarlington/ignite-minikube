# Kubernetes Demo

This repo accompanies GridGain's Kubernetes Workshop, and demonstrates some of the
concepts discussed.

The code sets up a relatively minimal GridGain cluster and connects a SpringBoot
REST client to it. It should run in pretty much any Kuberenetes environment,
including MiniKube and Docker Desktop. See below for some magic incantations that
are required for specific versions of Kubernetes.

## Start the servers

Create the cluster in the "ignite" namespace with the following commands.

```
kubectl apply -f ignite-namespace.yaml
kubectl create -f ignite-service.yaml -n ignite
kubectl create -f ignite-serviceaccount.yaml -n ignite
kubectl create configmap ignite-config --from-file=ignite-config.xml -n ignite
kubectl create configmap ignite-licence --from-file=gridgain-license.xml -n ignite
kubectl apply -n ignite -f ignite-credentials.yaml
kubectl apply -f ignite-statefulset.yaml -n ignite
```

Since the cluster is persistent, you'll need to activate the cluster. The simplest
option is to use Control Center. You can also `exec` into one of the pods and run
`control.sh`.

## Build the client

```
cd client
mvn spring-boot:build-image
```

## Start the client

The client is a SpringBoot/SpringData application that connects to the GridGain
servers as a thin-client. After you've built the image and uploaded it to a registry
(if necessary), you can run it with the following command.


```
kubectl apply -f ignite-client.yaml -n ignite
```

Make the client available outside your Kubernetes environment. How to do that is
going to vary depending on your deployment platform.

Certainly not a production-ready option, but this might work:

```
kubectl port-forward -n ignite deployment/ignite-client 8080:80
```

Example calling the client program, using [httpie](https://httpie.io/cli):

```
$ http POST http://localhost/person/ id=10 name=Jeremy height=180
HTTP/1.1 200
Connection: keep-alive
Content-Type: application/json
Date: Wed, 07 Jun 2023 16:59:01 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "height": 180,
    "id": 10,
    "name": "Jeremy"
}


$ http http://localhost/person/10
HTTP/1.1 200
Connection: keep-alive
Content-Type: application/json
Date: Wed, 07 Jun 2023 16:59:11 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "height": 180,
    "id": 10,
    "name": "Jeremy"
}
```

## Docker Desktop

## MiniKube

To access the cluster from outside of the Kubernetes (cluster), you'll need to set up
a tunnel.

```
minikube tunnel
```

The nice thing about MiniKube is that comes with the Kubernetes Dashboard. You can
launch it with:

```
minikube dashboard
```
