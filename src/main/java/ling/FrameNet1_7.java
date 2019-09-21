package ling;

import markup.Sentence;
import markup.Token;

import java.util.HashMap;
import java.util.Map;

import static ling.Utils.getFeature;

/**
 * @author jld
 */
public class FrameNet1_7 extends Features {

    int start;

    //FILLPRED\tPRED\tAPRED

    int end1;   //fill in Q
    int end2;   //predicates in Q
    int end3;   //arguments in Q

    int end4;     //fill in Expl
    int end5;     //predicates in Expl
    int end6;     //predicates in Expl

    Map<String, Integer> fillIndexed = new HashMap<>();
    Map<String, Integer> predIndexed = new HashMap<>();
    Map<String, Integer> argIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end1;
    }

    @Override
    public int getLastSize() {
        return end6;
    }

    @Override
    public void setUniFeatures(Token token) {

    }

    public void setUniFeatures(Sentence sentence) {
        for (String fill : sentence.getFramefills1_7()) {
            if (fillIndexed.isEmpty()) fillIndexed.put(fill, 0);
            else if (!fillIndexed.containsKey(fill)) fillIndexed.put(fill, fillIndexed.size());
        }
        for (String pred : sentence.getFramepredicates1_7()) {
            if (predIndexed.isEmpty()) predIndexed.put(pred, 0);
            else if (!predIndexed.containsKey(pred)) predIndexed.put(pred, predIndexed.size());
        }
        for (String arg : sentence.getFramearguments1_7()) {
            if (argIndexed.isEmpty()) argIndexed.put(arg, 0);
            else if (!argIndexed.containsKey(arg)) argIndexed.put(arg, argIndexed.size());
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end1 = start + fillIndexed.size() + 1;
        end2 = end1 + predIndexed.size() + 1;
        end3 = end2 + argIndexed.size() + 1;

        end4 = end3 + fillIndexed.size() + 1;
        end5 = end4 + predIndexed.size() + 1;
        end6 = end5 + argIndexed.size() + 1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, fillIndexed, question.getFramefills1_7(), end1)+" "+
                getFeature(end1, predIndexed, question.getFramepredicates1_7(), end2) +" "+
                getFeature(end2, argIndexed, question.getFramearguments1_7(), end3)+" "+

                getFeature(end3, fillIndexed, expl.getFramefills1_7(), end4)+" "+
                getFeature(end4, predIndexed, expl.getFramepredicates1_7(), end5) +" "+
                getFeature(end5, argIndexed, expl.getFramearguments1_7(), end6);
    }
}
