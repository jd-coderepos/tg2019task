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
public class OpenIERel extends Features {

    int start;

    //only consider non-stop terms
    int end21; //Q lemma-wise relations
    int end41; //A lemma-wise relations
    int end61; //Expl lemma-wise relations

    int end22; //Q subject lemmas
    int end42; //A subject lemmas
    int end62; //Expl subject lemmas
    int end82; //Q + A + Expl subject lemmas

    int end23; //Q object lemmas
    int end43; //A object lemmas
    int end63; //Expl object lemmas
    int end83; //Q + A + Expl object lemmas

    Map<String, Integer> relationLemmaIndexed = new HashMap<>();
    Map<String, Integer> subjectLemmaIndexed = new HashMap<>();
    Map<String, Integer> objectLemmaIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end21;
    }

    @Override
    public int getLastSize() {
        return end83;
    }

    @Override
    public void setUniFeatures(Token token) {

    }

    public void setUniFeatures(Sentence sentence) {
        if (!sentence.getRel_lemmas().isEmpty()) {
            for (String rel_lemma : sentence.getRel_lemmas()) {
                if (relationLemmaIndexed.isEmpty()) relationLemmaIndexed.put(rel_lemma, 0);
                else if (!relationLemmaIndexed.containsKey(rel_lemma)) relationLemmaIndexed.put(rel_lemma, relationLemmaIndexed.size());
            }
        }
        if (!sentence.getSubj_lemmas().isEmpty()) {
            for (String subj_lemma : sentence.getSubj_lemmas()) {
                if (subjectLemmaIndexed.isEmpty()) subjectLemmaIndexed.put(subj_lemma, 0);
                else if (!subjectLemmaIndexed.containsKey(subj_lemma)) subjectLemmaIndexed.put(subj_lemma, subjectLemmaIndexed.size());
            }
        }
        if (!sentence.getObj_lemmas().isEmpty()) {
            for (String obj_lemma : sentence.getObj_lemmas()) {
                if (objectLemmaIndexed.isEmpty()) objectLemmaIndexed.put(obj_lemma, 0);
                else if (!objectLemmaIndexed.containsKey(obj_lemma)) objectLemmaIndexed.put(obj_lemma, objectLemmaIndexed.size());
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end21 = start+relationLemmaIndexed.size()+1;
        end41 = end21+relationLemmaIndexed.size()+1;
        end61 = end41+relationLemmaIndexed.size()+1;

        end22 = end61+subjectLemmaIndexed.size()+1;
        end42 = end22+subjectLemmaIndexed.size()+1;
        end62 = end42+subjectLemmaIndexed.size()+1;
        end82 = end62+subjectLemmaIndexed.size()+1;

        end23 = end82+objectLemmaIndexed.size()+1;
        end43 = end23+objectLemmaIndexed.size()+1;
        end63 = end43+objectLemmaIndexed.size()+1;
        end83 = end63+objectLemmaIndexed.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, relationLemmaIndexed, question.getRel_lemmas(), end21) +" "+
                getFeature(end21, relationLemmaIndexed, correctAns.getRel_lemmas(), end41) +" "+
                getFeature(end41, relationLemmaIndexed, expl.getRel_lemmas(), end61) +" "+

                getFeature(end61, subjectLemmaIndexed, question.getSubj_lemmas(), end22) +" "+
                getFeature(end22, subjectLemmaIndexed, correctAns.getSubj_lemmas(), end42) +" "+
                getFeature(end42, subjectLemmaIndexed, expl.getSubj_lemmas(), end62) +" "+
                getFeature(end62, subjectLemmaIndexed, getCommon(getGroup(List.copyOf(question.getSubj_lemmas()), List.copyOf(correctAns.getSubj_lemmas())), List.copyOf(expl.getSubj_lemmas())), end82) +" "+

                getFeature(end82, objectLemmaIndexed, question.getObj_lemmas(), end23) +" "+
                getFeature(end23, objectLemmaIndexed, correctAns.getObj_lemmas(), end43) +" "+
                getFeature(end43, objectLemmaIndexed, expl.getObj_lemmas(), end63) +" "+
                getFeature(end63, objectLemmaIndexed, getCommon(getGroup(List.copyOf(question.getObj_lemmas()), List.copyOf(correctAns.getObj_lemmas())), List.copyOf(expl.getObj_lemmas())), end83);
    }
}
