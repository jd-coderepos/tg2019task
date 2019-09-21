package main;

import ling.*;
import utils.IO;
import utils.NLP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author jld
 */
public class Main {

    //Features
    Lemma lemma;

    TableStore ts;

    Affix affix;

    Concepts con;
    OpenIERel openRel;

    ConceptNetRelations cnrel;

    WikiCategories wikicat;

    WikiTitles wikit;

    FrameNet1_7 frameNet1_7;

    FrameNet1_5 frameNet1_5;

    WordNet wn;

    public Main() {
        lemma = new Lemma();

        ts = new TableStore();

        affix = new Affix();

        con = new Concepts();
        openRel = new OpenIERel();

        cnrel = new ConceptNetRelations();

        wikicat = new WikiCategories();

        wikit = new WikiTitles();

        frameNet1_7 = new FrameNet1_7();

        frameNet1_5 = new FrameNet1_5();

        wn = new WordNet();
    }

    public Lemma getLemma() {
        return lemma;
    }

    public TableStore getTs() {
        return ts;
    }

    public Affix getAffix() {
        return affix;
    }

    public Concepts getCon() {
        return con;
    }

    public OpenIERel getOpenRel() {
        return openRel;
    }

    public ConceptNetRelations getCnrel() {
        return cnrel;
    }

    public WikiCategories getWikicat() {
        return wikicat;
    }

    public WikiTitles getWikit() {
        return wikit;
    }

    public FrameNet1_7 getFrameNet1_7() {
        return frameNet1_7;
    }

    public FrameNet1_5 getFrameNet1_5() {
        return frameNet1_5;
    }

    public WordNet getWordNet() {
        return wn;
    }

    public void initTestSetup(String data_dir, String feat_grp) throws IOException {
        Test test = new Test(this);
        String qa_file = "Elem-Test-Rel.csv";
        test.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        String expl_file = "expl-tablestore-rel.csv";
        test.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        String output_file = "test-"+feat_grp+".dat";
        String id_file = "test-ids.txt";
        //test.writeOutput(new FileOutputStream(data_dir+"\\"+output_file), new FileOutputStream(data_dir+"\\"+id_file));
        test.writeOutput(new FileOutputStream(data_dir+"\\"+output_file), null);

        System.out.println("Done writing Test data!");
    }

    public void initDevSetup(String data_dir, String feat_grp) throws IOException {
        Dev dev = new Dev(this);
        String qa_file = "Elem-Dev-Rel.csv";
        dev.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        String expl_file = "expl-tablestore-rel.csv";
        dev.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        String gold_data = "Elem-Dev-Expl.csv";
        dev.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
        String output_file = "dev-"+feat_grp+".dat";
        String id_file = "dev-ids-"+feat_grp+".txt";
        //dev.writeOutput(new FileOutputStream(data_dir+"\\"+output_file), new FileOutputStream(data_dir+"\\"+id_file));
        dev.writeOutput(new FileOutputStream(data_dir+"\\"+output_file), null);

        System.out.println("Done writing Dev data!");
    }

    public void initTrainSetup(String data_dir, int numNeg, String feat_grp) throws IOException {
        Train train = new Train(this);
        String qa_file = "Elem-Train-Rel.csv";
        train.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        String expl_file = "expl-tablestore-rel.csv";
        train.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        lemma.setFeatureSizes(0);
        ts.setFeatureSizes(lemma.getLastSize());
        affix.setFeatureSizes(ts.getLastSize());
        con.setFeatureSizes(affix.getLastSize());
        openRel.setFeatureSizes(con.getLastSize());
        cnrel.setFeatureSizes(openRel.getLastSize());
        wikicat.setFeatureSizes(cnrel.getLastSize());
        wikit.setFeatureSizes(wikicat.getLastSize());
        frameNet1_7.setFeatureSizes(wikit.getLastSize());
        frameNet1_5.setFeatureSizes(frameNet1_7.getLastSize());

        System.out.println("Done generating training features!");

        String gold_data = "Elem-Train-Expl.csv";
        train.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
        String output_file = "train-"+1000+"-"+feat_grp+".dat";
        String id_file = "train-1000-ids.txt";

        train.setNegAnn(IO.readCSV(data_dir+"\\"+id_file, '\t', 0));
        //train.generateNegativeQAExpl(numNeg);
        //train.writePosAndNegIDs(new FileOutputStream(data_dir+"\\train-1000-ids.txt"));
        train.writePosAndNegSelectInstances(new FileOutputStream(data_dir+"\\"+output_file),
                /*new FileOutputStream(data_dir+"\\"+id_file)*/   null);

        System.out.println("Done writing training data!");

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -jar tg2019task.jar <data_dir>");
            System.exit(-1);
        }

        String data_dir = args[0];

        NLP.setStopwords(IO.readFile(data_dir+"/resources/stopwords", StandardCharsets.UTF_8).split("\\n"));

        NLP.setConcepts(IO.readCSV(data_dir+"/resources/conceptnet/concepts.txt", '\t', 0));
        NLP.setConceptRelations(IO.readCSV(data_dir+"/resources/conceptnet/wordtriples.txt", '\t', 0));

        NLP.setWordNet(IO.readFile(data_dir+"/resources/WordNet.txt", StandardCharsets.UTF_8).split("\\n"));

        NLP.setWikiCategories(IO.readCSV(data_dir+"/resources/wiki/concept-categories.txt", '\t', 0));
        NLP.setWikiTitles(IO.readCSV(data_dir+"/resources/wiki/concept-search-titles.txt", '\t', 0));

        NLP.setFrames1_7(IO.readFile(data_dir+"/resources/framenet/predicted-args-train.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_7(IO.readFile(data_dir+"/resources/framenet/predicted-args-dev.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_7(IO.readFile(data_dir+"/resources/framenet/predicted-args-test.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_7(IO.readFile(data_dir+"/resources/framenet/predicted-args-expl.txt", StandardCharsets.UTF_8).split("\\n"));

        NLP.setFrames1_5(IO.readFile(data_dir+"/resources/framenet/predicted-mateplus-train.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_5(IO.readFile(data_dir+"/resources/framenet/predicted-mateplus-dev.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_5(IO.readFile(data_dir+"/resources/framenet/predicted-mateplus-test.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_5(IO.readFile(data_dir+"/resources/framenet/predicted-mateplus-expl.txt", StandardCharsets.UTF_8).split("\\n"));

        Main main = new Main();

        String feat_grp = "framenet";
        main.initTrainSetup(data_dir, 500, feat_grp);
        main.initDevSetup(data_dir, feat_grp);
        main.initTestSetup(data_dir, feat_grp);
    }
}
