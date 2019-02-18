/**
 *
 *  author  :   Chia Yuan Lin (林家源)
 *
 *  email   :   lo919201@gmail.com
 *
 * **/
public class SearchFile
{
    private String search_File_name;	// 檔名
    private int search_search_number;	// 第幾創建檔
    public String getSearch_File_name()
    {
        return search_File_name;
    }

    public int getSearch_search_number()
    {
        return search_search_number;
    }

    public int getSearch_table_where()
    {
        return search_table_where;
    }

    private int search_table_where;	// 所屬目錄

    public SearchFile(String name ,int num,int ta)
    {
        search_File_name= name;
        search_search_number= num;
        search_table_where = ta;
    }
}
