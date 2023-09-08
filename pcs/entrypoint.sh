#!/usr/bin/env bash

term_handler() {
  podman stop -a;

  exit 143; # 128 + 15 -- SIGTERM
}

trap 'kill ${!}; term_handler' SIGTERM;

set -e;

gosu debian podman pull docker.io/alpine/socat || true;
gosu debian podman network create service_tunnels || true;

chmod +t,o+w /var/run
gosu debian podman system migrate
gosu debian podman system service --time=0 unix:///var/run/docker.sock &
sleep 5
chmod -t,o-w /var/run
/usr/sbin/sshd -D &

for f in /app/init/*.sh; do
    set +e
    gosu debian bash "$f"
    set -e
done

while true; do
    sleep 5 &
    wait $!;
done
