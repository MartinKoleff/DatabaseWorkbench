import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseEditor databaseEditor = new DatabaseEditor();

        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        String commandType;
        while (!command.equals("Stop")) {
            commandType = Utility.toLowerCase(Utility.split(command, ' ').get(0));
            switch (commandType) {
                case "createtable":
                    databaseEditor.createTable(command); //"CreateTable Sample(Id:int, Name:string, BirthDate:date default “01.01.2022”)"
                    break;
                case "droptable":
                    databaseEditor.dropTable(command);
                    break;
                case "listtables":
                    databaseEditor.listTables(command);
                    break;
                case "tableinfo":
                    databaseEditor.tableInfo(command);
                    break;
                case "insert":
                    break;
                case "select":
                    break;
                case "join":
                    break;
                case "delete":
                    break;
                case "createindex":
                    break;
                case "dropindex":
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
