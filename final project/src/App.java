import conditions.Cases;
import helpers.GeneralHelpers;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {

        while(true){

            ArrayList<String> query = GeneralHelpers.getUserInput();
            
            Cases.checkAllCases(query);

          
            if(query.get(0).equalsIgnoreCase("insert")){

                GeneralHelpers.insertInfo(query);

            }

            
            if(query.get(0).equalsIgnoreCase("delete")){

                GeneralHelpers.deleteInfo(query);

            }


            if(query.get(0).equalsIgnoreCase("drop") && query.get(1).equalsIgnoreCase("table")){

                GeneralHelpers.dropTable(query);

            }

            if(query.get(0).equalsIgnoreCase("select")){

                GeneralHelpers.selectInfo(query);

            }

            

            String joinedStrings = String.join("", query);
            if(joinedStrings.equalsIgnoreCase("clear")){
               GeneralHelpers.clearConsole();
            }  

        }

     }

         
}


       


  
        


