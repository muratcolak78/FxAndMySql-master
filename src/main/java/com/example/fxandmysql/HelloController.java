package com.example.fxandmysql;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.*;


public class HelloController {

    @FXML
    public Label labelDatabase, labelTable, labelDatabase1, labelTable1, labelDatabase11, labelTable11, labelColumn1, labelColumn2,
            labelColumn3, labelColumn4, labelColumn5, labelColumn6, labelColumn7, labelColumn8, labelColumn9, labelKayitDurumu,
            showUsernameLabel, labelColumn11, labelColumn21,
            labelColumn31, labelColumn41, labelColumn51, labelColumn61, labelColumn71, labelColumn81, labelColumn91, labelNotification;
    @FXML
    public TextField textFieldAdd, textFieldFindItem, textFieldFound, textFieldShowTableContents,
            textFiledValue1, textFiledValue2, textFiledValue3, textFiledValue4, textFiledValue5,
            textFiledValue6, textFiledValue7, textFiledValue8, textFiledValue9,
            textFiledValue11, textFiledValue21, textFiledValue31, textFiledValue41, textFiledValue51,
            textFiledValue61, textFiledValue71, textFiledValue81, textFiledValue91, url, username,
            password, inputTableName, textFieldgetItemWithName, fileNameField,fileNameField1;
    @FXML
    public Button buttonAdd, butoonFindItems, buttonAdd2, buttonGetIt, buttonUpdate, buttonDelete, buttonJsonExport;

    @FXML
    public TextArea textArea, textAreaTableContents, textAreaTableContents1, textAreaTableContents11;

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
    //private JSONArray result = new JSONArray();
    public int columsNumber, jokerColumnNumber, number = 0;


    @FXML
    public void getDatabase() throws SQLException {
        textAreaTableContents.setText("");
        textAreaTableContents1.setText("");
        textAreaTableContents11.setText("");

        database = new ArrayList<>();


        this.URL = url.getText();
        this.USERNAME = username.getText();
        this.PASSWORD = password.getText();


        ResultSet queryOutPut = null;
        String coonectQuery = "SHOW DATABASES;";


        try {
            System.out.println("uğraştık");
            Statement statement = DBConnection().createStatement();
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

            DBConnection().close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void getDatabaseTables() {
        textAreaTableContents.setText("");
        textAreaTableContents1.setText("");
        textAreaTableContents11.setText("");

        String dataBase = inputTableName.getText();

        if (dataBase.isEmpty()) {
            showUsernameLabel.setText(" lütfen bir database seçiniz");

        } else if (!database.contains(dataBase)) {
            showUsernameLabel.setText(" lütfen geçerli bir database seçiniz");

        } else {

            String coonectQuery = "SHOW TABLES FROM " + dataBase + ";";

            try {
                Statement statement = DBConnection().createStatement();
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
                DBConnection().close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            showUsernameLabel.setText("");
        }
    }

    @FXML
    public void getTableContents() {

        textAreaTableContents.setText("");
        textAreaTableContents1.setText("");
        textAreaTableContents11.setText("");
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();


        if (tablename.isEmpty()) {
            showUsernameLabel.setText(" lütfen bir tablo seçiniz");

        } else if (!tables.contains(tablename)) {
            showUsernameLabel.setText(" lütfen geçerli bir tablo seçiniz");
        } else {
            getcolumnsNameList(databaseName, tablename);
            String coonectQuery = "select * from " + databaseName + "." + tablename;

            try {
                Statement statement = DBConnection().createStatement();
                ResultSet queryOutPut = statement.executeQuery(coonectQuery);
                String text = "";
                String text2 = makeColumName(columnNames) + "\n";

                while (queryOutPut.next()) {
                    text = makeText2(queryOutPut, columsNumber);
                    text2 += text + "\n";
                    textAreaTableContents.setText(text2);
                    textAreaTableContents1.setText(text2);
                    textAreaTableContents11.setText(text2);
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
                DBConnection().close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        showUsernameLabel.setText("");

    }

    @FXML
    public void allActions(ActionEvent event) throws SQLException {
        String action = ((Button) event.getSource()).getText();
        switch (action) {
            case "Find":
                findItem();
                break;
            case "Get It":
                getItem();
                break;
            case "Update":
                updateItem();
                break;
            case "Delete":
                deleteItem();
                break;
            case "Add2":
                addItem2();
                break;
            case "Add":
                addItem();
                break;
            case "Export Json":
                if(!fileNameField.getText().isEmpty()){
                    exportAsJson(fileNameField.getText());
                }else{
                    popupWindow("lütfen dosya adını yazınız");
                }
                break;
            case "Export CSV":
                if(!fileNameField1.getText().isEmpty()){
                    exportAsCSV(fileNameField1.getText());
                }else{
                    popupWindow("lütfen dosya adını yazınız");
                }

                break;
        }


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

            String columnNames = "(" + makeColumName(jokerColumnName) + ")";
            String extra = databaseName + "." + tablename + " " + columnNames + " values " + makeValueName(addFiled) + ";";
            String sqlCode = "INSERT INTO " + extra;
            System.out.println(sqlCode);


            try {
                PreparedStatement preparedStatement = DBConnection().prepareStatement(sqlCode);
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

        String columnNames = "(" + makeColumName(jokerColumnName) + ")";
        String extra = databaseName + "." + tablename + " " + columnNames + " values " + makeValueName(valueMaker()) + ";";
        String sqlCode = "INSERT INTO " + extra;
        System.out.println(sqlCode);

        try {
            PreparedStatement preparedStatement = DBConnection().prepareStatement(sqlCode);
            preparedStatement.executeUpdate(sqlCode);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jokerColumnNumber = 0;
        jokerColumnName.clear();
        for (int i = 0; i < listTextFieldColumns.size(); i++) {
            listTextFieldColumns.get(i).setText("");
        }
        labelNotification.setText("Kayıt Eklendi");
        textFieldgetItemWithName.setText("");
        getTableContents();

    }

    @FXML
    public void findItem() throws SQLException {
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();
        String findItem = textFieldFindItem.getText();
        System.out.println(findItem);
        getcolumnsNameList(databaseName, tablename);

        String x = "select * from " + databaseName + "." + tablename + " where " + columnNames.get(1) + " like '" + findItem + "%';";
        String sql3 = "SELECT " + jokerColumnName.get(0) + "," + jokerColumnName.get(1) + "," + jokerColumnName.get(2) + " FROM " + databaseName + "." + tablename + "  WHERE '" + textFieldFindItem.getText() + "%';";
        System.out.println(x);

        try {
            Statement statement = DBConnection().prepareStatement(x);
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
                    text2 += text + "\n";

                }

                while (queryOutPut.next()) {


                }
                textFieldFound.setText(text);
                popupWindow(text2);
                textAreaTableContents1.setText(text2);
                textAreaTableContents11.setText(text2);
                System.out.println(text);
                labelNotification.setText("Kişiler Bulundu");
            } catch (Exception e) {
                labelNotification.setText("Kişiler Bulunamadı");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }


    }

    @FXML
    public void getItem() {

        for (int i = 0; i < listTextFieldUpdatePage.size(); i++) {
            listTextFieldUpdatePage.get(i).setText("");
        }

        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();
        String getItem = textFieldgetItemWithName.getText();
        System.out.println(getItem);
        getcolumnsNameList(databaseName, tablename);

        String x = "select * from " + databaseName + "." + tablename + " where " + columnNames.get(0) + " = " + Integer.valueOf(getItem) + ";";
        String x2 = "select * from " + databaseName + "." + tablename + " where " + columnNames.get(1) + " = " + getItem + ";";

        System.out.println("--------" + columnNames.get(0) + " " + getItem);
        System.out.println(x);

        try {
            Statement statement = DBConnection().prepareStatement(x);
            ResultSet queryOutPut = null;
            if (columnNames.get(0).equals(null)) {
                queryOutPut = statement.executeQuery(x2);
            } else {
                queryOutPut = statement.executeQuery(x);
            }


            String text = "";
            String[] text2;
            try {
                while (queryOutPut.next()) {
                    for (int i = 0; i < jokerColumnNumber; i++) {
                        text += queryOutPut.getString(jokerColumnName.get(i)) + " ";
                        System.out.println(text);
                    }
                }

                text2 = text.split(" ");
                for (int i = 0; i < jokerColumnNumber; i++) {
                    listTextFieldUpdatePage.get(i).setText(text2[i]);
                }


            } catch (Exception e) {
                labelNotification.setText("Kayıt getirilemedi");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println(makeUpdateCode());
        labelNotification.setText("Kayıt getirildi");


    }

    @FXML
    public void updateItem() {
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();

        String sqlCode = "UPDATE " + databaseName + "." + tablename + " SET " + makeUpdateCode() + " WHERE " + jokerColumnName.get(0) + "=" + listTextFieldUpdatePage.get(0).getText() + ";";
        System.out.println(sqlCode);

        try {
            PreparedStatement preparedStatement = DBConnection().prepareStatement(sqlCode);
            preparedStatement.executeUpdate(sqlCode);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jokerColumnNumber = 0;
        jokerColumnName.clear();
        for (int i = 0; i < listTextFieldColumns.size(); i++) {
            listTextFieldColumns.get(i).setText("");
        }
        getTableContents();
        labelNotification.setText("Kayıt güncellendi");

    }

    public void deleteItem() {
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();

        //String sqlCode = "UPDATE " + databaseName + "." + tablename + " SET " + makeUpdateCode() + " WHERE " + jokerColumnName.get(0) + "=" + listTextFieldUpdatePage.get(0).getText() + ";";
        //System.out.println(sqlCode);
        String sqlCode2 = "DELETE FROM " + databaseName + "." + tablename + " WHERE " + jokerColumnName.get(0) + "=" + listTextFieldUpdatePage.get(0).getText() + ";";
        try {
            PreparedStatement preparedStatement = DBConnection().prepareStatement(sqlCode2);
            preparedStatement.executeUpdate(sqlCode2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jokerColumnNumber = 0;
        jokerColumnName.clear();
        for (int i = 0; i < listTextFieldColumns.size(); i++) {
            listTextFieldColumns.get(i).setText("");
        }
        getTableContents();
        for (int i = 0; i < listTextFieldUpdatePage.size(); i++) {
            listTextFieldUpdatePage.get(i).setText("");
        }
        labelNotification.setText("Kayıt silindi");

    }

    public void exportAsJson(String fileName) {
             fileName = fileNameField.getText();
            String databaseName = inputTableName.getText();
            String tablename = textFieldShowTableContents.getText();
            System.out.println(databaseName);
            Statement stmt = null;
            String sqlCode2 = makeJsonCode(databaseName, tablename, fileName);
            System.out.println(sqlCode2);
            try {
                stmt = DBConnection().createStatement();
                stmt.executeQuery(sqlCode2);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            jokerColumnNumber = 0;
            jokerColumnName.clear();
            for (int i = 0; i < listTextFieldColumns.size(); i++) {
                listTextFieldColumns.get(i).setText("");
            }
            getTableContents();
            for (int i = 0; i < listTextFieldUpdatePage.size(); i++) {
                listTextFieldUpdatePage.get(i).setText("");
            }
            String x = "C:/ProgramData/MySQL/\nMySQL Server 8.0/Uploads/" + fileName + ".json";
            popupWindow("Dosya \n" + x + "\n olarak export edildi ");
            fileName = "";
        }



    public void exportAsCSV(String fileName) {
        fileName = fileNameField1.getText();
        String databaseName = inputTableName.getText();
        String tablename = textFieldShowTableContents.getText();
        System.out.println(databaseName);

        String csvCode = "SELECT * FROM " + databaseName + "." + tablename + " INTO OUTFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/" + fileName+ ". csv';";
        System.out.println(csvCode);
        try {
            Statement stmt = DBConnection().createStatement();
            stmt.executeQuery(csvCode);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jokerColumnNumber = 0;
        jokerColumnName.clear();
        for (int i = 0; i < listTextFieldColumns.size(); i++) {
            listTextFieldColumns.get(i).setText("");
        }
        getTableContents();
        for (int i = 0; i < listTextFieldUpdatePage.size(); i++) {
            listTextFieldUpdatePage.get(i).setText("");
        }
        String x = "C:/ProgramData/MySQL/\nMySQL Server 8.0/Uploads/" + fileName + ".csv";
        popupWindow("Dosya \n" + x + "\n olarak export edildi ");
        fileName = "";
    }


    public String makeJsonCode(String data, String table, String fileName) {
        number++;
        String x = "('" + jokerColumnName.get(0) + "', " + jokerColumnName.get(0);
        for (int i = 1; i < jokerColumnNumber; i++) {
            x += ",'" + jokerColumnName.get(i) + "', " + jokerColumnName.get(i);
        }
        return "SELECT JSON_ARRAYAGG(JSON_OBJECT " + x + ")) FROM " + data + "." + table + " INTO OUTFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/" + fileName + ".json';";

    }


    @FXML
    public void getcolumnsNameList(String databaseName, String tablename) {
        columnNames = new ArrayList<>();
        String coonectQuery = "show COLUMNS FROM " + databaseName + "." + tablename + ";";

        try {
            Statement statement = DBConnection().createStatement();
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

    public String makeColumnNameList2() {
        String text = columnNames.get(0);
        for (int i = 0; i < columnNames.size(); i++) {
            text += ", " + columnNames.get(i);
        }
        return text;
    }

    public String makeText2(ResultSet queryOutPut, int number) throws SQLException {
        String text = queryOutPut.getString(1);
        for (int i = 2; i <= number; i++) {
            text += "  " + queryOutPut.getString(i);
        }
        return text;
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
            for (int i = 1; i < jokerColumnNumber; i++) {
                listLabelColumns.get(i - 1).setText(jokerColumnName.get(i));

            }
            for (int i = 0; i < jokerColumnNumber; i++) {
                listLabelUpdatePage.get(i).setText(jokerColumnName.get(i));
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


        for (int i = 0; i < jokerColumnNumber; i++) {
            listTextFieldColumns.get(i).setEditable(false);
            listTextFieldUpdatePage.get(i).setEditable(false);

        }
        for (int i = 0; i < number - 1; i++) {
            listTextFieldColumns.get(i).setEditable(true);

        }
        for (int i = 0; i < number; i++) {

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


    @FXML
    public void popupWindow(String text) {
        Stage popup = new Stage();
        popup.setTitle(".........");
        popup.initModality(Modality.APPLICATION_MODAL);
        Pane pane = new Pane();

        TextArea textArea1 = new TextArea();

        pane.getChildren().add(textArea1);
        textArea1.setEditable(false);
        textArea1.usesMirroring();
        textArea1.setText(text);

        Scene dialogScene = new Scene(pane, 300, 200);
        popup.setScene(dialogScene);
        popup.show();

    }

    public String makeUpdateCode() {

        String text = jokerColumnName.get(1) + "='" + listTextFieldUpdatePage.get(1).getText() + "'";

        for (int i = 2; i < jokerColumnNumber; i++) {
            text += ", " + jokerColumnName.get(i) + "= '" + listTextFieldUpdatePage.get(i).getText() + "'";
        }
        return text;
    }


    public Connection DBConnection() throws SQLException {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection(URL, USERNAME, PASSWORD);
        return connectDB;
    }

}






