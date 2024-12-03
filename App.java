import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        File directory = new File("src/Files/");
        directory.mkdir();
        Scanner input = new Scanner(System.in);

        GestioneFile<GestioneStudenti> fileStudenti = new GestioneFile<GestioneStudenti>("studenti.csv");
        GestioneFile<GestioneDocenti> fileDocenti = new GestioneFile<GestioneDocenti>("docenti.csv");
        GestioneFile<GestioneInsegna> fileInsegna = new GestioneFile<GestioneInsegna>("insegna.csv");
        
        GestioneStudenti studenti = new GestioneStudenti();
        GestioneDocenti docenti = new GestioneDocenti();
        GestioneInsegna insegna = new GestioneInsegna();

        if(directory.list().length>0){
            clearScreen();
            if(fileStudenti.caricaDati()!=null)
                studenti=fileStudenti.caricaDati();
            if(fileDocenti.caricaDati()!=null)
                docenti=fileDocenti.caricaDati();
            if(fileInsegna.caricaDati()!=null)
                insegna=fileInsegna.caricaDati();

            System.out.println("Dati relativi all'ultimo salvataggio caricati correttamente.");
        }
        else clearScreen();

        int userChoice;
        boolean hasSaved = true;

        do{
            System.out.print(printMenu());
            userChoice = input.nextInt();
            hasSaved = menuChoice(hasSaved, userChoice, fileStudenti, fileDocenti, fileInsegna, studenti, docenti, insegna);
        
            clearScreen();
        }while(userChoice != 0 || !hasSaved);

        input.close();
    }

    public static boolean menuChoice(boolean hasSaved, int userChoice, GestioneFile<GestioneStudenti> fileStudenti, GestioneFile<GestioneDocenti> fileDocenti, GestioneFile<GestioneInsegna> fileInsegna, GestioneStudenti studenti, GestioneDocenti docenti, GestioneInsegna insegna){
        Scanner input = new Scanner(System.in);
        String entitySelection;
        
        switch(userChoice){
            case 1:
                entitySelection = selectEntity();
                if(entitySelection.equalsIgnoreCase("d")){
                    if(docenti.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da visualizzare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        String viewSelection;
                        System.out.print("Visualizzare tutto? (Y/N): ");
                        do{
                            viewSelection = input.nextLine();
                            if(!viewSelection.equalsIgnoreCase("y") && !viewSelection.equalsIgnoreCase("n"))
                                System.out.print("Inserire un valore valido: ");
                        }while(!viewSelection.equalsIgnoreCase("y") && !viewSelection.equalsIgnoreCase("n"));
                        
                        if(viewSelection.equalsIgnoreCase("y"))
                            System.out.println(docenti);
                        else{
                            String searchInput;
                            System.out.print("Inserire un parametro riconducibile a Codice, Cognome, Nome o Materia: ");
                            searchInput = input.nextLine();

                            System.out.print(docenti.searchData(searchInput));
                        }
                    }
                }else if(entitySelection.equalsIgnoreCase("s")){
                    if(studenti.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da visualizzare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        String viewSelection;
                        System.out.print("Visualizzare tutto? (Y/N): ");
                        do{
                            viewSelection = input.nextLine();
                            if(!viewSelection.equalsIgnoreCase("y") && !viewSelection.equalsIgnoreCase("n"))
                                System.out.print("Inserire un valore valido: ");
                        }while(!viewSelection.equalsIgnoreCase("y") && !viewSelection.equalsIgnoreCase("n"));
                        
                        if(viewSelection.equalsIgnoreCase("y"))
                            System.out.println(studenti);
                        else{
                            String searchInput;
                            System.out.print("Inserire un parametro riconducibile a Codice, Cognome, Nome o Classe: ");
                            searchInput = input.nextLine();

                            System.out.print(studenti.searchData(searchInput));
                        }
                    }
                }else if(entitySelection.equalsIgnoreCase("i")){
                    if(insegna.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da visualizzare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        String viewSelection;
                        System.out.print("Visualizzare tutto? (Y/N): ");
                        do{
                            viewSelection = input.nextLine();
                            if(!viewSelection.equalsIgnoreCase("y") && !viewSelection.equalsIgnoreCase("n"))
                                System.out.print("Inserire un valore valido: ");
                        }while(!viewSelection.equalsIgnoreCase("y") && !viewSelection.equalsIgnoreCase("n"));
                        
                        if(viewSelection.equalsIgnoreCase("y"))
                            System.out.println(insegna);
                        else{
                            int searchInput;
                            System.out.print("Inserire un parametro riconducibile a Codice Insegna, Codice Docente o Codice Studente: ");
                            searchInput = input.nextInt();

                            System.out.print(insegna.searchData(searchInput));
                        }
                    }
                }
                break;
            case 2:
                entitySelection = selectEntity();
                if(entitySelection.equalsIgnoreCase("d")){
                    System.out.print("Inserisci il cognome del docente: ");
                    String surnameInput = input.nextLine();

                    System.out.print("Inserisci il nome del docente: ");
                    String nameInput = input.nextLine();

                    System.out.print("Inserisci la materia del docente: ");
                    String subjectInput = input.nextLine();

                    docenti.addDocente(surnameInput, nameInput, subjectInput);
                    System.out.println(ConsoleColors.GREEN_BRIGHT + nameInput + " " + surnameInput + " (" + subjectInput + ") aggiunto correttamente in Docenti." + ConsoleColors.RESET);
                    
                    hasSaved = false;
                }else if(entitySelection.equalsIgnoreCase("s")){
                    System.out.print("Inserisci il cognome dello studente: ");
                    String surnameInput = input.nextLine();

                    System.out.print("Inserisci il nome dello studente: ");
                    String nameInput = input.nextLine();

                    System.out.print("Inserisci la classe dello studente: ");
                    String classInput = input.nextLine();

                    studenti.addStudente(surnameInput, nameInput, classInput);
                    System.out.println(ConsoleColors.GREEN_BRIGHT + nameInput + " " + surnameInput + " (" + classInput + ") aggiunto correttamente in Studenti." + ConsoleColors.RESET);
                    
                    hasSaved = false;
                }else if(entitySelection.equalsIgnoreCase("i")){
                    int docInput;
                    int stuInput;
                    if(docenti.isEmpty() || studenti.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Non ci sono studenti e/o docenti da poter collegare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        System.out.print("Inserisci il codice di un docente: ");
                        do{
                            docInput = input.nextInt();
                            if(!docenti.codeExists(docInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!docenti.codeExists(docInput));

                        System.out.print("Inserisci il codice di uno studente: ");
                        do{
                            stuInput = input.nextInt();
                            if(!studenti.codeExists(stuInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!studenti.codeExists(stuInput));

                        insegna.addInsegna(docInput, stuInput);
                        System.out.println(ConsoleColors.GREEN_BRIGHT + "La relazione tra Docente '" + docInput + "' e Studente '" + stuInput + "' aggiunta correttamente in Insegna." + ConsoleColors.RESET);
                    
                        hasSaved = false;
                    }
                }
                break;
            case 3:
                entitySelection = selectEntity();
                if(entitySelection.equalsIgnoreCase("d")){
                    if(docenti.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da modificare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        int docInput;
                        System.out.print("Inserisci il codice del docente che vuoi modificare: ");
                        do{
                            docInput = input.nextInt();
                            if(!docenti.codeExists(docInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!docenti.codeExists(docInput));
                        input.nextLine();
                        int index = docenti.getIndexFromCode(docInput);

                        System.out.print("Inserisci il cognome del docente (INVIO per saltare): ");
                        String surnameInput = input.nextLine();

                        System.out.print("Inserisci il nome del docente (INVIO per saltare): ");
                        String nameInput = input.nextLine();

                        System.out.print("Inserisci la materia del docente (INVIO per saltare): ");
                        String subjectInput = input.nextLine();

                        if(!surnameInput.equals("")){
                            docenti.setSurname(surnameInput, index);
                        }
                        if(!nameInput.equals("")){
                            docenti.setName(nameInput, index);
                        }
                        if(!subjectInput.equals("")){
                            docenti.setSubject(subjectInput, index);
                        }

                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Dati del Docente '" + docInput + "' aggiornati correttamente." + ConsoleColors.RESET);
                    
                        hasSaved = false;
                    }
                }else if(entitySelection.equalsIgnoreCase("s")){
                    if(studenti.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da modificare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        int stuInput;
                        System.out.print("Inserisci il codice dello studente che vuoi modificare: ");
                        do{
                            stuInput = input.nextInt();
                            if(!studenti.codeExists(stuInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!studenti.codeExists(stuInput));
                        input.nextLine();
                        int index = studenti.getIndexFromCode(stuInput);

                        System.out.print("Inserisci il cognome dello studente (INVIO per saltare): ");
                        String surnameInput = input.nextLine();

                        System.out.print("Inserisci il nome dello studente (INVIO per saltare): ");
                        String nameInput = input.nextLine();

                        System.out.print("Inserisci la classe dello studente (INVIO per saltare): ");
                        String classeInput = input.nextLine();

                        if(!surnameInput.equals("")){
                            studenti.setSurname(surnameInput, index);
                        }
                        if(!nameInput.equals("")){
                            studenti.setName(nameInput, index);
                        }
                        if(!classeInput.equals("")){
                            studenti.setClasse(classeInput, index);
                        }

                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Dati dello Studente '" + stuInput + "' aggiornati correttamente." + ConsoleColors.RESET);
                    
                        hasSaved = false;
                    }
                }else if(entitySelection.equalsIgnoreCase("i")){
                    if(insegna.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da modificare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        int insInput;
                        System.out.print("Inserisci il codice della relazione Insegna che vuoi modificare: ");
                        do{
                            insInput = input.nextInt();
                            if(!insegna.codeExists(insInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!insegna.codeExists(insInput));
                        input.nextLine();
                        int index = docenti.getIndexFromCode(insInput);

                        int docInput;
                        System.out.print("Inserisci il codice del Docente che vuoi modificare (-1 per saltare): ");
                        do{
                            docInput = input.nextInt();
                            if(!docenti.codeExists(docInput) && docInput != -1)
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!docenti.codeExists(docInput) && docInput != -1);

                        int stuInput;
                        System.out.print("Inserisci il codice dello Studente che vuoi modificare (-1 per saltare): ");
                        do{
                            stuInput = input.nextInt();
                            if(!studenti.codeExists(stuInput) && stuInput != -1)
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!studenti.codeExists(stuInput) && stuInput != -1);

                        if(docInput != -1){
                            insegna.setCodDoc(docInput, index);
                        }
                        if(stuInput != -1){
                            insegna.setCodStu(stuInput, index);
                        }

                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Dati della relazione Insegna '" + insInput + "' aggiornati correttamente." + ConsoleColors.RESET);

                        hasSaved = false;
                    }
                }
                break;
            case 4:
                entitySelection = selectEntity();
                if(entitySelection.equalsIgnoreCase("d")){
                    if(docenti.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da eliminare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        int docInput;

                        System.out.print("Inserisci il codice del Docente che vuoi eliminare: ");
                        do{
                            docInput = input.nextInt();
                            if(!docenti.codeExists(docInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!docenti.codeExists(docInput));

                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Docente " + docInput + " eliminato correttamente dall'archivio." + ConsoleColors.RESET);
                        System.out.println("Allo stesso tempo, sono state eliminate " + insegna.deleteLinkedDoc(docInput) + " relazioni che coinvolgevano l'entità eliminata.");
                        docenti.removeDocente(docenti.getIndexFromCode(docInput));

                        hasSaved = false;
                    }
                }else if(entitySelection.equalsIgnoreCase("s")){
                    if(studenti.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da eliminare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        int stuInput;

                        System.out.print("Inserisci il codice dello Studente che vuoi eliminare: ");
                        do{
                            stuInput = input.nextInt();
                            if(!studenti.codeExists(stuInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!studenti.codeExists(stuInput));

                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Studente " + stuInput + " eliminato correttamente dall'archivio." + ConsoleColors.RESET);
                        System.out.println("Allo stesso tempo, sono state eliminate " + insegna.deleteLinkedStu(stuInput) + " relazioni che coinvolgevano l'entità eliminata.");
                        studenti.removeStudente(studenti.getIndexFromCode(stuInput));

                        hasSaved = false;
                    }
                }else if(entitySelection.equalsIgnoreCase("i")){
                    if(insegna.isEmpty()){
                        System.out.println(ConsoleColors.RED_BRIGHT + "Nessun elemento da eliminare. Aggiungi dei dati per poter accedere alla funzionalità." + ConsoleColors.RESET);
                    }
                    else{
                        int insInput;

                        System.out.print("Inserisci il codice della relazione Insegna che vuoi eliminare: ");
                        do{
                            insInput = input.nextInt();
                            if(!insegna.codeExists(insInput))
                                System.out.print(ConsoleColors.RED_BRIGHT + "Il codice inserito non esiste, inseriscine uno esistente: " + ConsoleColors.RESET);
                        }while(!insegna.codeExists(insInput));

                        System.out.println(ConsoleColors.GREEN_BRIGHT + "Relazione Insegna " + insInput + " eliminata correttamente dall'archivio." + ConsoleColors.RESET);
                        insegna.removeInsegna(insegna.getIndexFromCode(insInput));

                        hasSaved = false;
                    }
                }
                break;
            case 5:
                fileStudenti.salvaFile(studenti);
                fileDocenti.salvaFile(docenti);
                fileInsegna.salvaFile(insegna);
                hasSaved = true;
                System.out.println(ConsoleColors.GREEN_BRIGHT + "Salvataggio effettuato correttamente." + ConsoleColors.RESET);
                break;
            case 0:
                if(!hasSaved){
                    System.out.print("Non hai salvato le ultime modifiche. Vuoi comunque chiudere il programma? (Y/N): ");
                    
                    String userInput;
                    do{
                        userInput = input.nextLine();
                        if(!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n"))
                            System.out.print("> Inserire un valore valido: ");
                    }while(!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n"));
                    
                    if(userInput.equalsIgnoreCase("y"))
                        hasSaved = true;
                    else hasSaved = false;
                }
        }
        if(userChoice != 0)
            pause();

        return hasSaved;
    }

    public static String selectEntity(){
        Scanner input = new Scanner(System.in);
        String userInput;

        System.out.print("> Seleziona l'entità (D docenti, S studenti, I insegna, Q per uscire): ");
        do{
            userInput = input.nextLine();
            if(!userInput.equalsIgnoreCase("d") && !userInput.equalsIgnoreCase("s") && !userInput.equalsIgnoreCase("i") && !userInput.equalsIgnoreCase("q"))
                System.out.print("> Inserire un valore valido: ");
        }while(!userInput.equalsIgnoreCase("d") && !userInput.equalsIgnoreCase("s") && !userInput.equalsIgnoreCase("i") && !userInput.equalsIgnoreCase("q"));
    
        return userInput;
    }

    public static String printMenu(){
        String retValue = "";

        retValue += ConsoleColors.WHITE_BOLD_BRIGHT + "\nProgramma di gestione file multipli" + ConsoleColors.RESET + "\n";
        retValue += ConsoleColors.BLACK_BRIGHT + "1. " + ConsoleColors.RESET + "Visualizza entità\n";
        retValue += ConsoleColors.BLACK_BRIGHT + "2. " + ConsoleColors.RESET + "Aggiungi entità\n";
        retValue += ConsoleColors.BLACK_BRIGHT + "3. " + ConsoleColors.RESET + "Modifica entità\n";
        retValue += ConsoleColors.BLACK_BRIGHT + "4. " + ConsoleColors.RESET + "Elimina entità\n";
        retValue += ConsoleColors.BLACK_BRIGHT + "5. " + ConsoleColors.GREEN_BRIGHT + "Salva le modifiche\n";
        retValue += ConsoleColors.BLACK_BRIGHT + "0. " + ConsoleColors.RED_BRIGHT + "Esci dal programma\n";
        retValue += ConsoleColors.RESET + "> Seleziona un'opzione: ";
        return retValue;
    }

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
    }

    public static void pause(){
        Scanner input = new Scanner(System.in);
        System.out.print("Premi INVIO per proseguire..");
        input.nextLine();
    }
}
