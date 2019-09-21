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
public class WikiTitles extends Features {

    int start;

    int end1;  //Q wiki titles
    int end2;  //A wiki titles
    int end3;  //Expl wiki titles
    int end4;  //Q + A + Expl wiki titles

    Map<String, Integer> wikiTitleIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end1;
    }

    @Override
    public int getLastSize() {
        return end4;
    }

    @Override
    public void setUniFeatures(Token token) {
        for (String wikiTitle : token.getWikititles()) {
            if (wikiTitleIndexed.isEmpty()) wikiTitleIndexed.put(wikiTitle, 0);
            else if (!wikiTitleIndexed.containsKey(wikiTitle)) wikiTitleIndexed.put(wikiTitle, wikiTitleIndexed.size());
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end1 = start+wikiTitleIndexed.size();
        end2 = end1+wikiTitleIndexed.size();
        end3 = end2+wikiTitleIndexed.size();
        end4 = end3+wikiTitleIndexed.size();
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, wikiTitleIndexed, question.getWikititles(), end1)+" "+
                getFeature(end1, wikiTitleIndexed, correctAns.getWikititles(), end2) +" "+
                getFeature(end2, wikiTitleIndexed, expl.getWikititles(), end3) +" "+
                getFeature(end3, wikiTitleIndexed, getCommon(getGroup(List.copyOf(question.getWikititles()), List.copyOf(correctAns.getWikititles())), List.copyOf(expl.getWikititles())), end4);
    }
}
