for VARIABLE in 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60
do
echo "yarn jar warcexamples-1.1-fatjar.jar odp model odp-vectors/dictionary.file-0 odp-vectors/df-count/part-r-00000 labelindex /data/public/common-crawl/crawl-data/CC-MAIN-2014-10/segments/1393999635677/seq/CC-MAIN-20140305060715-000$VARIABLE-ip-10-183-142-35.ec2.internal.warc.seq output$VARIABLE"

yarn jar warcexamples-1.1-fatjar.jar odp model odp-vectors/dictionary.file-0 odp-vectors/df-count/part-r-00000 labelindex /data/public/common-crawl/crawl-data/CC-MAIN-2014-10/segments/1393999635677/seq/CC-MAIN-20140305060715-000$VARIABLE-ip-10-183-142-35.ec2.internal.warc.seq output$VARIABLE
wait
done
