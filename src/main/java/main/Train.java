package main;

import ling.Utils;
import markup.Explanation;
import markup.QA;
import markup.Sentence;
import utils.NLP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jld
 */
public class Train {

    Map<String, QA> idQa;
    Map<String, Explanation> idExpl;
    Map<String, List<String>> posAnn;
    Map<String, List<String>> negAnn;
    Main main;

    public Train(Main m) {
        main = m;
        idQa = new HashMap<>();
        idExpl = new HashMap<>();
        posAnn = new HashMap<>();
        negAnn = new HashMap<>();
    }

    public void processExpl(List<String[]> lines) {
        for (String[] line : lines) {
            Explanation obj = new Explanation(line, true, main);
            idExpl.put(line[0], obj);
        }
    }

    public void processQA(List<String[]> lines) {
         for (String[] line : lines) {
             QA obj = new QA(line, true, main);
             idQa.put(line[0], obj);
        }
    }

    public void processPositiveQAExpl(List<String[]> lines) {
        for (String[] line : lines) {
            String quesID = line[0];
            String explID = line[1];
            List<String> annotations = posAnn.get(quesID);
            if (annotations == null) posAnn.put(quesID, annotations = new ArrayList<>());
            annotations.add(explID);
        }
    }

    public void generateNegativeQAExpl(int num) {
        for (String qa : posAnn.keySet()) {
            List<String> posExplIDs = posAnn.get(qa);

            List<String> explIDs = new ArrayList<>(idExpl.keySet());

            explIDs.removeAll(posExplIDs);

            List<String> negExplIDs = negAnn.get(qa);

            if (negExplIDs == null) {
                System.out.println(qa);
                System.exit(-1);
            }
            else if (negExplIDs.size() != 500) {
                System.out.println(negExplIDs.size());
                System.out.println(qa);
                System.exit(-1);
            }

            explIDs.removeAll(negExplIDs);

            List<Integer> range = IntStream.range(0, explIDs.size()).boxed().collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(range);

            for (int i = 0; i < num; i++) {
                String explID = explIDs.get(range.get(i));
                negExplIDs.add(explID);
            }
        }
    }

    public void setNegAnn(List<String[]> instances) {
        for (String[] instance : instances) {
            String qaID = instance[0];
            String explID = instance[1];

            List<String> posExplIDs = posAnn.get(qaID);
            if (posExplIDs.contains(explID)) continue;

            List<String> negExplIDs = negAnn.get(qaID);
            if (negExplIDs == null) negAnn.put(qaID, negExplIDs = new ArrayList<>());
            negExplIDs.add(explID);
        }
    }

    public int iterateExplanations(Integer qid, String qaID, List<String> explIDs, Sentence q, Sentence a, int rank, FileOutputStream output, FileOutputStream idLog) throws IOException {
        for (String explID : explIDs) {
            Explanation expl = idExpl.get(explID);
            Sentence explSent = expl.getSentence();

            String featureStr = Utils.getFeatureStr(main, q, a, explSent, expl.getSource()).replaceAll("\\s+", " ");

            String outputStr = qid != -1 ? (rank+" qid:"+qid+" "+featureStr) : (/*rank+" "+*/featureStr);
            output.write((outputStr+"\n").getBytes());

            if (idLog != null) idLog.write((qaID+"\t"+explID+"\n").getBytes());

            //if (rank==0 && qid != -1) rank--;

            if(rank > 1) rank--;
        }
        return rank;
    }

    public void writePosAndNegSelectInstances(FileOutputStream output, FileOutputStream idLog) throws IOException {
        int qid = 1;
        for (String qaID : posAnn.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            List<String> explIDs = posAnn.get(qaID);
            int rank = explIDs.size()+1;
            rank = iterateExplanations(qid, qaID, explIDs, quesSent, ansSent, rank, output, idLog);

            if (rank != 1) {
                System.out.println(qaID);
                System.out.println(rank);
                System.out.println("~strange [Train line 108]");
                System.exit(-1);
            }
            explIDs = negAnn.get(qaID);
            iterateExplanations(qid, qaID, explIDs, quesSent, ansSent, rank, output, idLog);

            if (qid > 0) qid++;
        }
    }

    public void writePosAndNegSelectInstances(FileOutputStream data, FileOutputStream query, FileOutputStream idLog) throws IOException {
        int qid = 1;
        for (String qaID : posAnn.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            List<String> explIDs = posAnn.get(qaID);
            int rank = explIDs.size();
            rank = iterateExplanations(-1, qaID, explIDs, quesSent, ansSent, rank, data, idLog);

            int total = explIDs.size();

            explIDs = negAnn.get(qaID);
            iterateExplanations(-1, qaID, explIDs, quesSent, ansSent, 0, data, idLog);

            total += explIDs.size();

            query.write((total+"\n").getBytes());

            qid++;
        }
    }

    public void writePosAndNegIDs(FileOutputStream output) throws IOException {
        for (String qaID : posAnn.keySet()) {

            List<String> explIDs = posAnn.get(qaID);
            for (String explID : explIDs) {
                output.write((qaID+"\t"+explID+"\t1\n").getBytes());
            }

            //negative explanation IDs
            explIDs = negAnn.get(qaID);
            for (String explID : explIDs) {
                output.write((qaID+"\t"+explID+"\t1\n").getBytes());
            }

        }
    }

    public void writeSelectedInstances(FileOutputStream output, Map<String, Map<String, Integer>> annotations) throws IOException {
        int qid = 1;
        for (String qaID : annotations.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            Map<String, Integer> subannotations = annotations.get(qaID);

            for (String explID : subannotations.keySet()) {
                Explanation expl = idExpl.get(explID);
                int rank = subannotations.get(explID);
                Sentence explSent = expl.getSentence();

                String featureStr = Utils.getFeatureStr(main, quesSent, ansSent, explSent, expl.getSource());
                String outputStr = rank + " qid:" + qid + " " + featureStr;
                output.write((outputStr + "\n").getBytes());
            }

            qid++;
        }
    }

    public void writeOutput(FileOutputStream output) throws IOException {
        int qid = 1;
        for (String qaID : posAnn.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            List<String> explIDs = posAnn.get(qaID);
            int rank = explIDs.size()+1;
            iterateExplanations(qid, qaID, explIDs, quesSent, ansSent, rank, output, null);

            if (rank != 1) {
                System.out.println("~strange");
                System.exit(-1);
            }
            for (String explID : idExpl.keySet()) {
                if (explIDs.contains(explID)) continue;

                Explanation expl = idExpl.get(explID);
                Sentence explSent = expl.getSentence();

                String featureStr = Utils.getFeatureStr(main, quesSent, ansSent, explSent, expl.getSource());

                String outputStr = rank+" qid:"+qid+" "+featureStr;
                output.write((outputStr+"\n").getBytes());
            }

            qid++;
        }
    }

}

