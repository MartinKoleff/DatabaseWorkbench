import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseEditor{ // implements Initializable
//    private static String defaultFolder = "D:\\test\\databaseEditor";
    private static String defaultFolder = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";
    private Database selectedDatabase;
    public static List<Database> databases = new ArrayList<>(); //encapsulate...

    public void createTable(String command) {
        setupData(command);
    }

    //Run on launch (implement interface?)
    private void loadData(){
        //read default folder
        //get data...
    }

    private void setupData(String command) { //	CreateTable Sample(Id:int, Name:string, BirthDate:date default “01.01.2022”)
        List<String> dataRaw = Arrays.asList(command.split("[(: ,\")]"));
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
        String[] columnsData = command.split("[(,)]");
        String[] columnData;
        String columnName, columnDataType, defaultValue = "null";
        for (int i = 1; i < totalColumns; i++) {
            columnData = Arrays.stream(columnsData[i].split("[ :]")) //split by \" doesnt work...
                    .filter(e -> !e.isEmpty())
                    .toArray(String[]::new);
            columnName = columnData[0];
            columnDataType = columnData[1];
            if (columnData.length > 2 && columnData[2].equals("default")) {
                defaultValue = String.join("", columnData[3].split("\""));
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
