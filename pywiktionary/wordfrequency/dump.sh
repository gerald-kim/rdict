#!/bin/bash

lynx -dump "$1?action=render" | awk '{print $2}' | grep '\[[0-9]'

