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
public class Affix extends Features {

    int start;

    int end11;  //Q prefixes
    int end12;  //A prefixes
    int end13;  //Expl prefixes
    int end14;  //Q + A + Expl prefixes

    int end21;  //Q suffixes
    int end22;  //A suffixes
    int end23;  //Expl suffixes
    int end24;  //Q + A + Expl sufixes

    Map<String, Integer> prefixIndexed = new HashMap<>();
    Map<String, Integer> suffixIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end11;
    }

    @Override
    public int getLastSize() {
        return end24;
    }

    @Override
    public void setUniFeatures(Token token) {
        for (String pref : token.getPrefix()) {
            if (prefixIndexed.isEmpty()) prefixIndexed.put(pref, 0);
            else if (!prefixIndexed.containsKey(pref)) prefixIndexed.put(pref, prefixIndexed.size());
        }
        for (String suff : token.getSuffix()) {
            if (suffixIndexed.isEmpty()) suffixIndexed.put(suff, 0);
            else if (!suffixIndexed.containsKey(suff)) suffixIndexed.put(suff, prefixIndexed.size());
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+prefixIndexed.size();
        end12 = end11+prefixIndexed.size();
        end13 = end12+prefixIndexed.size();
        end14 = end13+prefixIndexed.size();

        end21 = end14+suffixIndexed.size();
        end22 = end21+suffixIndexed.size();
        end23 = end22+suffixIndexed.size();
        end24 = end23+suffixIndexed.size();
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {

        //System.out.println("Fetching Lemma String");

        return getFeature(this.start, prefixIndexed, question.getPrefix(), end11) +" "+
                getFeature(end11, prefixIndexed, correctAns.getPrefix(), end12) +" "+
                getFeature(end12, prefixIndexed, expl.getPrefix(), end13) +" "+
                getFeature(end13, prefixIndexed, getCommon(getGroup(List.copyOf(question.getPrefix()), List.copyOf(correctAns.getPrefix())), List.copyOf(expl.getPrefix())), end14) +" "+
                getFeature(end14, suffixIndexed, question.getSuffix(), end21) +" "+
                getFeature(end21, suffixIndexed, correctAns.getSuffix(), end22) +" "+
                getFeature(end22, suffixIndexed, expl.getSuffix(), end23) +" "+
                getFeature(end23, suffixIndexed, getCommon(getGroup(List.copyOf(question.getSuffix()), List.copyOf(correctAns.getSuffix())), List.copyOf(expl.getSuffix())), end24);
    }
}
