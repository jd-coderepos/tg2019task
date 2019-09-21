package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class TableStore extends Features {

    public int start;
    public int end1;

    List<String> tablestore = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end1;
    }

    @Override
    public int getLastSize() {
        return end1;
    }

    @Override
    public void setUniFeatures(Token token) {

    }

    public void setTablestore(String source) {
        if (!tablestore.contains(source)) tablestore.add(source);
    }

    @Override
    public void setFeatureSizes(int s) {
        start = s;
        end1 = start+tablestore.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return null;
    }

    public String toSVMRankString(String source) {
        return Utils.getFeature(start, tablestore, source, end1);
    }

}
