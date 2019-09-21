package main;

import ling.Utils;
import markup.Explanation;
import markup.QA;
import markup.Sentence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Test {

    Map<String, QA> idQa;
    Map<String, Explanation> idExpl;
    Main main;

    public Test(Main m) {
        main = m;
        idQa = new HashMap<>();
        idExpl = new HashMap<>();
    }

    public void processExpl(List<String[]> lines) {
        for (String[] line : lines) {
            Explanation obj = new Explanation(line, false, main);
            idExpl.put(line[0], obj);
        }
    }

    public void processQA(List<String[]> lines) {
        for (String[] line : lines) {
            QA obj = new QA(line, false, main);
            idQa.put(line[0], obj);
        }
    }

    public void writeOutput(FileOutputStream output, FileOutputStream idLog) throws IOException {
        int qid = 1;
        for (String qaID : idQa.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            for (String explID : idExpl.keySet()) {
                Explanation expl = idExpl.get(explID);
                Sentence explSent = expl.getSentence();

                String featureStr = Utils.getFeatureStr(main, quesSent, ansSent, explSent, expl.getSource()).replaceAll("\\s+", " ");

                String outputStr = "1 qid:"+qid+" "+featureStr;
                output.write((outputStr+"\n").getBytes());
                if (idLog != null) idLog.write((qaID+"\t"+explID+"\n").getBytes());
            }

            if (qid > 0) qid++;
        }
    }


}
