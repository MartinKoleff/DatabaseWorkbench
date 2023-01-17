import DataStructures.MyLinkedList;
import DataStructures.Node;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Setup DB obj via databaseEditor and edit / insert and delete from here...
public class Database {
    private String tableName;
    private static String defaultFolder = "D:\\test\\databaseEditor";
//    private static String defaultFolder = "C:\\Users\\Martin\\IdeaProjects\\DatabaseWorkbench";
//    private static String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";

    private String fullPath;
    private boolean isLoaded = false;
    //initialize from file?
    private List<String> columnNames = new ArrayList<>();

    //In every container there is a row from the DB
    private MyLinkedList data = new MyLinkedList();

    public Database(String tableName, boolean loadData) {
        this.tableName = tableName;
        fullPath = defaultFolder + "\\" + tableName + ".txt";

        if (loadData) {
            loadData();
        }
    }

    private void loadData() {
        if (!isLoaded) {  //reset isLoaded in insert / add the new row?
            try {
                File file = new File(this.getFullPath());
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line;
                int counter = 1;
                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
                    if (counter >= 2) {
                        data.insertNode(Utility.split(line, '\t'));
                    }
                    counter++;
                }
                isLoaded = true;
                System.out.println();
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void deleteDatabase() {
        File file = new File(this.getFullPath());
        if (file.exists()) {
            file.delete();
            DatabaseEditor.getDatabases().remove(this);
            System.out.printf("Table %s has been deleted.\n", tableName);
        } else {
            System.out.printf("Table with the name %s doesn't exist. Please try again with a different name.", tableName);
        }
    }

    public void deleteRow() {

    }

    public void join() {

    }

    public String getTableName() {
        return tableName;
    }

    public String getFullPath() {
        return fullPath;
    }

    private List<String> getColumnTypeOrder() {
        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int counter = 1;
            List<String> columnTypeOrder = new ArrayList<>();
            String dataTypeWithDefault, dataType;
            while ((line = br.readLine()) != null) {
                if (counter == 2) { //2nd line -> columns
                    List<String> columns = Utility.split(line, '\t');
                    for (String column : columns) {
                        dataTypeWithDefault = Utility.split(column, ':').get(1);
                        dataType = Utility.split(dataTypeWithDefault, ' ').get(0);
                        columnTypeOrder.add(dataType);
                    }
                    return columnTypeOrder;
                }
                counter++;
            }
            return columnTypeOrder;
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    private List<String> getColumnOrder() {
        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int counter = 1;
            List<String> columnOrder = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (counter == 2) { //2nd line -> columns
                    List<String> columns = Utility.split(line, '\t');
                    for (String column : columns) {
                        columnOrder.add(Utility.split(column, ':').get(0));
                    }
                }
                counter++;
            }
            return columnOrder;
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    private String getColumnRow() {
        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int counter = 1;
            while ((line = br.readLine()) != null) {
                if (counter == 2) { //2nd line -> columns
                    return line;
                }
                counter++;
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public List<String> getUserOrderColumnTypes(List<String> userInputColumnOrder) {
        List<String> userOrderColumnTypes = new ArrayList<>();
        List<String> columnTypeOrder = this.getColumnTypeOrder();
        List<String> columnOrder = this.getColumnOrder();

        int counter = 0;
        for (int i = 0; i < userInputColumnOrder.size(); i++) {
            for (String column : columnOrder) {
                if (column.equals(userInputColumnOrder.get(i))) {
                    userOrderColumnTypes.add(columnTypeOrder.get(counter));
                }
                counter++;
            }
            counter = 0;
        }

        return userOrderColumnTypes;
    }

    //No columns from user scenario...
    public List<String> orderUserInput(List<String> userInput, List<String> userInputColumnOrder) {
        List<String> sortedRow = new ArrayList<>();
        List<String> columnTypeOrder = this.getColumnTypeOrder();
        List<String> columnOrder = this.getColumnOrder();

        int counter = 0;
        String column;
        for (int i = 0; i < columnOrder.size(); i++) {
            column = columnOrder.get(i);
            for (int j = 0; j < columnOrder.size(); j++) {
                try {
                    if (column.equals(userInputColumnOrder.get(j))) {
                        sortedRow.add(userInput.get(counter));
                    }
                } catch (IndexOutOfBoundsException e) {
                    if (sortedRow.size() >= i + 1) {
                        break;
                    }
                    sortedRow.add(this.getDefaultValue(columnOrder.get(i)));
                    j = columnOrder.size();
                }
                counter++;
            }

            //Default value
            if (sortedRow.size() < i + 1) {
                sortedRow.add(this.getDefaultValue(columnOrder.get(i)));
            }
            counter = 0;
        }
        return sortedRow;
    }

    private String getDefaultValue(String userColumn) {
        List<String> columnOrder = this.getColumnOrder();
        List<String> columnTypeOrder = this.getColumnTypeOrder();

        int selectedColumnIndex = 0;
        for (int i = 0; i < columnOrder.size(); i++) {
            if (columnOrder.get(i).equals(userColumn)) {
                selectedColumnIndex = i;
            }
        }

        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int counter = 1;
            while ((line = br.readLine()) != null) {
                if (counter == 2) { //2nd line -> columns
                    try {
                        List<String> columns = Utility.split(line, '\t');
                        String selectedColumn = columns.get(selectedColumnIndex);
                        List<String> columnData = Utility.split(selectedColumn, new char[]{' ', ':'});
                        if (columnData.size() > 2 && columnData.get(2).equals("default")) {
                            String defaultValue = columnData.get(3);
                            return defaultValue;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        return null;
                    }
                }
                counter++;
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    //If all default columns are used in the user command
    public boolean defaultColumnsAreUsed(List<String> userInputColumnOrder) {
        String columnRow = this.getColumnRow();
        List<String> columnRowSplit = Utility.split(columnRow, '\t');
        List<String> columnData;
        String columnName;
        int defaultCounter = 0;
        for (String column : columnRowSplit) {
            columnData = Utility.split(column, new char[]{' ', ':'});
            columnName = columnData.get(0);
            if (columnData.size() > 2 && columnData.get(2).equals("default") && !userInputColumnOrder.contains(columnName)) {
                defaultCounter++;
            }
        }
        return userInputColumnOrder.size() + defaultCounter == columnRowSplit.size();
    }

    public void select(List<String> selectedColumns, List<String> whereConditions) {
        List<List<String>> filteredRows = new ArrayList<>();
        List<Integer> selectedColumnIndexes = new ArrayList<>();
        List<String> columnOrder = this.getColumnOrder();
        List<String> columnTypeOrder = this.getColumnTypeOrder();
        boolean showAllColumns = false;

        if (selectedColumns.get(0).equals("*")) {
            showAllColumns = true;
        }

        for (int totalColumns = 0; totalColumns < selectedColumns.size(); totalColumns++) {
            for (int i = 0; i < columnOrder.size(); i++) {
                if (selectedColumns.get(totalColumns).equals(columnOrder.get(i))) {
                    selectedColumnIndexes.add(i);
                    i = columnOrder.size();
                }
            }
        }

        if (whereConditions.isEmpty()) {
            try {
                File file = new File(this.getFullPath());
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line;
                int counter = 1;
                List<String> splitLine;
                List<String> filteredRow = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    if (counter >= 3) { //3rd row -> data
                        splitLine = Utility.split(line, '\t');
                        for (int index : selectedColumnIndexes) {
                            filteredRow.add(splitLine.get(index));
                        }
                        filteredRows.add(filteredRow);
                    }
                    counter++;
                }
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (Exception e) {
                System.out.println("Something went wrong in SELECT...");
            }
        } else {
            String selectedColumn;
            String mathSign;
            String comparator;
            List<String> conditionSplit;

            for (String condition : whereConditions) {
                conditionSplit = Utility.split(condition, ' ');
                selectedColumn = conditionSplit.get(0);
                mathSign = conditionSplit.get(1);
                comparator = conditionSplit.get(2);

                String columnValue;
                String columnName;
                String columnType;
                Node head;
                switch (mathSign) {
                    case "!=":
                    case "<>": //same as !=
                        loadData();

                        head = data.findNodeAt(1);
                        for (int i = 1; i < data.getListSize(); i++) {
                            for (int j = 0; j < head.item.size(); j++) { //Each column in a row...
                                columnValue = head.item.get(0);
                                columnName = columnOrder.get(j);
                                if (columnName.equals(selectedColumn) && !columnValue.equals(comparator)) {
                                    System.out.println("yey");
                                    filteredRows.add(filterRow(selectedColumns, head.item));
                                    break;
                                }
                            }
                            head = head.next; //Change row...
                        }
                        break;
                    case "==":
                        loadData();

                        head = data.findNodeAt(1);
                        for (int i = 1; i < data.getListSize(); i++) {
                            for (int j = 0; j < head.item.size(); j++) { //Each column in a row...
                                columnValue = head.item.get(0);
                                columnName = columnOrder.get(j);
                                if (columnName.equals(selectedColumn) && columnValue.equals(comparator)) {
                                    System.out.println("yey");
                                    filteredRows.add(filterRow(selectedColumns, head.item));
                                    break;
                                }
                            }
                            head = head.next; //Change row...
                        }
                        break;
                    case "<=":
                        loadData();
                        //get column type (int or date)...
                        //parse column...

                        head = data.findNodeAt(1);
                        for (int i = 1; i < data.getListSize(); i++) {
                            for (int j = 0; j < head.item.size(); j++) { //Each column in a row...
                                columnValue = head.item.get(0);
                                columnName = columnOrder.get(j);
                                columnType = columnTypeOrder.get(j);
                                if (columnName.equals(selectedColumn)) {
                                    if (Utility.parser.tryParse(columnValue, columnType)) { //Valid data...
                                        switch (columnType) {
                                            case "date":
                                                DateFormat format = new SimpleDateFormat("dd.mm.yyyy", Locale.ENGLISH);
                                                try {
                                                    Date tableDate = format.parse(columnValue);
                                                    Date comparatorDate = format.parse(comparator);

                                                    if (Utility.parser.compareDates(tableDate, comparatorDate, mathSign)) {
                                                        filteredRows.add(filterRow(selectedColumns, head.item));
                                                    }
                                                } catch (ParseException e) {
                                                    return;
                                                } catch (Exception e) { //compareDates error handling...
                                                    return;
                                                }
                                                break;
                                            case "int":
                                                try {
                                                    int tableNumber = Integer.parseInt(columnValue);
                                                    int comparatorNumber = Integer.parseInt(comparator);
                                                    if (Utility.parser.compareInts(tableNumber, comparatorNumber, mathSign)) {
                                                        System.out.println("yeyy");
                                                        filteredRows.add(filterRow(selectedColumns, head.item));
                                                    }
                                                } catch (ParseException e) {
                                                    return;
                                                } catch (Exception e) { //compareInts error handling...
                                                    return;
                                                }
                                                break;
                                        }
                                    }
                                    break;
                                }
                            }
                            head = head.next; //Change row...
                        }
                        break;
                    case ">=":
                        break;
                    case "<":
                        break;
                    case ">":
                        break;
                    default:
                        System.out.println("Wrong select operator. Please try again!");
                        break;
                }
            }
        }
    }

    /**
     * Filters only the selected columns in a given row
     */
    private List<String> filterRow(List<String> selectedColumns, List<String> splitRow) {
        List<String> filteredRow = new ArrayList<>();
        List<String> columnOrder = this.getColumnOrder();

        for (int i = 0; i < selectedColumns.size(); i++) {
            for (String column : columnOrder) {
                if (column.equals(selectedColumns.get(i))) {
                    filteredRow.add(splitRow.get(i));
                }
            }
        }
        return filteredRow;
    }
}

