package ling;

import markup.Sentence;
import markup.Token;

import java.util.HashMap;
import java.util.Map;

import static ling.Utils.getFeature;

/**
 * @author jld
 */
public class FrameNet1_5 extends Features {

    int start;

    //FILLPRED\tPRED\tAPRED

    int end1;   //fill in Q
    int end2;   //arguments in Q
    int end3;   //words-arguments in Q

    int end4;     //fill in Expl
    int end5;     //arguments in Expl
    int end6;     //words-arguments in Expl

    Map<String, Integer> fillIndexed = new HashMap<>();
    Map<String, Integer> argIndexed = new HashMap<>();
    Map<String, Integer> wordargIndexed = new HashMap<>();

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
        for (String fill : sentence.getFramefills1_5()) {
            if (fillIndexed.isEmpty()) fillIndexed.put(fill, 0);
            else if (!fillIndexed.containsKey(fill)) fillIndexed.put(fill, fillIndexed.size());
        }
        for (String arg : sentence.getFramearguments1_5()) {
            if (argIndexed.isEmpty()) argIndexed.put(arg, 0);
            else if (!argIndexed.containsKey(arg)) argIndexed.put(arg, argIndexed.size());
        }
        for (String wordarg : sentence.getFramewordarguments1_5()) {
            if (wordargIndexed.isEmpty()) wordargIndexed.put(wordarg, 0);
            else if (!wordargIndexed.containsKey(wordarg)) wordargIndexed.put(wordarg, wordargIndexed.size());
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end1 = start + fillIndexed.size() + 1;
        end2 = end1 + argIndexed.size() + 1;
        end3 = end2 + wordargIndexed.size() + 1;

        end4 = end3 + fillIndexed.size() + 1;
        end5 = end4 + argIndexed.size() + 1;
        end6 = end5 + wordargIndexed.size() + 1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, fillIndexed, question.getFramefills1_5(), end1)+" "+
                getFeature(end1, argIndexed, question.getFramearguments1_5(), end2) +" "+
                getFeature(end2, wordargIndexed, question.getFramewordarguments1_5(), end3)+" "+

                getFeature(end3, fillIndexed, expl.getFramefills1_5(), end4)+" "+
                getFeature(end4, argIndexed, expl.getFramearguments1_5(), end5) +" "+
                getFeature(end5, wordargIndexed, expl.getFramewordarguments1_5(), end6);
    }
}
