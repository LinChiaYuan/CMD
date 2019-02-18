/**
 *
 *  author  :   Chia Yuan Lin (林家源)
 *
 *  email   :   lo919201@gmail.com
 *
 * **/
public class SystemComputer
{
    String[] Sys_Con=
            {
                    "pwd",			//0 pwd -> 顯示工作路徑
                    "mkdir",		//1 mkdir -> 建立目錄
                    "rmdir",		//2 rmdir -> 刪除目錄
                    "cd",			//3 cd -> 切換工作目錄
                    "ls",			//4 ls -> 顯示目錄下所有檔案
                    "cp",			//5 cp -> 複製檔案
                    "mv",			//6 mv -> 移動檔案
                    "findfirst",	//7 findfirst -> 搜尋資料夾下之檔案
                    "vi",			//8 vi -> 創檔(非資料夾)
                    "read"			//9 read ->讀取inode和block
            };

    // 0
    public void Sys_pwd(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num) //輸入切割,目錄,目錄數量,檔案,檔案數
    {
        if(str[0].equals(Sys_Con[0]))					// pwd -> 顯示工作路徑
        {
            for(int i = 0 ;i<File_num;i++)
                System.out.print("\\" + File[Table[i]].getFile_name());
            System.out.println();
        }
    }
    // str[0] = 指令 ,str[1] = 第二切割 ,str[2] = 第三切割
    //8
    public boolean Sys_vi(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        for(int i=1 ; i<=File_All_num;i++)
        {
            if(str[1].equals(File[i].getFile_name()) && File[i].getTable_where() == Table[File_num -1] && File[i].isOk()) // 在目前目錄底下 + 同檔名 + 存在
            {
                return false;
            }
        }
        return true;
    }
    //5
    public boolean Sys_cp(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        for(int i=1 ; i<=File_All_num;i++)
        {
            if(str[1].equals(File[i].getFile_name()) && File[i].getTable_where() == Table[File_num -1] && File[i].isOk()) // 在目前目錄底下 + 同檔名 + 存在
            {
                return true;
            }
        }
        return false;
    }
    public boolean Sys_cp2(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        int temp=0; //存目前的目錄編號
        for(int j = 0 ;j<str.length;j ++)
        {
            for(int i=1 ; i<=File_All_num;i++)
            {										// 同檔名 + 存在  + 是資料夾
                if(str[j].equals(File[i].getFile_name()) && File[i].isOk()&& File[i].getFile_type().equals("folder"))
                {
                    if(temp == 0)
                    {
                        temp=File[i].getNumber();
                        continue;
                    }
                    else if(File[i].getTable_where() == temp)
                    {
                        temp=File[i].getNumber();
                        continue;
                    }
                    else
                        return false;
                }
            }
        }
        if(temp == 0)
            return false;
        return true;
    }
    public int Search_cp(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        int temp=0; //存目前的目錄編號
        for(int j = 0 ;j<str.length;j ++)
        {
            for(int i=1 ; i<=File_All_num;i++)
            {										// 同檔名 + 存在  + 是資料夾
                if(str[j].equals(File[i].getFile_name()) && File[i].isOk()&& File[i].getFile_type().equals("folder"))
                {
                    if(temp == 0)
                    {
                        temp=File[i].getNumber();
                        continue;
                    }
                    else if(File[i].getTable_where() == temp)
                    {
                        temp=File[i].getNumber();
                        continue;
                    }
                    else
                        return 0;
                }
            }
        }
        if(temp == 0)
            return 0;
        return temp;
    }
    public int Search_cp2(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        int a=0;
        for(int i=1 ; i<=File_All_num;i++) // 在目前目錄底下 + 同檔名 + 存在
        {
            if(str[1].equals(File[i].getFile_name()) && File[i].getTable_where() == Table[File_num -1]&& File[i].isOk())
                a=File[i].getNumber();
        }
        return a;
    }
    //2
    public boolean Sys_rmdir(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        for(int i=1 ; i<=File_All_num;i++)
        {
            if(str[1].equals(File[i].getFile_name()) && File[i].getTable_where() == Table[File_num -1] && File[i].isOk()) // 在目前目錄底下 + 同檔名 + 存在
            {
                return true;
            }
        }
        return false;
    }
    //3
    public int Sys_cd(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        for(int i=1 ; i<=File_All_num;i++) // 在目前目錄底下 + 同檔名 + 存在 + 是資料夾
        {
            if(str[1].equals(File[i].getFile_name()) && File[i].getTable_where() == Table[File_num -1]&& File[i].isOk() && File[i].getFile_type().equals("folder"))
                return 1;
        }
        if(str[1].equals("..")) // ..
            return 2;
        return 3;
    }
    //1
    public boolean Sys_mkdir(String[] str,int[] Table,int File_num ,FileType[] File,int File_All_num)
    {
        for(int i=1 ; i<=File_All_num;i++)
        {
            if(str[1].equals(File[i].getFile_name()) && File[i].getTable_where() == Table[File_num -1] && File[i].isOk()) // 在目前目錄底下 + 同檔名 + 存在
            {
                return false;
            }
        }
        return true;
    }
    //
    public boolean Sys_Com(String[] str)
    {
        for(int i=0 ; i<Sys_Con.length;i++)
        {
            if(str[0].equals(Sys_Con[i]))
            {
                return true;
            }
        }
        return false;
    }

}
