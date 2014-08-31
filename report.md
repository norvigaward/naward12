#Final Report for Norvig Award 2014 Competition


![alt tag](/pictures/report/categorie-front.jpg)
<l><I> Source: http://www.newrafael.com/cultural-categories/ </I>


## 

- <b>Team:</b> Naward12 (on Github)
- <b>Subject:</b> Categorization of common web crawl
- <b>Course:</b> Datascience (TU Delft) IN4144
- *Members:* 

- <table>
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



#### Table of contents:

1. [Introduction to the idea](1#-introduction to the idea)
2. [Core Functionalities as of a UI perspective](2#-core functionalities as of a ui perspective)]
3. [Methodology followed in experiments](3#-methodology followed in experiments)
4. [Description of the results](4#-description of the results)
5. [Interpretation of the results](5#-interpretation of the results)
6. [Classification into categories](6#-classification into different categories)
7. [Visualization of the data towards valuable insights into Big Data](7#-visualization of the data towards valuable insights into Big Data)

### Abstract 
<i> As part of the Norvig Award competition we participate in this competition, as three master students from Delft University of Technology, in order to get more experience into Data Science concepts and particularly to gain a better understanding of the content that is present into the different websites which are characterized by their domain name. The goal of the competition is to encourage innovative usage and exploitation of a 3 billion web page common crawl dataset that is publicly available. </b>
</i>

Background as basis for the idea to be started with this project has its origin into search engines providing lists of URL’s which could be presumed to be deduced from the web as a result of web related queries. This list contains indeed links to websites which have that information. However there is a difference between getting a list of URL’s which probably will contain that short of information that can be assigned to the given queries but without the initial specific text. 

An end-user need to analyze the URL’s first to possibly find the information he or she initially demand for by typing in a set of queries. With the solution which is proposed, the end-users will have a search engine which can refine their search results further in case that a lot of content on a specific URL appears for instance, but need to be analyzed in a short amount of time to be able to either dismiss or accept a URL, which was proposed by a search engine. In this way users can refine their results further and gain more specific results towards desired information in less time. The big advantage is that content can be analyzed with more depth and used to decide whether or not a website is interesting for an end-user to use or not. That usage can be seen from different perspectives according to the different kind of information that can be assigned to a website for example, personal interests, hobbies, academic or business.
<br>
<br>
Secondly the insight into the web content is also applicable in case of looking for more information into specific end-users' interests within websites’ borders, usually concerning websites that contain big amounts of information in general but user-specific interest must be deduced from it in short amount of processing time. When end-users have the desire to gain more specific information into a specific interest and using then common web crawl as 
the data source, this is also functional. For instance typing in a domain and getting visual graphs about the content of a website will also help to improve websites due to the ability to compare the composition of the websites with each other, and taking into account the specific public interest for those websites in order to target groups more detailed for advertising campaigns.
 
## 1. Introduction to the idea
The idea is to derive all relevant textual data from the common web crawl in order to classify it into different categories and exhibit websites's relativity towards different categories. The idea's main starting point was a demand of gaining more insight into the data that can be reached through the common web crawl. This categorization idea could lead to further and more enlightening conclusions regarding the crawled data once further data mining or more machine learning processes are applied. Another motivation towards this direction was the need for acquiring information regarding the topic of a website in advance, meaning before even navigating to it, with a view to aiding in search engine results relevancy.
<br>
In the light of the motivations explained above, the implementation of idea tarkets to making possible to gain insight into a domain's topic relevancy by typing the domain name into the search engine designed towards this direction. That insight is strengthened by providing visual representations in form of <i>Scalable Vector Graphics</i>, so as to help the end-user understand the overall picture. 

## 2. Core Functionalities as of a UI perspective
The application is available to the end user via a web interface and it is reachable via the following url: <a href=http://www.Naward12.com> http://www.naward12.com </a>.
<br>
A menu page on the left reveals the options given; (1) access to the domain name search engine, (2) see an overview of all availabel domain names via the search engine and finally (3) a page providing a link to this very document. The options are further presented below.

###2.1. Domain Name Search Engine
The user can fill in the form a domain name or a part of it and attempt to search for the categories it falls in. An accuratelly typed domain name or a an accuratelly typed part of a domain name will return results to user; the categories that content of this domain name is mosty relevant to, along with a score to indicate the relevacy (see figure 1 below). The higher the score the higher the higher the convergence. In case of typing a part of the domain name, an indication will explicitelly mention the domain for which the results are displayed. In both cases, sugestions will be displayed if they are applicable, eg: similar or identical, deferentiated only in the suffix, domain names.

An innacuratelly typed domain name provokes a relative message to the user (see fig. 2), along with suggestions to the user, based on the Levenshtein Distance[1].

![alt tag](/pictures/report/didyoumean-searched.JPG)
<b>Figure 1. Appearance of descriptive statitiscs concerning calssification of the domain

![alt tag](/pictures/report/search-didyoumean.JPG)
<b>Figure 2. Case of wrong entrance of domainname, appearance of corrective name

###2.2. Domain Names Overview
An complete summarizing picture is provided to the user in a table form. All domain names are displayed in an alphabetical order, along with the categories each is more semantically relavant to along with the corresponding relevancy score.

###2.2. Report
A link to this docment is provided as a point of reference to explain the concept and the background of the web appication

<br>
## 3. Methodology towards performing experiments
A possible method is to first verify categories that are initially provided by a common brainstorm session between the teammembers. Afterwards the different words that can be assigned to each of the categories can be measured by count. 
So this is about synonyms that can be interpreted as common. of course the count of unique words is also incorporated to be able to specify each category more according to the different meaning of those words.<br> For getting a list of meaningful and therefore valuable amount of classifications there will be made use of <a href="https://www.dmoz.org">this website</a>. This website is one of the largest comprehensive human-edited directory of the Word Wide Web. Moreover, it is historically called the Open Directory Project (ODP). This site is presumed to be de most widely distributed data base of content that is classified by humans. 
In order to produce a machine-learning based classifier. The directory of this website is used in order to make the classifier as complete as possible. The website is therefore presumed to be a good approximation of all the content that is currently available on the web per domain. From every URL, the domain name will be deduced and consequently assigned to one or more categories in terms of probability chance that a given domain name, which is deduced from a URL, can be assigned to the categories. For Example a record will therefore be presented as follows:

<b>Table 1. Example of a record containing data from the common web crawl 
 <body>  
<table>
      <tr>
         <th>URL</th>
         <th>First Category</th>
         <th>Second category</th>
         <th>Third Category</th>
         <th>Fourth Category</th>
      </tr>
 
       <tr>
        <td>www.amazon.com</td>
        <td>20</td>
        <td>40</td>
        <td>20</td>
         <td>20</td>
       </tr>
              
</table>

<p>
*To provide insight into the intepeation by the application of the data from the common web crawl: below you'll find an example table including values for different domains that can be assigned to different categories, as stated. The format is the same as the first table which was given earlier in this report. Only now a sample picture is shown from the actual datacontent, as it will be shown on the aplication interface for the NorvigAward competition:*

![alt tag](/pictures/report/data-content.JPG)
<b>Figure 3. Table with probability values according to classification of the common web crawl
    
    
<br>       
## 4. Describement of the results
This section will emphasize on the results that can be expected from this project. First of all the Common crawl dataset is indeed the main source concerning the data that is analyzed in order to classify it. However the classification is done with help of another <a href=http://www.dmoz.org> source </a>. This website has been used to classify the content from the common crawl dataset. This implicates that the results are strongly dependent on the quality of that website. This means that the quality of the classification concerning the common crawl dataset is strongly dependent on the quality of the website. The better the classification on the website is done, the better the classifier is, and the better the classification of the content can be completed. Machine-learning has been used to produce the classifier. All categories are assigned according to the words on a specific website. So this means that different websites with various content is classified on that website. And therefore the website can provide the possibility to machine-learning and as a result of this, a suitable classifier. 
 
 
 
<br>
## 5. Interpretation of the results
The interpretations of the results of the categorization is strongly dependent on the scalability of the retrieved categories with words. Therefore this section is filled whenever the results are interpreted. The visual representation, according to a given domain name is representational for the results that can be gained from the common crawl dataset. The visual graphing will show visual representations in order to interpret the results in a way which is representational for all of the different domains that are classified according to the classifier which is defined with help of a website which already contains different classifications. The visual graphs provide an overview on the classification of the content and therefore the chance of which the content of a given domain can be assigned to different amounts of categories. The results must be interpreted from the perspective of a general understanding of the content which is stated onto the different websites, in form of a categorization.



<br>
## 6. Classification into different categories
There are many categories possible to be used, in order to classify the content from the data in the common web crawl. These categories are adopted from dmoz.com as mentined earlier in paragraph 3. Below is presented several categories which are defined from the brainstorm between teammembers for the validation of the categories as presented in figure5.
World news, Food, Cars, Religion, Psychology, Sociology, Cooking, sports, education, science-fiction, literature, science, Electronics engineering, Civil engineering, computer science, Industrial design, Architecture, IT, Web shops, forum, Social Media, Maritime technology, sustainability,  entertainment, career opportunities, motorcycles, fashion, medicine treatment, government information, archeology, clothing and many more...

*`Adoption of categorization to the common web crawl:` below you find a list with the first 100 categories that are gained from the website www.dmoz.com by machine-learning and are used in to classify the data that is gained from the common webcrawl to gain more insight in the data content. This is used to gain insight into the webpage content per domain, but also to get an overall insight into the total content of web by gaining data from the common web crawl:*

![alt tag](/pictures/report/categories.png)
<p>Figure 5: example list of first 100 categories used for categorization of data on webpages



<br>
## 7. Visualization of the data towards valuable insights into Big Data 
Influence/popularity of category on the web. Pie diagram which can show what kind of category contains the most textual data. Specification of a category. Tables with the frequency of different unique founded words for each category (that also is part of the web crawl). Similarity or overlapping of different categories with each other. By comparing the results of founded words from the same websites but assigned to different categories (why is that text together?).
Which websites contain most different meaningful words, and which one the fewest? Producing a Pie diagram as well with showing this interesting observation.

## `Advantages`: 
1. With more insight into the webcontent a web-user can quickly scan per domain what kind of content is presented in a robust way. this will save time as a web-user doesn't need to scan the whole site for deciding upon the question whether this website is interesting or not. In this case more effective use of the internet is made possible.
2. Moreover, with the insight into an anourmous amount of domains, entrepeneurs, companies and the government can gain worthfull insight into a representational amount of domains and thereby having access to knowledge that can help them to gain economic value by investments, understandability of upcoming markets and opportunities for new platforms.


# <a href=http://www.Naward12.com> Visit the website yourself </a>
