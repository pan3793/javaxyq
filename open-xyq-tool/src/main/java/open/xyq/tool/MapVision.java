package open.xyq.tool;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.fmt.map.TileMapProvider;

import java.io.ByteArrayInputStream;
import java.io.File;


/**
 * 地图查看器，基于 JavaFX 实现
 * TODO：
 *   update status labels
 *   export tile maps
 *   export entire map
 * NOT DO:
 *   support JPG2
 */
@Slf4j
public class MapVision extends Application {
    private Stage mainStage;

    private final TilePane tileMapPane = new TilePane();

    private final Label hitsLabel = new Label("");
    private final Label sizeLabel = new Label("");
    private final Label pathLabel = new Label("");
    private final Label viewRectLabel = new Label("");
    private final Label segmentsLabel = new Label("");

    private File lastOpenDir;

    @Override
    public void start(Stage stage) {
        mainStage = stage;

        VBox rootPane = new VBox();
        rootPane.getChildren().addAll(createMenuBar(), createTileMapPane(), createStatusPane());
        Scene scene = new Scene(rootPane, 800, 600);

        stage.setTitle("Map Vision");
        stage.setScene(scene);
        stage.show();
    }

    private MenuBar createMenuBar() {
        Menu fileMenu = new Menu("File");
        MenuItem openMenuItem = new MenuItem("open");
        openMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.setInitialDirectory(lastOpenDir);
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XYQ Map Files", "*.map"));
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            if (selectedFile != null) {
                log.info("open file: " + selectedFile.getName());
                lastOpenDir = selectedFile.getParentFile();
                openMap(selectedFile);
            }
        });
        fileMenu.getItems().addAll(openMenuItem);

        Menu helpMenu = new Menu("Help");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(helpMenu);
        return menuBar;
    }

    private ScrollPane createTileMapPane() {
        ScrollPane scrollPane = new ScrollPane();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setContent(tileMapPane);
        return scrollPane;
    }

    private HBox createStatusPane() {
        HBox statusPane = new HBox();
        statusPane.getChildren().addAll(hitsLabel, sizeLabel, pathLabel, viewRectLabel, segmentsLabel);
        return statusPane;
    }

    private void openMap(File file) {
        TileMapProvider mapProvider = new TileMapProvider(file);
        int vCnt = mapProvider.getVBlockCount();
        int hCnt = mapProvider.getHBlockCount();

        tileMapPane.setPrefRows(vCnt);
        tileMapPane.setPrefColumns(hCnt);

        tileMapPane.getChildren().clear();
        for (int v = 0; v < vCnt; v++) {
            for (int h = 0; h < hCnt; h++) {
                byte[] data = mapProvider.readJpegData(h, v);
                tileMapPane.getChildren().add(new ImageView(new Image(new ByteArrayInputStream(data))));
            }
        }
    }
}
