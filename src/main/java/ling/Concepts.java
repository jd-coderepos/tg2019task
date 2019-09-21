package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ling.Utils.*;

/**
 * @author jld
 */
public class Concepts extends Features {

    int start;

    int end7;     //concepts in Q
    int end8;     //concepts in A
    int end9;     //concepts in Expl

    int end10;    //concepts in Q+Expl
    int end11;    //concepts in A+Expl
    int end12;    //concepts in Q+A+Expl

    Map<String, Integer> conceptsIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end7;
    }

    @Override
    public int getLastSize() {
        return end12;
    }

    @Override
    public void setUniFeatures(Token token) {
        if (token.getConcepts() != null) {
            for (String conceptStr : token.getConcepts()) {
                if (conceptsIndexed.isEmpty()) conceptsIndexed.put(conceptStr, 0);
                else if (!conceptsIndexed.containsKey(conceptStr)) conceptsIndexed.put(conceptStr, conceptsIndexed.size());
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end7 = start+conceptsIndexed.size()+1;
        end8 = end7+conceptsIndexed.size()+1;
        end9 = end8+conceptsIndexed.size()+1;

        end10 = end9+conceptsIndexed.size()+1;
        end11 = end10+conceptsIndexed.size()+1;
        end12 = end11+conceptsIndexed.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, conceptsIndexed, question.getConcepts(), end7) +" "+
                getFeature(end7, conceptsIndexed, correctAns.getConcepts(), end8) +" "+
                getFeature(end8, conceptsIndexed, expl.getConcepts(), end9) +" "+
                getFeature(end9, conceptsIndexed, getCommon(List.copyOf(question.getConcepts()), List.copyOf(expl.getConcepts())), end10) +" "+
                getFeature(end10, conceptsIndexed, getCommon(List.copyOf(correctAns.getConcepts()), List.copyOf(expl.getConcepts())), end11) +" "+
                getFeature(end11, conceptsIndexed, getCommon(getGroup(List.copyOf(question.getConcepts()), List.copyOf(correctAns.getConcepts())), List.copyOf(expl.getConcepts())), end12);
    }
}
