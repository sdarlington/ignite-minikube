# Minikube Demo

```
minikube start

kubectl apply -f ignite-namespace.yaml
kubectl create -f ignite-service.yaml -n ignite
kubectl create -f ignite-serviceaccount.yaml -n ignite
kubectl create configmap ignite-config --from-file=ignite-config.xml -n ignite
kubectl create configmap ignite-licence --from-file=gridgain-license.xml -n ignite
kubectl apply -n ignite -f ignite-credentials.yaml
kubectl apply -f ignite-statefulset.yaml -n ignite
kubectl apply -f ignite-client.yaml -n ignite

minikube tunnel

$ kubectl get svc -n ignite
NAME     TYPE           CLUSTER-IP     EXTERNAL-IP    PORT(S)                                                                          AGE
ignite   LoadBalancer   10.103.88.60   10.103.88.60   11211:31837/TCP,10800:32106/TCP,47100:31072/TCP,47500:32002/TCP,8080:32721/TCP   82s
$ sqlline.sh -u jdbc:ignite:thin://10.103.88.60
sqlline version 1.3.0
0: jdbc:ignite:thin://10.103.88.60>

$ kubectl scale statefulset ignite --replicas=3 -n ignite

minikube dashboard

minikube stop

minikube delete
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