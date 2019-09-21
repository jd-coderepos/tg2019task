package markup;

import utils.NLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Token {

    String word;
    String lemma;
    String pos;
    String concretenessLabel;
    Double concretenessScore;
    List<String> prefix;
    List<String> suffix;
    List<String> concepts;

    List<String> cn_wordrelations;

    List<String> wikicategories;

    List<String> wikititles;

    List<String> synonyms;
    List<String> hyponyms;
    List<String> hypernyms;
    List<String> meronyms;
    List<String> holonyms;
    List<String> antonyms;

    public Token(String w, String l, String p, String cl, Double cs, List<String> concepts) {
        this.word = w;
        this.lemma = l;
        this.pos = p;
        this.concretenessLabel = cl;
        this.concretenessScore = cs;

        this.prefix = new ArrayList<>();
        this.suffix = new ArrayList<>();
        setAffix(w);

        this.concepts = concepts;

        cn_wordrelations = new ArrayList<>();
        wikicategories = new ArrayList<>();
        wikititles = new ArrayList<>();

        synonyms = new ArrayList<>();
        hyponyms = new ArrayList<>();
        hypernyms = new ArrayList<>();
        meronyms = new ArrayList<>();
        holonyms = new ArrayList<>();
        antonyms = new ArrayList<>();
    }

    public void setCn_wordrelations(String word) {
        Map<String, List<String>> relatedTerms = NLP.conceptRelations.get(word);
        if (relatedTerms != null) {
            for (String relation : relatedTerms.keySet()) {
                List<String> terms = relatedTerms.get(relation);
                for (String term : terms) {
                    if (!cn_wordrelations.contains(relation + "-" + term)) cn_wordrelations.add(relation + "-" + term);
                }
            }
        }
    }

    public void setWikicategories(String word) {
        List<String> categories = NLP.wikiCategories.get(word);
        if (categories != null) {
            for (String category : categories) {
                if (!wikicategories.contains(category)) wikicategories.add(category);
            }
        }
    }

    public void setWikititles(String word) {
        List<String> titles = NLP.wikiTitles.get(word);
        if (titles != null) {
            for (String title : titles) {
                if (!wikititles.contains(title)) wikititles.add(title);
            }
        }
    }

    public void setWordNetLinguisticRelations(String word, String pos) {
        Map<Integer, List<String>> linguisticRelations = NLP.wordLinguisticRelations.get(word+"||"+pos);
        if (linguisticRelations == null) return;
        if (linguisticRelations.containsKey(1)) {
            synonyms = linguisticRelations.get(1);
        }
        if (linguisticRelations.containsKey(2)) {
            hyponyms = linguisticRelations.get(2);
        }
        if (linguisticRelations.containsKey(3)) {
            hypernyms = linguisticRelations.get(3);
        }
        if (linguisticRelations.containsKey(4)) {
            meronyms = linguisticRelations.get(4);
        }
        if (linguisticRelations.containsKey(5)) {
            holonyms = linguisticRelations.get(5);
        }
        if (linguisticRelations.containsKey(6)) {
            antonyms = linguisticRelations.get(6);
        }
    }

    public String getWord() {
        return word;
    }

    public String getLemma() {
        return lemma;
    }

    public String getPos() {
        return pos;
    }

    public String getConcretenessLabel() {
        return concretenessLabel;
    }

    public Double getConcretenessScore() {
        return concretenessScore;
    }

    public List<String> getConcepts() {
        return concepts;
    }

    public void setAffix(String word) {
        String w = word.toLowerCase();
        int length = w.length() >= 5 ? 5 : w.length() >= 4 ? 4 : w.length() >= 3 ? 3 : 0;
        setAffix(length, w);
    }

    public void setAffix(int length, String str) {
        switch(length) {
            case 5:
                if (!prefix.contains(str.substring(0,5))) prefix.add(str.substring(0, 5));
                if (!suffix.contains(str.substring(str.length()-5))) suffix.add(str.substring(str.length()-5));
            case 4:
                if (!prefix.contains(str.substring(0,4))) prefix.add(str.substring(0, 4));
                if (!suffix.contains(str.substring(str.length()-4))) suffix.add(str.substring(str.length()-4));
            case 3:
                if (!prefix.contains(str.substring(0,3))) prefix.add(str.substring(0, 3));
                if (!suffix.contains(str.substring(str.length()-3))) suffix.add(str.substring(str.length()-3));
        }
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<String> getSuffix() {
        return suffix;
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

