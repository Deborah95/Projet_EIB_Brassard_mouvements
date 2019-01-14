package model;

import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.wpan.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

public class XbeeListener {
	public Signal acc_x;
	public Signal acc_y;
	public Signal acc_z;
	public Signal emg;

	XBee xbee;
	PacketListener packetListener;

	// Contructeur : initialisation du module XBee et des sorties
	public XbeeListener(XBee xbee, Signal acc_x, Signal acc_y, Signal acc_z, Signal emg) throws XBeeException {
		this.xbee = xbee;
		this.xbee.open("COM9", 9600); // Initialisation du port d'�coute pour la transmission de donn�es : modifier le
										// num�ro du port selon les cas
		this.acc_x = acc_x;
		this.acc_y = acc_y;
		this.acc_z = acc_z;
		this.emg = emg;
	}

	// M�thode pour l'acquisition des donn�es
	public void start_acquisition_xbee() {

		this.packetListener = new PacketListener() {
			public void processResponse(XBeeResponse response) {
				System.out.println("Re�u");
				if (response.getApiId() == ApiId.RX_16_IO_RESPONSE || response.getApiId() == ApiId.RX_64_RESPONSE) {
					RxResponseIoSample ioSample = (RxResponseIoSample) response;

					for (IoSample sample : ioSample.getSamples()) {

						double Ax = sample.getAnalog1(); // Valeur analogique lue en sortie du XBee
						double tension_x = Ax * 3.3 / 1023; // Valeur tension (en V)

						double Ay = sample.getAnalog2();
						double tension_y = Ay * 3.3 / 1023;

						double Az = sample.getAnalog3();
						double tension_z = Az * 3.3 / 1023;

						double m = sample.getAnalog0();
						// double tension_m = m*3.3/1023;

						// Acc�l�ration selon x, y et z
						double acceleration_x = (tension_x - 1.65) / 0.34;
						double acceleration_y = (tension_y - 1.65) / 0.34;
						double acceleration_z = (tension_z - 1.65) / 0.34;

						// Capteur EMG
						// double donnee_emg;

						// System.out.println("Acc�l�ration selon x Ax = " + acceleration_x + "g");
						// System.out.println("Acc�l�ration selon y Ay = " + acceleration_y + "g");
						// System.out.println("Acc�l�ration selon z Az = " + acceleration_z + "g");

						// Enregistrement des donn�es de chaque sortie dans le vecteur echantillon de
						// Signal
						acc_x.add_echantillon_end(acceleration_x);
						acc_y.add_echantillon_end(acceleration_y);
						acc_z.add_echantillon_end(acceleration_z);
						emg.add_echantillon_end(m);
					}
				}
			}
		};
		xbee.addPacketListener(packetListener);
	}

	// M�thode pour arr�ter l'acquisition des donn�es
	public int stop_acquisition_xbee(int mouvement, int ligne) throws IOException {
		xbee.removePacketListener(packetListener);
		
		// Cr�ation fichier Excel pour enregistrement des donn�es
		FileOutputStream database = new FileOutputStream("database.xls");
		Workbook wb = new HSSFWorkbook();
		Sheet Sheet1 = wb.createSheet();

		Row row = Sheet1.createRow(0);

		Sheet1.setColumnWidth((short) 0, (short) (20 * 256));
		Sheet1.setColumnWidth((short) 1, (short) (20 * 256));
		Sheet1.setColumnWidth((short) 2, (short) (20 * 256));
		Sheet1.setColumnWidth((short) 3, (short) (20 * 256));
		Sheet1.setColumnWidth((short) 4, (short) (20 * 256));

		row.createCell(0).setCellValue("Mouvement effectu�");
		row.createCell(1).setCellValue("Acc�l�ration selon x");
		row.createCell(2).setCellValue("Acc�l�ration selon y");
		row.createCell(3).setCellValue("Acc�l�ration selon z");
		row.createCell(4).setCellValue("Donn�e EMG");

		// Remplissage de la 1�re colonne selon le mouvement effectu� (utile pour
		// l'apprentissage)
		if (mouvement == 1) {
			row = Sheet1.createRow(ligne);
			row.createCell(0).setCellValue("Haut");
		} else if (mouvement == 2) {
			row = Sheet1.createRow(ligne);
			row.createCell(0).setCellValue("Bas");
		} else if (mouvement == 3) {
			row = Sheet1.createRow(ligne);
			row.createCell(0).setCellValue("Gauche");
		} else if (mouvement == 4) {
			row = Sheet1.createRow(ligne);
			row.createCell(0).setCellValue("Droite");
		}
		
		DecimalFormat df = new DecimalFormat("#.##"); // Permet de formater les donn�es avec 2 d�cimales
		
		// Remplissage des 4 colonnes associ�es aux 4 sorties des capteurs (3 pour
		// l'acc�l�rom�tre et 1 pour l'EMG)
		//int ltemp = ligne;
		int i = 0;
		int lignetemp = ligne;
		while (ligne < lignetemp + acc_x.get_size()) {
			for (i = acc_x.get_size()-lignetemp; i < acc_x.get_size(); i++) {
				row.createCell(4).setCellValue(emg.get_echantillon(i));
				row.createCell(2).setCellValue(df.format(acc_x.get_echantillon(i)));
				row.createCell(3).setCellValue(df.format(acc_y.get_echantillon(i)));
				row.createCell(1).setCellValue(df.format(acc_z.get_echantillon(i)));
				ligne++;
				row = Sheet1.createRow(ligne);
			}
		}

		wb.write(database);
		database.close();
		wb.close();
		
		return ligne;
	}

}
