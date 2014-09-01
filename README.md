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


## 2. Methodology
Below we describe in detail of the methodology involved in categorizing the pages of the Common Crawl dataset through the use of a classifier.

###2.1 Data Collection
In order to train our classifier to categorize a web page, we must first collect pages that have already have been classified and have accurate labels. These are available through <a href=http://www.dmoz.org/>Open Directory Project</a> which comprises of human assigned labels to web pages. We utilized 517 categories in total that can be found in the <a href="https://github.com/norvigaward/naward12/blob/master/stats/labels.txt">repository</a>.

- The ODP comprises of multiple levels of categories, to maintain simplicity we only traverse through two levels of the categories and pull the URLs. This is stored in a <a href="https://copy.com/L10OrSzm6DjH">json</a> file which is actually a parse of the <a href="http://www.dmoz.org/rdf.html">rdf</a> file provided by ODP. 
- We then utilize a custom <a href="http://doc.scrapy.org/en/latest/index.html">Scrapy</a> module that can be found in our <a href="https://github.com/norvigaward/naward12/tree/master/scraper">repository</a> to crawl the content of the URLs listed on the second level of ODP. Only this time, we ignore all script, style and most common english words to attain only the most informative words for each web page. 
- The most informative words stored in a large TSV file found that can be found <a href="https://copy.com/L10OrSzm6DjH">here</a>. The TSV file is then parsed into a hadoop sequence file using a script found <a href="https://github.com/norvigaward/naward12/tree/master/mahout">here</a>.

There are in total 599,981 pages that were crawled for the purpose of training.

###2.2 Training
Once we attain the training data, a naive bayes classifier is trained through the use of <a href="https://mahout.apache.org/">Apache Mahout</a>. First, the new sequence file must be uploaded and the following commands must be executed in sequence:

<pre>
$ mahout seq2sparse -i odp-seq -o odp-vectors
$ mahout trainnb -i odp-vectors/tfidf-vectors -el -li labelindex -o model -ow -c
</pre>

The first line of code converts the uploaded hadoop sequence file into vectors that would be usable by the classifier. The tfidif-vectors folder is then supplied as input to the naive bayes classifier to finally give the resultant fitted model and a list of all the labels used.

###2.3 Classifying CC
Once the classifier is trained, the next course of action is to begin classification of the common crawl data. Before classification, we parse the sequence files by removing the script, style and inline tags finally utilizing the clean body of the web page for classification. We take into consideration the top 3 most relevant categories based upon the values of their log probability so as to compensate for user perception of websites. The java project in the <a href="https://github.com/norvigaward/naward12/tree/master/odp">repository</a> must be installed with maven. Finally the resulting jar file must be built by supplying the model, dictionary-frequency, labels, input sequence and output paths in the following manner:

<pre>
yarn jar odp-1.0-fatjar.jar odp model odp-vectors/dictionary.file-0 odp-vectors/df-count/part-r-00000 labelindex /data/public/common-crawl/crawl-data/CC-MAIN-2014-10/segments/1393999635677/seq/CC-MAIN-20140305060715-000$VARIABLE-ip-10-183-142-35.ec2.internal.warc.seq output$VARIABLE
</pre>
where $VARIABLE is substituted with the required values.

Each classification takes around 2hrs to complete. Due to time constraints, we were only able to classify 60 sequence files present in the main common crawl data. The results of the classification can be found <a href="https://copy.com/BUacI4uhhmVm">here</a>. Since the results are in the form of log probability, the values would be negative. There are in total 15,278,873 pages that have been classified.

## 3. Search Engine
A search engine is implemented in an attempt to materialize the idea of the domain name categorization mentioned earlier. The main functionality provided is the ability to query on a domain name and receive a set of categories that the domains content is relevant to, along with a score to indicate teh degree of relevancy.

As of a technical perspective, the search engine was implemented by a web application ruled by the architecture presented below.

### 3.1. User Interface - Presentation Layer
The implementation of the user interface is relied on Microsoft Technologies; Microsoft Web Matrix was used as the IDE for designing and structuring teh web applicationa and Microsoft .NET Framework 4 was used so as to include ASP.NET 4.0 and the RAZOR syntax.

### 3.2. Business Layer
As said, the Razor syntax was used so as to include the busines layer within the web pages. C# 4.0 was used throughout the whole business layer. In particular, regarding the search process, 
<br><br>
The user can fill in the form a domain name and attempt to search for the categories it falls in. An accuratelly typed domain name will return results to user; the categories that the content of this domain name is mosty relevant to, along with a score to indicate the relevacy (see Figure 1 below). The higher the score the higher the convergence. 
<br><br>
An innacuratelly typed domain name provokes a relative message to the user (see Figure 2), along with suggestions to the user, based on the Levenshtein Distance[1]. The user can either select one of the recomendation or perform a new search (see Figure 3).

![alt tag](/pictures/report/search.JPG)
<b>Figure 1. Appearance of descriptive statitiscs concerning calssification of the domain

![alt tag](/pictures/report/didyoumeannew.JPG)
<b>Figure 2. Case of wrong entrance of domainname, appearance of corrective name

![alt tag](/pictures/report/didyoumeannew2.JPG)
<b>Figure 3. Case of wrong entrance of domainname, user selects one of teh recomendations

The source files that regard the web application can be found <a href="https://github.com/norvigaward/naward12/tree/master/src/web%20application%20-%20search%20engine">here</a>.

### 3.3. Data Layer
The data layer consists of the files that contain the information returned by the search engine's responses. All crawled data was initially stored in files and indexed by URL so as to contain the top three categories closer in terms of content relevancy, along with the score each URL exhibited to each category. These files were transformed, so as to group the URLs deriving from the same source website and assign the average of the score per category to the root website for this specific category. Subsequenty, the websites were indexed on their domain names and their information were stored in files named after the first two letters of their domain names, so as to increase efficiency and decrease response time. Java was used for transforming files and providing the final data and file structure, so as to be used by the business layer.
<br><br>
The described process was completed in two steps: (1) extracting the domain names, re-grouping the URLs into alphabetical order and spreading them each to the appropriate file and (2) grouping the URLs to their corresponding source website and merging the categories and their scores to point to the webstes as well.
<br><br>
The source code for the Java applcation performing the data transformation tasks can be found <a href="https://github.com/norvigaward/naward12/tree/master/src/data%20transformation%20app/src">here</a>.

<br>
## 4. Visualization
The classified domains and their corresponding categories are counted for each unique category. This count is then employed in a d3js script to visualize in the form of a simple donut chart which displays the number of domains found in common crawl that correspond to each category. By moving the slider, you may view the different distribution of domains in a number of sub-categories (see Fihure 4).

![alt tag](/pictures/report/overview.JPG)
<b>Figure 4. Case of wrong entrance of domainname, user selects one of teh recomendations

## 5. Further Improvements
- The training data can be increased further so as to accomodate all of ODP (4,182,639) at the time of writing. 
- A more accurate classifier such as support vector can be implemented rather than the simple naive bayes classifier.
- Improvements to the search engine by accomodating more verbose results and a general improvement of the UI through CSS and jquery to accomodate nicer animation.

## 6. References
[1] Black, Paul E., ed. (14 August 2008), "Levenshtein distance", Dictionary of Algorithms and Data Structures [online], U.S. National Institute of Standards and Technology, retrieved 3 April 2013

## 7. Gist
- 517 <a href="https://github.com/norvigaward/naward12/blob/master/stats/labels.txt">Categories</a>
- 599,981 categorized <a href="https://copy.com/L10OrSzm6DjH">URls</a> from <a href="http://www.dmoz.org/">ODP</a>
- Use of <a href="https://mahout.apache.org/">Mahout</a> naive bayes classifier for training.
- 15,278,873 <a href="https://github.com/norvigaward/naward12/blob/master/stats/cc_stats.tsv>CC pages</a> categorized.
- Categorized CC labels used to implement a <a href="http://naward12.com/">search engine</a>.


# <a href=http://www.naward12.com> Visit the website yourself </a>
