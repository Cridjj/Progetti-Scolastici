#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define SIZE_CHAR 15            //numero massimo caratteri (punti inclusi) da scrivere per un IP o SM
#define SIZE_INT 4              //numero di ottetti in un IP e SM
#define SIZE_BIN 8              //numero di cifre per ogni ottetto in un IP e SM in binario
#define MAX_SOTTORETI 4194304   //numero massimo di sottoreti creabili (max sottoreti con un classe A)

const char* separatore = ".";

//array che deve essere globale affinché possa supportare una così grande dimensione
//!utilizzato esclusivamente in sottoreti_mask_variabile()!
int host[MAX_SOTTORETI];        //numero di host per ogni sottorete

void clear();
void pause();
int menu();

//funzione che acquisisce un indirizzo ip e lo memorizza nell'array passato come parametro, in decimale
void input_ip(int ip[]);
//funzione che acquisisce una subnet mask e la memorizza nell'array passato come parametro, in decimale
void input_sm(int sm[], int classe);

//funzione che acquisisce un indirizzo ip e ne indica la classe e se è privato
void caratteristiche_ip();
/*funzione che separa l'indirizzo inserito e converte le parti separate come intero verificando
che il valore inserito sia valido, restituisce 1 o 0 in base all'esito del controllo*/
int controllo_ip(char temp_ip[], int ip[]);
//funzione che individua la classe a cui appartiene l'ip passato come parametro e restituisce un numero corrispondente a ciascuna classe
int classe_ip(int ip[]);
/*funzione che verifica, in base alla classe individuata, se l'IP è privato o meno
restituisce Y, N o 0 (per indirizzi di classe D e E)*/
char controllo_privato(int classe, int ip[]);

//funzione che indica se due ip fanno parte della stessa rete (stesso net id), fondendosi anche della subnet mask
void individuazione_netid();
/*funzione che separa la subnet mask inseritoa e converte le parti separate come intero verificando
che il valore inserito sia valido, restituisce 1 o 0 in base all'esito del controllo*/
int controllo_sm(char temp_sm[], int sm[], int classe);
/*funzione che converte in binario i 4 valori decimali presenti in "input" e li inserisce, cifra per
cifra, in "output" - ogni riga è un ottetto convertito, ogni colonna una cifra dell'ottetto*/
void conversione_binario(int input[], int output[][SIZE_BIN]);
//funzione che calcola il netid in binario degli IP binari "ip1" e "ip2": se sono uguali, restituisce 1, altrimenti 0
int controllo_netid(int ip1[][SIZE_BIN], int ip2[][SIZE_BIN], int sm[][SIZE_BIN]);

/*funzione che acquisisce in input un indirizzo ip e il numero di sottoreti (entro i limiti possibili) da dover creare su di esso:
oltre a creare n sottoreti, stampa anche gli attributi principali di esse, ovvero NETID - BROADCAST - PRIMO HOST - ULTIMO HOST - GATEWAY
- SUBNET MASK - NUMERO DI HOST UTILIZZABILI sia in decimale che binario per la maggior parte*/
void sottoreti_mask_fissa();

//funzione di calcolo di NETID - BROADCAST - PRIMO HOST - ULTIMO HOST - GATEWAY della ennesima sottorete
void attributi_sottorete(int ip_bin[][SIZE_BIN], int classe, int bit_host, int scelta);
//funzione di stampa della sottorete (solo degli ottetti con almeno un bit di host)
void stampa_binario(int bin_value[][SIZE_BIN], int classe, int bit_host);
//funzione di conversione e stampa del valore binario, in decimale
void stampa_decimale(int bin_value[][SIZE_BIN], int classe);

/*funzione che acquisce in input un indirizzo ip, il numero di sottoreti (entro i limiti basati dalla classe dell'IP) e il numero di host
da dedicare a ciascuna sottorete (anch'esso in un range stabilito dalla classe) - la funzione inoltre calcola e stampa i principali attributi
di ogni sottorete, ovvero NETID - BROADCAST - PRIMO HOST - ULTIMO HOST - GATEWAY - SUBNET MASK - NUMERO DI HOST UTILIZZABILI sia in decimale
che binario per la maggior parte*/
void sottoreti_mask_variabile();

int main(void){
    int scelta; //output funzione menu relativa alla funzione scelta dall'utente
    
    clear();
    //si prosegue fino a quando la variabile scelta non vale 0
    do{
        scelta = menu();

        switch(scelta){
            //Opzione 1 "Individuazione caratteristiche generali IP"
            case 1:
                caratteristiche_ip();
                pause();
                break;
            //Opzione 2 "Comparazione Net-ID di due indirizzi IP"
            case 2:
                individuazione_netid();
                pause();
                break;
            //Opzione 3 "Creazione sottoreti a maschera fissa"
            case 3:
                sottoreti_mask_fissa();
                pause();
                break;
            //Opzione 4 "Creazione sottoreti a maschera variabile"
            case 4:
                sottoreti_mask_variabile();
                pause();
                break;
        }
        clear();
    }while(scelta); //Opzione 0 "Uscita programma"
}

void clear(){
    printf("\033[H\033[2J");
}

void pause(){
    printf("\nPremere INVIO per continuare..");
    getchar();
}

int menu(){
    int scelta;

    printf("--Programma di analisi indirizzi IP--\n\n");
    printf("1. Individuazione caratteristiche generali IP\n");
    printf("2. Comparazione Net-ID di due indirizzi IP\n");
    printf("3. Creazione sottoreti a maschera fissa\n");
    printf("4. Creazione sottoreti a maschera variabile\n");
    printf("0. Uscita programma\n\n");
    do{
        printf("Scegli un'opzione (0-4): ");
        scanf("%d", &scelta);
        if(scelta<0 || scelta>4)
            printf("Valore inserito non valido, riprova.\n");
    }while(scelta<0 || scelta>4);

    return scelta;
}

void caratteristiche_ip(){
    int ip[SIZE_INT];        //array contenente per ogni indice un "ottetto" dell'IP inserito
    char private_status;
    int classe;             //output della funzione classe_ip()

    printf("Inserire l'IP in decimale: ");
    input_ip(ip);
    getchar();
    printf("Caratteristiche:\n- Classe: ");
    
    classe = classe_ip(ip);
    printf("%c\n", classe + 64);    //stampa codifica in ascii della lettera corrispondente alla classe

    printf("Privato? ");
    /*per indirizzi di classe D e E non sono attualmente presenti modi per
    determinare se sono privati o meno*/
    if(classe<4){
        private_status = controllo_privato(classe, ip);
        printf("%c", private_status);
    }
    else printf("Classe troppo avanzata per acquisire questa informazione.");
}

int classe_ip(int ip[]){
    switch(ip[0]){
        //primo "ottetto" IP da 1 a 127 = classe A
        case 1 ... 127:
            return 1;
        //primo "ottetto" IP da 128 a 191 = classe B
        case 128 ... 191:
            return 2;
        //primo "ottetto" IP da 192 a 223 = classe C
        case 192 ... 223:
            return 3;
        //primo "ottetto" IP da 224 a 239 = classe D
        case 224 ... 239:
            return 4;
        //primo "ottetto" IP da 240 a 255 = classe E
        case 240 ... 255:
            return 5;
    }
}

void input_ip(int ip[]){
    char temp_ip[SIZE_CHAR]; //variabile temporanea su cui acquisire l'ip come stringa
    int output;              //risultato del controllo della stringa da controllo_ip()

    do{
        scanf("%s", temp_ip);
        output = controllo_ip(temp_ip, ip);
        if(strlen(temp_ip) > SIZE_CHAR || output == 0){
            printf("Indirizzo IP non valido. Riprova: ");
        }
    }while(strlen(temp_ip) > SIZE_CHAR || output == 0);
}

int controllo_ip(char temp_ip[], int ip[]){
    int i;              //indice dell'array ip
    char* stringa;      //token della divisione della stringa temp_ip
    int counter = 0;    //contatore del numero di divisioni effettuate

    stringa = strtok(temp_ip, separatore);
    counter++;

    i = 0;
    while(stringa != NULL && i<SIZE_INT){
        ip[i] = atoi(stringa);      //si converte ad intero l'ottetto digitato e diviso
        if((stringa = strtok(NULL, separatore)) != NULL)
            counter++;              //se la divisione viene effettivamente fatta, il contatore si incrementa
        //qualsiasi valore diverso da 0-255 non è accettato
        if(ip[i]<0 || ip[i]>255)
            return 0;
        //il primo ottetto non può valere 0 e non può esistere un IP binario con soli 1
        if((ip[0] == 0) || (ip[0] == 255 && ip[1] == 255 && ip[2] == 255 && ip[3] == 255))
            return 0;
        i++;
    }
    if(counter == 4)    //se le divisioni effettuate nella stringa sono 4 (dunque 4 ottetti, come dovrebbe essere)
        return 1;       //allora si restituisce un esito positivo del controllo (1)
    else return 0;      //altrimenti l'esito è negativo (0)
}

char controllo_privato(int classe, int ip[]){
    switch(classe){
        case 1:
            if(ip[0] == 10)
                return 'Y';
            return 'N';
        case 2:
            if(ip[0] == 172 && (ip[1] > 15 && ip[1] < 32))
                return 'Y';
            return 'N';
        case 3:
            if(ip[0] == 192 && ip[1] == 168)
                return 'Y';
            return 'N';
            break;
    }
}

void individuazione_netid(){
    int ip1[SIZE_INT];  //primo indirizzo IP decimale inserito in input
    int ip2[SIZE_INT];  //secondo indirizzo IP decimale inserito in input
    int sm[SIZE_INT];   //subnet mask, di classe uguale a quella dei due IP, inserita in input

    //output delle conversioni in binario dei valori in input
    //ogni riga è un ottetto, ogni colonna è una cifra dell'ottetto
    int ip1_bin[SIZE_INT][SIZE_BIN];
    int ip2_bin[SIZE_INT][SIZE_BIN];
    int sm_bin[SIZE_INT][SIZE_BIN];

    //valori relativi alle classi dei due ip inseriti
    int classe1;
    int classe2;

    //input primo IP
    printf("Inserire il primo IP in decimale: ");
    do{
        input_ip(ip1);
        getchar();

        classe1 = classe_ip(ip1);
        //classi D ed E non supportano la Subnet Mask
        if(classe1>3)
            printf("Inserire un IP di una classe che supporti la Subnet Mask (A-C). Riprova: ");
    }while(classe1>3);

    conversione_binario(ip1, ip1_bin);

    //input secondo IP
    printf("Inserire il secondo IP in decimale: ");
    do{
        input_ip(ip2);
        getchar();

        classe2 = classe_ip(ip2);

        //la classe del secondo IP deve essere la stessa del primo
        if(classe1 != classe2)
            printf("Inserire un IP con la stessa classe di '%d.%d.%d.%d'. Riprova: ", ip1[0], ip1[1], ip1[2], ip1[3]);
    }while(classe1 != classe2);

    conversione_binario(ip2, ip2_bin);

    //input Subnet Mask che deve rientrare nel range della classe inserita precedentemente
    printf("Inserire una Subnet Mask valida per la classe %c in decimale: ", classe1 + 64);
    input_sm(sm, classe1);
    getchar();

    conversione_binario(sm, sm_bin);

    if(controllo_netid(ip1_bin, ip2_bin, sm_bin) == 1)
        printf("\nI due indirizzi IP appartengono alla stessa rete.\n");
    else printf("\nI due indirizzi IP non appartengono alla stessa rete.\n");
}

void input_sm(int sm[], int classe){
    char temp_sm[SIZE_CHAR]; //variabile temporanea su cui acquisire la subnet mask come stringa
    int output;              //risultato del controllo della stringa da controllo_sm()

    do{
        scanf("%s", temp_sm);
        output = controllo_sm(temp_sm, sm, classe);
        if(strlen(temp_sm) > SIZE_CHAR || output == 0){
            printf("Subnet Mask inserita non valida. Riprova: ");
        }
    }while(strlen(temp_sm) > SIZE_CHAR || output == 0);
}

int controllo_sm(char temp_sm[], int sm[], int classe){
    int i;              //indice dell'array sm
    char* stringa;      //token della divisione della stringa temp_sm
    int counter = 0;    //contatore del numero di divisioni effettuate

    stringa = strtok(temp_sm, separatore);
    counter++;

    i = 0;
    while(stringa != NULL && i<SIZE_INT){
        sm[i] = atoi(stringa);      //si converte ad intero l'ottetto digitato e diviso
        if((stringa = strtok(NULL, separatore)) != NULL)
            counter++;              //se la divisione viene effettivamente fatta, il contatore si incrementa
        if(sm[i]<0 || sm[i]>255)     //qualsiasi valore diverso da 0-255 non è accettato
            return 0;
        i++;
    }

    if(counter != 4)    //se le divisioni effettuate nella stringa non sono 4 (dunque 4 ottetti)
        return 0;       //allora si restituisce un esito negativo del controllo (0)
    
    switch(classe){
        //range SM di classe A (1): 255.0.0.0 - 255.254.0.0
        case 1:
            if(sm[0] != 255)
                return 0;
            if(sm[1] >= 255)
                return 0;
            for(i=2; i<SIZE_INT; i++){
                if(sm[i] != 0)
                    return 0;
            }
            return 1;
        //range SM di classe B (2): 255.255.0.0 - 255.255.254.0
        case 2:
            for(i=0; i<2; i++){
                if(sm[i] != 255)
                    return 0;
            }
            if(sm[2] >= 255)
                return 0;
            if(sm[3] != 0)
                return 0;
            return 1;
        //range SM di classe C (3): 255.255.255.0 - 255.255.255.254
        case 3:
            for(i=0; i<3; i++){
                if(sm[i] != 255)
                    return 0;
            }
            if(sm[3] >= 255)
                return 0;
            return 1;
        //classi D ed E non hanno una SM attualmente
        default:
            return 0;
    }
}

void conversione_binario(int input[], int output[][SIZE_BIN]){
    int i, j;
    int temp_ip[SIZE_INT];

    for(i=0; i<SIZE_INT; i++){
        temp_ip[i] = input[i];
        for(j=(SIZE_BIN-1); j>=0; j--){
            if(temp_ip[i] > 0){
                output[i][j] = temp_ip[i] % 2;
                temp_ip[i] = temp_ip[i] / 2;
            }
            else output[i][j] = 0;
        }
    }
}

int controllo_netid(int ip1[][SIZE_BIN], int ip2[][SIZE_BIN], int sm[][SIZE_BIN]){
    //netid binari corrispondenti ai due IP
    int netid1[SIZE_INT][SIZE_BIN];
    int netid2[SIZE_INT][SIZE_BIN];

    int i, j;

    for(i=0; i<SIZE_INT; i++){
        for(j=0; j<SIZE_BIN; j++)
            netid1[i][j] = ip1[i][j] * sm[i][j];
    }

    for(i=0; i<SIZE_INT; i++){
        for(j=0; j<SIZE_BIN; j++){
            netid2[i][j] = ip2[i][j] * sm[i][j];
            if(netid1[i][j] != netid2[i][j])
                return 0;
        }
    }

    return 1;
}

void sottoreti_mask_fissa(){
    int ip[SIZE_INT];               //ip inserito in input
    int ip_bin[SIZE_INT][SIZE_BIN]; //output binario delle sottoreti
    int sm[SIZE_INT];               //subnet mask risultante dalla creazione delle sottoreti

    int classe;         //risultato della funzione classe_ip(), acquisisce la classe dell'IP inserito
    int max_sottoreti;  //in base alla classe calcolata, è il numero massimo di sottoreti creabili
    int sottoreti;      //il numero di sottoreti da creare, inserito in input

    int bit_rete;       //numero di bit utilizzati dalla rete per la creazione delle sottoreti
    int bit_host;       //numero di bit totali dedicati agli host

    int temp_bit_rete;

    int i, j;           //indici per cicli for
    int k, z, c;

    int n;              //variabile su cui appoggiare il valore attuale della sottorete da stampare

    printf("Inserire l'IP su cui creare le sottoreti: ");
    do{
        input_ip(ip);
        getchar();
        classe = classe_ip(ip);
        if(classe > 3)
            printf("La classe dell'IP inserito non permette la creazione di sottoreti. Riprova: ");
    }while(classe > 3);

    //il numero minimo di bit da dedicare agli host sono 2
    max_sottoreti = pow(2, (24-8*(classe-1))-2);

    printf("Inserire il numero di sottoreti da creare (2 - %d): ", max_sottoreti);
    do{
        scanf("%d", &sottoreti);
        getchar();
        if(sottoreti<2 || sottoreti>max_sottoreti)
            printf("Numero di sottoreti non valido. Riprova: ");
    }while(sottoreti<2 || sottoreti>max_sottoreti);

    //calcolo del numero di bit necessari per la creazione delle sottoreti
    bit_rete = log2(sottoreti);
    if(pow(2, bit_rete)<sottoreti)
        bit_rete++;

    //calcolo del numero di bit rimanenti agli host
    bit_host = 8*(4-classe) - bit_rete;

    printf("\nI bit riservati agli host sono passati da %d a %d.\n", 8*(4-classe), bit_host);

    //costruzione subnet mask di default
    for(i=0; i<SIZE_INT; i++){
        if(i<classe)
            sm[i] = 255;
        else sm[i] = 0;
    }

    temp_bit_rete = bit_rete;
    c = classe;

    //modifica degli ottetti valenti 0, aggiungendo i bit utilizzati per la sottorete (convertiti in decimale)
    while(temp_bit_rete>=0){
        if(temp_bit_rete>7){
            for(i=7; i>=0; i--){
                sm[c] += pow(2, i);
            }
            c++;
        }
        else{
            for(i=7; i>(7-temp_bit_rete); i--){
                sm[c] += pow(2, i);
            }
        }
        temp_bit_rete -= 8;
    }

    //stampa subnet mask
    printf("\nSUBNET MASK = ");
    for(i=0; i<SIZE_INT; i++){
        printf("%d", sm[i]);
        if(i<SIZE_INT-1)
            printf(".");
        else printf("\n");
    }

    //calcolo e stampa numero di host
    printf("NUMERO HOST PER SOTTORETE = %.0lf", pow(2, bit_host)-3);

    for(k=0; k<sottoreti; k++){
        printf("\n------ Rete #%d ------", k+1);
        n = k;

        //calcolo bit della sottorete
        temp_bit_rete = bit_rete;
        c = bit_rete/8;
        c += classe;
        for(j=temp_bit_rete%8-1; j>=0; j--){
            if(n > 0){
                ip_bin[c][j] = n%2;
                n = n / 2;
            }
            else ip_bin[c][j] = 0;
        }

        while(n>0){
            c--;
            for(j=7; j>=0; j--){
                if(n > 0){
                    ip_bin[c][j] = n%2;
                    n = n / 2;
                }
                else ip_bin[c][j] = 0;
            }
        }
        
        //calcolo net-id, broadcast, primo e ultimo host
        for(z=0; z<5; z++){
            attributi_sottorete(ip_bin, classe, bit_host, z);
            //stampa di ogni valore, in binario e decimale
            stampa_binario(ip_bin, classe, bit_host);
            stampa_decimale(ip_bin, classe);
        }
    }

}

void attributi_sottorete(int ip_bin[][SIZE_BIN], int classe, int bit_host, int scelta){
    int i, j;

    i = 3;
    switch(scelta){
        case 0: //calcolo net-id nei bit degli host
            while(bit_host>=0){
                if(bit_host>7){
                    for(j=0; j<SIZE_BIN; j++){
                        ip_bin[i][j] = 0;
                    }
                    i--;
                }
                else{
                    for(j=7; j>7-bit_host; j--){
                        ip_bin[i][j] = 0;
                    }
                }
                bit_host -= 8;
            }
            printf("\nNET-ID\n");
            break;
        case 1: //calcolo broadcast nei bit degli host
            while(bit_host>=0){
                if(bit_host>7){
                    for(j=0; j<SIZE_BIN; j++){
                        ip_bin[i][j] = 1;
                    }
                    i--;
                }
                else{
                    for(j=7; j>7-bit_host; j--){
                        ip_bin[i][j] = 1;
                    }
                }
                bit_host -= 8;
            }
            printf("\nBROADCAST\n");
            break;
        case 2: //calcolo primo host nei bit degli host
            while(bit_host>=0){
                if(bit_host>7){
                    for(j=0; j<SIZE_BIN; j++){
                        ip_bin[i][j] = 0;
                    }
                    i--;
                }
                else{
                    for(j=7; j>7-bit_host; j--){
                        ip_bin[i][j] = 0;
                    }
                }
                bit_host -= 8;
            }
            ip_bin[SIZE_INT-1][SIZE_BIN-1] = 1;
            printf("\nPRIMO HOST\n");
            break;
        case 3: //calcolo (pen)ultimo host nei bit degli host
            while(bit_host>=0){
                if(bit_host>7){
                    for(j=0; j<SIZE_BIN; j++){
                        ip_bin[i][j] = 1;
                    }
                    i--;
                }
                else{
                    for(j=7; j>7-bit_host; j--){
                        ip_bin[i][j] = 1;
                    }
                }
                bit_host -= 8;
            }
            ip_bin[SIZE_INT-1][SIZE_BIN-2] = 0;
            ip_bin[SIZE_INT-1][SIZE_BIN-1] = 1;
            printf("\nULTIMO HOST\n");
            break;
        case 4: //calcolo gateway (ultimo host) nei bit degli host
            while(bit_host>=0){
                if(bit_host>7){
                    for(j=0; j<SIZE_BIN; j++){
                        ip_bin[i][j] = 1;
                    }
                    i--;
                }
                else{
                    for(j=7; j>7-bit_host; j--){
                        ip_bin[i][j] = 1;
                    }
                }
                bit_host -= 8;
            }
            ip_bin[SIZE_INT-1][SIZE_BIN-1] = 0;
            printf("\nGATEWAY\n");
            break;
    }
}

void stampa_binario(int bin_value[][SIZE_BIN], int classe, int bit_host){
    int i, j;

    for(i=classe; i<SIZE_INT; i++){
        for(j=0; j<SIZE_BIN; j++){
            printf("%d", bin_value[i][j]);
            //quando si è raggiunto l'ultimo bit della rete si aggiunge una barra
            //suddividere bit della rete e bit degli host
            if((j+1)+(8*i)==(32-bit_host))
                printf("|");
        }
        if(i<SIZE_INT-1)
            printf(".");
    }
}

void stampa_decimale(int bin_value[][SIZE_BIN], int classe){
    int i, j;
    int convertito;

    printf(" = ");
    for(i=classe; i<SIZE_INT; i++){
        convertito = 0;
        for(j=0; j<SIZE_BIN; j++){
            if(bin_value[i][j] == 1)
                convertito += pow(2, 7-j);
        }

        printf("%d", convertito);
        if(i<SIZE_INT-1)
            printf(".");
    }
}

void sottoreti_mask_variabile(){
    int ip[SIZE_INT];               //ip inserito in input
    int ip_bin[SIZE_INT][SIZE_BIN]; //output binario delle sottoreti
    int sm[SIZE_INT];               //subnet mask risultante dalla creazione delle sottoreti

    int classe;         //risultato della funzione classe_ip(), acquisisce la classe dell'IP inserito
    int max_sottoreti;  //in base alla classe calcolata, è il numero massimo di sottoreti creabili
    int sottoreti;      //il numero di sottoreti da creare, inserito in input

    int bit_rete;       //numero di bit utilizzati dalla rete per la creazione delle sottoreti
    int bit_host;       //numero di bit totali dedicati agli host
    int temp_bit_rete;
    
    int bit_rete_prec;
    int bit_host_prec;

    int i, j;           //indici per cicli for
    int k, z, c;

    //variabili necessari all'ordinamento
    int n_scambi;
    int temp;

    int n;              //variabile su cui appoggiare il valore attuale della sottorete da stampare

    printf("Inserire l'IP su cui creare le sottoreti: ");
    do{
        input_ip(ip);
        getchar();
        classe = classe_ip(ip);
        if(classe > 3)
            printf("La classe dell'IP inserito non permette la creazione di sottoreti. Riprova: ");
    }while(classe > 3);

    //il numero minimo di bit da dedicare agli host sono 2
    max_sottoreti = pow(2, (24-8*(classe-1))-2);

    printf("Inserire il numero di sottoreti da creare (2 - %d): ", max_sottoreti);
    do{
        scanf("%d", &sottoreti);
        getchar();
        if(sottoreti<2 || sottoreti>max_sottoreti)
            printf("Numero di sottoreti non valido. Riprova: ");
    }while(sottoreti<2 || sottoreti>max_sottoreti);

    //input host per x sottoreti
    for(i=0; i<sottoreti; i++){
        printf("Inserire il numero di host da assegnare alla sottorete #%d (2 - %d): ", i+1, max_sottoreti-3);
        do{
            scanf("%d", &host[i]);
            getchar();
            if(host[i]<2 || host[i]>max_sottoreti-3)
                printf("Numero host non valido. Riprova: ");
        }while(host[i]<2 || host[i]>max_sottoreti-3);
        host[i] += 3;   //aggiunta gateway, netid e broadcast
    }
    printf("Aggiunti 3 host per Gateway, Net-ID e Broadcast a ciascuna sottorete inserita.\n");

    //ordinamento array host in modo decrescente (bubble sort)
    for(i=0; i<sottoreti; i++){
        n_scambi=0;
        for(j=0; j<(sottoreti-1-i); j++){
            if(host[j]<host[j+1]){
                temp = host[j];
                host[j] = host[j+1];
                host[j+1] = temp;
                n_scambi++;
            }
        }
        if(n_scambi==0)
            break;
    }

    //calcolo del numero di bit necessari per la creazione delle sottoreti
    bit_host = log2(host[0]);
    if(pow(2, bit_host)<host[0]){
            bit_host++;
    }

    //calcolo del numero di bit rimanenti agli host
    bit_rete = 8*(4-classe) - bit_host;

    n=0;
    for(k=0; k<sottoreti; k++){
        printf("\n------ Rete #%d ------", k+1);
        bit_rete_prec = bit_rete;

        //calcolo del numero di bit necessari per la creazione delle sottoreti
        bit_host = log2(host[k]);
        if(pow(2, bit_host)<host[k]){
            bit_host++;
        }
        //calcolo del numero di bit rimanenti agli host
        bit_rete = 8*(4-classe) - bit_host;
        
        printf("\nI bit riservati agli host sono passati da %d a %d.", 8*(4-classe), bit_host);

        //costruzione subnet mask di default
        for(i=0; i<SIZE_INT; i++){
            if(i<classe)
                sm[i] = 255;
            else sm[i] = 0;
        }

        //modifica degli ottetti valenti 0 nella subnet mask, aggiungendo i bit utilizzati per la sottorete (convertiti in decimale)
        temp_bit_rete = bit_rete;
        c = classe;

        while(temp_bit_rete>=0){
            if(temp_bit_rete>7){
                for(i=7; i>=0; i--){
                    sm[c] += pow(2, i);
                }
                c++;
            }
            else{
                for(i=7; i>(7-temp_bit_rete); i--){
                    sm[c] += pow(2, i);
                }
            }
            temp_bit_rete -= 8;
        }

        //azzeramento bit rete
        temp_bit_rete = bit_rete;
        c = classe;

        while(temp_bit_rete>=0){
            if(temp_bit_rete>7){
                for(j=7; j>=0; j--){
                    ip_bin[c][j] = 0;
                }
                c++;
            }
            else{
                for(j=7; j>=0; j--){
                    ip_bin[c][j] = 0;
                }
            }
            temp_bit_rete -= 8;
        }

        //calcolo bit della sottorete in binario
        temp_bit_rete = bit_rete_prec;
        c = bit_rete/8;
        c += classe;
        for(j=temp_bit_rete%8-1; j>=0; j--){
            if(n > 0){
                ip_bin[c][j] = n%2;
                n = n / 2;
            }
            else ip_bin[c][j] = 0;
        }

        while(n>0){
            c--;
            for(j=7; j>=0; j--){
                if(n > 0){
                    ip_bin[c][j] = n%2;
                    n = n / 2;
                }
                else ip_bin[c][j] = 0;
            }
        }

        //si converte in decimale la parte presente nei bit della rete
        n = 0;
        temp_bit_rete = bit_rete;
        c = classe;

        while(temp_bit_rete>=0){
            if(temp_bit_rete>7){
                for(j=7; j>=0; j--){
                    if(ip_bin[c][j] == 1)
                        n += pow(2, temp_bit_rete-j);
                }
                c++;
            }
            else{
                for(j=temp_bit_rete; j>=0; j--){
                    if(ip_bin[c][j] == 1)
                        n += pow(2, temp_bit_rete-j-1);
                }
            }
            temp_bit_rete -= 8;
        }
        printf("\n%d\n",n);

        //se in una sottorete che non è la prima, nei bit della rete c'è 0, vuol dire che non ci sono
        //abbastanza bit per contenere la sottorete stessa, dunque la creazione delle sottoreti va interrotta
        if(n == 0 && k != 0){
            printf("\nImpossibile proseguire, bit non sufficienti.\n");
            return;
        }

        //si indica il numero successivo alla parte convertita in decimale dei bit della rete
        n++;

        //stampa subnet mask
        printf("\nSUBNET MASK = ");
        for(i=0; i<SIZE_INT; i++){
            printf("%d", sm[i]);
            if(i<SIZE_INT-1)
                printf(".");
            else printf("\n");
        }

        //calcolo net-id, broadcast, primo e ultimo host
        for(z=0; z<5; z++){
            attributi_sottorete(ip_bin, classe, bit_host, z);
            //stampa di ogni valore, in binario e decimale
            stampa_binario(ip_bin, classe, bit_host);
            stampa_decimale(ip_bin, classe);
        }

        //calcolo e stampa numero di host
        printf("\nNUMERO HOST = %.0lf", pow(2, bit_host)-3);
        printf("\n");
    }

}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     