# Module 21 - Docker & Kubernetes

Packaging the order service as a container and running it on Kubernetes with health probes, resource limits, and a non-root user.

## Files
- `Dockerfile`: multi-stage build, non-root runtime user.
- `k8s/deployment.yaml`: 2 replicas, liveness and readiness probes, resource limits.
- `k8s/service.yaml`: cluster-internal service.
- `src/main/java/.../HealthStatus.java`: the logic the probes depend on, with a unit test.

## Interview questions
1. **Image vs container?** An image is a read-only template. A container is a running instance.
2. **Why multi-stage builds?** The runtime image carries the JRE and jar, not Maven or the source.
3. **Liveness vs readiness probe?** Liveness restarts a stuck container. Readiness removes it from load balancing until it can serve traffic.
4. **Why set CPU and memory limits?** Without them, one pod can starve the node. The JVM also needs a memory limit to size its heap.
5. **Deployment vs StatefulSet?** Deployments suit stateless services. StatefulSets give stable identity and storage.

## Run
```bash
mvn test
docker build -t order-service:1.0 .
kubectl apply -f k8s/
```
