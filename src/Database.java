import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private String tableName;
    private String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";
    private String fullPath;

    //initialize from file?
    private List<String> columnNames = new ArrayList<>();
    private List<List<Object>> data = new ArrayList<>();

    //Setup db obj via databaseEditor and edit / insert and delete from here...
    public Database(String tableName){
        this.tableName = tableName;
        fullPath = defaultFolder + "\\" + tableName + ".txt";
    }

    public void select(){ //Select Name, DateBirth FROM Sample WHERE Id <> 5 AND DateBirth > “01.01.2000”

    }

    public void delete(){
        File file = new File(this.getFullPath());
        if (file.exists()) {
            file.delete();
            DatabaseEditor.databases.remove(this);
            System.out.printf("Table %s has been deleted.\n", tableName);
        } else {
            System.out.printf("Table with the name %s doesn't exist. Please try again with a different name.", tableName);
        }
    }

    public void join(){

    }

    public void insert(String data){ //Insert INTO Sample (Id, Name) VALUES (1, “Иван”)
     //Validate data...
    }


    public String getTableName() {
        return tableName;
    }

    public String getFullPath(){
        return fullPath;
    }
}
