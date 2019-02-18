/**
 *
 *  author  :   Chia Yuan Lin (林家源)
 *
 *  email   :   lo919201@gmail.com
 *
 * **/
import java.util.Scanner;


public class CMD
{

    public static void main(String[] args)
    {
        SignIn client = new SignIn("aclient","bclient",1);
        SignIn ore = new SignIn("aore","bore",2);
        Scanner Scan = new Scanner(System.in);
        String User_Account;
        int Competence=0;
        /*********************************     登入   ********************************************/
        while(true)
        {
            System.out.print("ID:");
            String str1 = Scan.nextLine();
            if( client.getAccount().equals(str1))	//client 登入
            {
                System.out.print("Password:");
                String str2 = Scan.nextLine();
                if(client.getPassword().equals(str2))
                {
                    User_Account = client.getAccount();	//把User後傳
                    Competence = client.getCompetence();
                    break;
                }
            }
            else if(ore.getAccount().equals(str1))	//自己登入
            {
                System.out.print("Password:");
                String str3 = Scan.nextLine();
                if(ore.getPassword().equals(str3))
                {
                    User_Account = ore.getAccount();;	//把User後傳
                    Competence = ore.getCompetence();
                    break;
                }
            }
        }

        /*********************************     系統建構  ********************************************/
        int[] table=new int[100];						//	目錄	<C槽起點>
        int table_num= 2;								//	目錄的檔數量
        int File_All_num=2;								//	總檔案數量 < 1.C槽 2.User >
        FileType[] File = new FileType[10000];			//	總檔案容量 10000 - 1  ( 第0 不用)
        File[1] = new FileType("C","disk",1,0,true);			// 	創C槽
        File[2] = new FileType("User_"+User_Account,"folder",2,1,true);	//	創User資料夾
        table[0] = File[1].getNumber();					//	目錄加入C槽	= 1
        table[1] = File[2].getNumber();					//	目錄加入User = 2
        SearchFile[] Search_File = new SearchFile[10000];	//	尋找用的
        int Search_num= 0;									//	尋找用的
        int inode=2000,block=1000;							// inode block
        /*********************************     CMD   ********************************************/
        Table_print(table,table_num,File);
        SystemComputer SysComp = new SystemComputer();
        while(true)
        {
            String str4="";
            str4 = Scan.nextLine();
            String[] tokens = str4.split(" ");
            if(SysComp.Sys_Com(tokens))
            {
                SysComp.Sys_pwd(tokens, table, table_num,File,File_All_num);		// pwd -> 顯示工作路徑
                if(tokens[0].equals("mkdir") && Competence>1 && tokens.length==2)				// mkdir -> 建立目錄
                {
                    if(SysComp.Sys_mkdir(tokens, table, table_num,File,File_All_num))
                    {
                        File_All_num++;
                        File[File_All_num] = new FileType(tokens[1],"folder",File_All_num,File[table[table_num -1]].getNumber(),true);//檔案[目錄[目錄數量-1]].創號

                        if((inode-2)>=0)			//增加資料夾 inode-2 block-1
                        {
                            if((block-1)>=0)
                            {
                                inode=inode-2;
                                block--;
                            }
                            else
                                System.out.println("block 已滿");
                        }
                        else
                            System.out.println("inode 已滿");
                    }
                    else
                        System.out.println("已存在");
                }
                else if(tokens[0].equals("rmdir")&& Competence>1 && tokens.length==2)							//2 rmdir -> 刪除目錄
                {
                    if(SysComp.Sys_rmdir(tokens, table, table_num,File,File_All_num))
                    {
                        int move = 0 ,folder_num=0,file_num=0;
                        for(int i=1;i<=File_All_num;i++)
                        {
                            if(tokens[1].equals(File[i].getFile_name()) && File[i].getTable_where() == table[table_num -1])	// 在目前目錄底下 + 同檔名
                                move=i;
                        }
                        if(move>2)
                        {
                            if(File[move].getFile_type().equals("folder"))
                            {
                                //被尋找目錄名 , 被尋找目錄編號 , 被尋找目錄所屬目錄
                                Search_File[1] = new SearchFile(File[move].getFile_name() ,move,File[move].getTable_where());
                                int count =0;	//需要找幾次
                                Search_num = 1;	//計錄尋找檔案數量
                                do
                                {
                                    count++;
                                    for(int j =count ; j<=Search_num;j++)	//尋找之目錄Search_File[j]
                                    {
                                        for(int i =1 ;i<=File_All_num ;i ++)	//從頭開始找屬於目前收尋目錄底之資料
                                        {
                                            if(File[i].getTable_where() == Search_File[j].getSearch_search_number() && File[i].isOk())	//所屬收尋目錄 == 目前目錄
                                            {
                                                Search_num++;	//	有找到檔案+1
                                                Search_File[Search_num]=new SearchFile(File[i].getFile_name() ,i,File[i].getTable_where());
                                                //等等需遞回被尋找目錄名 , 被尋找目錄編號 , 被尋找目錄所屬目錄
                                                if(File[i].getFile_type().equals("folder"))
                                                    folder_num++;
                                                else if(File[i].getFile_type().equals("files"))
                                                    file_num++;
                                            }

                                        }
                                        count = j;	//已找到哪個位置了
                                    }

                                }while(count != Search_num);
                                folder_num++;
                            }
                            else if(File[move].getFile_type().equals("files"))
                                file_num++;
                            inode = inode + file_num + folder_num*2;
                            block = block + file_num + folder_num;
                            File[move].setOk(false);	//把檔改成不存在
                        }
                        else
                            System.out.println("此目錄不能刪");

                    }
                    else
                        System.out.println("不存在此目錄");
                }
                else if(tokens[0].equals("cd") && tokens.length==2)								//3 cd -> 切換工作目錄
                {
                    if(SysComp.Sys_cd(tokens, table, table_num,File,File_All_num)==1)	//往下跳
                    {
                        for(int i=1;i<=File_All_num;i++)
                        {
                            if(tokens[1].equals(File[i].getFile_name()) && File[i].getTable_where() == table[table_num -1])	// 在目前目錄底下 + 同檔名
                                table[table_num]=File[i].getNumber();
                        }
                        table_num++;
                    }
                    else if(SysComp.Sys_cd(tokens, table, table_num,File,File_All_num)==2)	//往前跳
                    {

                        table_num--;
                    }
                    else
                        System.out.println("目的不存在");
                }
                else if(tokens[0].equals("ls")&& tokens.length==1)				// ls -> 顯示目錄下所有檔案
                {
                    for(int i =1 ;i<=File_All_num ;i ++)
                    {
                        if(File[i].getTable_where() == table[table_num -1] && File[i].isOk())	//所屬目錄 == 目前目錄
                        {
                            System.out.print(File[i].getFile_name()+" ");
                        }
                    }
                    System.out.println();
                }
                else if(tokens[0].equals("cp")&& Competence>1 && tokens.length==3)		// cp複製檔案	cp a -b-f-g 把a複製到g
                {
                    String[] tokens2 = tokens[2].split("-");	// 切割"-"
                    int folder_num=0,file_num=0;
                    if(SysComp.Sys_cp(tokens, table, table_num,File,File_All_num))
                    {
                        if(SysComp.Sys_cp2(tokens2, table, table_num,File,File_All_num))
                        {
                            int temp = SysComp.Search_cp2(tokens, table, table_num,File,File_All_num);	//要cp的檔案編號
                            Search_File[1] = new SearchFile(File[temp].getFile_name() ,temp,File[temp].getTable_where());
                            int count =0;	//需要找幾次
                            Search_num = 1;	//計錄尋找檔案數量
                            do{
                                count++;
                                for(int j =count ; j<=Search_num;j++)	//尋找之目錄Search_File[j]
                                {
                                    for(int i =1 ;i<=File_All_num ;i ++)	//從頭開始找屬於目前收尋目錄底之資料
                                    {
                                        if(File[i].getTable_where() == Search_File[j].getSearch_search_number() && File[i].isOk())	//所屬收尋目錄 == 目前目錄
                                        {
                                            Search_num++;	//	有找到檔案+1
                                            Search_File[Search_num]=new SearchFile(File[i].getFile_name() ,i,File[i].getTable_where());
                                            //等等需遞回被尋找目錄名 , 被尋找目錄編號 , 被尋找目錄所屬目錄
                                        }
                                    }
                                    count = j;	//已找到哪個位置了
                                }

                            }while(count != Search_num);
                        }
                        else
                            System.out.println("error");
                    }
                    else
                        System.out.println("error");
                    int cp_here=0;
                    cp_here = SysComp.Search_cp(tokens2, table, table_num,File,File_All_num);
                    if(cp_here==0)
                        System.out.println("error");
                    else
                    {
                        int jump =0,first=0;
                        first=File_All_num;				// first 未串上cp的數量
                        for(int z= 1;z<=Search_num;z++)
                        {
                            if(z==1)			// 判斷是不是第一個
                                jump=cp_here;
                            else
                            {
                                for(int k= 1;k<=Search_num;k++)		// 尋找所屬目錄
                                {
                                    if(Search_File[k].getSearch_search_number()==Search_File[z].getSearch_table_where())
                                        jump=File[k+first].getNumber();
                                }
                            }
                            if(File[Search_File[z].getSearch_search_number()].getFile_type().equals("files"))	//如果是檔案
                            {
                                File_All_num++;
                                File[File_All_num] = new FileType(Search_File[z].getSearch_File_name(),"files",File_All_num,jump,true);//檔案[目錄[目錄數量-1]].創號
                                file_num++;
                            }
                            else
                            {
                                File_All_num++;
                                File[File_All_num] = new FileType(Search_File[z].getSearch_File_name(),"folder",File_All_num,jump,true);//檔案[目錄[目錄數量-1]].創號
                                folder_num++;
                            }
                        }
                    }
                    inode = inode - file_num - folder_num*2;
                    block = block - file_num - folder_num;
                }
                else if(tokens[0].equals("mv")&& Competence>1 && tokens.length== 3)		// mv移動檔案
                {
                    String[] tokens2 = tokens[2].split("-");	// 切割"/"
                    if(SysComp.Sys_cp(tokens, table, table_num,File,File_All_num))
                    {
                        if(SysComp.Sys_cp2(tokens2, table, table_num,File,File_All_num))
                        {
                            int temp = SysComp.Search_cp2(tokens, table, table_num,File,File_All_num);	//要cp的檔案編號
                            int mv_here=0;
                            mv_here = SysComp.Search_cp(tokens2, table, table_num,File,File_All_num);
                            File[temp].setTable_where(mv_here);
                        }
                        else
                            System.out.println("error");
                    }
                    else
                        System.out.println("error");
                }
                else if(tokens[0].equals("findfirst") && tokens.length==1)		//  findfirst搜尋資料夾下之檔案
                {										//	被尋找目錄名 , 被尋找目錄編號 , 被尋找目錄所屬目錄
                    Search_File[1] = new SearchFile(File[table[table_num -1]].getFile_name() ,table[table_num -1],File[table[table_num -1]].getTable_where());
                    int count =0;	//需要找幾次
                    Search_num = 1;	//計錄尋找檔案數量
                    do{
                        count++;
                        for(int j =count ; j<=Search_num;j++)	//尋找之目錄Search_File[j]
                        {
                            for(int i =1 ;i<=File_All_num ;i ++)	//從頭開始找屬於目前收尋目錄底之資料
                            {
                                if(File[i].getTable_where() == Search_File[j].getSearch_search_number() && File[i].isOk())	//所屬收尋目錄 == 目前目錄
                                {
                                    Search_num++;	//	有找到檔案+1
                                    Search_File[Search_num]=new SearchFile(File[i].getFile_name() ,i,File[i].getTable_where());
                                }			//	等等需遞回被尋找目錄名 , 被尋找目錄編號 , 被尋找目錄所屬目錄
                            }
                            count = j;	//已找到哪個位置了
                        }

                    }while(count != Search_num);
                    for(int z=2;z<=Search_num;z++)
                        System.out.print(Search_File[z].getSearch_File_name()+" ");
                    System.out.println();
                }
                else if(tokens[0].equals("vi")&& Competence>1 && tokens.length==2)		// 創檔(非資料夾)
                {
                    if(SysComp.Sys_vi(tokens, table, table_num,File,File_All_num))	//輸入切割,目錄,目錄數量,檔案,檔案數
                    {
                        File_All_num++;
                        File[File_All_num] = new FileType(tokens[1],"files",File_All_num,File[table[table_num -1]].getNumber(),true);//檔案[目錄[目錄數量-1]].創號

                        if((inode-1)>=0)			//增加檔案  inode-2 block-1
                        {
                            if((block-1)>=0)
                            {
                                inode--;
                                block--;
                            }
                            else
                                System.out.println("block 已滿");
                        }
                        else
                            System.out.println("inode 已滿");
                    }
                    else
                        System.out.println("已存在");
                }
                else if(tokens[0].equals("read")&& Competence>1 && tokens.length==1)		// 顯示inode和block
                {
                    int inod_leave=2000-inode,block_leave=1000-block;
                    System.out.println("inode count = "+inode);
                    System.out.println("block count = "+block);
                    System.out.println("free inode = "+inod_leave);
                    System.out.println("free block = "+block_leave);
                }
                else if(Competence<2)
                    System.out.println("你沒資格");
                else
                    System.out.println("error");
            }
            else
                System.out.println("error");
            Table_print(table,table_num,File);
        }
    }
    public static void Table_print(int[] Table,int File_num,FileType[] File)
    {
        System.out.print(File[Table[0]].getFile_name()	+	":");	//第一個是C槽
        for(int i = 1 ;i<File_num;i++)
            System.out.print("\\" + File[Table[i]].getFile_name());
        System.out.print(">");
    }
}
