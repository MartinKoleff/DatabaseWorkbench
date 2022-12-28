import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseEditor databaseEditor = new DatabaseEditor();

        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        String commandType;
        Database selectedDatabase;
        List<String> dataRaw;
        String tableName;
        while (!command.equals("Stop")) {
            commandType = Utility.split(command, ' ').get(0); //Utility.toLowerCase()
            switch (commandType) {
                case "CreateTable": //CreateTable Sample(Id:int, Name:string, BirthDate:date default "01.01.2022")
                    //To refactor as insert...

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
                case "Insert":  //Insert INTO Sample (Id, Name) VALUES (1, "Иван") //Insert INTO Sample (Name, Id) VALUES ("Mikhail", 2) ("Pedro", 3)
                    //Split the strings from the start...
                    dataRaw = Utility.split(command, new char[]{' ', ',', '\"', '(', ')'});
                    tableName = dataRaw.get(2);

                    selectedDatabase = new Database(tableName, false);
                    databaseEditor.setSelectedDatabase(selectedDatabase);

                    List<List<String>> rows = new ArrayList<>();
                    List<String> dataRaw2 = Utility.trimList(
                            Utility.split(command, new char[]{'(', ')'}));

                    List<String> userInputColumnOrder = null;
                    List<String> userInputData;
                    List<String> userInputColumnTypes;
                    try {
                        if (!dataRaw.get(1).equals("INTO") && !dataRaw2.get(2).equals(" VALUES ")) return;

                        //Add multiple rows...
                        for (int i = 3; i < dataRaw2.size(); i++) {
                            userInputColumnOrder = Utility.split(dataRaw2.get(1), new char[]{' ', ','});
                            userInputData = Utility.split(dataRaw2.get(i), new char[]{' ', ','});
                            userInputColumnTypes = selectedDatabase.getUserOrderColumnTypes(userInputColumnOrder);

                            if (!selectedDatabase.defaultColumnsAreUsed(userInputColumnOrder)) {
                                continue;
                            }

                            //get defaults split counter...
                            if (userInputColumnOrder.size() == userInputData.size()
                                    && Utility.parser.tryParse(userInputData, userInputColumnTypes)) {
                                rows.add(selectedDatabase.orderUserInput(userInputData, userInputColumnOrder));
                            } else {
                                System.out.printf("Invalid input %s\n", dataRaw2.get(i)); //Adds only the valid inputs...
                            }
                        }

                        if (rows.isEmpty()) {
                            throw new Exception();
                        }

                        databaseEditor.insert(rows);
                        rows.clear();
                    } catch (Exception e) {
                        System.out.println("Invalid command. Please try again.");
                        break;
                    }
                    break;
                case "Select": //Select Name, DateBirth FROM Sample WHERE Id <> 5 AND DateBirth > "01.01.2000"
                    dataRaw = Utility.split(command, new char[]{' ', ',', '\"'});

                    int fromIndex = dataRaw.indexOf("FROM");
                    tableName = dataRaw.get(fromIndex + 1);
                    selectedDatabase = new Database(tableName, false);
                    databaseEditor.setSelectedDatabase(selectedDatabase);

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
    }
}
