package com.example.fxandmysql;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloController {

    @FXML
    public Label labelDatabase, labelTable, labelDatabase1, labelTable1, labelDatabase11, labelTable11, labelColumn1, labelColumn2,
            labelColumn3, labelColumn4, labelColumn5, labelColumn6, labelColumn7, labelColumn8, labelColumn9, labelKayitDurumu,
            showUsernameLabel, labelColumn11, labelColumn21,
            labelColumn31, labelColumn41, labelColumn51, labelColumn61, labelColumn71, labelColumn81, labelColumn91;
    @FXML
    public TextField textFieldAdd, textFieldFindItem, textFieldFound, textFieldShowTableContents,
            textFiledValue1, textFiledValue2, textFiledValue3, textFiledValue4, textFiledValue5,
            textFiledValue6, textFiledValue7, textFiledValue8, textFiledValue9,
            textFiledValue11, textFiledValue21, textFiledValue31, textFiledValue41, textFiledValue51,
            textFiledValue61, textFiledValue71, textFiledValue81, textFiledValue91, url, username,
            password, inputTableName;
    @FXML
    public Button buttonAdd, butoonFindItems, buttonAdd2;

    @FXML
    public TextArea textArea, textAreaTableContents;

    @FXML
    private List<String> tables = new ArrayList<>();
    @FXML
    private List<String> columnNames, database, jokerColumnName, textFieldColumnsValue;
    @FXML
    private List<Label> listLabelColumns, listLabelUpdatePage;
    @FXML
    private List<TextField> listTextFieldColumns, listTextFieldUpdatePage;
    @FXML
    private String URL, USERNAME, PASSWORD;
    public int columsNumber, jokerColumnNumber = 0;


    @FXML
    public void getDatabase() throws SQLException {
        textAreaTableContents.setText("");

        database = new ArrayList<>();

        /*this.URL = url.getText();
        this.USERNAME = username.getText();
        this.PASSWORD = password.getText();*/

        this.URL = "jdbc:mysql://localhost:3306/";
        this.USERNAME = "root";
        this.PASSWORD = "M974202m.";

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
        ResultSet queryOutPut = null;
        String coonectQuery = "SHOW DATABASES;";


        try {
            System.out.println("uğraştık");
            Statement statement = connectDB.createStatement();
            System.out.println("statement oluşturuldu");
            queryOutPut = statement.executeQuery(coonectQuery);
            System.out.println("Connected to the database.");
            popupWindow("Connected to the database.");
            showUsernameLabel.setText("Connected to the database.");
        } catch (Exception e) {
            System.out.println("Failed to connect ot the database.");
            showUsernameLabel.setText("Failed to connect ot the database.");
            popupWindow("Failed to connect ot the database.");
        }


        try {
            String text = "";
            String text2 = "";
            while (queryOutPut.next()) {
                text = queryOutPut.getString(1);
                database.add(text);
                text2 += text + "\n";
            }
            if (database.size() > 0) {
                inputTableName.setEditable(true);
                textFieldShowTableContents.setEditable(true);
            }
            System.out.println(database);
            textArea.setText(text2);

            connectDB.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void getDatabaseTables() {
        textAreaTableContents.setText("");

        String dataBase = inputTableName.getText();

        if (dataBase.isEmpty()) {
            showUsernameLabel.setText(" lütfen bir database seçiniz");

        } else if (!database.contains(dataBase)) {
            showUsernameLabel.setText(" lütfen geçerli bir database seçiniz");

        } else {

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
            String coonectQuery = "SHOW TABLES FROM " + dataBase + ";";

            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryOutPut = statement.executeQuery(coonectQuery);
                String text = "";
                String text2 = "";

                while (queryOutPut.next()) {
                    text =
                            queryOutPut.getString(1);

                    tables.add(text);
                    text2 += text + "\n";
                    textArea.setText(text2);
                }
                if (tables.isEmpty()) {
                    textAreaTableContents.setText("bu database içinde tablo yok");
                }
                showUsernameLabel.setText("");


                System.out.println(text2);
                connectDB.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            showUsernameLabel.setText("");
        }
    }

    @FXML
    public void getTableContents() {

        textAreaTableContents.setText("");
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();


        if (tablename.isEmpty()) {
            showUsernameLabel.setText(" lütfen bir tablo seçiniz");

        } else if (!tables.contains(tablename)) {
            showUsernameLabel.setText(" lütfen geçerli bir tablo seçiniz");
        } else {
            getcolumnsNameList(databaseName, tablename);
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
            String coonectQuery = "select * from " + databaseName + "." + tablename;

            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryOutPut = statement.executeQuery(coonectQuery);
                String text = "";
                String text2 = makeColumName(columnNames) + "\n";

                while (queryOutPut.next()) {
                    text = makeText2(queryOutPut, columsNumber);
                    text2 += text + "\n";
                    textAreaTableContents.setText(text2);
                }


                this.jokerColumnName = columnNames;
                this.jokerColumnNumber = columsNumber;

                System.out.println(jokerColumnName);
                System.out.println(jokerColumnNumber);
                makeColumName(jokerColumnName);
                makeValueName("hasan, hüseyin, murat");

                System.out.println("************" + textFieldShowTableContents.getText());

                labelTable.setText(textFieldShowTableContents.getText());
                labelDatabase.setText(inputTableName.getText());
                labelTable1.setText(textFieldShowTableContents.getText());
                labelDatabase1.setText(inputTableName.getText());
                labelTable11.setText(textFieldShowTableContents.getText());
                labelDatabase11.setText(inputTableName.getText());


                makeAddPaneValueName();

                this.columsNumber = 0;

                System.out.println(text2);
                System.out.println(tablename + " " + jokerColumnName);
                System.out.println(tablename + " " + jokerColumnName);
                connectDB.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        showUsernameLabel.setText("");

    }


    @FXML
    public void getcolumnsNameList(String databaseName, String tablename) {
        columnNames = new ArrayList<>();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
        String coonectQuery = "show COLUMNS FROM " + databaseName + "." + tablename + ";";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutPut = statement.executeQuery(coonectQuery);
            String text = "";
            String text2 = "";

            while (queryOutPut.next()) {
                text = queryOutPut.getString(1);

                columnNames.add(text);
                text2 += text + "\n";

            }
            System.out.println(text2);
            this.columsNumber = columnNames.size();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String makeText2(ResultSet queryOutPut, int number) throws SQLException {
        String text = queryOutPut.getString(1);
        for (int i = 2; i <= number; i++) {
            text += "  " + queryOutPut.getString(i);
        }
        return text;
    }

    @FXML
    public void addItem() {

        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();
        String addFiled = textFieldAdd.getText();


        if (databaseName.isEmpty() || tablename.isEmpty()) {
            showUsernameLabel.setText("database ve tablo bilgileri eksik olamaz!!");
        } else if (!database.contains(databaseName) || !tables.contains(tablename)) {
            showUsernameLabel.setText("database veya tablo bilgileri hatalı!!");
        } else if (addFiled.isEmpty()) {
            showUsernameLabel.setText("Lütfen data yazınız!!");
        } else {


            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
            String columnNames = "(" + makeColumName(jokerColumnName) + ")";
            String extra = databaseName + "." + tablename + " " + columnNames + " values " + makeValueName(addFiled) + ";";
            String sqlCode = "INSERT INTO " + extra;
            System.out.println(sqlCode);

            try {
                PreparedStatement preparedStatement = connectDB.prepareStatement(sqlCode);
                preparedStatement.executeUpdate(sqlCode);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            jokerColumnNumber = 0;
            jokerColumnName.clear();
            getTableContents();
        }
    }

    @FXML
    public void addItem2() {

        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
        String columnNames = "(" + makeColumName(jokerColumnName) + ")";
        String extra = databaseName + "." + tablename + " " + columnNames + " values " + makeValueName(valueMaker()) + ";";
        String sqlCode = "INSERT INTO " + extra;
        System.out.println(sqlCode);

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(sqlCode);
            preparedStatement.executeUpdate(sqlCode);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jokerColumnNumber = 0;
        jokerColumnName.clear();
        for (int i = 0; i < listTextFieldColumns.size(); i++) {
            listTextFieldColumns.get(i).setText("");
        }
        labelKayitDurumu.setText("Kayıt eklendi");
        getTableContents();

    }

    public String makeColumName(List<String> nameList) {

        String column = nameList.get(1);
        for (int i = 2; i < nameList.size(); i++) {
            column += "," + nameList.get(i);
        }
        System.out.println(column);
        return column;
    }

    public String makeValueName(String textAdd) {
        String[] addItemList = textAdd.split(",");
        String value = "('" + addItemList[0] + "'";
        int fark = 0;
        if (addItemList.length < jokerColumnNumber) {
            fark = jokerColumnNumber - addItemList.length;
        }

        if (addItemList.length == 0) {
            showUsernameLabel.setText("girilen data değeri null");
        } else if (addItemList.length > jokerColumnNumber) {
            showUsernameLabel.setText("Girdi parçaları data veri aralığından fazla, işlem yapılamaz");
            return null;
        } else {

            for (int i = 0; i < addItemList.length; i++) {
                addItemList[i] = addItemList[i].trim();
                addItemList[i] = "'" + addItemList[i] + "'";
            }

            for (int i = 1; i < addItemList.length; i++) {
                value += "," + addItemList[i];
            }
        }
        if (fark > 0) {
            for (int i = 1; i < fark; i++) {
                value += "," + "'null'";
            }
        }
        System.out.println(value + ")");
        return value + ")";
    }

    @FXML
    public void findItem() throws SQLException {
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();
        String findItem = textFieldFindItem.getText();
        System.out.println(findItem);
        getcolumnsNameList(databaseName, tablename);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);

        String x = "select * from " + databaseName + "." + tablename + " where " + columnNames.get(1) + " like '" + findItem + "%';";
        String sql3 = "SELECT " + jokerColumnName.get(0) + "," + jokerColumnName.get(1) + "," + jokerColumnName.get(2) + " FROM " + databaseName + "." + tablename + "  WHERE '" + textFieldFindItem.getText() + "%';";
        System.out.println(x);

        try {
            Statement statement = connectDB.prepareStatement(x);
            ResultSet queryOutPut = statement.executeQuery(x);

            String text = "";
            String text2 = "";

            showUsernameLabel.setText(" bulundu");
            try {
                while (queryOutPut.next()) {
                    text =
                            queryOutPut.getString(1) + "  " +
                                    queryOutPut.getString(2) + "  " +
                                    queryOutPut.getString(3);
                    text2 += text+"\n";

                }

                while (queryOutPut.next()) {


                }
                textFieldFound.setText(text);
                popupWindow(text2);
                System.out.println(text);
            } catch (Exception e) {
                textFieldFound.setText("Şahıs bulunamadı");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }


    }

    public void makeAddPaneValueName() {

        listLabelColumns = new ArrayList<>();
        listLabelUpdatePage = new ArrayList<>();

        listLabelColumns.add(labelColumn1);
        listLabelColumns.add(labelColumn2);
        listLabelColumns.add(labelColumn3);
        listLabelColumns.add(labelColumn4);
        listLabelColumns.add(labelColumn5);
        listLabelColumns.add(labelColumn6);
        listLabelColumns.add(labelColumn7);
        listLabelColumns.add(labelColumn8);
        listLabelColumns.add(labelColumn9);

        listLabelUpdatePage.add(labelColumn11);
        listLabelUpdatePage.add(labelColumn21);
        listLabelUpdatePage.add(labelColumn31);
        listLabelUpdatePage.add(labelColumn41);
        listLabelUpdatePage.add(labelColumn51);
        listLabelUpdatePage.add(labelColumn61);
        listLabelUpdatePage.add(labelColumn71);
        listLabelUpdatePage.add(labelColumn81);
        listLabelUpdatePage.add(labelColumn91);


        for (int i = 0; i < listLabelColumns.size(); i++) {
            listLabelColumns.get(i).setText("");
            listLabelUpdatePage.get(i).setText("");
        }

        if (!jokerColumnName.isEmpty()) {
            for (int i = 1; i < jokerColumnName.size(); i++) {
                listLabelColumns.get(i - 1).setText(jokerColumnName.get(i));
                listLabelUpdatePage.get(i - 1).setText(jokerColumnName.get(i - 1));
            }

        }
        setTextFielValue(jokerColumnNumber);
    }

    public void setTextFielValue(int number) {

        listTextFieldColumns = new ArrayList<>();
        listTextFieldUpdatePage = new ArrayList<>();

        listTextFieldColumns.add(textFiledValue1);
        listTextFieldColumns.add(textFiledValue2);
        listTextFieldColumns.add(textFiledValue3);
        listTextFieldColumns.add(textFiledValue4);
        listTextFieldColumns.add(textFiledValue5);
        listTextFieldColumns.add(textFiledValue6);
        listTextFieldColumns.add(textFiledValue7);
        listTextFieldColumns.add(textFiledValue8);
        listTextFieldColumns.add(textFiledValue9);

        listTextFieldUpdatePage.add(textFiledValue11);
        listTextFieldUpdatePage.add(textFiledValue21);
        listTextFieldUpdatePage.add(textFiledValue31);
        listTextFieldUpdatePage.add(textFiledValue41);
        listTextFieldUpdatePage.add(textFiledValue51);
        listTextFieldUpdatePage.add(textFiledValue61);
        listTextFieldUpdatePage.add(textFiledValue71);
        listTextFieldUpdatePage.add(textFiledValue81);
        listTextFieldUpdatePage.add(textFiledValue91);


        for (int i = 0; i < listTextFieldColumns.size(); i++) {
            listTextFieldColumns.get(i).setEditable(false);
            listTextFieldUpdatePage.get(i).setEditable(false);

        }
        for (int i = 0; i < number - 1; i++) {
            listTextFieldColumns.get(i).setEditable(true);
            listTextFieldUpdatePage.get(i).setEditable(true);
        }

    }

    public String valueMaker() {
        String values = listTextFieldColumns.get(0).getText();
        for (int i = 1; i < listTextFieldColumns.size(); i++) {
            values += "," + listTextFieldColumns.get(i).getText();
        }
        return values;
    }

    public void updateItem() {
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
        String columnNames = "(" + makeColumName(jokerColumnName) + ")";
        String extra = databaseName + "." + tablename + " " + columnNames + " values " + makeValueName(valueMaker()) + ";";
        String sqlCode = "INSERT INTO " + extra;
        System.out.println(sqlCode);

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(sqlCode);
            preparedStatement.executeUpdate(sqlCode);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jokerColumnNumber = 0;
        jokerColumnName.clear();
        for (int i = 0; i < listTextFieldColumns.size(); i++) {
            listTextFieldColumns.get(i).setText("");
        }
        labelKayitDurumu.setText("Kayıt eklendi");
        getTableContents();

    }

    @FXML
    public void popupWindow(String text) {
        Stage popup = new Stage();
        popup.setTitle(".........");
        popup.initModality(Modality.APPLICATION_MODAL);
        Pane pane=new Pane();
        //VBox popupVbox = new VBox(20);
        TextArea textArea1=new TextArea();
        //popupVbox.getChildren().add(textArea1);
        pane.getChildren().add(textArea1);
        textArea1.setEditable(false);
        textArea1.usesMirroring();
        textArea1.setText(text);
        //Scene dialogScene = new Scene(popupVbox, 300, 300);
        Scene dialogScene = new Scene(pane, 300, 200);
        popup.setScene(dialogScene);
        popup.show();

    }
   /* public void popupWindow(String text) {
        Stage dialog = new Stage();
        dialog.setTitle(" warning ");
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        //Label label=new Label();
        TextArea textArea1=new TextArea();
        //dialogVbox.getChildren().add(new Text(text));
        //dialogVbox.getChildren().add(label);
        dialogVbox.getChildren().add(textArea1);
        //label.setText("deneme");
        textArea1.setText(text);
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();

    }*/
}



