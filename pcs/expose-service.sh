#!/usr/bin/env bash
HOSTNAME="$1"
shift
PORTS=("$@")
SERVICEIP=$(getent hosts $HOSTNAME | awk '{ print $1 }' | head -n 1)

if [[ $SERVICEIP =~ .*:.* ]]
then
  SERVICEIP="[$SERVICEIP]"
fi

podman create --rm --network service_tunnels --name $HOSTNAME-tunnel --entrypoint "sh" docker.io/alpine/socat -c "trap 'exit' SIGTERM; while true; do sleep 2 & wait \$!; done" || true
podman start $HOSTNAME-tunnel

for p in "${PORTS[@]}"; do
    podman exec -d $HOSTNAME-tunnel socat "tcp-listen:$p,fork,reuseaddr" "tcp-connect:$SERVICEIP:$p"
done