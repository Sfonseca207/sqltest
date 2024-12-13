package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.Tokenizer;

public class GeneralHelpers {

    
    
    static Scanner myScanner = new Scanner(System.in);

    public static ArrayList<String> getUserInput(){

        myScanner.useDelimiter(";");
        String userInput = myScanner.next();
        ArrayList<String> splitedUserInput = Tokenizer.tokenizeUserInput(userInput);
         return splitedUserInput;

    }

    //create tables

    public static void createTable(ArrayList<String> splittedQuery){

        if (splittedQuery.size() < 5) {
            
            System.out.println("Llave, escribe bien mlp");
            return;

        }
        
        String fileName = splittedQuery.get(2) + ".csv";
        String path = "src/myTables/";
        File directory = new File(path);

        try {
            
            File myTable = new File(directory+"\\"+fileName);
            new File(myTable.getParent()).mkdirs();
            if (myTable.createNewFile()) {

                String headers = "";

                for(int i = 3; i<splittedQuery.size(); i = i+2){


                        if (i == splittedQuery.size() - 2) {
                            
                            headers = headers + splittedQuery.get(i);

                            
                        }else{

                            headers = headers + splittedQuery.get(i) + ",";

                        }



                }

                FileWriter writer = new FileWriter(myTable);
                writer.write(headers);
                writer.close();
                //To do: Create properties

                System.out.println("Your table has been created succesfully!");
                getUserInput();
                
            }else{

                System.out.println("An error has ocurred while creating your table");
                getUserInput();


            }

          
        
        } catch (Exception e) {
            e.printStackTrace();
        }

                
        

        //trabajar aqui
        
    }

    //clear


    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }




    public static void closeProgram(){

        myScanner.close();
        
    }

    //list tables

    public static void listAbles(){

        String path = "src/myTables/";
        File directory = new File(path);

        File[] fileDirectory = directory.listFiles();
        

       for (int i = 0; i < fileDirectory.length; i++) {

            System.out.println(fileDirectory[i].getName());
        
       }

    }


    //insert first

    public static void insertInfo(ArrayList<String> query){
        

        String nameofTable = query.get(2);
        String path = "src/myTables/";
        File directory = new File(path);
        File[] fileDirectory = directory.listFiles();
        File sourceFile = new File(path + nameofTable + ".csv");
        
     

        for (int i = 0; i < fileDirectory.length; i++) {
 
             if(fileDirectory[i].getName().equalsIgnoreCase(nameofTable + ".csv")){  
                
                try{  

                    BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
                    String firstLine = reader.readLine();
                    System.out.println(firstLine);
                    reader.close();


                    String[] columns = firstLine.split(",");
                    String[] scrambledColumns = new String[columns.length];
                    String[] emptyArray = new String[columns.length];


                    for(int j = 0; j<query.size();j++){

                        if(query.get(j).equalsIgnoreCase(nameofTable)){ 
                            
                            for(int m = j; m<query.size();m++){ 
                                
                                if(query.get(m).equalsIgnoreCase("value")){ 

                                    List<String> subList = query.subList(j+1,m);
                                    scrambledColumns = subList.toArray(new String[0]);
                                    
                                }
                            }
                        }
            
                     } 

                     String[] UserString = OrganizeInsertedInfo(query);

                     for (int k = 0; k < columns.length; k++) {
                        for (int n = 0; n < columns.length; n ++) {
                            if (scrambledColumns[n].equalsIgnoreCase(columns[k])) {

                                emptyArray[k] = UserString[n]; 
                               
                            }
                        }
                     }
                     
                     String result = "";
        
                    
                    for (int counter = 0; counter < emptyArray.length; counter++) {
                       
                        result += emptyArray[counter]; 
                    
                          if (counter < emptyArray.length - 1) {
                    
                             result += ", "; 
                    
                            } 
                   }
                     

                FileWriter writer = new FileWriter("src/myTables/" + nameofTable + ".csv", true);
                
                if (sourceFile.length() > 0) {
                    writer.write("\n");
                }
                
                writer.write(result);
                writer.close();
                    
                }catch(IOException e){
                    e.printStackTrace();

                }
}
           
        }
 
    }

    //insert two

    public static String[] OrganizeInsertedInfo(ArrayList<String> query){

        for(int i = 0; i<query.size();i++){

            if(query.get(i).equalsIgnoreCase("value")){ 

                List<String> subList = query.subList(i+1,query.size());
                String[] ListToArray = subList.toArray(new String[0]);

                return ListToArray;

            }

         }

         return new String[0];

     }

    //drop tables

    public static void dropTable(ArrayList<String> query){


            String nameofTable = query.get(2);
            String path = "src/myTables/";
            File directory = new File(path);
            File[] fileDirectory = directory.listFiles();
            File deleteFile = new File(path + nameofTable + ".csv");

            for (int i = 0; i < fileDirectory.length; i++) {

                if(fileDirectory[i].getName().equalsIgnoreCase(nameofTable + ".csv")){ 

                    
                    deleteFile.delete();


                } 


            }

        


    }

    //delete info command

    public static class Condition {
        String column;
        String operator;
        String value;
    
        public Condition(String column, String operator, String value) {
            this.column = column;
            this.operator = operator;
            this.value = value;
        }
    }

    public static Condition parseWhere(ArrayList<String> query){


        int indexOfWhere = query.indexOf("where");

        if (indexOfWhere == -1 || indexOfWhere + 3 >= query.size()) {
            throw new IllegalArgumentException("WHERE inválido o faltante.");
        }
    

        String column = query.get(indexOfWhere + 1);
        String operator = query.get(indexOfWhere + 2);
        String value = query.get(indexOfWhere + 3);


        return new Condition(column, operator, value);

    }

    public static Boolean evaluate(Condition condition, Map<String, String> row){

        String columnValue = row.get(condition.column);

        if (columnValue == null) {
            return false;
        }

        switch (condition.operator) {

            case "=":

                return columnValue.equals(condition.value);

            case ">":

                return Integer.parseInt(columnValue) > Integer.parseInt(condition.value);

            case "<":

                return Integer.parseInt(columnValue) < Integer.parseInt(condition.value);

            case ">=":
            
                return Integer.parseInt(columnValue) >= Integer.parseInt(condition.value);

            case "<=":

                return Integer.parseInt(columnValue) <= Integer.parseInt(condition.value);

            case "!=":

                return !columnValue.equals(condition.value);

            default:

                throw new IllegalArgumentException("Operador no soportado: " + condition.operator);
            
        }

    }

    public static void deleteInfo(ArrayList<String> query) {

        String nameofTable = query.get(2).trim(); // Asegurar que no haya espacios extra
        String path = "src/myTables/";
        File sourceFile = new File(path, nameofTable + ".csv");
    
        if (!sourceFile.exists()) {
            System.err.println("La tabla especificada no existe.");
            return;
        }
    
        try {
           
            Condition condition = parseWhere(query);
    
           
            Scanner scanner = new Scanner(sourceFile);
            String headline = scanner.nextLine(); 
            System.out.println("Encabezado: " + headline);
            String[] columns = headline.split(",");
    
            int columnIndex = -1;
    
          
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].trim().equalsIgnoreCase(condition.column)) {
                    columnIndex = i;
                    break;
                }
            }
    
            if (columnIndex == -1) {
                System.err.println("La columna especificada en WHERE no existe.");
                scanner.close();
                return;
            }
    
            List<String> updatedLines = new ArrayList<>();
            updatedLines.add(headline.trim()); 

           
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); 
                if (line.isEmpty()) {
                    continue; 
                }
    
                String[] rowData = line.split(",");
    
                
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < columns.length; i++) {
                    row.put(columns[i].trim(), rowData[i].trim());
                }
    
               
                if (!evaluate(condition, row)) {
                    updatedLines.add(line.trim()); 
                } else {
                    System.out.println("Fila eliminada: " + line);
                }
            }
    
            scanner.close();
    
          
            try (FileWriter writer = new FileWriter(sourceFile)) {
                for (int i = 0; i < updatedLines.size(); i++) {
                    writer.write(updatedLines.get(i));
                    if (i < updatedLines.size() - 1) {
                        writer.write("\n");
                    }
                }
                System.out.println("Archivo actualizado con éxito.");
            } catch (IOException e) {
                System.err.println("Error escribiendo el archivo: " + e.getMessage());
            }
    







             
             
        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }

    public static void selectInfo(ArrayList<String> query) {
    
        String nameofTable = query.get(3).trim(); // Nombre de la tabla (hawk en este caso)
        String path = "src/myTables/";
        File sourceFile = new File(path, nameofTable + ".csv");

        if (!sourceFile.exists()) {
            System.err.println("La tabla especificada no existe.");
            return;
        }

        try {
            // Leer el archivo
            Scanner scanner = new Scanner(sourceFile);
            String headline = scanner.nextLine(); // Leer el encabezado
            String[] columns = headline.split(",");

            // Manejar el caso de '*'
            List<String> selectedColumns = new ArrayList<>();
            if (query.get(1).equals("*")) {
                selectedColumns.addAll(List.of(columns)); // Selecciona todas las columnas
            } else {
                // Seleccionar columnas específicas
                for (int i = 1; i < query.size(); i++) {
                    String column = query.get(i).trim();
                    if (column.equalsIgnoreCase("from")) {
                        break; // Detenerse al llegar a 'from'
                    }
                    selectedColumns.add(column);
                }
            }

            // Manejar condición WHERE
            Condition condition = null;
            if (query.contains("where")) {
                condition = parseWhere(query);
            }

            // Imprimir encabezado de las columnas seleccionadas
            StringBuilder headerOutput = new StringBuilder();
            for (String col : selectedColumns) {
                headerOutput.append(col).append(",");
            }
            System.out.println(headerOutput.substring(0, headerOutput.length() - 1));

            // Procesar cada fila
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] rowData = line.split(",");

                // Crear mapa para asociar columna -> valor
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < columns.length; i++) {
                    row.put(columns[i].trim(), rowData[i].trim());
                }

                // Evaluar condición WHERE (si existe)
                if (condition == null || evaluate(condition, row)) {
                    StringBuilder rowOutput = new StringBuilder();
                    for (String column : selectedColumns) {
                        rowOutput.append(row.get(column)).append(",");
                    }
                    System.out.println(rowOutput.substring(0, rowOutput.length() - 1));
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }

    }
    

        
           
       
    
    }