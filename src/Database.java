import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Setup DB obj via databaseEditor and edit / insert and delete from here...
public class Database {
    private String tableName;
    //    private String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";
//    private static String defaultFolder = "D:\\test\\databaseEditor";
    private static String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";

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
}
