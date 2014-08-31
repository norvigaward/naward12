#!/usr/bin/env python
import os
import csv
sub_dir = "/home/sathya/github/scraper/Top"
count = 100000
with open("largev2.tsv", 'wb') as tsv_file:
    csv_writer = csv.writer(tsv_file,delimiter='\t')
    for root, dirs, files in os.walk(sub_dir):
        for big_filename in files:
            big_filedir = os.path.join(root,big_filename)
            with open(big_filedir, 'rb') as big_file:
                count = count+1
                label = os.path.relpath(root,sub_dir)
                big_list = []
                for line in big_file:
                    term = line.strip()
                    if len(term) > 1 and "@attribute" not in term and "@data" not in term and '\t' not in term and ';' not in term:
                        big_list.append(term)
            big_string = ' '.join(big_list)
            csv_writer.writerow([label, count, big_string])