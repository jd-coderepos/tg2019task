package markup;

import main.Main;
import utils.NLP;

import java.util.List;

/**
 * @author jld
 */
public class Explanation {

    String id;
    Sentence sentence;
    String source;

    public Explanation(String id, String source) {
        this.id = id;
        this.source = source;
    }

    public Explanation(String[] line, boolean train, Main main) {
        this.id = line[0];
        this.source = line[2];

        this.sentence = new Sentence();
        if (NLP.frames1_7.containsKey(this.id)) {
            String frameStr = NLP.frames1_7.get(this.id);
            this.sentence.setFrames1_7(frameStr, train, main);
        }
        if (NLP.frames1_5.containsKey(this.id)) {
            String frameStr = NLP.frames1_5.get(this.id);
            this.sentence.setFrames1_5(frameStr, train, main);
        }
        this.sentence.setTokens(line[1].split("\\s"), train, main);
        this.sentence.setRelations(line.length > 3 ? line[3] : null, train, main);

        if (train) {
            main.getTs().setTablestore(source);
        }
    }

    public String getId() {
        return id;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public String getSource() {
        return source;
    }

}
