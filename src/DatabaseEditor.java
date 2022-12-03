import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseEditor{ // implements Initializable
    //private static String defaultFolder = "D:\\test\\databaseEditor";
    private static String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";
    private Database selectedDatabase;
    private static List<Database> databases = new ArrayList<>();

    public static List<Database> getDatabases(){
        return databases;
    }

    public void createTable(String command) {
        setupData(command);
    }

    //Run on launch (implement interface?)
    private void loadData(){
        //read default folder
        //get data... (initialize databases...)
    }

    private void setupData(String command) { //CreateTable Sample(Id:int, Name:string, BirthDate:date default “01.01.2022”)
        List<String> dataRaw = Utility.split(command, new char[]{'(', ':', ',', '\"', ')'});
        selectedDatabase = new Database(dataRaw.get(1));
        databases.add(selectedDatabase);

        //Create file
        File file = new File(selectedDatabase.getFullPath());
        try {
            file.createNewFile();

            //Clears the file... (Find better way)
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Write table name on first row
        writeInFile(selectedDatabase.getTableName(), true);

        int totalColumns = command.split(",").length + 1;
        List<String> columnsData = Utility.split(command, new char[]{'(', ',', ')'});
        List<String> columnData;
        String columnName, columnDataType, defaultValue = "null";
        for (int i = 1; i < totalColumns; i++) {
            columnData = Utility.split(columnsData.get(i), new char[]{' ', ':'});
//                    .stream() //split by \" doesnt work...
//                    .filter(e -> !e.isEmpty())
//                    .toArray(String[]::new);
            columnName = columnData.get(0);
            columnDataType = columnData.get(1);
            if (columnData.size() > 2 && columnData.get(2).equals("default")) {
                defaultValue = Utility.join(Utility.split(columnData.get(3), '\"'), "");
            }

            writeInFile(columnName + ':' + defaultValue + "\t", false);
        }
    }

    private void writeInFile(String text, boolean hasNewLine) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedDatabase.getFullPath(), true));
            writer.write(text);
            if (hasNewLine) {
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dropTable(String command) { //DropTable Sample
        String tableName = Utility.split(command, ' ').get(0);
        Database databaseToDelete = null;

        for (Database database: databases) {
            if(database.getTableName().equals(tableName)){
                databaseToDelete = database;
                break;
            }
        }

        if(databaseToDelete != null){
            databaseToDelete.delete();
            //reload UI...
        }
    }

    public void listTables(String command) {

    }

    public void tableInfo(String command) {

    }
}
