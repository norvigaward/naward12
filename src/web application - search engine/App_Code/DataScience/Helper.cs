using System;
using System.Collections.Generic;
using System.Web;
using System.Linq;
using System.Text;
using System.IO;

/// <summary>
/// Summary description for Helper
/// </summary>
public static class Helper
{
    public const double NORMALIZING_FACTOR = -10000.0;
    public const String URL_PREFIX = "http://";
    public const String URL_PREFIX_S = "https://";
    public const String SLASH = "/";
    public const int MAXIMUM_DOMAIN_NAME_DISTANCE = 8;

    public static double NormalizeScore(double score)
    {
        return Math.Round( NORMALIZING_FACTOR * (1.0 / score) , 3);
    }

    public static String NormalizeDomainName(String url)
    {
        String domainName = url;
        domainName = domainName.Replace(Helper.URL_PREFIX, String.Empty);
        domainName = domainName.Replace(Helper.URL_PREFIX_S, String.Empty);
        
        if(domainName.Contains(Helper.SLASH))
        {
            int slashIndex = domainName.IndexOf(Helper.SLASH);
            domainName = domainName.Substring(0, slashIndex);
        }

        String domainSuffix = Helper.GetAllDomainSuffixes()
                                    .Where(suffix => domainName.EndsWith(suffix))
                                    .OrderBy(suffix => suffix.Length)
                                    .FirstOrDefault();
        if(domainSuffix != null)
        {
            domainName = domainName.Replace(domainSuffix, String.Empty);
        }

        return domainName;
    }

    private static List<String> allDomainSuffixes;
    public static List<String> GetAllDomainSuffixes()
    {
        

        if(Helper.allDomainSuffixes == null || Helper.allDomainSuffixes.Count == 0)
        {
            allDomainSuffixes = new List<String>();
            
            StreamReader reader = new StreamReader(  HttpRuntime.AppDomainAppPath + "/App_Code/DataScience/allDomainSuffixes.txt");

            String line = null;
            while(true)
            {
                line = reader.ReadLine();
                if(line == null){
                    break;
                }

                if(!line.StartsWith("."))
                {
                    continue;
                }

                allDomainSuffixes.Add(line.Split(' ', '\t')[0]);

                    
            }
            reader.Close();
        }
        return allDomainSuffixes;
        
        
    }

}
