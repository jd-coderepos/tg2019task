package markup;

import main.Main;
import utils.NLP;

/**
 * @author jld
 */
public class QA {

    String qaID;
    Sentence question;
    String correctAnsOption;
    Sentence correctAns;

    public QA(String[] line, boolean train, Main main) {
        this.qaID = line[0];
        this.question = new Sentence();
        if (NLP.frames1_7.containsKey(this.qaID)) {
            String frameStr = NLP.frames1_7.get(this.qaID);
            this.question.setFrames1_7(frameStr, train, main);
        }
        if (NLP.frames1_5.containsKey(this.qaID)) {
            String frameStr = NLP.frames1_5.get(this.qaID);
            this.question.setFrames1_5(frameStr, train, main);
        }
        this.question.setTokens(line[1].split("\\s"), train, main);
        this.question.setRelations(line.length > 3 ? line[3] : null, train, main);

        this.correctAns = new Sentence();
        this.correctAns.setTokens(line[2].split("\\s"), train, main);
        this.correctAns.setRelations(line.length > 4 ? line[4] : null, train, main);
    }

    public int getCorrectAns(String[] tokens) {
        if (correctAnsOption.equals("A")) return 3;
        else if (correctAnsOption.equals("B")) return 4;
        else if (correctAnsOption.equals("C")) return 5;
        else return 6;
    }

    public String getQaID() {
        return qaID;
    }

    public Sentence getQuestion() {
        return question;
    }

    public String getCorrectAnsOption() {
        return correctAnsOption;
    }

    public Sentence getCorrectAns() {
        return correctAns;
    }

}
