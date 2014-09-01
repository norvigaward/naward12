using System;
using System.Collections.Generic;
using System.Web;

public enum Category
{
    Sports,
    Politics,
    Entertainment,
    News,
    School
}

public class CatogoriesScoreSet
{
    public Dictionary<String, double> Score{ get; set; }


    public CatogoriesScoreSet(List<String> categoriesAnsScores)
    {
        Score = new Dictionary<String, double>();
        
        String[] items = null;
        double tmpScore;
        foreach(String cns in categoriesAnsScores)
        {
             items =  cns.Split(':');   
             tmpScore = Helper.NormalizeScore(Double.Parse(items[1]));
             if(tmpScore > 20)
             {
                 continue;
             }
             Score.Add(items[0].Replace("_", " "), tmpScore);       
        }
    }
}

public class WeightedDomainResult
{
    public String Domain{ get; set; }
    public CatogoriesScoreSet Score{ get; set; }
    public String Website{get;set;}
}

