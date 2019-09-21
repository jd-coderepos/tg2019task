package ling;

import markup.Sentence;
import markup.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ling.Utils.*;

/**
 * @author jld
 */
public class Word extends Features {

    int start;

    int end1;     //non-stop words in Q
    int end2;     //non-stop words in A
    int end3;     //non-stop words in Expl

    int end4;     //non-stop words common in Q+Expl
    int end5;     //non-stop words common in A+Expl
    int end6;     //non-stop words common in Q+A+Expl

    Map<String, Integer> wordIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end1;
    }

    @Override
    public int getLastSize() {
        return end6;
    }

    public void setUniFeatures(Token token) {
        if (wordIndexed.isEmpty()) wordIndexed.put(token.getWord(), 0);
        else if (!wordIndexed.containsKey(token.getWord())) wordIndexed.put(token.getWord(), wordIndexed.size());
    }

    public void setFeatureSizes(int s) {
        start = s;

        end1 = start + wordIndexed.size() + 1;   //words in Q
        end2 = end1 + wordIndexed.size() + 1;  //words in A
        end3 = end2 + wordIndexed.size() + 1;  //words in Expl

        end4 = end3 + wordIndexed.size() + 1;   //words in Q + Expl
        end5 = end4 + wordIndexed.size() + 1;   //words in A + Expl
        end6 = end5 + wordIndexed.size() + 1;   //words in Q + A + Expl
    }

    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, wordIndexed, question.getWords(), end1)+" "+
                getFeature(end1, wordIndexed, correctAns.getWords(), end2) +" "+
                getFeature(end2, wordIndexed, expl.getWords(), end3) +" "+
                getFeature(end3, wordIndexed, getCommon(List.copyOf(question.getWords()), List.copyOf(expl.getWords())), end4) + " " +
                getFeature(end4, wordIndexed, getCommon(List.copyOf(correctAns.getWords()), List.copyOf(expl.getWords())), end5) + " " +
                getFeature(end5, wordIndexed, getCommon(getGroup(List.copyOf(question.getWords()), List.copyOf(correctAns.getWords())), List.copyOf(expl.getWords())), end6);
    }
}
