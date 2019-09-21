package ling;

import markup.Sentence;
import markup.Token;

/**
 * @author jld
 */
abstract class Features {

    public abstract int getFirstSize();

    public abstract int getLastSize();

    public abstract void setUniFeatures(Token token);

    public abstract void setFeatureSizes(int start);

    public abstract String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl);

}
