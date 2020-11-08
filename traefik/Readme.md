# Traefik installation

Following this project: [Labs-Tooling](https://github.com/Zenika/labs-tooling/tree/master/ci-cd-platform-deployment/kubernetes-gke)
I've installted Traefik on my kubernetes cluster hosted by GKE.

## Steps to reproduce the installation

### Creating a traefik service account
Create a service account and only add the role `DNS Administator`
```shell script
# Create the service account
gcloud iam service-accounts create traefik \
    --description="traefik service account" \
    --display-name="Traefik"

# Add the role DNS Administator
gcloud projects add-iam-policy-binding truaro-test-gcp \
    --member="serviceAccount:traefik@truaro-test-gcp.iam.gserviceaccount.com" \
    --role="roles/dns.admin"

# Create a key 
gcloud iam service-accounts keys create traefik-service-account.key \
  --iam-account traefik@truaro-test-gcp.iam.gserviceaccount.com
```

### Configuring the cluster
```shell script
## not mandatory, but you can create a cluster
gcloud container clusters create cloudcmr-cluster \
  --zone=europe-west2-b \
  --machine-type=e2-small \
  --num-nodes=3 \
  --enable-autoscaling \
  --max-nodes=6 \
  --min-nodes=3 \
  --service-account=cloudcmr-gke@truaro-test-gcp.iam.gserviceaccount.com
# After this command, every kubectl command will be executed in the specified cluster

# create the traefik namespace
kubectl create namespace traefik

# create the Custom Resource Definition to be used in the cluster later by some k8s file
kubectl apply -f crd.yaml

# Create the Role-Based Access Control
kubectl apply -f rbac.yaml

# Create a secret to connect to traefik dashboard
htpasswd -bc traefik-auth-file.key traefik [PASSWORD]

# import the secret into your cluster under the traefik namespace
kubectl create secret generic traefik-auth --from-file traefik-auth-file.key -n traefik

# Create a secret that holds the service account key file to interact with Cloud DNS
kubectl create secret generic traefik-service-account --from-file=traefik-service-account.json=traefik-service-account.key -n traefik

# Apply the traefik configuration file
kubectl apply -f traefik.yaml
```