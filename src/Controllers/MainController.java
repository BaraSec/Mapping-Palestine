package Controllers;

import Data_Structures.Edge;
import Data_Structures.Graph;
import Data_Structures.Vertex;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;


public class MainController
{
	// Data fields
	@FXML
	private TextField stat;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private ImageView map;

	@FXML
	private JFXComboBox<String> from;

	@FXML
	private JFXComboBox<String> to;

	@FXML
	private JFXButton find;

	@FXML
	private JFXTextField distance;

	@FXML
	private JFXToggleButton mirror;

	@FXML
	private JFXButton copy;

	@FXML
	private JFXButton screenshot;

	@FXML
	private JFXButton export;

	@FXML
	private JFXButton resetUI;

	@FXML
	private JFXButton switchVals;

	@FXML
	private TableView<ObservableList> sPtable;

	@FXML
	private JFXRadioButton dijAlgo;

	@FXML
	private JFXRadioButton aAlgo;

	@FXML
	private JFXColorPicker srcColor;

	@FXML
	private JFXColorPicker distColor;

	@FXML
	private JFXColorPicker midColor;

	@FXML
	private JFXColorPicker lineColor;

	@FXML
	private JFXTextField time;

	@FXML
	private JFXButton resetColors;

	private boolean isClickedTwice = false;
	private Circle circleReference = new Circle();
	private Graph graph = new Graph();
	private String shoPath;
	private boolean switchClicked;
	private ObservableList<ObservableList> data;
	ToggleGroup algos = new ToggleGroup();
	Color srcColorVal = Color.BROWN, distColorVal = Color.LIGHTGREEN, midColorVal = Color.ORANGE, lineColorVal = Color.RED;

	// A method to initialize the UI objects
	public void initialize()
	{
		dijAlgo.setToggleGroup(algos);
		dijAlgo.setSelected(true);
		dijAlgo.setUserData("Dijkstra's Algorithm");
		aAlgo.setToggleGroup(algos);
		aAlgo.setSelected(false);
		aAlgo.setUserData("A* Search Algorithm");
		aAlgo.setSelectedColor(Color.web("#750000"));
		dijAlgo.setSelectedColor(Color.web("#750000"));

		srcColor.setValue(srcColorVal);
		distColor.setValue(distColorVal);
		midColor.setValue(midColorVal);
		lineColor.setValue(lineColorVal);

		initialize_table();

		find.setDisable(true);
		copy.setDisable(true);
		screenshot.setDisable(true);
		export.setDisable(true);

		readData();

		mirror.setTooltip(
				new Tooltip("Toggle Automation")
		);

		anchorPane.setPickOnBounds(true);

		initialize_boxes();

		from.setEditable(true);
		TextFields.bindAutoCompletion(from.getEditor(), from.getItems().toArray());

		to.setEditable(true);
		TextFields.bindAutoCompletion(to.getEditor(), to.getItems());

		srcColor.valueProperty().addListener(e ->
		{
			srcColorVal = srcColor.getValue();

			if(!mirror.isSelected())
				return;

			String fromValue = from.getSelectionModel().getSelectedItem();

			if(fromValue != "" && fromValue != null)
			{
				from.getSelectionModel().clearSelection();
				from.getSelectionModel().select(fromValue);
			}

			String toValue = to.getSelectionModel().getSelectedItem();

			if(toValue != "" && toValue != null)
			{
				to.getSelectionModel().clearSelection();
				to.getSelectionModel().select(toValue);
			}
		});

		distColor.valueProperty().addListener(e ->
		{
			distColorVal = distColor.getValue();

			if(!mirror.isSelected())
				return;

			String fromValue = from.getSelectionModel().getSelectedItem();

			if(fromValue != "" && fromValue != null)
			{
				from.getSelectionModel().clearSelection();
				from.getSelectionModel().select(fromValue);
			}

			String toValue = to.getSelectionModel().getSelectedItem();

			if(toValue != "" && toValue != null)
			{
				to.getSelectionModel().clearSelection();
				to.getSelectionModel().select(toValue);
			}
		});

		midColor.valueProperty().addListener(e ->
		{
			midColorVal = midColor.getValue();

			if(!mirror.isSelected())
				return;

			String fromValue = from.getSelectionModel().getSelectedItem();

			if(fromValue != "" && fromValue != null)
			{
				from.getSelectionModel().clearSelection();
				from.getSelectionModel().select(fromValue);
			}

			String toValue = to.getSelectionModel().getSelectedItem();

			if(toValue != "" && toValue != null)
			{
				to.getSelectionModel().clearSelection();
				to.getSelectionModel().select(toValue);
			}
		});

		lineColor.valueProperty().addListener(e ->
		{
			lineColorVal = lineColor.getValue();

			if(!mirror.isSelected())
				return;

			String fromValue = from.getSelectionModel().getSelectedItem();

			if(fromValue != "" && fromValue != null)
			{
				from.getSelectionModel().clearSelection();
				from.getSelectionModel().select(fromValue);
			}

			String toValue = to.getSelectionModel().getSelectedItem();

			if(toValue != "" && toValue != null)
			{
				to.getSelectionModel().clearSelection();
				to.getSelectionModel().select(toValue);
			}
		});

		dijAlgo.setOnMouseClicked(e ->
		{
			if(!mirror.isSelected())
			{
				time.clear();

				return;
			}

			String fromValue = from.getSelectionModel().getSelectedItem();

			if(fromValue != "" && fromValue != null)
			{
				from.getSelectionModel().clearSelection();
				from.getSelectionModel().select(fromValue);
			}

			String toValue = to.getSelectionModel().getSelectedItem();

			if(toValue != "" && toValue != null)
			{
				to.getSelectionModel().clearSelection();
				to.getSelectionModel().select(toValue);
			}
		});

		aAlgo.setOnMouseClicked(e ->
		{
			if(!mirror.isSelected())
			{
				time.clear();

				return;
			}

			String fromValue = from.getSelectionModel().getSelectedItem();

			if(fromValue != "" && fromValue != null)
			{
				from.getSelectionModel().clearSelection();
				from.getSelectionModel().select(fromValue);
			}

			String toValue = to.getSelectionModel().getSelectedItem();

			if(toValue != "" && toValue != null)
			{
				to.getSelectionModel().clearSelection();
				to.getSelectionModel().select(toValue);
			}
		});

		// if the Instantaneous Mirroring was changed
		mirror.setOnMouseClicked(e ->
		{
			if(mirror.isSelected())
			{
				find.setDisable(true);

				String fromValue = from.getSelectionModel().getSelectedItem();

				if(fromValue != "" && fromValue != null)
				{
					from.getSelectionModel().clearSelection();
					from.getSelectionModel().select(fromValue);
				}

				String toValue = to.getSelectionModel().getSelectedItem();

				if(toValue != "" && toValue != null)
				{
					to.getSelectionModel().clearSelection();
					to.getSelectionModel().select(toValue);
				}
			}
			else
				find.setDisable(false);
		});

		// if the "From" choicebox was changed
		from.valueProperty().addListener(e ->
		{
			if(!mirror.isSelected())
			{
				stat.clear();
				time.clear();

				return;
			}

			if(graph.findVertexByName(from.getEditor().getText()) == null)
				return;
			else
			{
				if(!switchClicked && from.getSelectionModel().getSelectedItem() != null)
				{
					String fr = from.getEditor().getText();

					String[] disp = fr.split(" ");

					String display = "";

					for(int i = 0; i < disp.length - 1; i++)
						display += disp[i].substring(0, 1).toUpperCase() + disp[i].substring(1).toLowerCase() + " ";

					display += disp[disp.length - 1].substring(0, 1).toUpperCase() + disp[disp.length - 1].substring(1).toLowerCase();

					if(to.getSelectionModel().getSelectedItem() != null && display.matches(to.getSelectionModel().getSelectedItem()))
					{
						from.getSelectionModel().clearSelection();

						return;
					}

					from.getSelectionModel().select(display);

//					isClickedTwice = true;
				}
				else
					switchClicked = !switchClicked;
			}

			if(from.getSelectionModel().getSelectedItem() != "" && from.getSelectionModel().getSelectedItem() != null)
			{
				shoPath = null;
				copy.setDisable(true);
				screenshot.setDisable(true);
				export.setDisable(true);
				anchorPane.getChildren().clear();

				Vertex initFrom = graph.findVertexByName(from.getSelectionModel().getSelectedItem());

				isClickedTwice = true;

				anchorPane.getChildren().clear();

				try
				{
					circleReference.setCenterX(initFrom.getxCo());
					circleReference.setCenterY(initFrom.getyCo());
				}
				catch (NullPointerException ex)
				{
					return;
				}
				
				if(initFrom.getType().toLowerCase().matches("Major".toLowerCase()))
					circleReference.setRadius(5);
				else
					circleReference.setRadius(4);
				circleReference.setFill(srcColorVal);
				circleReference.setStroke(Color.BLACK);

				anchorPane.getChildren().add(circleReference);

				String toValue = to.getSelectionModel().getSelectedItem();
				distance.clear();
				sPtable.getItems().clear();
				stat.clear();
				time.clear();

				if(toValue != "" && toValue != null)
				{
					to.getSelectionModel().clearSelection();
					to.getSelectionModel().select(toValue);
				}
			}
		});

		from.setOnMouseClicked(e ->
		{
			from.getSelectionModel().clearSelection();
//			from.getEditor().clear();
		});

		to.setOnMouseClicked(e ->
		{
			to.getSelectionModel().clearSelection();
//			to.getEditor().clear();
		});

		// if the "To" choicebox was changed
		to.valueProperty().addListener(e ->
		{
			if(!mirror.isSelected())
			{
				stat.clear();
				time.clear();

				return;
			}

			if(graph.findVertexByName(to.getEditor().getText()) == null)
				return;
			else
			{
				if(!switchClicked && to.getSelectionModel().getSelectedItem() != null)
				{
					String fr = to.getEditor().getText();

					String[] disp = fr.split(" ");

					String display = "";

					for(int i = 0; i < disp.length - 1; i++)
						display += disp[i].substring(0, 1).toUpperCase() + disp[i].substring(1).toLowerCase() + " ";

					display += disp[disp.length - 1].substring(0, 1).toUpperCase() + disp[disp.length - 1].substring(1).toLowerCase();

					if(from.getSelectionModel().getSelectedItem() != null && display.matches(from.getSelectionModel().getSelectedItem()))
					{
						to.getSelectionModel().clearSelection();

						return;
					}

					to.getSelectionModel().select(display);

//					isClickedTwice = false;
				}
			}

			copy.setDisable(true);
			screenshot.setDisable(true);
			export.setDisable(true);

			if((to.getSelectionModel().getSelectedItem() != "" && to.getSelectionModel().getSelectedItem() != null) && from.getSelectionModel().getSelectedItem() != "" && from.getSelectionModel().getSelectedItem() != null)
			{
				shoPath = null;

				Vertex initFrom = graph.findVertexByName(from.getSelectionModel().getSelectedItem());
				Vertex initTo = graph.findVertexByName(to.getSelectionModel().getSelectedItem());

				isClickedTwice = false;

				try
				{
					if(initFrom.getName() == initTo.getName())
					{
						stat.setText("Choose two different locations please!");
						time.clear();

						// Generating an alert
						Platform.runLater(() ->
						{
							Alert alert = new Alert(Alert.AlertType.ERROR);

							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

							alert.setTitle("Error");
							alert.setHeaderText("Failure!");
							alert.setContentText("Choose two different locations please.");
							alert.show();
						});

						from.getSelectionModel().clearSelection();
						to.getSelectionModel().clearSelection();

						copy.setDisable(true);
						screenshot.setDisable(true);
						export.setDisable(true);

						anchorPane.getChildren().clear();
						sPtable.getItems().clear();
						distance.clear();

						return;
					}
				}
				catch (NullPointerException ex)
				{
					return;
				}


				Vertex res = null;

				if(algos.getSelectedToggle().getUserData().toString().matches("Dijkstra's Algorithm"))
				{
					long startTime = System.nanoTime();

					res = graph.getShortestPath(initFrom, initTo);

					if(res != null && !time.isDisable())
						time.setText((System.nanoTime() - startTime) + " nanoseconds");
					else
						time.setText("");
				}
				else
				{
					long startTime = System.nanoTime();

					res = graph.AstarSearch(initFrom, initTo);

					if(res != null && !time.isDisable())
						time.setText((System.nanoTime() - startTime) + " nanoseconds");
					else
						time.setText("");
				}

				if(res == null)
				{
					stat.setText("No path was found!");

					// Generating an alert
					Platform.runLater(() ->
					{
						Alert alert = new Alert(Alert.AlertType.WARNING);

						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

						alert.setTitle("Warning");
						alert.setHeaderText("Failure!");
						alert.setContentText("No path was found from \"" + initFrom.getName() + "\" to \"" + initTo.getName() + "\".");
						alert.show();
					});

					copy.setDisable(true);
					screenshot.setDisable(true);
					export.setDisable(true);

					from.getSelectionModel().clearSelection();
					to.getSelectionModel().clearSelection();
					anchorPane.getChildren().clear();
					sPtable.getItems().clear();
					distance.clear();

					return;
				}

				LinkedList<String> resStr = graph.getShortestPathDescription(res);
				LinkedList<Vertex> resVercs = graph.getShortestPathVertices(res);

				shoPath = "From \"" +  initFrom.getName() + "\" to \"" + initTo.getName() + "\":" + System.getProperty("line.separator") + System.getProperty("line.separator");

				Double dist = 0.0;

				for(String p : resStr)
				{
					dist += Double.parseDouble(p.substring(p.lastIndexOf(" : ") + 2, p.indexOf(" km")));

					shoPath += p + System.getProperty("line.separator");
				}

				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(3);

				distance.setText(nf.format(dist) + " km");

				fillSpTable();

				ArrayList<Circle> circles = new ArrayList<>();
				ArrayList<Line> lines = new ArrayList<>();

				for(int i = 0; i < resVercs.size(); i++)
				{
					if(i == 0)
					{
						Circle initCircle = new Circle();
						initCircle.setCenterX(initFrom.getxCo());
						initCircle.setCenterY(initFrom.getyCo());
						if(initFrom.getType().toLowerCase().matches("Major".toLowerCase()))
							initCircle.setRadius(5);
						else
							initCircle.setRadius(4);
						initCircle.setFill(srcColorVal);
						initCircle.setStroke(Color.BLACK);

						Line line = new Line();

						line.setStartX(initFrom.getxCo());
						line.setStartY(initFrom.getyCo());
						line.setEndX(resVercs.get(i).getxCo());
						line.setEndY(resVercs.get(i).getyCo());
						line.setStroke(lineColorVal);
						line.setStrokeWidth(2);

						Circle circle = new Circle();

						circle.setCenterX(resVercs.get(i).getxCo());
						circle.setCenterY(resVercs.get(i).getyCo());
						
						if(resVercs.get(i).getType().toLowerCase().matches("Major".toLowerCase()))
							circle.setRadius(5);
						else
							circle.setRadius(4);
						

						if(resVercs.size() != i + 1)
							circle.setFill(midColorVal);
						else
							circle.setFill(distColorVal);

						circle.setStroke(Color.BLACK);

						circles.add(circle);
						circles.add(circleReference);
						lines.add(line);
					}
					else
					{
						Line line = new Line();

						line.setStartX(resVercs.get(i - 1).getxCo());
						line.setStartY(resVercs.get(i - 1).getyCo());
						line.setEndX(resVercs.get(i).getxCo());
						line.setEndY(resVercs.get(i).getyCo());
						line.setStroke(lineColorVal);
						line.setStrokeWidth(2);

						Circle circle = new Circle();

						circle.setCenterX(resVercs.get(i).getxCo());
						circle.setCenterY(resVercs.get(i).getyCo());
						
						if(resVercs.get(i).getType().toLowerCase().matches("Major".toLowerCase()))
							circle.setRadius(5);
						else
							circle.setRadius(4);

						if(resVercs.size() != i + 1)
							circle.setFill(midColorVal);
						else
							circle.setFill(distColorVal);

						circle.setStroke(Color.BLACK);

						circles.add(circle);
						lines.add(line);
					}
				}

				anchorPane.getChildren().clear();

				for(int i = 0; i < lines.size(); i++)
					anchorPane.getChildren().add(lines.get(i));
				for(int i = 0; i < circles.size(); i++)
					anchorPane.getChildren().add(circles.get(i));

				copy.setDisable(false);
				screenshot.setDisable(false);
				export.setDisable(false);

				stat.setText("Path was found successfully!");
			}
			else if (to.getSelectionModel().getSelectedItem() != "" && to.getSelectionModel().getSelectedItem() != null)
			{
				shoPath = null;

				Circle circle = new Circle();

				Vertex toV = graph.findVertexByName(to.getSelectionModel().getSelectedItem());

				try
				{
					circle.setCenterX(toV.getxCo());
					circle.setCenterY(toV.getyCo());
				}
				catch (NullPointerException ex)
				{
					return;
				}

				if(toV.getType().toLowerCase().matches("Major".toLowerCase()))
					circle.setRadius(5);
				else
					circle.setRadius(4);

				circle.setFill(distColorVal);
				circle.setStroke(Color.BLACK);

				anchorPane.getChildren().clear();
				anchorPane.getChildren().add(circle);

				distance.clear();
				sPtable.getItems().clear();
				stat.clear();
			}
		});

		// if the mouse was moved inside the imageView
		anchorPane.setOnMouseMoved(e ->
		{
			Vertex hoveredOn = graph.findVertexByCoos(e.getX(), e.getY());

			if(hoveredOn == null || ((graph.getVertices().size() + graph.getEdges().size() > anchorPane.getChildren().size()) && (hoveredOn.getType().toLowerCase().matches("Minor".toLowerCase()) && shoPath == null) || (hoveredOn.getType().toLowerCase().matches("Minor".toLowerCase()) && shoPath != null && !shoPath.contains(hoveredOn.getName()))))
			{
				stat.setText("");

				return;
			}

			stat.setText(hoveredOn.getName());
		});

		// if the mouse was clicked on a point inside the imageView
		anchorPane.setOnMouseClicked(e ->
		{
			if(!graph.isFound(e.getX(), e.getY()) || !graph.findVertexByCoos(e.getX(), e.getY()).getType().toLowerCase().matches("Major".toLowerCase()))
			{
				stat.setText("Choose a valid city please!");

				// Generating an alert
				Platform.runLater(() ->
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);

					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

					alert.setTitle("Error");
					alert.setHeaderText("Failure!");
					alert.setContentText("Choose a valid city please.");
					alert.show();
				});

				if(sPtable.getItems().size() == 0)
				{
					copy.setDisable(true);
					export.setDisable(true);
					screenshot.setDisable(true);
				}

				return;
			}

			Vertex clicked = graph.findVertexByCoos(e.getX(), e.getY());

			if(isClickedTwice)
			{
				shoPath = null;

				to.getSelectionModel().select(graph.findVertexByCoos(e.getX(), e.getY()).getName());

				isClickedTwice = false;

				Vertex from = graph.findVertexByCoos(circleReference.getCenterX(), circleReference.getCenterY());
				Vertex to = graph.findVertexByCoos(e.getX(), e.getY());

				if(from.getName() == to.getName())
				{
					stat.setText("Choose two different locations please!");
					time.clear();

					if(!mirror.isSelected())
					{
						// Generating an alert
						Platform.runLater(() ->
						{
							Alert alert = new Alert(Alert.AlertType.ERROR);

							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

							alert.setTitle("Error");
							alert.setHeaderText("Failure!");
							alert.setContentText("Choose two different locations please.");
							alert.show();
						});
					}

					this.from.getSelectionModel().clearSelection();
					this.to.getSelectionModel().clearSelection();

					copy.setDisable(true);
					screenshot.setDisable(true);
					export.setDisable(true);

					anchorPane.getChildren().clear();

					sPtable.getItems().clear();
					distance.clear();

					return;
				}

				Vertex res = null;

				if(algos.getSelectedToggle().getUserData().toString().matches("Dijkstra's Algorithm"))
				{
					long startTime = System.nanoTime();

					res = graph.getShortestPath(from, to);

					if(res != null && !time.isDisable())
						time.setText((System.nanoTime() - startTime) + " nanoseconds");
					else
						time.setText("");
				}
				else
				{
					long startTime = System.nanoTime();

					res = graph.AstarSearch(from, to);

					if(res != null && !time.isDisable())
						time.setText((System.nanoTime() - startTime) + " nanoseconds");
					else
						time.setText("");
				}

				if(res == null)
				{
					if(!mirror.isSelected())
					{
						stat.setText("No path was found!");

						// Generating an alert
						Platform.runLater(() ->
						{
							Alert alert = new Alert(Alert.AlertType.WARNING);

							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

							alert.setTitle("Warning");
							alert.setHeaderText("Failure!");
							alert.setContentText("No path was found from \"" + from.getName() + "\" to \"" + to.getName() + "\".");
							alert.show();
						});
					}

					this.from.getSelectionModel().clearSelection();
					this.to.getSelectionModel().clearSelection();

					anchorPane.getChildren().clear();
					sPtable.getItems().clear();
					distance.clear();

					return;
				}

				LinkedList<String> resStr = resStr = graph.getShortestPathDescription(res);
				LinkedList<Vertex> resVercs = graph.getShortestPathVertices(res);

				shoPath = "From \"" +  from.getName() + "\" to \"" + to.getName() + "\":" + System.getProperty("line.separator") + System.getProperty("line.separator");

				Double dist = 0.0;

				for(String p : resStr)
				{
					dist += Double.parseDouble(p.substring(p.lastIndexOf(" : ") + 2, p.indexOf(" km")));

					shoPath += p + System.getProperty("line.separator");
				}

				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(3);

				distance.setText(nf.format(dist) + " km");

				fillSpTable();

				ArrayList<Circle> circles = new ArrayList<>();
				ArrayList<Line> lines = new ArrayList<>();

				for(int i = 0; i < resVercs.size(); i++)
				{
					if(i == 0)
					{
						Line line = new Line();

						line.setStartX(circleReference.getCenterX());
						line.setStartY(circleReference.getCenterY());
						line.setEndX(resVercs.get(i).getxCo());
						line.setEndY(resVercs.get(i).getyCo());
						line.setStroke(lineColorVal);
						line.setStrokeWidth(2);

						Circle circle = new Circle();

						circle.setCenterX(resVercs.get(i).getxCo());
						circle.setCenterY(resVercs.get(i).getyCo());
						circle.setRadius(5);
						
						if(resVercs.get(i).getType().toLowerCase().matches("Major".toLowerCase()))
							circle.setRadius(5);
						else
							circle.setRadius(4);
						
						if(resVercs.size() != i + 1)
							circle.setFill(midColorVal);
						else
							circle.setFill(distColorVal);

						circle.setStroke(Color.BLACK);

						lines.add(line);
						circles.add(circle);
						circles.add(circleReference);
					}
					else
					{
						Line line = new Line();

						line.setStartX(resVercs.get(i - 1).getxCo());
						line.setStartY(resVercs.get(i - 1).getyCo());
						line.setEndX(resVercs.get(i).getxCo());
						line.setEndY(resVercs.get(i).getyCo());
						line.setStroke(lineColorVal);
						line.setStrokeWidth(2);

						Circle circle = new Circle();

						circle.setCenterX(resVercs.get(i).getxCo());
						circle.setCenterY(resVercs.get(i).getyCo());
						circle.setRadius(5);
						
						if(resVercs.get(i).getType().toLowerCase().matches("Major".toLowerCase()))
							circle.setRadius(5);
						else
							circle.setRadius(4);

						if(resVercs.size() != i + 1)
							circle.setFill(midColorVal);
						else
							circle.setFill(distColorVal);

						circle.setStroke(Color.BLACK);

						lines.add(line);
						circles.add(circle);
					}
				}

				anchorPane.getChildren().clear();

				for(int i = 0; i < lines.size(); i++)
					anchorPane.getChildren().add(lines.get(i));
				for(int i = 0; i < circles.size(); i++)
					anchorPane.getChildren().add(circles.get(i));

				copy.setDisable(false);
				screenshot.setDisable(false);
				export.setDisable(false);

				stat.setText("Path was found successfully!");
			}
			else
			{
				from.getSelectionModel().clearSelection();
				from.getEditor().clear();
				to.getSelectionModel().clearSelection();
				to.getEditor().clear();

				shoPath = null;

				circleReference.setCenterX(clicked.getxCo());
				circleReference.setCenterY(clicked.getyCo());
				circleReference.setRadius(5);
				circleReference.setFill(srcColorVal);
				circleReference.setStroke(Color.BLACK);
				anchorPane.getChildren().clear();
				anchorPane.getChildren().add(circleReference);

				isClickedTwice = true;

				copy.setDisable(true);
				screenshot.setDisable(true);
				export.setDisable(true);

				if(mirror.isSelected() && to.getSelectionModel().getSelectedItem() != null && to.getSelectionModel().getSelectedItem() != "" && (from.getSelectionModel().getSelectedItem() == null || from.getSelectionModel().getSelectedItem() == ""))
					from.getSelectionModel().select(graph.findVertexByCoos(circleReference.getCenterX(), circleReference.getCenterY()).getName());
				else
				{
					to.getSelectionModel().clearSelection();
					from.getSelectionModel().select(graph.findVertexByCoos(circleReference.getCenterX(), circleReference.getCenterY()).getName());

					distance.clear();
					sPtable.getItems().clear();
					stat.setText("");
				}
			}
		});
	}

	// Draw the graph on the map
	@FXML
	void drawGraph(ActionEvent event)
	{
		resetUI(new ActionEvent());

		for(Edge e : graph.getEdges())
		{
			Line line = new Line();

			line.setStartX(e.getFrom().getxCo());
			line.setStartY(e.getFrom().getyCo());
			line.setEndX(e.getTo().getxCo());
			line.setEndY(e.getTo().getyCo());
			line.setStrokeWidth(2);

			if(graph.findEdge(e.getTo(), e.getFrom()) != null)
				line.setStroke(Color.RED);
			else
				line.setStroke(Color.BLUE);

			anchorPane.getChildren().addAll(line);
		}

		for(Vertex v : graph.getVertices())
		{
			Circle circle = new Circle();

			circle.setCenterX(v.getxCo());
			circle.setCenterY(v.getyCo());

			if(v.getType().toLowerCase().matches("Major".toLowerCase()))
			{
				circle.setFill(Color.LIGHTGREEN);
				circle.setRadius(5);
			}
			else
			{
				circle.setFill(Color.BROWN);
				circle.setRadius(4);
			}

			circle.setStroke(Color.BLACK);

			anchorPane.getChildren().add(circle);
		}

		Platform.runLater(() ->
		{
			Alert alert = new Alert(Alert.AlertType.INFORMATION);

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

			alert.setTitle("Info");
			alert.setHeaderText("Drawn!");
			alert.setContentText("Number of Vertices = " + graph.getVertices().size() + "\nNumber of Edges = " + graph.getEdges().size());
			alert.show();
		});

		stat.setText("Vertices = " + graph.getVertices().size() + " ~~ Edges = " + graph.getEdges().size());

		screenshot.setDisable(false);
	}

	// Switch "From" with "To"
	@FXML
	void switchVals(ActionEvent event)
	{
		switchClicked = true;

		String swapFrom = from.getSelectionModel().getSelectedItem();
		String swapTo = to.getSelectionModel().getSelectedItem();

		to.getSelectionModel().clearSelection();
		from.getSelectionModel().clearSelection();

		to.getSelectionModel().select(swapFrom);
		from.getSelectionModel().select(swapTo);

//		if(!mirror.isDisable())
//			return;
//
//		if(from.getEditor().getText() != "" && from.getSelectionModel().getSelectedItem() != "" && (graph.findVertexByName(from.getEditor().getText()) == null || graph.findVertexByName(from.getSelectionModel().getSelectedItem()) == null))
//		{
//			Alert alert = new Alert(Alert.AlertType.ERROR);
//
//			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//			stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));
//
//			alert.setTitle("ERROR");
//			alert.setHeaderText("Failure!");
//			alert.setContentText("Location \"" + from.getSelectionModel().getSelectedItem() + "\" is not available!");
//			alert.show();
//
//			from.getEditor().clear();
//			anchorPane.getChildren().clear();
//			copy.setDisable(true);
//			screenshot.setDisable(true);
//			export.setDisable(true);
//			from.getSelectionModel().clearSelection();
//			distance.clear();
//			sPtable.getItems().clear();
//			shoPath = null;
//			time.clear();
//
//			return;
//		}
//		else if(to.getEditor().getText() != "" && to.getSelectionModel().getSelectedItem() != "" && (graph.findVertexByName(to.getEditor().getText()) == null || graph.findVertexByName(to.getSelectionModel().getSelectedItem()) == null))
//		{
//			Alert alert = new Alert(Alert.AlertType.ERROR);
//
//			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//			stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));
//
//			alert.setTitle("ERROR");
//			alert.setHeaderText("Failure!");
//			alert.setContentText("Location \"" + to.getSelectionModel().getSelectedItem() + "\" is not available!");
//			alert.show();
//
//			to.getEditor().clear();
//			anchorPane.getChildren().clear();
//			copy.setDisable(true);
//			screenshot.setDisable(true);
//			export.setDisable(true);
//			to.getSelectionModel().clearSelection();
//			distance.clear();
//			sPtable.getItems().clear();
//			shoPath = null;
//			time.clear();
//
//			return;
//		}
	}

	// find the SP - Button way
	@FXML
	public void solve()
	{
		if(from.getSelectionModel().getSelectedItem() == null || to.getSelectionModel().getSelectedItem() == null)
		{
			stat.setText("Choose both From and To destinations please!");

			// Generating an alert
			Platform.runLater(() ->
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);

				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

				alert.setTitle("Error");
				alert.setHeaderText("Failure!");
				alert.setContentText("Choose both From and To destinations please.");
				alert.show();
			});

			return;
		}

		if(graph.findVertexByName(from.getEditor().getText()) == null || graph.findVertexByName(from.getSelectionModel().getSelectedItem()) == null)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

			alert.setTitle("ERROR");
			alert.setHeaderText("Failure!");
			alert.setContentText("Location \"" + from.getSelectionModel().getSelectedItem() + "\" is not available!");
			alert.show();

			from.getEditor().clear();
			anchorPane.getChildren().clear();
			copy.setDisable(true);
			screenshot.setDisable(true);
			export.setDisable(true);
			from.getSelectionModel().clearSelection();
			distance.clear();
			sPtable.getItems().clear();
			shoPath = null;
			time.clear();

			return;
		}
		else if(graph.findVertexByName(to.getEditor().getText()) == null || graph.findVertexByName(to.getSelectionModel().getSelectedItem()) == null)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

			alert.setTitle("ERROR");
			alert.setHeaderText("Failure!");
			alert.setContentText("Location \"" + to.getSelectionModel().getSelectedItem() + "\" is not available!");
			alert.show();

			to.getEditor().clear();
			anchorPane.getChildren().clear();
			copy.setDisable(true);
			screenshot.setDisable(true);
			export.setDisable(true);
			to.getSelectionModel().clearSelection();
			distance.clear();
			sPtable.getItems().clear();
			shoPath = null;
			time.clear();

			return;
		}

		isClickedTwice = false;

		shoPath = null;

		if(from.getSelectionModel().getSelectedItem() == "" || from.getSelectionModel().getSelectedItem() == null || to.getSelectionModel().getSelectedItem() == "" || to.getSelectionModel().getSelectedItem() == null)
		{
			distance.clear();
			sPtable.getItems().clear();
			copy.setDisable(true);
			screenshot.setDisable(true);
			export.setDisable(true);
			stat.setText("Choose both From and To destinations please!");

			// Generating an alert
			Platform.runLater(() ->
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);

				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

				alert.setTitle("Error");
				alert.setHeaderText("Failure!");
				alert.setContentText("Choose both From and To destinations please.");
				alert.show();
			});

			copy.setDisable(true);
			screenshot.setDisable(true);
			export.setDisable(true);

			return;
		}

		anchorPane.getChildren().clear();

		Vertex fromV = graph.findVertexByName(from.getSelectionModel().getSelectedItem());
		Vertex toV = graph.findVertexByName(to.getSelectionModel().getSelectedItem());

		try
		{
			if(fromV.getName() == toV.getName())
			{
				stat.setText("Choose two different locations please!");
				time.clear();

				// Generating an alert
				Platform.runLater(() ->
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);

					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

					alert.setTitle("Error");
					alert.setHeaderText("Failure!");
					alert.setContentText("Choose two different locations please.");
					alert.show();
				});

				this.from.getSelectionModel().clearSelection();
				this.to.getSelectionModel().clearSelection();

				copy.setDisable(true);
				export.setDisable(true);
				screenshot.setDisable(true);

				anchorPane.getChildren().clear();
				from.getSelectionModel().clearSelection();
				to.getSelectionModel().clearSelection();
				sPtable.getItems().clear();
				distance.clear();

				return;
			}
		}
		catch (NullPointerException ex)
		{
			return;
		}

		Vertex res = null;

		if(algos.getSelectedToggle().getUserData().toString().matches("Dijkstra's Algorithm"))
		{
			long startTime = System.nanoTime();

			res = graph.getShortestPath(fromV, toV);

			if(res != null && !time.isDisable())
				time.setText((System.nanoTime() - startTime) + " nanoseconds");
			else
				time.setText("");
		}
		else
		{
			long startTime = System.nanoTime();

			res = graph.AstarSearch(fromV, toV);

			if(res != null && !time.isDisable())
				time.setText((System.nanoTime() - startTime) + " nanoseconds");
			else
				time.setText("");
		}

		if(res == null)
		{
			stat.setText("No path was found!");

			// Generating an alert
			Platform.runLater(() ->
			{
				Alert alert = new Alert(Alert.AlertType.WARNING);

				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

				alert.setTitle("Warning");
				alert.setHeaderText("Failure!");
				alert.setContentText("No path was found from \"" + fromV.getName() + "\" to \"" + toV.getName() + "\".");
				alert.show();
			});

			copy.setDisable(true);
			screenshot.setDisable(true);
			export.setDisable(true);

			anchorPane.getChildren().clear();
			from.getSelectionModel().clearSelection();
			to.getSelectionModel().clearSelection();
			sPtable.getItems().clear();
			distance.clear();

			return;
		}

		LinkedList<String> resStr = graph.getShortestPathDescription(res);
		LinkedList<Vertex> resVercs = graph.getShortestPathVertices(res);

		shoPath = "From \"" +  fromV.getName() + "\" to \"" + toV.getName() + "\":" + System.getProperty("line.separator") + System.getProperty("line.separator");

		Double dist = 0.0;

		for(String p : resStr)
		{
			dist += Double.parseDouble(p.substring(p.lastIndexOf(" : ") + 2, p.indexOf(" km")));

			shoPath += p + System.getProperty("line.separator");
		}

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(3);

		distance.setText(nf.format(dist) + " km");

		fillSpTable();

		ArrayList<Circle> circles = new ArrayList<>();
		ArrayList<Line> lines = new ArrayList<>();

		for(int i = 0; i < resVercs.size(); i++)
		{
			if(i == 0)
			{
				Circle initCircle = new Circle();
				initCircle.setCenterX(fromV.getxCo());
				initCircle.setCenterY(fromV.getyCo());

				if(fromV.getType().toLowerCase().matches("Major".toLowerCase()))
					initCircle.setRadius(5);
				else
					initCircle.setRadius(4);
				
				initCircle.setFill(srcColorVal);
				initCircle.setStroke(Color.BLACK);

				Line line = new Line();

				line.setStartX(fromV.getxCo());
				line.setStartY(fromV.getyCo());
				line.setEndX(resVercs.get(i).getxCo());
				line.setEndY(resVercs.get(i).getyCo());
				line.setStroke(lineColorVal);
				line.setStrokeWidth(2);

				Circle circle = new Circle();

				circle.setCenterX(resVercs.get(i).getxCo());
				circle.setCenterY(resVercs.get(i).getyCo());
				
				if(resVercs.get(i).getType().toLowerCase().matches("Major".toLowerCase()))
					circle.setRadius(5);
				else
					circle.setRadius(4);

				if(resVercs.size() != i + 1)
					circle.setFill(midColorVal);
				else
					circle.setFill(distColorVal);

				circle.setStroke(Color.BLACK);

				circles.add(circle);
				circles.add(initCircle);
				lines.add(line);
			}
			else
			{
				Line line = new Line();

				line.setStartX(resVercs.get(i - 1).getxCo());
				line.setStartY(resVercs.get(i - 1).getyCo());
				line.setEndX(resVercs.get(i).getxCo());
				line.setEndY(resVercs.get(i).getyCo());
				line.setStroke(lineColorVal);
				line.setStrokeWidth(2);

				Circle circle = new Circle();

				circle.setCenterX(resVercs.get(i).getxCo());
				circle.setCenterY(resVercs.get(i).getyCo());

				if(resVercs.get(i).getType().toLowerCase().matches("Major".toLowerCase()))
					circle.setRadius(5);
				else
					circle.setRadius(4);

				if(resVercs.size() != i + 1)
					circle.setFill(midColorVal);
				else
					circle.setFill(distColorVal);

				circle.setStroke(Color.BLACK);

				circles.add(circle);
				lines.add(line);
			}
		}

		anchorPane.getChildren().clear();

		for(int i = 0; i < lines.size(); i++)
			anchorPane.getChildren().add(lines.get(i));
		for(int i = 0; i < circles.size(); i++)
			anchorPane.getChildren().add(circles.get(i));

		copy.setDisable(false);
		screenshot.setDisable(false);
		export.setDisable(false);

		stat.setText("Path was found successfully!");
	}

	// initialize the choice boxes
	private void initialize_boxes()
	{
		ObservableList<String> data = FXCollections.observableArrayList();

		for(Vertex u : graph.getVertices())
			data.add(u.getName());

		Collections.sort(data, String.CASE_INSENSITIVE_ORDER);

		from.setItems(data);
		to.setItems(data);
	}

	// to copy to the clipboard
	@FXML
	void copy(ActionEvent event)
	{
		Clipboard clip = Clipboard.getSystemClipboard();
		ClipboardContent clipContent = new ClipboardContent();

		String paste = "Shortest Path " + shoPath.replaceFirst("F", "f");

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(3);

		paste += "\n--------------\n\nTotal distance: " + nf.format(Double.parseDouble(distance.getText().split(" km")[0])) + " km";

		clipContent.putString(paste);
		clip.setContent(clipContent);

		stat.setText("Copied to Clipboard successfully!");
	}

	// to export to a file
	@FXML
	void export(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(mirror.getScene().getWindow());

		if (file != null)
		{
			String expo = "Shortest Path " + shoPath.replaceFirst("F", "f");

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(3);

			expo += System.getProperty("line.separator") + "--------------" + System.getProperty("line.separator") + System.getProperty("line.separator") + "Total distance: " + nf.format(Double.parseDouble(distance.getText().split(" km")[0])) + " km";

			PrintWriter writer = null;
			try
			{
				writer = new PrintWriter(file);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			writer.print(expo);
			writer.close();

			stat.setText("Result was exported successfully to \"" + file.getName() + "\"!");

			// Generating an alert
			Platform.runLater(() ->
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);

				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

				alert.setTitle("Success");
				alert.setHeaderText("Done!");
				alert.setContentText("Result was exported successfully to \"" + file.getName() + "\".");
				alert.show();
			});
		}
	}

	// to reset the UI
	@FXML
	void resetUI(ActionEvent event)
	{
		readData();

		anchorPane.getChildren().clear();
		copy.setDisable(true);
		screenshot.setDisable(true);
		export.setDisable(true);
		from.getSelectionModel().clearSelection();
		to.getSelectionModel().clearSelection();
		distance.clear();
		sPtable.getItems().clear();
		shoPath = null;
		time.clear();

		isClickedTwice = false;

		from.getEditor().clear();
		to.getEditor().clear();

		stat.setText("UI and Data have been reset!");
	}

	// to reset the Colors
	@FXML
	void resetColors(ActionEvent event)
	{
		if(srcColorVal == Color.BROWN && distColorVal == Color.LIGHTGREEN && midColorVal == Color.ORANGE && lineColorVal == Color.RED)
		{
			stat.setText("Colors have been reset!");

			return;
		}

		srcColorVal = Color.BROWN;
		distColorVal = Color.LIGHTGREEN;
		midColorVal = Color.ORANGE;
		lineColorVal = Color.RED;

		srcColor.setValue(srcColorVal);
		distColor.setValue(distColorVal);
		midColor.setValue(midColorVal);
		lineColor.setValue(lineColorVal);

		if(!mirror.isSelected())
			return;

		String fromValue = from.getSelectionModel().getSelectedItem();

		if(fromValue != "" && fromValue != null)
		{
			from.getSelectionModel().clearSelection();
			from.getSelectionModel().select(fromValue);
		}

		String toValue = to.getSelectionModel().getSelectedItem();

		if(toValue != "" && toValue != null)
		{
			to.getSelectionModel().clearSelection();
			to.getSelectionModel().select(toValue);
		}

		stat.setText("Colors have been reset!");
	}

	// to read the file
	private void readData()
	{
		File file = new File("src/Resources/IO/Graph Data/Palestine/Palestine_Map.txt");
		Scanner input = null;
		String str = null;

		try
		{
			input = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}

		Vertex fromV = null;

		graph.getEdges().clear();
		graph.getVertices().clear();
		anchorPane.getChildren().clear();

		while(input.hasNextLine())
		{
			str = input.nextLine();

			if(str.matches(""))
				continue;

			String[] parts = str.split(": ");

			if(parts.length == 4)
				fromV = new Vertex(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[0]);
			else
			{
				Vertex toV = new Vertex(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[0]);

				graph.addData(fromV, toV, Double.parseDouble(parts[4]));

				if(parts.length == 5)
					graph.addData(toV, fromV, Double.parseDouble(parts[4]));
			}

		}

		input.close();
	}

	// Initialize the table with the appropriate columns
	private void initialize_table()
	{
		TableColumn<ObservableList, String> num = new TableColumn<>("#");
		num.setMinWidth(5);
		num.setMaxWidth(20);
		num.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0).toString()));
		num.setSortable(false);

		TableColumn<ObservableList, String> source = new TableColumn<>("Source");
		source.setMinWidth(106);
		source.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1).toString()));
		source.setSortable(false);

		TableColumn<ObservableList, String> destination = new TableColumn<>("Destination");
		destination.setMinWidth(106);
		destination.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2).toString()));
		destination.setSortable(false);

		TableColumn<ObservableList, String> distance = new TableColumn<>("Distance");
		distance.setMinWidth(40);
		distance.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3).toString()));
		distance.setSortable(false);

		sPtable.getColumns().addAll(num, source, destination, distance);
	}

	// to display the SP in the table
	public void fillSpTable()
	{
		data = FXCollections.observableArrayList();
		ObservableList<String> row = null;

		String[] inits = shoPath.split("[\n]+");

		for(int i = 2; i < inits.length; i++)
		{
			String[] show = inits[i].split("[>:]+");

			row = FXCollections.observableArrayList();

			row.add((i - 1) + "");
			row.add(show[0].trim());
			row.add(show[1].trim());
			row.add(show[2].trim());

			data.add(row);
		}

		sPtable.getItems().clear();
		sPtable.setItems(data);
	}

	// to take a screenshot
	@FXML
	void takeScreenshot(ActionEvent event)
	{
		Scene scene = mirror.getScene();

		WritableImage writableImage = new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
		scene.snapshot(writableImage);

		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(scene.getWindow());

		if(file != null)
		{
			try
			{
				ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);

				stat.setText("Screenshot was saved successfully to \"" + file.getName() + "\"!");

				// Generating an alert
				Platform.runLater(() ->
				{
					Alert alert = new Alert(Alert.AlertType.INFORMATION);

					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("Resources/Images/MapIcon.png"));

					alert.setTitle("Success");
					alert.setHeaderText("Done!");
					alert.setContentText("Screenshot was saved successfully to \"" + file.getName() + "\".");
					alert.show();
				});
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	// Closing the application
	public void close()
	{
		mirror.getScene().getWindow().hide();
	}
}
