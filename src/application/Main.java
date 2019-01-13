package application;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;

import model.Signal;
import model.XbeeListener;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
//import javafx.scene.control.Separator;
//import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.text.DecimalFormat;

//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;

public class Main extends Application {
//	Row row = null;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
//			root.setTop(createToolbar());
			root.setCenter(createMainContent());
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Acquisition de données");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

//	private Node createToolbar() {
//		return new ToolBar(new Button("New"), new Separator(), new Button("Clear"));
//	}

	private Node createMainContent() throws XBeeException, IOException {
		Group g = new Group();

		// Initialisation de l'interface
		Button button_start_haut = new Button("Start : en haut");
		Button button_start_bas = new Button("Start : en bas");
		Button button_start_droite = new Button("Start : à droite");
		Button button_start_gauche = new Button("Start : à gauche");
		Button button_stop = new Button("Stop");

		GridPane gridPane = new GridPane();

		gridPane.setMinSize(400, 200);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		gridPane.setAlignment(Pos.CENTER);

		gridPane.add(button_start_haut, 0, 0);
		gridPane.add(button_start_bas, 0, 10);
		gridPane.add(button_start_gauche, 0, 20);
		gridPane.add(button_start_droite, 0, 30);
		gridPane.add(button_stop, 10, 0);

		g.getChildren().add(gridPane);

		// Initialisation du module XBee récepteur pour l'acquisition des données
		XBee xbee = new XBee();
		Signal acc_x = new Signal();
		Signal acc_y = new Signal();
		Signal acc_z = new Signal();
		Signal emg = new Signal();
		XbeeListener data = new XbeeListener(xbee, acc_x, acc_y, acc_z, emg);

		int ligne = 1;

//		DecimalFormat df = new DecimalFormat("#.##");

		// Création fichier Excel pour enregistrement des données
//		FileOutputStream database = new FileOutputStream("database.xls");
//		Workbook wb = new HSSFWorkbook();
//		Sheet Sheet1 = wb.createSheet();

//		row = Sheet1.createRow(0);

//		Sheet1.setColumnWidth((short) 0, (short) (20 * 256));
//		Sheet1.setColumnWidth((short) 1, (short) (20 * 256));
//		Sheet1.setColumnWidth((short) 2, (short) (20 * 256));
//		Sheet1.setColumnWidth((short) 3, (short) (20 * 256));
//		Sheet1.setColumnWidth((short) 4, (short) (20 * 256));

//		row.createCell(0).setCellValue("Mouvement effectué");
//		row.createCell(1).setCellValue("Accélération selon x");
//		row.createCell(2).setCellValue("Accélération selon y");
//		row.createCell(3).setCellValue("Accélération selon z");
//		row.createCell(4).setCellValue("Donnée EMG");

		// Acquisition des données à l'appui d'un des boutons start
		button_start_haut.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				System.out.println("Début de l'acquisition");
//				row = Sheet1.createRow(k);
//				row.createCell(0).setCellValue("Haut");
				try {
					data.start_acquisition_xbee(1, ligne);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		button_start_bas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				System.out.println("Début de l'acquisition");
//				row = Sheet1.createRow(k);
//				row.createCell(0).setCellValue("Bas");
				try {
					data.start_acquisition_xbee(2, ligne);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		button_start_gauche.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				System.out.println("Début de l'acquisition");
//				row = Sheet1.createRow(k);
//				row.createCell(0).setCellValue("Gauche");
				try {
					data.start_acquisition_xbee(3, ligne);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		button_start_droite.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				System.out.println("Début de l'acquisition");
//				row = Sheet1.createRow(k);
//				row.createCell(0).setCellValue("Droite");
				try {
					data.start_acquisition_xbee(4, ligne);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		// Fin de l'acquisition à l'appui du bouton stop
		button_stop.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				System.out.println("Fin de l'acquisition");
				data.stop_acquisition_xbee();
//				int i = 0;
//				int ktemp = k;
//				while (k < ktemp + data.acc_x.get_size()) {
//					for (i = 0; i < data.acc_x.get_size(); i++) {
//						row.createCell(1).setCellValue(data.emg.get_echantillon(i));
//						row.createCell(2).setCellValue(df.format(data.acc_x.get_echantillon(i)));
//						row.createCell(3).setCellValue(df.format(data.acc_y.get_echantillon(i)));
//						row.createCell(4).setCellValue(df.format(data.acc_z.get_echantillon(i)));
//						row = Sheet1.createRow(k);
//						k++;
//					}
//				}
			}
		});

//		wb.write(database);
//		database.close();
//		wb.close();

		return g;
	}

}
