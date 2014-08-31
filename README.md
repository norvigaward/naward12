#Final Report for Norvig Award 2014 Competition



![alt tag](/pictures/report/report-coverpic.png)
<br><l><I> Source: http://www.iharvest.co.uk/wp-content/uploads/2014/03/iHarvest_Screen_Scrape_Image_Of_Text.png </I>



## 

- <b>Team:</b> Naward12 (on Github)
- <b>Subject:</b> Categorization of common web crawl
- <b>Course:</b> Datascience (TU Delft) IN4144
- *Members:* 

<table>
      <tr>
         <th>Teammember</th>
         <th>Name</th>
         <th>Student number</th>
         <th>Study</th>
      </tr>
 
       <tr>
        <td>1</td>
        <td>P.S.N.Chandrasekaran Ayyanathan</td>
        <td>4314506</td>
        <td>Master CS TU Delft</td>
       </tr>
       <td>2</td>
       <td>Spyros Foniadakis</td>
       <td>4318773</td>
       <td>Master CS TU Delft</td>
      </tr>
      <tr>
       <td>3</td>
       <td>Rafik Chelah</td>
       <td>4113136</td>
      <td>Master SEPAM TU Delft</td>
              
</table>



<i> As part of the Norvig Award competition we participate in this competition, as three master students from Delft University of Technology, in order to get more experience into data science concepts by making use of Common Crawl data that is publicly available in the process also learning to use hadoop and also make novel use of big data. </b>
</i>

## 1. Introduction
Background for the idea takes its origin from search engines that provide lists of URL's which are the result of web related queries. The user would not have prior knowledge whether the URL truly pertains to what they wish to search and would only be certain of this by manually opening the URL and reading its contents.

With the solution which is proposed, the end-users will have a search engine which analyzes the content of the URL beforehand through categorization and decide whether or not a website is interesting for the end-user.

Secondly it also allows to gain an insight into the World Wide Web as a whole and narrow down user interests since the major part of the web comprises of content by individual users and not only major corporations. This provides a wide variety of opportunities that would include personalized recommendations, consumer targeted advertising, etc.
 
The idea is to derive all relevant textual data from the common web crawl in order to classify it into different categories and exhibit websites's relativity towards different categories. That insight is strengthened by providing visual representations in form of <i>Scalable Vector Graphics</i>, so as to help the end-user understand the overall picture.

<br>
## 2. Methodology
Below we describe in detail of the methodology involved in categorizing the pages of the Common Crawl dataset through the use of a classifier.

###2.1 Data Collection
In order to train our classifier to categorize a web page, we must first collect pages that have already have been classified and have accurate labels. These are available through <a href=http://www.dmoz.org/>Open Directory Project</a> which comprises of human assigned labels to web pages. We utilized 517 categories in total that can be found in the <a href="https://github.com/norvigaward/naward12/blob/master/stats/labels.txt">repository</a>.

- The ODP comprises of multiple levels of categories, to maintain simplicity we only traverse through two levels of the categories and pull the URLs. This is stored in a <a href="https://copy.com/L10OrSzm6DjH">json</a> file which is actually a parse of the <a href="http://www.dmoz.org/rdf.html">rdf</a> file provided by ODP. 
- We then utilize a custom <a href="http://doc.scrapy.org/en/latest/index.html">Scrapy</a> module that can be found in our <a href="https://github.com/norvigaward/naward12/tree/master/scraper">repository</a> to crawl the content of the URLs listed on the second level of ODP. Only this time, we ignore all script, style and most common english words to attain only the most informative words for each web page. 
- The most informative words stored in a large TSV file found that can be found <a href="https://copy.com/L10OrSzm6DjH">here</a>. The TSV file is then parsed into a hadoop sequence file using a script found <a href="https://github.com/norvigaward/naward12/tree/master/mahout">here</a>.

###2.2 Training
Once we attain the training data, a naive bayes classifier is trained through the use of <a href="https://mahout.apache.org/">Apache Mahout</a>. First, the new sequence file must be uploaded and the following commands must be executed in sequence:

<pre>
$ mahout seq2sparse -i odp-seq -o odp-vectors
$ mahout trainnb -i odp-vectors/tfidf-vectors -el -li labelindex -o model -ow -c
</pre>

The first line of code converts the uploaded hadoop sequence file into vectors that would be usable by the classifier. The tfidif-vectors folder is then supplied as input to the naive bayes classifier to finally give the resultant fitted model and a list of all the labels used.

###2.3 Classifying CC
Once the classifier is trained, the next course of action is to begin classification of the common crawl data. Before classification, we parse the sequence files by removing the script, style and inline tags finally utilizing the clean body of the web page for classification. The java project in the <a href="https://github.com/norvigaward/naward12/tree/master/odp"repository</a> must be installed with maven. Finally the resulting jar file must be built by supplying the model, dictionary-frequency, labels, input sequence and output paths in the following manner:

<pre>
yarn jar odp-1.0-fatjar.jar odp model odp-vectors/dictionary.file-0 odp-vectors/df-count/part-r-00000 labelindex /data/public/common-crawl/crawl-data/CC-MAIN-2014-10/segments/1393999635677/seq/CC-MAIN-20140305060715-000$VARIABLE-ip-10-183-142-35.ec2.internal.warc.seq output$VARIABLE
</pre>
where <pre>$VARIABLE</pre> is substituted with the required values.

Each classification takes around 2hrs to complete. Due to time constraints, we were only able to classify 60 sequence files present in the main common crawl data.

## 3. Core Functionalities as of a UI perspective
The application is available to the end user via a web interface and it is reachable via the following url: <a href=http://www.naward12.com> http://www.naward12.com </a>.
<br>
A menu page on the left reveals the options given; (1) access to the domain name search engine, (2) see an overview of all availabel domain names via the search engine and finally (3) a page providing a link to this very document. The options are further presented below.

###3.1. Domain Name Search Engine
The user can fill in the form a domain name or a part of it and attempt to search for the categories it falls in. An accuratelly typed domain name or a an accuratelly typed part of a domain name will return results to user; the categories that content of this domain name is mosty relevant to, along with a score to indicate the relevacy. The higher the score the higher the higher the convergence. In case of typing a part of the domain name, an indication will explicitelly mention the domain for which the results are displayed. In both cases, sugestions will be displayed if they are applicable, eg: similar or identical, deferentiated only in the suffix, domain names.


<br>
## 4. Visualization
The classified domains and their corresponding categories are counted for each unique category. This count is then employed in a d3js script to visualize in the form of a simple donut chart which displays the number of domains found in common crawl that correspond to each category. By moving the slider, you may view the different distribution of domains in a number of sub-categories.


# <a href=http://www.naward12.com> Visit the website yourself </a>
