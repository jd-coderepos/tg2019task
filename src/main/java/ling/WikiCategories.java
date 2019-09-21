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
public class WikiCategories extends Features {

    int start;

    int end1;  //Q wiki categories
    int end2;  //A wiki categories
    int end3;  //Expl wiki categories
    int end4;  //Q + A + Expl wiki categories

    Map<String, Integer> wikiCatIndexed = new HashMap<>();

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
        for (String wikiCategory : token.getWikicategories()) {
            if (wikiCatIndexed.isEmpty()) wikiCatIndexed.put(wikiCategory, 0);
            else if (!wikiCatIndexed.containsKey(wikiCategory)) wikiCatIndexed.put(wikiCategory, wikiCatIndexed.size());
        }
    }


    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end1 = start+wikiCatIndexed.size();
        end2 = end1+wikiCatIndexed.size();
        end3 = end2+wikiCatIndexed.size();
        end4 = end3+wikiCatIndexed.size();
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, wikiCatIndexed, question.getWikicategories(), end1)+" "+
                getFeature(end1, wikiCatIndexed, correctAns.getWikicategories(), end2) +" "+
                getFeature(end2, wikiCatIndexed, expl.getWikicategories(), end3) +" "+
                getFeature(end3, wikiCatIndexed, getCommon(getGroup(List.copyOf(question.getWikicategories()), List.copyOf(correctAns.getWikicategories())), List.copyOf(expl.getWikicategories())), end4);
    }

}
