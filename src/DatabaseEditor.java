import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DatabaseEditor { // implements Initializable
    private static String defaultFolderPath = "D:\\test\\databaseEditor";
    //private static String defaultFolderPath = "C:\\Users\\Martin\\IdeaProjects\\DatabaseWorkbench";
//    private static String defaultFolderPath = "C:\\Users\\Martin.Kolev\\Documents\\test\\databaseEditor";


    private static Database selectedDatabase;
    private File defaultFolder = new File(defaultFolderPath);
    private static List<Database> databases = new ArrayList<>();

    public static List<Database> getDatabases() {
        return databases;
    }

    public Database getSelectedDatabase() {
        return selectedDatabase;
    }

    public void setSelectedDatabase(Database database) {
        selectedDatabase = database;
    }

    public void createTable(String command) {
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
            String columnName, columnDataType, defaultValue;
            for (int i = 1; i < totalColumns; i++) {
                columnData = Utility.split(columnsData.get(i), new char[]{' ', ':'});
                columnName = columnData.get(0);
                columnDataType = columnData.get(1);
                if (columnData.size() > 2 && columnData.get(2).equals("default")) {
                    defaultValue = Utility.join(Utility.split(columnData.get(3), '\"'), "");
                    writeInFile(columnName + ':' + columnDataType + " default " + defaultValue + "\t", false);
                } else {
                    writeInFile(columnName + ':' + columnDataType + "\t", false);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
        writeInFile("", true);
    }

    //Run on launch (implement interface?)
    private void loadData(String path) {
        File folder;
        if (path.equals("default")) { //Used in listTables...
            folder = new File(defaultFolderPath);
        } else {
            folder = new File(path);
        }
        Database database;
        databases.clear();
        for (File file :
                folder.listFiles()) {
            database = new Database(Utility.split(file.getName(), '.').get(0), false);
            databases.add(database);
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

    public void insert(List<List<String>> sortedRows) {
        File file = new File(selectedDatabase.getFullPath());
        String currentRow;
        if (file.exists()) {
            for (List<String> sortedRow : sortedRows) {
                currentRow = Utility.concat(sortedRow, '\t');
                writeInFile(currentRow, true);
            }
        } else {
            System.out.println("Database doesn't exist! Please create it before trying to insert values.");
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

        for (Database database :
                databases) {
            System.out.println(database.getTableName());
        }
    }

    public void tableInfo(String command) {
        String tableName = Utility.split(command, ' ').get(1);
        for (int i = 0; i < defaultFolder.listFiles().length; i++) {
            if (defaultFolder.listFiles()[i].getName().equals(tableName + ".txt")) {
                File file = defaultFolder.listFiles()[i];
                FileTime creationTime = null;
                long fileSizeBytes = 0;
                try {
                     creationTime = (FileTime) Files.getAttribute(Path.of(file.getPath()), "creationTime");
                   fileSizeBytes = Files.size(Path.of(file.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(file.getName() + "\n"
                        + fileSizeBytes / 1024 + "\n" //kb
                        + creationTime);
                return;
//                Database database = new Database(tableName, false);
//                return;
            }
        }
    }
}
