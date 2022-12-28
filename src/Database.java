import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Setup DB obj via databaseEditor and edit / insert and delete from here...
public class Database {
    private String tableName;
    //    private String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";
    private static String defaultFolder = "D:\\test\\databaseEditor";
    //    private static String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";

    private String fullPath;

    //initialize from file?
    private List<String> columnNames = new ArrayList<>();
    private List<List<Object>> data = new ArrayList<>();

    public Database(String tableName, boolean loadData) {
        this.tableName = tableName;
        fullPath = defaultFolder + "\\" + tableName + ".txt";

        if (loadData) {
            loadData();
        }
    }

    private void loadData() {
        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                //Put in list...
                System.out.println(line);
            }
            System.out.println();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void select(String command) { //Select Name, DateBirth FROM Sample WHERE Id <> 5 AND DateBirth > "01.01.2000"

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

    public List<String> getColumnTypeOrder() {
        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int counter = 1;
            List<String> columnTypeOrder = new ArrayList<>();
            String dataTypeWithDefault, dataType;
            while ((line = br.readLine()) != null) {
                if(counter == 2){ //2nd line -> columns
                    List<String> columns = Utility.split(line, '\t');
                    for (String column: columns){
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

    public List<String> getColumnOrder() {
        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int counter = 1;
            List<String> columnOrder = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if(counter == 2){ //2nd line -> columns
                    List<String> columns = Utility.split(line, '\t');
                    for (String column: columns){
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

    public List<String> getUserOrderColumnTypes(List<String> userInputColumnOrder) {
        List<String> userOrderColumnTypes = new ArrayList<>();
        List<String> columnTypeOrder = this.getColumnTypeOrder();
        List<String> columnOrder = this.getColumnOrder();

        int counter = 0;
        for(int i = 0; i < userInputColumnOrder.size(); i++) {
            for (String column : columnOrder) {
                if(column.equals(userInputColumnOrder.get(i))){
                    userOrderColumnTypes.add(columnTypeOrder.get(counter)); //to fix...
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
        try {
            for (int i = 0; i < columnOrder.size(); i++) {
                for (String column : columnOrder) {
                    if (column.equals(userInputColumnOrder.get(i))) {
                        sortedRow.add(userInput.get(counter));
                    }
                    counter++;
                }

                //Default value
                if (sortedRow.size() < i + 1) {
                    sortedRow.add(this.getDefaultValue(columnOrder.get(i)));
                }
                counter = 0;
            }
        }catch (IndexOutOfBoundsException e){
            sortedRow.add(this.getDefaultValue(columnOrder.get(userInputColumnOrder.size())));
        }
        return sortedRow;
    }

    private String getDefaultValue(String userColumn) {
        List<String> columnOrder = this.getColumnOrder();

        int selectedColumnIndex = 0;
        for (int i = 0; i < columnOrder.size(); i++){
            if(columnOrder.get(i).equals(userColumn)){
                selectedColumnIndex = i;
            }
        }

        try {
            File file = new File(this.getFullPath());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            int counter = 1;
            while ((line = br.readLine()) != null) {
                if(counter == 2) { //2nd line -> columns
                   try {
                       List<String> columns = Utility.split(line, '\t');
                       String selectedColumn = columns.get(selectedColumnIndex);
                       List<String> columnData = Utility.split(selectedColumn, new char[]{' ', ':'});
                       if (columnData.size() > 2 && columnData.get(2).equals("default")) {
                           String defaultValue = columnData.get(3);
                           return defaultValue;
                       }
                   }catch (IndexOutOfBoundsException e) {
                       //switch case via column name and column type (int -> 0, date -> null, string -> null?)
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
}
