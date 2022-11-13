public class Database {
    private String tableName;
    private String defaultFolder = "D:\\test\\databaseEditor";
    private String fullPath;

    //Setup db obj via databaseEditor and edit / insert and delete from here...
    public Database(String tableName){
        this.tableName = tableName;
        fullPath = defaultFolder + "\\" + tableName + ".txt";
    }

    public void select(){ //Select Name, DateBirth FROM Sample WHERE Id <> 5 AND DateBirth > “01.01.2000”

    }

    public void delete(){

    }

    public void join(){

    }

    public void insert(String data){ //Insert INTO Sample (Id, Name) VALUES (1, “Иван”)
     //Validate data...
    }


    public String getTableName() {
        return tableName;
    }

    public String getFullPath(){
        return fullPath;
    }
}
