using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class Requesty : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        string imie = Request["imie"];
        string naz = Request["naz"];
        //zabezpieczenia 
        if (imie != null && naz != null)
        {
            Response.Write("Witaj "+imie+" "+naz);
        }
        else
        {
            Response.Write("Brak danych");
        }

    }
}