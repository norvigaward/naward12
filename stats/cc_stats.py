#!/usr/bin/env python
import os
import csv

sub_dir ="/home/sathya/Copy/output-final"
d = {'sub':'count'}
cat_dict = {'sub' : 'main'}
for root, dirs, files in os.walk(sub_dir):
    for filename in files:
        if "part" in filename:
            file_dir = os.path.join(root,filename)
            print file_dir
            with open(file_dir, 'rb') as tsvfile:
                tsvreader = csv.reader(tsvfile, delimiter ='\t')
                for row in tsvreader:
                    cat = row[1].split(':', 1)[0]
                    main_cat = cat.split('-',1)[0]
                    sub_cat = cat.split('-',1)[1]
                    if not(sub_cat in cat_dict):
                        cat_dict[sub_cat] = main_cat
                    if not(sub_cat in d):
                        d[sub_cat] = 0
                    d[sub_cat] = d[sub_cat] + 1;

with open('count.tsv', 'wb') as countfile:
    countwriter = csv.writer(countfile, delimiter = '\t')
    for key, value in d.iteritems():
        row = [key,cat_dict[key],d[key]]
        countwriter.writerow(row)