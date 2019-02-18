/**
 *
 *  author  :   Chia Yuan Lin (林家源)
 *
 *  email   :   lo919201@gmail.com
 *
 * **/
public class FileType
{
    private String File_name;	// 檔名
    private String File_type;	// 檔形態->1.資料夾folder 2. 文件檔 3.disk 4.User_folder
    private int number;		// 第幾創建檔
    private int table_where;	// 所屬目錄
    public void setTable_where(int table_where) {
        this.table_where = table_where;
    }

    private boolean Ok;



    public FileType()
    {}

    public FileType(String name ,String type,int num,int ta,boolean ok)
    {
        File_name= name;
        File_type=type;
        number= num;
        table_where = ta;
        Ok=ok;
    }

    public void setOk(boolean ok) {
        Ok = ok;
    }

    public boolean isOk() {
        return Ok;
    }

    public String getFile_name() {
        return File_name;
    }

    public String getFile_type() {
        return File_type;
    }

    public int getNumber() {
        return number;
    }

    public int getTable_where() {
        return table_where;
    }

    public String toString()
    {
        return(File_name + "." +File_type);
    }
}
