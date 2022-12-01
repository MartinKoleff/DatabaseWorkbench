import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseEditor databaseEditor = new DatabaseEditor();

        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        String commandType;
        while (!command.equals("Stop")) {
            commandType = Utility.split(command, ' ').get(0);
            switch (commandType) {
                case "CreateTable":
                    databaseEditor.createTable(command); //"CreateTable Sample(Id:int, Name:string, BirthDate:date default “01.01.2022”)"
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
                    break;
                case "Select":
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
