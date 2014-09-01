using System;
using System.Collections.Generic;
using System.Web;

/// <summary>
/// Summary description for DidYouMeanCandidate
/// </summary>
public class DidYouMeanCandidate : IComparable<DidYouMeanCandidate>
{
    public int Score { get; set; }
    public String Value { get; set; }

    public int CompareTo(DidYouMeanCandidate obj) {
        if (obj == null)
        { 
            return 1; 
        } 
            
        return this.Score.CompareTo(obj.Score);
    }
}

public class DidYouMeanCandidateComparer : Comparer<DidYouMeanCandidate>
{

    public override int Compare(DidYouMeanCandidate x, DidYouMeanCandidate y)
    {
        return x.Score - y.Score;
    }
}