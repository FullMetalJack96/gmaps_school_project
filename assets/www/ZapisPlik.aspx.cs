using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.IO;
using System.Text; 

public partial class ZapisPlik : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        Response.AppendHeader("Access-Control-Allow-Origin", "*");
        string trasa = Request["trasa"];
        SaveFile(trasa);
       

    }
    private string TXT_PATH = HttpContext.Current.Server.MapPath("files/pliki.txt");

    private void SaveFile(string trasa){
        try{
        StreamWriter writer = new StreamWriter(TXT_PATH, true, Encoding.Default);
        writer.WriteLine(trasa+",");
        writer.Close(); 
        Response.Write("DANE ZAPISANE");
        }
    catch(Exception ex){
        Response.Write("BŁĄD ZAPISU"+ex.Message);
    }
}
}