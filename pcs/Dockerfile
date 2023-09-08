FROM debian:12.1

RUN apt-get update &&\
    apt-get install -y ca-certificates curl gnupg &&\
    install -m 0755 -d /etc/apt/keyrings &&\
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg &&\
    chmod a+r /etc/apt/keyrings/docker.gpg &&\
    echo \
    "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
    "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
    tee /etc/apt/sources.list.d/docker.list > /dev/null &&\
    apt-get update &&\
    apt-get install -y openssh-server gosu podman containernetworking-plugins docker-ce-cli docker-compose-plugin

RUN useradd -m -u 1000 -s /bin/bash debian &&\
    echo "debian:12345" | chpasswd

RUN mkdir /run/sshd

RUN mkdir -p /app/init &&\
    mkdir -p /run/user/1000 &&\
    chown -R debian:debian /run/user/1000 &&\
    echo debian:10000:5000 > /etc/subuid &&\
    echo debian:10000:5000 > /etc/subgid &&\
    echo "[registries.insecure]" | tee -a /etc/containers/registries.conf &&\
    echo "registries = ['local-registry:5000']" | tee -a /etc/containers/registries.conf &&\
    echo "[engine]" | tee -a /etc/containers/containers.conf &&\
    echo "cgroup_manager = 'cgroupfs'" | tee -a /etc/containers/containers.conf

COPY ./entrypoint.sh /app/entrypoint.sh
COPY ./expose-service.sh /usr/local/bin/expose-service.sh

RUN chmod +x /app/entrypoint.sh

EXPOSE 22

CMD ["/app/entrypoint.sh"]