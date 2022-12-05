import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatabaseEditor { // implements Initializable
    //    private static String defaultFolder = "D:\\test\\databaseEditor";
    private static String defaultFolderPath = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";
    private static Database selectedDatabase;
    private File defaultFolder = new File(defaultFolderPath);
    private static List<Database> databases = new ArrayList<>();

    public static List<Database> getDatabases() {
        return databases;
    }

    public Database getSelectedDatabase(){
        return selectedDatabase;
    }
    public void createTable(String command) {
        setupData(command);
    }

    //Run on launch (implement interface?)
    private void loadData(String path) {
        File folder;
        if (path.equals("default")) {
            folder = new File(defaultFolderPath);
        } else {
            folder = new File(path);
        }
        Database database;
        databases.clear();
        for (File file :
                folder.listFiles()) {
            database = new Database(Utility.split(file.getName(), '.').get(0), true);
            databases.add(database);
        }
    }

    private void setupData(String command) { //CreateTable Sample(Id:int, Name:string, BirthDate:date default "01.01.2022")
        List<String> dataRaw = Utility.split(command, new char[]{'(', ':', ',', ' ', '\"', ')'});
        selectedDatabase = new Database(dataRaw.get(1), false);
        databases.add(selectedDatabase);

        //Create file
        File file = new File(selectedDatabase.getFullPath());
        file.delete();
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

        try {
            int totalColumns = Utility.split(command, ' ').size() + 1;
            List<String> columnsData = Utility.split(command, new char[]{'(', ',', ')'});
            List<String> columnData;
            String columnName, columnDataType, defaultValue = "null";
            for (int i = 1; i < totalColumns; i++) {
                columnData = Utility.split(columnsData.get(i), new char[]{' ', ':'});
                columnName = columnData.get(0);
                columnDataType = columnData.get(1);
                if (columnData.size() > 2 && columnData.get(2).equals("default")) {
                    defaultValue = Utility.join(Utility.split(columnData.get(3), '\"'), "");
                }

                writeInFile(columnName + ':' + defaultValue + "\t", false);
            }
        }catch (IndexOutOfBoundsException e){
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

        for (Database database : databases) {
            if (database.getTableName().equals(tableName)) {
                databaseToDelete = database;
                break;
            }
        }

        if (databaseToDelete != null) {
            databaseToDelete.deleteDatabase();
            //reload UI...
        }
    }

    public void listTables(String command) {
        String path = Utility.split(command, ' ').get(1);
        loadData(path);

//        for (Database database :
//                databases) {
//            System.out.println(database.getTableName());
//        }
    }

    public void tableInfo(String command) {
        String tableName = Utility.split(command, ' ').get(1);
        for (int i = 0; i < defaultFolder.listFiles().length; i++) {
            if (defaultFolder.listFiles()[i].getName().equals(tableName + ".txt")) {
                Database database = new Database(tableName, true);
                return;
            }
        }
    }
}
