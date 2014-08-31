#!/usr/bin/env python
import os

_dir = '/home/sathya/github/scraper/Top'
big_dir = '/home/sathya/github/scraper/Big'
_list = []

with open('dirs.txt', 'rb') as txtfile:
    with open("dmoz_stats.txt", 'wb') as stat_file:
        for line in txtfile:
            big_file = []
            for root, _, files in os.walk(line.strip()):
                for filename in files:
                    relative_path = os.path.relpath(root,_dir)
                    big_path = os.path.join(big_dir,relative_path)
                    file_dir = os.path.join(root, filename)
                    with open(file_dir, 'rb') as small_file:
                        for line in small_file:
                            big_file.append(line)
                stat = [relative_path.encode('utf8'), ' - '.encode('utf8'), str(len(big_file)).encode('utf8'), '\n'.encode('utf8')]
                print ''.join(stat)
                stat_file.write(''.join(stat))