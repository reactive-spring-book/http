#!/bin/bash 
port=${1:-8080}

# <1>
curl -H"content-type: application/json" -d'{"email":"random"}' http://localhost:${port}/profiles
