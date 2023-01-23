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
                case "CreateTable":
                    databaseEditor.createTable(command);
                    break;
                case "DropTable":
                    databaseEditor.dropTable(command);
                    break;
                case "ListTables":
                    databaseEditor.listTables(command);
                    break;
                case "TableInfo":
                    databaseEditor.tableInfo(command);
                    break;
                case "Insert":
                    dataRaw = Utility.split(command, new char[]{' ', ',', '\"', '(', ')'});
                    tableName = dataRaw.get(2);

                    selectedDatabase = new Database(tableName, false);
                    databaseEditor.setSelectedDatabase(selectedDatabase);

                    List<List<String>> rows = new ArrayList<>();
                    List<String> dataRaw2 = Utility.trimList(
                            Utility.split(command, new char[]{'(', ')'}));

                    List<String> userInputColumnOrder;
                    List<String> userInputData;
                    List<String> userInputColumnTypes;
                    try {
                        if (!dataRaw.get(1).equals("INTO") && !dataRaw2.get(2).equals(" VALUES ")) return;

                        //Add multiple rows...
                        for (int i = 3; i < dataRaw2.size(); i++) {
                            userInputColumnOrder = Utility.split(dataRaw2.get(1), new char[]{' ', ','});
                            userInputData = Utility.split(dataRaw2.get(i), new char[]{' ', ','});
                            userInputColumnTypes = selectedDatabase.getUserOrderColumnTypes(userInputColumnOrder);

                            //All non-default columns are used...
                            if (!selectedDatabase.defaultColumnsAreUsed(userInputColumnOrder)) {
                                continue;
                            }

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
                case "Select":
                    dataRaw = Utility.split(command, new char[]{' ', ',', '\"', '(', ')'});

                    //Validate command...
                    if (!dataRaw.contains("FROM")) {
                        break;
                    }

                    int fromIndex = dataRaw.indexOf("FROM");
                    tableName = dataRaw.get(fromIndex + 1);

                    selectedDatabase = new Database(tableName, false);
                    databaseEditor.setSelectedDatabase(selectedDatabase);
                    List<String> selectedColumns;
                    try {
                        selectedColumns = Utility.split(Utility.split(command, new char[]{'(', ')'}).get(1), new char[]{' ', ','});
                    } catch (IndexOutOfBoundsException e) { //*
                        selectedColumns = new ArrayList<>();
                        selectedColumns.add("*");
                    }

                    List<String> whereConditions;
                    try {
                         whereConditions = Utility.split(Utility.split(command, " WHERE ").get(1), " AND "); //case when BETWEEN AND how to handle?
                    }catch (IndexOutOfBoundsException e){
                        whereConditions = new ArrayList<>();
                    }
//                    List<String> whereConditions = Utility.split(command, new String[]{"WHERE", "AND"});
                    //Utility.split("Id <> 5", " AND ")

                    selectedDatabase.select(selectedColumns, whereConditions);
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
//CreateTable Sample(Id:int, Name:string, BirthDate:date default "01.01.2022")
//DropTable Sample
//ListTables default
//TableInfo Sample
//Insert INTO Sample (Id, Name) VALUES (1, "Иван")
//Insert INTO Sample (Name, Id) VALUES ("Mikhail", 2) ("Pedro", 3)
//Insert INTO Sample (Name, Id) VALUES ("Ivan", 2) (3, "Messi") //Invalid command...
//Insert INTO Sample (Name, Id) VALUES ("Ivan", 2, 01.01.2002)  //Invalid command...
//Insert INTO Sample (Name) VALUES ("Mikhail") ("Pedro") //Invalid command...
//Insert INTO Sample (Id, Name) VALUES () () //Invalid command...
//Insert INTO Sample (Name) VALUES () () //Invalid command...
//Select (Name, DateBirth) FROM Sample WHERE Id <> 5 AND DateBirth > "01.01.2000"
//Select (Name, DateBirth) FROM Sample WHERE Id <> 5
//Select (Name, DateBirth) FROM Sample
//Insert INTO Sample (Id, Name, BirthDate) VALUES (1, "Иван", 01.01.2002)
//Select (Name, DateBirth) FROM Sample WHERE BirthDate > "01.01.2000"
//Select * FROM Sample
//Select (Id, Name) FROM Sample
//<> -> different from