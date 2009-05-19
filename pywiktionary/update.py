#!/usr/bin/env python

import os, re, sys, urllib
import md5

index = urllib.urlopen("http://download.wikimedia.org/backup-index.html").read()
m = re.search(r"""<a href="enwiktionary/(\d+)">""", index)
if m is None:
    print "error downloading wikimedia index"
    sys.exit(1)
ver = m.group(1)
print ver

md5file = urllib.urlopen("http://download.wikimedia.org/enwiktionary/%s/enwiktionary-%s-md5sums.txt" % (ver, ver)).read()
m = re.search(r"([0-9a-z]+)\s+enwiktionary-"+ver+"-pages-articles.xml.bz2", md5file)
if m is None:
    print "error checking md5 page"
    sys.exit(1)
md5sum = m.group(1)
print md5sum


fn = "enwiktionary-%s-pages-articles.xml.bz2" % ver
url = "http://download.wikimedia.org/enwiktionary/%s/%s" % (ver, fn)

if os.access(fn, os.F_OK) and md5sum == md5.new(open(fn).read()).hexdigest():
    print "There recent data file. You don't need update."
    sys.exit(0)

os.system( "curl -O %s" % (url) )