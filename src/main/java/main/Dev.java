package main;

import ling.Utils;
import markup.Explanation;
import markup.QA;
import markup.Sentence;
import utils.NLP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author jld
 */
public class Dev {

    Map<String, QA> idQa;
    Map<String, Explanation> idExpl;
    Map<String, List<String>> posAnn;
    Map<String, List<String>> negAnn;
    Main main;

    public Dev(Main m) {
        main = m;
        idQa = new HashMap<>();
        idExpl = new HashMap<>();
        posAnn = new HashMap<>();
        negAnn = new HashMap<>();
    }

    public void processExpl(List<String[]> lines) {
        for (String[] line : lines) {
            Explanation obj = new Explanation(line, false, main);
            //Explanation obj = new Explanation(line[0], line[2]);
            //obj.setTokens(line[1].split("\\s"), false, main);
            idExpl.put(line[0], obj);
        }
    }

    public void processQA(List<String[]> lines) {
        for (String[] line : lines) {
            QA obj = new QA(line, false, main);
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

    public int iterateExplanations(Integer qid, String qaID, List<String> explIDs, Sentence q, Sentence a, int rank, FileOutputStream output, FileOutputStream idLog) throws IOException {
        for (String explID : explIDs) {
            Explanation expl = idExpl.get(explID);
            Sentence explSent = expl.getSentence();

            String featureStr = Utils.getFeatureStr(main, q, a, explSent, expl.getSource()).replaceAll("\\s+", " ");

            String outputStr = qid != -1 ? (rank+" qid:"+qid+" "+featureStr) : (/*rank+" "+*/featureStr);
            output.write((outputStr+"\n").getBytes());
            if (idLog != null) idLog.write((qaID+"\t"+explID+"\n").getBytes());

            if(rank > 1) rank--;
        }
        return rank;
    }

    public void writeOutput(FileOutputStream output, FileOutputStream idLog) throws IOException {
        int qid = 1;
        for (String qaID : posAnn.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            List<String> explIDs = posAnn.get(qaID);
            int rank = explIDs.size()+1;
            //System.out.println(qaID);
            //System.out.println(explIDs);
            rank = iterateExplanations(qid, qaID, explIDs, quesSent, ansSent, rank, output, idLog);

            if (rank != 1) {
                System.out.println(qaID);
                System.out.println(rank);
                System.out.println("~strange [Dev line 88]");
                System.exit(-1);
            }
            for (String explID : idExpl.keySet()) {
                if (explIDs.contains(explID)) continue;

                Explanation expl = idExpl.get(explID);
                Sentence explSent = expl.getSentence();

                String featureStr = Utils.getFeatureStr(main, quesSent, ansSent, explSent, expl.getSource()).replaceAll("\\s+", " ");

                String outputStr = rank+" qid:"+qid+" "+featureStr;
                output.write((outputStr+"\n").getBytes());
                if (idLog != null) idLog.write((qaID+"\t"+explID+"\n").getBytes());
            }

            if (qid > 0) qid++;
        }
    }

    public void writeOutput(FileOutputStream data, FileOutputStream query, FileOutputStream idLog) throws IOException {
        int qid = 1;
        for (String qaID : posAnn.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            List<String> explIDs = posAnn.get(qaID);
            int rank = explIDs.size();
            rank = iterateExplanations(-1, qaID, explIDs, quesSent, ansSent, rank, data, idLog);

            int total = explIDs.size();

            for (String explID : idExpl.keySet()) {
                if (explIDs.contains(explID)) continue;

                total++;

                Explanation expl = idExpl.get(explID);
                Sentence explSent = expl.getSentence();

                String featureStr = Utils.getFeatureStr(main, quesSent, ansSent, explSent, expl.getSource()).replaceAll("\\s+", " ");

                String outputStr = qid != -1 ? ("0 qid:"+qid+" "+featureStr) : ("0 "+featureStr);
                data.write((outputStr+"\n").getBytes());
                if (idLog != null) idLog.write((qaID+"\t"+explID+"\n").getBytes());
            }

            query.write((total+"\n").getBytes());

            qid++;
        }
    }

}
