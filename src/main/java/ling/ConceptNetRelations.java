package ling;

import markup.Sentence;
import markup.Token;
import utils.NLP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ling.Utils.*;

/**
 * @author jld
 */
public class ConceptNetRelations extends Features {

    int start;

    int end11; //Q word related words
    int end21; //A word related words
    int end31; //Expl word related words
    int end41; //Q+A+Expl word related words

    Map<String, Integer> relationWordIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end11;
    }

    @Override
    public int getLastSize() {
        return end41;
    }

    @Override
    public void setUniFeatures(Token token) {
        Map<String, List<String>> relatedTerms = NLP.conceptRelations.get(token.getLemma().toLowerCase());
        if (relatedTerms == null) return;
        //Synonym
        for (String relation : relatedTerms.keySet()) {

            List<String> terms = relatedTerms.get(relation);

            for (String term : terms) {

                if (relationWordIndexed.isEmpty()) relationWordIndexed.put(relation+"-"+term, 0);
                else if (!relationWordIndexed.containsKey(relation+"-"+term)) relationWordIndexed.put(relation+"-"+term, relationWordIndexed.size());

            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end11 = start+relationWordIndexed.size()+1;
        end21 = end11+relationWordIndexed.size()+1;
        end31 = end21+relationWordIndexed.size()+1;
        end41 = end31+relationWordIndexed.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, relationWordIndexed, question.getCn_wordrelations(), end11) +" "+
                getFeature(end11, relationWordIndexed, correctAns.getCn_wordrelations(), end21) +" "+
                getFeature(end21, relationWordIndexed, expl.getCn_wordrelations(), end31) +" "+
                getFeature(end31, relationWordIndexed, getCommon(getGroup(List.copyOf(question.getCn_wordrelations()), List.copyOf(correctAns.getCn_wordrelations())), List.copyOf(expl.getCn_wordrelations())), end41);
    }
}
