FROM docker.io/jenkins/jenkins:2.401.3-lts-jdk17

USER root

RUN apt-get update &&\
    apt-get install -y podman containernetworking-plugins &&\
    echo jenkins:10000:5000 > /etc/subuid &&\
    echo jenkins:10000:5000 > /etc/subgid &&\
    echo "[registries.insecure]" | tee -a /etc/containers/registries.conf &&\
    echo "registries = ['local-registry:5000']" | tee -a /etc/containers/registries.conf &&\
    echo "[engine]" | tee -a /etc/containers/containers.conf &&\
    echo "cgroup_manager = 'cgroupfs'" | tee -a /etc/containers/containers.conf

USER jenkins