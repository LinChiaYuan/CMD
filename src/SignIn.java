/**
 *
 *  author  :   Chia Yuan Lin (林家源)
 *
 *  email   :   lo919201@gmail.com
 *
 * **/
public class SignIn
{

    private String password;	//±K½X
    private String account;		//±b¸¹
    private int Competence;	//Åv­­

    public String getPassword() {
        return password;
    }

    public String getAccount() {
        return account;
    }

    public int getCompetence() {
        return Competence;
    }

    public SignIn(String act,String psw,int com)
    {
        password = psw;
        account = act;
        Competence = com;
    }
    public SignIn()
    {
        password ="";
        account = "";
        Competence =0;
    }




}
