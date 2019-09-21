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
public class WordNet extends Features {

    int start;

    int end1;  //Q synonyms
    int end2;  //Q hyponyms
    int end3;  //Q hypernyms
    int end4;  //Q meronyms
    int end5;  //Q holonyms
    int end6;  //Q antonyms

    int end7;  //A synonyms
    int end8;  //A hyponyms
    int end9;  //A hypernyms
    int end10;  //A meronyms
    int end11;  //A holonyms
    int end12;  //A antonyms

    int end13;  //Expl synonyms
    int end14;  //Expl hyponyms
    int end15;  //Expl hypernyms
    int end16;  //Expl meronyms
    int end17;  //Expl holonyms
    int end18;  //Expl antonyms

    int end19;  //Q+A+Expl synonyms
    int end20;  //Q+A+Expl hyponyms
    int end21;  //Q+A+Expl hypernyms
    int end22;  //Q+A+Expl meronyms
    int end23;  //Q+A+Expl holonyms
    int end24;  //Q+A+Expl antonyms

    Map<String, Integer> synonymsIndexed = new HashMap<>();
    Map<String, Integer> hyponymsIndexed = new HashMap<>();
    Map<String, Integer> hypernymsIndexed = new HashMap<>();
    Map<String, Integer> meronymsIndexed = new HashMap<>();
    Map<String, Integer> holonymsIndexed = new HashMap<>();
    Map<String, Integer> antonymsIndexed = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end1;
    }

    @Override
    public int getLastSize() {
        return end24;
    }

    @Override
    public void setUniFeatures(Token token) {
        /*for (String synonym : token.getSynonyms()) {
            if (synonymsIndexed.isEmpty()) synonymsIndexed.put(synonym, 0);
            else if (!synonymsIndexed.containsKey(synonym)) synonymsIndexed.put(synonym, synonymsIndexed.size());
        }
        for (String hyponym : token.getHyponyms()) {
            if (hyponymsIndexed.isEmpty()) hyponymsIndexed.put(hyponym, 0);
            else if (!hyponymsIndexed.containsKey(hyponym)) hyponymsIndexed.put(hyponym, hyponymsIndexed.size());
        }*/
        for (String hypernym : token.getHypernyms()) {
            if (hypernymsIndexed.isEmpty()) hypernymsIndexed.put(hypernym, 0);
            else if (!hypernymsIndexed.containsKey(hypernym)) hypernymsIndexed.put(hypernym, hypernymsIndexed.size());
        }
        /*for (String meronym : token.getMeronyms()) {
            if (meronymsIndexed.isEmpty()) meronymsIndexed.put(meronym, 0);
            else if (!meronymsIndexed.containsKey(meronym)) meronymsIndexed.put(meronym, meronymsIndexed.size());
        }
        for (String holonym : token.getHolonyms()) {
            if (holonymsIndexed.isEmpty()) holonymsIndexed.put(holonym, 0);
            else if (!holonymsIndexed.containsKey(holonym)) holonymsIndexed.put(holonym, holonymsIndexed.size());
        }
        for (String antonym : token.getAntonyms()) {
            if (antonymsIndexed.isEmpty()) antonymsIndexed.put(antonym, 0);
            else if (!antonymsIndexed.containsKey(antonym)) antonymsIndexed.put(antonym, antonymsIndexed.size());
        }*/
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end1 = start+synonymsIndexed.size();
        end2 = end1+hyponymsIndexed.size();
        end3 = end2+hypernymsIndexed.size();
        end4 = end3+meronymsIndexed.size();
        end5 = end4+holonymsIndexed.size();
        end6 = end5+antonymsIndexed.size();

        end7 = end6+synonymsIndexed.size();
        end8 = end7+hyponymsIndexed.size();
        end9 = end8+hypernymsIndexed.size();
        end10 = end9+meronymsIndexed.size();
        end11 = end10+holonymsIndexed.size();
        end12 = end11+antonymsIndexed.size();

        end13 = end12+synonymsIndexed.size();
        end14 = end13+hyponymsIndexed.size();
        end15 = end14+hypernymsIndexed.size();
        end16 = end15+meronymsIndexed.size();
        end17 = end16+holonymsIndexed.size();
        end18 = end17+antonymsIndexed.size();

        end19 = end18+synonymsIndexed.size();
        end20 = end19+hyponymsIndexed.size();
        end21 = end20+hypernymsIndexed.size();
        end22 = end21+meronymsIndexed.size();
        end23 = end22+holonymsIndexed.size();
        end24 = end23+antonymsIndexed.size();
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, synonymsIndexed, question.getSynonyms(), end1)+" "+
                getFeature(end1, hyponymsIndexed, question.getHyponyms(), end2) +" "+
                getFeature(end2, hypernymsIndexed, question.getHypernyms(), end3) +" "+
                getFeature(end3, meronymsIndexed, question.getMeronyms(), end4) +" "+
                getFeature(end4, holonymsIndexed, question.getHolonyms(), end5) +" "+
                getFeature(end5, antonymsIndexed, question.getAntonyms(), end6) +" "+

                getFeature(end6, synonymsIndexed, correctAns.getSynonyms(), end7)+" "+
                getFeature(end7, hyponymsIndexed, correctAns.getHyponyms(), end8) +" "+
                getFeature(end8, hypernymsIndexed, correctAns.getHypernyms(), end9) +" "+
                getFeature(end9, meronymsIndexed, correctAns.getMeronyms(), end10) +" "+
                getFeature(end10, holonymsIndexed, correctAns.getHolonyms(), end11) +" "+
                getFeature(end11, antonymsIndexed, correctAns.getAntonyms(), end12) +" "+

                getFeature(end12, synonymsIndexed, expl.getSynonyms(), end13)+" "+
                getFeature(end13, hyponymsIndexed, expl.getHyponyms(), end14) +" "+
                getFeature(end14, hypernymsIndexed, expl.getHypernyms(), end15) +" "+
                getFeature(end15, meronymsIndexed, expl.getMeronyms(), end16) +" "+
                getFeature(end16, holonymsIndexed, expl.getHolonyms(), end17) +" "+
                getFeature(end17, antonymsIndexed, expl.getAntonyms(), end18) +" "+

                getFeature(end18, synonymsIndexed, getCommon(getGroup(List.copyOf(question.getSynonyms()), List.copyOf(correctAns.getSynonyms())), List.copyOf(expl.getSynonyms())), end19)+" "+
                getFeature(end19, hyponymsIndexed, getCommon(getGroup(List.copyOf(question.getHyponyms()), List.copyOf(correctAns.getHyponyms())), List.copyOf(expl.getHyponyms())), end20) +" "+
                getFeature(end20, hypernymsIndexed, getCommon(getGroup(List.copyOf(question.getHypernyms()), List.copyOf(correctAns.getHypernyms())), List.copyOf(expl.getHypernyms())), end21) +" "+
                getFeature(end21, meronymsIndexed, getCommon(getGroup(List.copyOf(question.getMeronyms()), List.copyOf(correctAns.getMeronyms())), List.copyOf(expl.getMeronyms())), end22) +" "+
                getFeature(end22, holonymsIndexed, getCommon(getGroup(List.copyOf(question.getHolonyms()), List.copyOf(correctAns.getHolonyms())), List.copyOf(expl.getHolonyms())), end23) +" "+
                getFeature(end23, antonymsIndexed, getCommon(getGroup(List.copyOf(question.getAntonyms()), List.copyOf(correctAns.getAntonyms())), List.copyOf(expl.getAntonyms())), end24);
    }
}
