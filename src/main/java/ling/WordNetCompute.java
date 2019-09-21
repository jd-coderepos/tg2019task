package ling;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerUtils;
import net.sf.extjwnl.data.list.PointerTargetNode;
import net.sf.extjwnl.dictionary.Dictionary;
import utils.IO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author jld
 */
public class WordNetCompute {

    private static Dictionary dictionary;

    public WordNetCompute(Dictionary dictionary) throws JWNLException {
        this.dictionary = dictionary;
    }

    public static Map<Integer, List<String>> lingrelations(IndexWord word) throws JWNLException {

        Map<Integer, List<String>> lingrelations = new HashMap<>();

        List<String> synonyms = new ArrayList<>();
        List<String> hypernyms = new ArrayList<>();
        List<String> hyponyms = new ArrayList<>();
        List<String> meronyms = new ArrayList<>();
        List<String> holonyms = new ArrayList<>();
        List<String> antonyms = new ArrayList<>();

        for (int i = 0; i < word.getSenses().size(); i++) {

            String w = word.getLemma();

            //System.out.println(word.getSenses().get(i).getWords());
            //synonym
            for (int j = 0; j < word.getSenses().get(i).getWords().size(); j++) {
                String synonym = word.getSenses().get(i).getWords().get(j).getLemma();
                if (synonym.equals(w)) continue;
                if (!synonyms.contains(synonym)) synonyms.add(synonym);
            }
            lingrelations.put(1, synonyms);

            //hyponyms
            Iterator<PointerTargetNode> iterHypo = PointerUtils.getDirectHyponyms(word.getSenses().get(i)).iterator();
            while (iterHypo.hasNext()) {
                PointerTargetNode ptn = iterHypo.next();
                for (int j = 0; j < ptn.getSynset().getWords().size(); j++) {
                    String hyponym = ptn.getSynset().getWords().get(j).getLemma();
                    if (hyponym.equals(w)) continue;
                    if (!hyponyms.contains(hyponym)) hyponyms.add(hyponym);
                }
            }
            lingrelations.put(2, hyponyms);

            //hypernyms
            Iterator<PointerTargetNode> iterHyper = PointerUtils.getDirectHypernyms(word.getSenses().get(i)).iterator();
            while (iterHyper.hasNext()) {
                PointerTargetNode ptn = iterHyper.next();
                for (int j = 0; j < ptn.getSynset().getWords().size(); j++) {
                    String hypernym = ptn.getSynset().getWords().get(j).getLemma();
                    if (hypernym.equals(w)) continue;
                    if (!hypernyms.contains(hypernym)) hypernyms.add(hypernym);
                }
            }
            lingrelations.put(3, hypernyms);

            //meronyms
            Iterator<PointerTargetNode> iterMero = PointerUtils.getMeronyms(word.getSenses().get(i)).iterator();
            while (iterMero.hasNext()) {
                PointerTargetNode ptn = iterMero.next();
                for (int j = 0; j < ptn.getSynset().getWords().size(); j++) {
                    String meronym = ptn.getSynset().getWords().get(j).getLemma();
                    if (meronym.equals(w)) continue;
                    if (!meronyms.contains(meronym)) meronyms.add(meronym);
                }
            }
            lingrelations.put(4, meronyms);

            //holonyms
            Iterator<PointerTargetNode> iterHolo = PointerUtils.getHolonyms(word.getSenses().get(i)).iterator();
            while (iterHolo.hasNext()) {
                PointerTargetNode ptn = iterHolo.next();
                for (int j = 0; j < ptn.getSynset().getWords().size(); j++) {
                    String holonym = ptn.getSynset().getWords().get(j).getLemma();
                    if (holonym.equals(w)) continue;
                    if (!holonyms.contains(holonym)) holonyms.add(holonym);
                }
            }
            lingrelations.put(5, holonyms);

            Iterator<PointerTargetNode> iterAnto = PointerUtils.getAntonyms(word.getSenses().get(i)).iterator();
            while (iterAnto.hasNext()) {
                PointerTargetNode ptn = iterAnto.next();
                for (int j = 0; j < ptn.getSynset().getWords().size(); j++) {
                    String antonym = ptn.getSynset().getWords().get(j).getLemma();
                    if (antonym.equals(w)) continue;
                    if (!antonyms.contains(antonym)) antonyms.add(antonym);
                }
            }
            lingrelations.put(6, antonyms);
        }

        return lingrelations;
    }

    public static IndexWord getIndexWord(String word, String pos) throws JWNLException {
        IndexWord indexWord = null;
        switch(pos) {
            case "NN":
                indexWord = dictionary.getIndexWord(POS.NOUN, word);
                break;
            case "JJ":
                indexWord = dictionary.getIndexWord(POS.ADJECTIVE, word);
                break;
            case "RB":
                indexWord = dictionary.getIndexWord(POS.ADVERB, word);
                break;
            case "VB":
                indexWord = dictionary.getIndexWord(POS.VERB, word);
                break;
        }
        return indexWord;
    }

    public static void writeWordNetOutput(FileOutputStream output) throws IOException {
        for (String word_pos : word_pos_lingrelations.keySet()) {
            //String[] word_pos_arr = word_pos.split("\\|\\|");
            Map<Integer, List<String>> typedlingrelations = word_pos_lingrelations.get(word_pos);
            String output_str = word_pos;
            for (int i = 1; i <= 6 ; i++) {
                List<String> lingrelations = typedlingrelations.get(i);
                String str = "";
                for (String lingrelation : lingrelations) {
                    str += lingrelation + "\t";
                }
                str = str.trim();
                output_str += "||"+str;
            }
            output.write((output_str+"\n").getBytes());
        }
    }

    public static Map<String, Map<Integer, List<String>>> word_pos_lingrelations = new HashMap<>();

    public static void setlingrelationsmap(String[] lines, boolean qa) throws JWNLException {
        for (String line : lines) {
            line = line.trim();
            String[] tokens = line.split("\t");
            String[] ques_words = tokens[1].split("\\s");
            for (String ques_word : ques_words) {
                String[] ques_word_tokens = ques_word.split("/");
                if (ques_word_tokens[2].length() < 2) continue;
                IndexWord iw = getIndexWord(ques_word_tokens[0].toLowerCase(), ques_word_tokens[2].substring(0, 2));
                if (iw == null) continue;
                Map<Integer, List<String>> lingrelations = lingrelations(iw);
                word_pos_lingrelations.put(ques_word_tokens[0].toLowerCase()+"||"+ques_word_tokens[2], lingrelations);
            }

            if (qa) {
                String[] ans_words = tokens[2].split("\\s");
                for (String ans_word : ans_words) {
                    String[] ans_word_tokens = ans_word.split("/");
                    if (ans_word_tokens[2].length() < 2) continue;
                    IndexWord iw = getIndexWord(ans_word_tokens[0].toLowerCase(), ans_word_tokens[2].substring(0, 2));
                    if (iw == null) continue;
                    Map<Integer, List<String>> lingrelations = lingrelations(iw);
                    word_pos_lingrelations.put(ans_word_tokens[0].toLowerCase() + "||" + ans_word_tokens[2], lingrelations);
                }
            }
        }
    }

    public static void main(String[] args) throws JWNLException, CloneNotSupportedException, IOException {
        dictionary = Dictionary.getDefaultResourceInstance();

        String[] lines = IO.readFile("C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data\\Elem-Train-Rel.csv", StandardCharsets.UTF_8).split("\\n");
        setlingrelationsmap(lines, true);

        lines = IO.readFile("C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data\\Elem-Dev-Rel.csv", StandardCharsets.UTF_8).split("\\n");
        setlingrelationsmap(lines, true);

        lines = IO.readFile("C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data\\expl-tablestore-rel.csv", StandardCharsets.UTF_8).split("\\n");
        setlingrelationsmap(lines, false);

        FileOutputStream output = new FileOutputStream("C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data\\WordNet.txt");
        writeWordNetOutput(output);
    }

}
