using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.IO; 
using System.Text; 

public partial class odczytDanych : System.Web.UI.Page
{
  
          private string TXT_PATH = HttpContext.Current.Server.MapPath("files/pliki.txt");
          protected void Page_Load(object sender, EventArgs e){
              Response.AppendHeader("Access-Control-Allow-Origin", "*");

              StreamReader reader = new StreamReader(TXT_PATH, Encoding.Default);
              string all = reader.ReadToEnd();

              string get = "[" + all.Substring(0, all.Length - 3) + "]"; 

              reader.Close();

              Response.Write(get);
          }

 }
