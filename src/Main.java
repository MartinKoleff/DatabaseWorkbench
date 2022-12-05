import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseEditor databaseEditor = new DatabaseEditor();

        Scanner sc = new Scanner(System.in);
        String command =  sc.nextLine();
        String commandType;
        Database selectedDatabase;
        while (!command.equals("Stop")) {
            commandType = Utility.split(command, ' ').get(0); //Utility.toLowerCase()
            switch (commandType) {
                case "CreateTable": //CreateTable Sample(Id:int, Name:string, BirthDate:date default "01.01.2022")
                    databaseEditor.createTable(command);
                    break;
                case "DropTable": //DropTable Sample
                    databaseEditor.dropTable(command);
                    break;
                case "ListTables": //ListTables default
                    databaseEditor.listTables(command);
                    break;
                case "TableInfo": //TableInfo Sample
                    databaseEditor.tableInfo(command);
                    break;
                case "Insert":  //Insert INTO Sample (Id, Name) VALUES (1, "Иван")
                    selectedDatabase = new Database(Utility.split(command, ' ').get(2), false);
                    //databaseEditor.getSelectedDatabase().insert(command);
                    selectedDatabase.insert(command);
                    break;
                case "Select": //Select Name, DateBirth FROM Sample WHERE Id <> 5 AND DateBirth > "01.01.2000"
                    List<String> dataRaw = Utility.split(command, new char[]{' ', ',', '\"'});

                    int fromIndex = dataRaw.indexOf("FROM");
                    String tableName = dataRaw.get(fromIndex + 1);
                    selectedDatabase = new Database(tableName, false);

                    selectedDatabase.select(command);
                    break;
                case "Join":
                    break;
                case "Delete":
                    break;
                case "CreateIndex":
                    break;
                case "DropIndex":
                    break;
                default:
                    System.out.println("Wrong command. Please try again");
            }


            command = sc.nextLine();
        }
//        databaseEditor.createTable("CreateTable Sample(Id:int, Name:string, BirthDate:date default “01.01.2022”)");
//        databaseEditor.dropTable("Sample");
    }
}
