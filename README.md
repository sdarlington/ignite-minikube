# Minikube Demo

```
minikube start

kubectl apply -f ignite-namespace.yaml
kubectl create -f ignite-service.yaml -n ignite
kubectl create -f ignite-serviceaccount.yaml -n ignite
kubectl create configmap ignite-config --from-file=ignite-config.xml -n ignite
kubectl create configmap ignite-licence --from-file=gridgain-license.xml -n ignite
kubectl create secret generic ignite-credentials --from-file=credentials.properties -n ignite
kubectl apply -f ignite-statefulset.yaml -n ignite

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