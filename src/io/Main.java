package io;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import english.Extractor;
import text.DictionaryFactory;
public class Main extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1 ;
	public static void main(String[] args) throws IOException {
		final Main mainEntry = new Main();
		mainEntry.frame = new JFrame("Extractie Date");

		mainEntry.frame.setLayout(new BorderLayout());


		mainEntry.alegere();
		mainEntry.Load();
		mainEntry.Dictionar();
		mainEntry.creareTabel(mainEntry);

		//
		//frame.getContentPane().add(mainEntry, "Center");
		//frame.setSize(mainEntry.getPreferredSize());
		//frame.setVisible(true);

		//JFrame f;
		//f = new JFrame();








	}
	// calea este hard-codata
	// puteti folosi un JFileChooser pentru a selecta fisierul
	private JFrame frame;
	private  final Vector < InregistrariObj > vInregistrari = new Vector < > ();
	private final  ArrayList < String > cuvinte = new ArrayList < String > ();
	private String sPath = null;

	// ++++++++ MEMBER FUNCTIONS +++++++++++

	public Main() {
		this.setPreferredSize();
	}

	public void alegere() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int returnValue = jfc.showOpenDialog(null);
		// int returnValue = jfc.showSaveDialog(null);
		{
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();

				sPath = selectedFile.getAbsolutePath();
			}
		}
	}

	public void creareTabel (Main m) throws IOException
	{
		String column[] = {
				"id",
				"cuvant",
				"parte de vorbire",
				"forma de baza",
				"ID-ul articolelor care il contin"
		};
		JTable jt = new JTable (Extractor.getData(m), column);
		JScrollPane sp = new JScrollPane(jt);
		frame.add(sp);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	public void Dictionar() {
		final DictionaryFactory dictFactory = new DictionaryFactory();
		dictFactory.Print(dictFactory.WordCount(vInregistrari));

		for (final String sCuvant: dictFactory.WordCount(vInregistrari).keySet()) {
			cuvinte.add(sCuvant);
		}
	}
	public  ArrayList < String > GetCuvinte() {
		return cuvinte;
	}

	public   Vector < InregistrariObj > getInregistrare() {
		return vInregistrari;
	}

	// +++++++++ MAIN +++++++++++

	public void Load() {
		// importam fisierul csv
		try (final Reader in = new FileReader(sPath)) {
			final Iterable < CSVRecord > records = CSVFormat
					.RFC4180
					.withFirstRecordAsHeader()
					// .withHeader("id", "eMail", "Autori", "Title", "Affilation")
					.parse( in );
			String data[][] = new String[5555][5];
			int i = 0;
			for (final CSVRecord record: records) {
				final String sID = record.get("ID");
				final int id = Integer.parseInt(sID);
				final String sEMail = record.get("eMail");
				data[i][0] = sID;
				data[i][1] = sEMail;
				final String sAutori = record.get("Autori");
				data[i][2] = sAutori;
				final String sTitle = record.get("Title");
				data[i][3] = sTitle;
				final String sAffiliation = record.get("Affiliation");
				data[i][4] = sAffiliation;
				vInregistrari.add(new InregistrariObj(id, sEMail, sAutori, sTitle, sAffiliation));
				i++;

				// TODO: salvat datele citite intr-o structura de date
				if (sEMail != null && !sEMail.isEmpty()) {
					System.out.println(sEMail);
				}
			}
			String column[] = {
					"ID",
					"Email",
					"Autor",
					"Titlu",
					"Affiliation"
			};
			JTable jt = new JTable(data, column);
			JFrame f;
			f = new JFrame();
			JScrollPane sp = new JScrollPane(jt);
			f.add(sp);
			f.setSize(1000, 700);
			f.setVisible(true);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	public void setPreferredSize() {
		super.setPreferredSize(new Dimension(600, 600));
	}








}