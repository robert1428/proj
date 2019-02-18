package text;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import io.InregistrariObj;

public class DictionaryFactory {

	public void Print(final Map<String, Integer> mapCuvinte) {
		for (final Map.Entry<String, Integer> entry : mapCuvinte.entrySet()) {
			System.out.println(entry.getValue() + "\t" + entry.getKey());

		}
	}

	public TreeMap<String, Integer> WordCount(final Vector<InregistrariObj> vInregistrari) {
		final TreeMap<String, Integer> mapCuvinte = new TreeMap<>();

		for (final InregistrariObj inregistrare : vInregistrari) {
			final String sTitlu = inregistrare.sTitlu;
			if (sTitlu == null) {
				continue;
			}

			// impartim pe cuvinte
			final String[] arrCuvinte = sTitlu.split("[^\\p{L}0-9]"); // TODO

			for (final String sCuvantOrig : arrCuvinte) {
				if (sCuvantOrig == null || sCuvantOrig.isEmpty()) {
					continue;
				}

				final String sCuvant = sCuvantOrig.toLowerCase();

				// verificam daca cuvantul exista deja in Dictionar
				// if(mapCuvinte.containsKey(sCuvant) ) {}
				final Integer count = mapCuvinte.get(sCuvant);
				if (count == null) {
					mapCuvinte.put(sCuvant, 1);
				} else {
					mapCuvinte.put(sCuvant, count + 1);
				}
			}
		}

		return mapCuvinte;
	}
}
