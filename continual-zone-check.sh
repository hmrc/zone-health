while :
    do
        curl http://localhost:9000/zone-health
        echo "$? $(date +"%r")"
        sleep 0.1
done
