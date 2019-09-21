package ling;

import main.Main;
import markup.Sentence;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jld
 */
public class Utils {

    public static String getFeatureStr(Main main, Sentence q, Sentence a, Sentence expl, String source) {
        return main.getLemma().toSVMRankString(q, a, expl)+" "+
                main.getTs().toSVMRankString(source)+" "+
                main.getAffix().toSVMRankString(q, a, expl)+" "+
                main.getCon().toSVMRankString(q, a, expl)+" "+
                main.getOpenRel().toSVMRankString(q, a, expl)+" "+
                main.getCnrel().toSVMRankString(q, a, expl)+" "+
                main.getWikicat().toSVMRankString(q, a, expl)+" "+
                main.getWikit().toSVMRankString(q, a, expl)+" "+
                main.getFrameNet1_7().toSVMRankString(q, a, expl)+" "+
                main.getFrameNet1_5().toSVMRankString(q, a, expl);
    }

    public static String getFeature(int start, Map<String, Integer> globalfeatures, List<String> localfeatures, int end) {
        String featureStr = "";
        Set<Integer> indexes = new HashSet<>();
        for (String localfeature : localfeatures) {
            int index = globalfeatures.containsKey(localfeature) ? globalfeatures.get(localfeature)+1+start : end;
            indexes.add(index);
        }
        List<Integer> list = new ArrayList<>(indexes);
        Collections.sort(list);
        for (int index : list) {
            featureStr += index+":1 ";
        }
        //featureStr = featureStr.trim();
        return featureStr.trim();
    }

    public static String getFeature(int start, Map<String, Integer> globalfeatures, Map<String, String> localfeatures, int end) {
        String featureStr = "";

        if (localfeatures == null || localfeatures.isEmpty()) return end+":1";

        Map<String, Integer> globalfeaturescopy = new HashMap<>(globalfeatures);
        globalfeaturescopy.keySet().retainAll(localfeatures.keySet());

        Map<String, Integer> sortedMap = globalfeaturescopy.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (String v : sortedMap.keySet()) {
            int index = sortedMap.get(v)+start+1;
            featureStr += index+":"+localfeatures.get(v)+" ";
        }

        return featureStr.trim();
    }

    public static String getFeature(int start, List<String> globalfeatures, List<String> localfeatures, int end) {
        String featureStr = "";

        if (localfeatures == null || localfeatures.isEmpty()) return end+":1";

        List<Integer> indexes = new ArrayList<>();
        for (String feat : localfeatures) {
            int index = globalfeatures.contains(feat) ? globalfeatures.indexOf(feat)+1+start : end;
            if (!indexes.contains(index)) indexes.add(index);
        }

        Collections.sort(indexes);
        for (int index : indexes) {
            featureStr += index+":1 ";
        }
        return featureStr.trim();
    }

    public static String getFeature(int start, List<String> globalfeatures, String feature, int end) {
        int index = globalfeatures.contains(feature) ? globalfeatures.indexOf(feature)+1+start : end;
        return index+":1";
    }

    public static List<String> getCommon(List<String> terms1, List<String> terms2) {
        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        terms1.retainAll(terms2);
        return terms1;
    }

    public static Map<String, Integer> getCommon(Map<String, Integer> terms1, Map<String, Integer> terms2) {
        terms1 = new HashMap<>(terms1);
        terms2 = new HashMap<>(terms2);
        terms1.keySet().retainAll(terms2.keySet());
        return terms1;
    }

    public static List<String> getGroup(List<String> terms1, List<String> terms2) {
        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        for (String term : terms2) {
            if (!terms1.contains(term)) terms1.add(term);
        }
        return terms1;
    }

    public static Map<String, Integer> getGroup(Map<String, Integer> terms1, Map<String, Integer> terms2) {
        terms1 = new HashMap<>(terms1);
        terms2 = new HashMap<>(terms2);
        terms1.putAll(terms2);
        return terms1;
    }

    public static List<String> getList(List<String[]> lines) {
        List<String> list = new ArrayList<>();
        for (String[] line : lines) {
            list.add(line[0]);
        }
        return list;
    }

}
