package markup;

import main.Main;
import utils.NLP;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class Sentence {
    List<Token> tokens;

    List<String> words;

    List<String> lemmas;

    List<String> posTags;
    List<String> concepts;

    List<String> prefix;
    List<String> suffix;

    List<String> subj_words;
    List<String> subj_lemmas;
    List<String> obj_words;
    List<String> obj_lemmas;
    List<String> rel_words;
    List<String> rel_lemmas;

    List<String> cn_wordrelations;

    List<String> wikicategories;

    List<String> wikititles;

    List<String> framefills1_7;
    List<String> framepredicates1_7;
    List<String> framearguments1_7;

    List<String> framefills1_5;
    List<String> framearguments1_5;
    List<String> framewordarguments1_5;

    List<String> synonyms;
    List<String> hyponyms;
    List<String> hypernyms;
    List<String> meronyms;
    List<String> holonyms;
    List<String> antonyms;

    public Sentence() {
        tokens = new ArrayList<>();
        words = new ArrayList<>();

        lemmas = new ArrayList<>();

        posTags = new ArrayList<>();
        concepts = new ArrayList<>();

        prefix = new ArrayList<>();
        suffix = new ArrayList<>();

        subj_words = new ArrayList<>();
        subj_lemmas = new ArrayList<>();
        obj_words = new ArrayList<>();
        obj_lemmas = new ArrayList<>();
        rel_words = new ArrayList<>();
        rel_lemmas = new ArrayList<>();

        cn_wordrelations = new ArrayList<>();

        wikicategories = new ArrayList<>();

        wikititles = new ArrayList<>();

        framefills1_7 = new ArrayList<>();
        framepredicates1_7 = new ArrayList<>();
        framearguments1_7 = new ArrayList<>();

        framefills1_5 = new ArrayList<>();
        framearguments1_5 = new ArrayList<>();
        framewordarguments1_5 = new ArrayList<>();

        synonyms = new ArrayList<>();
        hyponyms = new ArrayList<>();
        hypernyms = new ArrayList<>();
        meronyms = new ArrayList<>();
        holonyms = new ArrayList<>();
        antonyms = new ArrayList<>();
    }

    public void setFrames1_5(String frameStr, boolean train, Main main) {
        String[] tokens = frameStr.split("\\|\\|");
        if (tokens.length == 0) return;

        String[] fills = tokens[0].split("\t");
        for (String fill : fills) {
            framefills1_5.add(fill);
        }

        if (tokens.length > 1) {
            String[] arguments = tokens[1].split("\t");
            for (String argument : arguments) {
                framearguments1_5.add(argument);
            }
        }

        if (tokens.length > 2) {
            String[] wordarguments = tokens[2].split("\t");
            for (String wordargument : wordarguments) {
                framearguments1_5.add(wordargument);
            }
        }

        if (train) {
            main.getFrameNet1_5().setUniFeatures(this);
        }
    }

    public void setFrames1_7(String frameStr, boolean train, Main main) {
        String[] tokens = frameStr.split("\\|\\|");

        String[] fills = tokens[0].split("\t");
        for (String fill : fills) {
            framefills1_7.add(fill);
        }

        String[] predicates = tokens[1].split("\t");
        for (String predicate : predicates) {
            framepredicates1_7.add(predicate);
        }

        if (tokens.length > 2) {
            String[] arguments = tokens[2].split("\t");
            for (String argument : arguments) {
                framearguments1_7.add(argument);
            }
        }

        if (train) {
            main.getFrameNet1_7().setUniFeatures(this);
        }
    }

    //set only non-stopword tokens
    public void setTokens(String[] line, boolean train, Main main) {
        for (String token : line) {
            String[] elements = token.split("/");
            if (NLP.stopwords.contains(elements[0].toLowerCase())) continue;

            List<String> concepts = NLP.concepts.get(elements[0].toLowerCase());
            Token t = new Token(elements[0], elements[1], elements[2], elements[4], elements[5].equals("null") ? null : Double.parseDouble(elements[5]), concepts);
            if (elements.length == 7) {
                t.setDepRel(elements[6].split("\\|"), false);
            }
            if (elements.length == 8) {
                t.setDepRel(elements[7].split("\\|"), true);
            }

            String word_lowercase = t.word.toLowerCase();
            t.setCn_wordrelations(word_lowercase);
            t.setWikicategories(word_lowercase);
            t.setWikititles(word_lowercase);
            t.setWordNetLinguisticRelations(t.getWord(), t.getPos());

            tokens.add(t);
            setCumulativeValues(t);

            if (train) {
                main.getLemma().setUniFeatures(t);
                main.getAffix().setUniFeatures(t);
                main.getCon().setUniFeatures(t);
                main.getCnrel().setUniFeatures(t);
                main.getWikicat().setUniFeatures(t);
                main.getWikit().setUniFeatures(t);
                main.getWordNet().setUniFeatures(t);
            }
        }
    }

    public void setCumulativeValues(Token t){
        if (!words.contains(t.getWord())) words.add(t.getWord());

        if (!lemmas.contains(t.getLemma())) lemmas.add(t.getLemma());

        if (!posTags.contains(t.getPos())) posTags.add(t.getPos());
        if (t.getConcepts() != null) {
            for (String concept : t.getConcepts()) {
                if (!concepts.contains(concept)) concepts.add(concept);
            }
        }

        for (String pref : t.getPrefix()) {
            if (!prefix.contains(pref)) prefix.add(pref);
        }
        for (String suff : t.getSuffix()) {
            if (!suffix.contains(suff)) suffix.add(suff);
        }

        if (!t.getCn_wordrelations().isEmpty()) {
            for (String wordrelation : t.getCn_wordrelations()) {
                if (!cn_wordrelations.contains(wordrelation)) cn_wordrelations.add(wordrelation);
            }
        }

        if (!t.getWikicategories().isEmpty()) {
            for (String wikicategory : t.getWikicategories()) {
                if (!wikicategories.contains(wikicategory)) wikicategories.add(wikicategory);
            }
        }

        if (!t.getWikititles().isEmpty()) {
            for (String wikititle : t.getWikititles()) {
                if (!wikititles.contains(wikititle)) wikititles.add(wikititle);
            }
        }

        if (!t.getSynonyms().isEmpty()) {
            for (String synonym : t.getSynonyms()) {
                if (!synonyms.contains(synonym)) synonyms.add(synonym);
            }
        }

        if (!t.getHyponyms().isEmpty()) {
            for (String hyponym : t.getHyponyms()) {
                if (!hyponyms.contains(hyponym)) hyponyms.add(hyponym);
            }
        }

        if (!t.getHypernyms().isEmpty()) {
            for (String hypernym : t.getHypernyms()) {
                if (!hypernyms.contains(hypernym)) hypernyms.add(hypernym);
            }
        }

        if (!t.getMeronyms().isEmpty()) {
            for (String meronym : t.getMeronyms()) {
                if (!meronyms.contains(meronym)) meronyms.add(meronym);
            }
        }

        if (!t.getHolonyms().isEmpty()) {
            for (String holonym : t.getHolonyms()) {
                if (!holonyms.contains(holonym)) holonyms.add(holonym);
            }
        }

        if (!t.getAntonyms().isEmpty()) {
            for (String antonym : t.getAntonyms()) {
                if (!antonyms.contains(antonym)) antonyms.add(antonym);
            }
        }
    }

    public void setRelations(String stringRel, boolean train, Main main) {
        if (stringRel == null) return;
        String[] relations = stringRel.split("/");

        for (String relation : relations) {
            String[] relComponents = relation.split("\\|");
            if (relComponents.length == 1) continue;

            //first two subj
            //if (!subj_words.contains(relComponents[0])) subj_words.add(relComponents[0]);
            if (!subj_lemmas.contains(relComponents[1])) subj_lemmas.add(relComponents[1]);

            //next two obj
            //if (!obj_words.contains(relComponents[2])) obj_words.add(relComponents[2]);
            if (!obj_lemmas.contains(relComponents[3])) obj_lemmas.add(relComponents[3]);

            //next two rel
            //if (!rel_words.contains(relComponents[4])) rel_words.add(relComponents[4]);
            if (!rel_lemmas.contains(relComponents[5])) rel_lemmas.add(relComponents[5]);

        }
        if (train) {
            main.getOpenRel().setUniFeatures(this);
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<String> getLemmas() {
        return lemmas;
    }

    public List<String> getPosTags() {
        return posTags;
    }

    public List<String> getConcepts() {
        return concepts;
    }

    public List<String> getSubj_words() {
        return subj_words;
    }

    public List<String> getSubj_lemmas() {
        return subj_lemmas;
    }

    public List<String> getObj_words() {
        return obj_words;
    }

    public List<String> getObj_lemmas() {
        return obj_lemmas;
    }

    public List<String> getRel_words() {
        return rel_words;
    }

    public List<String> getRel_lemmas() {
        return rel_lemmas;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<String> getSuffix() {
        return suffix;
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> getCn_wordrelations() {
        return cn_wordrelations;
    }

    public List<String> getWikicategories() {
        return wikicategories;
    }

    public List<String> getWikititles() {
        return wikititles;
    }

    public List<String> getFramefills1_7() {
        return framefills1_7;
    }

    public List<String> getFramepredicates1_7() {
        return framepredicates1_7;
    }

    public List<String> getFramearguments1_7() {
        return framearguments1_7;
    }

    public List<String> getFramefills1_5() {
        return framefills1_5;
    }

    public List<String> getFramearguments1_5() {
        return framearguments1_5;
    }

    public List<String> getFramewordarguments1_5() {
        return framewordarguments1_5;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getHyponyms() {
        return hyponyms;
    }

    public List<String> getHypernyms() {
        return hypernyms;
    }

    public List<String> getMeronyms() {
        return meronyms;
    }

    public List<String> getHolonyms() {
        return holonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

}
