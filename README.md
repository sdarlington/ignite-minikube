# Kubernetes Demo

This repo accompanies GridGain's Kubernetes Workshop, and demonstrates some of the
concepts discussed.

The code sets up a relatively minimal GridGain cluster and connects a SpringBoot
REST client to it. The concepts should apply to pretty much any Kuberenetes environment,
though some minor changes may be required. The code steps below have been tested on
[kind](https://kind.sigs.k8s.io).

## Start your Kubernetes cluster

Create your cluster with the following command:

```
kind create cluster
```

## Start the servers

Create the cluster in the "ignite" namespace with the following commands.

```
kubectl apply -k .
```

(This uses [Kustomize](https://kustomize.io) to make the process easier, without
using heavy-weight tools like Helm or an Operator.)

Since the cluster is persistent, you'll need to activate the cluster. The simplest
option is to use Control Center. You can also `exec` into one of the pods and run
`control.sh`.

```
% kubectl exec -n ignite pod/ignite-0 --stdin --tty -c ignite-node -- /bin/bash
ignite-0:~$ bin/control.sh --set-state active
JVM_OPTS environment variable is set, but will not be used. To pass JVM options use CONTROL_JVM_OPTS
JVM_OPTS=-server -Djava.net.preferIPv4Stack=true -XX:+AlwaysPreTouch -XX:+UseG1GC -XX:+ScavengeBeforeFullGC -XX:+DisableExplicitGC -Djdk.tls.client.protocols=TLSv1.2
Failed to configure logging to file
user: prodroot
password:
Warning: the command will change state of cluster with name "null" to ACTIVE.
Press 'y' to continue . . . y
Control utility [ver. 8.9.33#20260429-sha1:1ba8bde0]
2026 Copyright(C) GridGain Systems, Inc. and Contributors
User: gridgain
Time: 2026-06-10T10:03:45.110
This cluster requires authentication.
Command [SET-STATE] started
Arguments: --set-state active
--------------------------------------------------------------------------------
Cluster state changed to ACTIVE
Command [SET-STATE] finished with code: 0
Control utility has completed execution at: 2026-06-10T10:04:24.991
Execution time: 39881 ms
```

## Build the client

```
cd client
mvn spring-boot:build-image
```

## Start the client

The client is a SpringBoot/SpringData application that connects to the GridGain
servers as a thin-client. After you've built the image and uploaded it to a registry:

```
kind load docker-image ignite-client:0.0.1-SNAPSHOT
```

You can run it with the following command.


```
kubectl apply -f ignite-client.yaml -n ignite
```

Make the client available outside your Kubernetes environment. How to do that is
going to vary depending on your deployment platform.

Certainly not a production-ready option, but this might work:

```
kubectl port-forward -n ignite service/ignite-client 8080:80
```

Example calling the client program, using [httpie](https://httpie.io/cli):

```
$ http POST http://localhost:8080/person/ id=10 name=Jeremy height=180
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


$ http http://localhost:8080/person/10
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

## Optional

### LinkerD

To run the cluster with mTLS between the various components, install linkerd and uncomment the lines in kustomization.yaml before you deploy the cluster.
