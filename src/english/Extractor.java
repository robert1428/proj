package english;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import io.InregistrariObj;
import io.Main;

public class Extractor {
	public static boolean containsDigit(String s) {
		char[] chars = s.toCharArray();
		for (char c : chars) {
			if (Character.isDigit(c)) {
				return true;
			}
		}
		return false;
	}

	public static String[][] getData(Main m) throws IOException {
		int i;
		edu.mit.jwi.Dictionary dict = new edu.mit.jwi.Dictionary(new File("lib/dict"));
		dict.open();
		WordnetStemmer stemmer = new WordnetStemmer(dict);
		ArrayList<String> cuv = new ArrayList<String>();
		for (i = 0; i <m.GetCuvinte().size(); i++) {
			cuv.add(m.GetCuvinte().get(i));
		}

		Iterator<String> iter = cuv.iterator();
		while (iter.hasNext()) {
			String str = iter.next();

			if (containsDigit(str) || str.length() <= 1) {
				iter.remove();
			}
		}
		String[] test = cuv.toArray(new String[cuv.size()]);

		ArrayList<String> substantive = new ArrayList<String>(Extractor.getNouns(test));
		ArrayList<String> verbe = new ArrayList<String>(Extractor.getVerbs(test));

		String table2[][] = new String[test.length][5];
		int l = 0;
		for (i = 0; i < cuv.size(); i++) {

			if (substantive.contains(cuv.get(i))) {
				table2[l][0] = String.valueOf(l + 1);
				table2[l][1] = cuv.get(i);
				table2[l][2] = "substantiv";

				String joined = String.join(" and ", stemmer.findStems(cuv.get(i), POS.NOUN));
				table2[l][3] = joined;
				l++;

			} else if (verbe.contains(cuv.get(i))) {
				table2[l][0] = String.valueOf(l + 1);
				table2[l][1] = cuv.get(i);
				table2[l][2] = "verb";
				String joined = String.join(" and ", stemmer.findStems(cuv.get(i), POS.VERB));
				table2[l][3] = joined;
				l++;

			}

		}
		String it;
		for (i = 0; i < table2.length; i++) {
			table2[i][4] = "";
		}

		for (InregistrariObj x : m.getInregistrare()) {
			String k = x.sTitlu;
			if (k == null) {
				continue;
			}

			final String[] arrCuvinte = k.split("[^\\p{L}0-9]"); // TODO

			for (final String sCuvantOrig : arrCuvinte) {
				if (sCuvantOrig == null || sCuvantOrig.isEmpty()) {
					continue;
				}

				final String sCuvant = sCuvantOrig.toLowerCase();

				for (i = 0; i < table2.length; i++) {
					it = table2[i][1];
					if (sCuvant.equals(it)) {
						if (!table2[i][3].contains(String.valueOf(x.id))) {
							table2[i][4] = table2[i][4] + x.id + " ";
						}
					}

				}

			}
		}
		return table2;
	}

	public static ArrayList<String> getNouns(String[] cuv) {
		Properties props = new Properties();

		props.setProperty("annotators", "tokenize, ssplit, pos");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		String words = Arrays.toString(cuv);

		Annotation document = new Annotation(words);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		ArrayList<String> nouns = new ArrayList<String>();
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				if (pos.equals("NN") || pos.equals("NNS")) {
					nouns.add(word);
				}

			}

		}

		return nouns;
	}

	public static ArrayList<String> getVerbs(String[] cuv) {
		Properties props = new Properties();

		props.setProperty("annotators", "tokenize, ssplit, pos");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		String words = Arrays.toString(cuv);

		Annotation document = new Annotation(words);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		ArrayList<String> verbs = new ArrayList<String>();
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String pos = token.get(PartOfSpeechAnnotation.class);
				if (pos.equals("VB") || pos.equals("VBZ") || pos.equals("VBN") || pos.equals("VBP") || pos.equals("VBG")
						|| pos.equals("VBD")) {
					verbs.add(word);
				}

			}

		}

		return verbs;
	}
}
