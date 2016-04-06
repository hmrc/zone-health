while :
    do
        curl http://localhost:9000/zone-health
        echo "$?"
        sleep 3
done
