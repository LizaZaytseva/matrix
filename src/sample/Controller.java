package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.sound.sampled.Line;
import java.io.*;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {
    @FXML
    private ResourceBundle resources;

    @FXML
    private MenuBar menuBar;

    @FXML
    private URL location;

    @FXML
    public Menu HelpMenu;

    @FXML
    public Menu FileMenu;

    @FXML
    private MenuItem About;

    @FXML
    private MenuItem Close;

    @FXML
    public MenuItem ChooseFile;

    @FXML
    public AnchorPane AnchorPane;

    @FXML
    public GridPane gridPane = null;

    Label res;

    ArrayList<String> resArr = new ArrayList<>();

    Logic x = new Logic();

    File file;

    @FXML
    public void initialize() throws IOException {
        Close.setOnAction(event -> Platform.exit()); // Закрытие программы из меню

        //Правила работы программы
        About.setOnAction(event -> {
            try {
                Label secondLabel = new Label("Правила пользования программой!\n"+
                        "Поле M*N заполняется целыми положительными числами, целое\n" +
                        "число в каждой клетке указывает, какой длины должен быть шаг из нее.\n" +
                        "Все шаги могут быть вправо или вниз. Программа находит ВСЕ возможные\n" +
                        "пути из левого верхнего угла в правый нижный\n" +
                        "Примечание:\n" +
                        "Для работы программы необходимо выбрать исходный файл, в котором\n" +
                        "в первой строке должен быть указан размер матрицы, например, 'm*n',\n" +
                        "а потом сама матрица. Числа в матрице должны быть записаны\n" +
                        "через пробел.");
                secondLabel.setFont(new Font("courier new", 18));

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 800, 250);

                //Создание главного окна
                Stage newWindow = new Stage();
                newWindow.setTitle("Rules");
                newWindow.setScene(secondScene);

                newWindow.initModality(Modality.WINDOW_MODAL);
                newWindow.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    ArrayList<String> table = new ArrayList<>(); //создание листа, хранящего все значения из входного файла

    public void openAndReadFile(javafx.event.ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser(); // диалоговое окно для выбора исходного файла
        fileChooser.setTitle("Open Document");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"); // проверка исходного файла на верность типа
        fileChooser.getExtensionFilters().add(extFilter);
        Stage newWindow = new Stage();
        file = fileChooser.showOpenDialog(newWindow);
        try {

            //чтение входного файла
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            int k = 0;//////////////////////////////
            int str = 0;////////////////////////////
            int stl = 0;/////////////////////////

            //очищаем листы от старых данных
            table.clear();
            resArr.clear();

            //разбор входного файла на матрицу и параметры матрицы, а также проверка на верность входных данных
            while (line != null) {
                if ((k == 0) && (line.matches("\\d+\\*\\d+"))) {
                    String [] size = line.split("\\" +
                            "*");
                    //количество строк в таблице
                    str = Integer.valueOf(size[0]);
                    //количество столбцов в таблице
                    stl = Integer.valueOf(size[1]);
                    }
                else if ((k != str && line.matches("[\\d+\\s]*") && ((line.split("\\s")).length == stl)) ||
                        (k == str && line.matches("[\\d+\\s]*0$") && ((line.split("\\s")).length == stl))) {
                    //добавление элементов матрицы в лист
                    for (String st : line.split("\\s")){
                        table.add(st);
                    }
                }

                //окно ошибки, в случае неверных входных данных
                else {
                    Label secondLabel = new Label("       Ошибка входных данных!\n" +
                            "Проверьте входной файл на наличие ошибок.\n" +
                            "Про необходимый формат вы можете прочитать\n" +
                            "в разделе HELP.");
                    secondLabel.setFont(new Font("courier new", 18));

                    StackPane secondaryLayout = new StackPane();
                    secondaryLayout.getChildren().add(secondLabel);

                    Scene secondScene = new Scene(secondaryLayout, 500, 150);

                    Stage exWindow = new Stage();
                    exWindow.setTitle("Input file is incorrect");
                    exWindow.setScene(secondScene);

                    newWindow.initModality(Modality.WINDOW_MODAL);
                    exWindow.show();

                    throw new Exception();
                }
                line = reader.readLine();
                k++;
                };
            if (k - 1 != str) {
                Label secondLabel = new Label("       Ошибка входных данных!\n" +
                        "Проверьте входной файл на наличие ошибок.\n" +
                        "Про необходимый формат вы можете прочитать\n" +
                        "в разделе HELP.");
                secondLabel.setFont(new Font("courier new", 18));

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 500, 150);

                Stage exWindow = new Stage();
                exWindow.setTitle("Input file is incorrect");
                exWindow.setScene(secondScene);

                newWindow.initModality(Modality.WINDOW_MODAL);
                exWindow.show();

                throw new Exception();
            }
            try {

                //создание двумерного массива, хранещего значения матрицы
                int[][] map = new int[str][stl];
                int l = 0;
                for (int i = 0; i < str; i++) {
                    for (int j = 0; j < stl; j++) {
                        map[i][j] = Integer.valueOf(table.get(l));
                        l++;
                    }
                }
                String m = "";

                //использование класса, осуществляющего логику программы
                resArr = x.result(map, 0, 0, str, stl, m);

                String finalResult = "";
                int r = 1;

                //форматируем полученный результат
                for (String s : resArr) {
                    finalResult += r + ") "+ s + " \n";
                    r++;
                }

                //обновление окна после работы программы
            if ( gridPane == null) gridPane = new GridPane();
            else {
                gridPane.getChildren().clear();
                gridPane = new GridPane();
                AnchorPane.getChildren().remove(res);

            }

            //отрисовка таблицы
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.ALWAYS);
            for (int i = 0; i < stl; i++) {
                gridPane.getColumnConstraints().add(cc);
            }
            RowConstraints rc = new RowConstraints();
            rc.setFillHeight(true);
            rc.setVgrow(Priority.ALWAYS);

            for (int i = 0; i < str; i++) {
                gridPane.getRowConstraints().add(rc);
            }
            gridPane.setHgap(0);
            gridPane.setVgap(0);

            gridPane.setGridLinesVisible(true);

            //заполнение таблицы
            int y = 0;
            int n = 1;
            int[][] arr = new int[str][stl];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    Label lb = new Label(" " + table.get(y) + " " + "(" + n + ") ");
                    lb.setFont(Font.font(25));
                    gridPane.add(lb, j , i);
                    y++;
                    n++;
                }
            }

            //характеристики окна
            gridPane.autosize();
            gridPane.setLayoutX(50);
            gridPane.setLayoutY(90);

            //результат работы программы
                if (!finalResult.isEmpty()){
                    res = new Label("Всевозможные пути:\n" + finalResult);
                } else {
                    res = new Label("Нет решений! Попробуйте ввести другую матрицу!");
                }
            Label mLabel = new Label("Заданная матрица:");
            mLabel.setFont(new Font("courier new", 22));

            //выбор координат расположения матрицы
            mLabel.setLayoutX(50);
            mLabel.setLayoutY(50);

            //расположение результата работы программы, формула расчитывается след. способом:
                // 50 - отступ от края, stl - количество столбцов, 80 - расстояние, выделенное на каждый столбец, 70 - отступ от края матрицы
            res.setLayoutX(50 + stl * 80 + 70);
            res.setLayoutY(50);
            res.setFont(new Font("courier new", 22));
            AnchorPane.getChildren().add(mLabel);
            AnchorPane.getChildren().add(res);
            AnchorPane.getChildren().add(gridPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}