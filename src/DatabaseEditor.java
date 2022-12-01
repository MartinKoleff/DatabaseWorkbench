import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseEditor {
    private static String defaultFolder = "D:\\test\\databaseEditor";

    private Database selectedDatabase;
    private static List<Database> databases;

    public void createTable(String command) {
        setupData(command);
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

    public void dropTable(String command) {
        String tableName = command.split(" ")[1];
        if(databases.stream()
                .filter(e -> e.getTableName().equals(tableName))
                .collect(Collectors.toList())
                .size() > 0){

            File file = new File(tableName);
            file.delete();
            //get DB from list...
            //remove DB from list...
            //delete .txt file...
            //reload UI...
        }

//        fullPath = defaultFolder + "\\" + tableName + ".txt";
//        File file = new File(fullPath);
//        if (file.exists()){
//            file.delete();
//            System.out.printf("Table %s has been deleted.\n", tableName);
//        }else{
//            System.out.printf("Table with the name %s doesn't exist. Please try again with a different name.", tableName);
//        }
    }

    public void listTables(String command) {

    }

    public void tableInfo(String command) {

    }
}
