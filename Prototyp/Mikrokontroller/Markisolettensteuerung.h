/*
Autor : Tobias Musiol
Created: 06.12.2020
Last Edited: 06.12.2020
Die Markisolettensteuerung.h definiert die Schnittstellen, die nacher in der
Main() benutzt werden. In der Markisolettensteueung.cpp werden die Schnittstellen
implementiert. Sollte sich zum Beispiel die Art und Weise der Ansteuerung eines Aktors
oder das Auslesen eines Sensors sich ändern, so muss man nur den cpp Code ändern.
*/

typedef enum{AUTO,MANUELL}Betriebsmodus;
typedef enum{STEHT_AUTO, FAHRE_HOCH_AUTO,FAHRE_RUNTER_AUTO}StatusMarkiseAuto;
typedef enum{WARTE_AUF_NACHRICHT, LESE_NACHRICHT, FAHRE_HOCH_MANUELL, FAHRE_RUNTER_MANUELL}StatusMarkiseManuell;


void initMarkitolettenSteuerung(Betriebsmodus *bmod, 
                                StatusMarkiseAuto *auto_stat, 
                                StatusMarkiseManuell *manu_stat)
{
    *bmod = AUTO;
    *auto_stat = STEHT_AUTO;
    *manu_stat = WARTE_AUF_NACHRICHT;         
}


void strFkt(Betriebsmodus *bmod, 
            StatusMarkiseAuto *auto_stat, 
            StatusMarkiseManuell* manu_stat, 
            int modus_maunell,
            int lichtstaerke_aussen, 
            int nicht_unten, 
            int nicht_oben, 
            int *nach_unten, 
            int *nach_oben,
            char *aktion,
            int *nachricht_vorhanden)
{
    
    if(*bmod == AUTO){
        
        switch(*auto_stat){
            
            case STEHT_AUTO:
            //Checke Zustand
            //Wechsel auf Manuellen Modus soll nur 
            //bei stehenden Markisoletten erfolgen.
            if(modus_maunell == 1)
                *bmod = MANUELL;
            else if(lichtstaerke_aussen < 180 && nicht_unten == '1')
                *auto_stat = FAHRE_RUNTER_AUTO;
            else if(lichtstaerke_aussen > 220 && nicht_oben == '1')
                *auto_stat = FAHRE_HOCH_AUTO;
            //Definiere Ausgabe
            *nach_oben = 0;
            *nach_unten = 0;
            break;

            case FAHRE_RUNTER_AUTO:
            //Checke Zustand
            if(nicht_unten == 0 )
                *auto_stat = STEHT_AUTO;
            //Definiere Ausgabe
            if(*nach_unten == 0)
                *nach_unten = 1;
            break;

            case FAHRE_HOCH_AUTO:
            //Checke Zustand
            if(nicht_oben == 0)
                *auto_stat == STEHT_AUTO;
            //Definiere Ausgabe
            if(nach_oben == 0)
                *nach_oben = 1;
            break;

        }

    }else{
        
        switch(*manu_stat){

            case WARTE_AUF_NACHRICHT:
            //Checke Zustand
            //Wechsel auf den Automatikmodus soll
            //nur bei stehenden Markisoletten erfolgen.
            if(modus_maunell == 0)
                *bmod = AUTO;
            else if(*nachricht_vorhanden == 1)
                *manu_stat = LESE_NACHRICHT;
            //Definiere Ausgabe
            *nach_unten = 0;
            *nach_oben = 0;
            break;

            case LESE_NACHRICHT:
            //Checke Zustand
            if(aktion == "runter")
                *manu_stat = FAHRE_RUNTER_MANUELL;
            if(aktion == "hoch")
                *manu_stat = FAHRE_HOCH_MANUELL;
            else
                *manu_stat = WARTE_AUF_NACHRICHT;
            //Definiere Ausgabe
            nachricht_vorhanden = 0;
            break;

            case FAHRE_RUNTER_MANUELL:
            //Checke Zustand
            if(nicht_unten == 0)
                *manu_stat = WARTE_AUF_NACHRICHT;
            //Definiere Ausgabe
            *nach_unten = 1;
            break;

            case FAHRE_HOCH_MANUELL:
            //Checke Zustand
            if(nicht_oben == 0)
                *manu_stat = WARTE_AUF_NACHRICHT;
            //Definiere Ausgabe
            *nach_oben = 1;
            break;

        }
    }
}


/*
Funktion zum herunterfahren der Markisolette
*/
void fahre_runter(){}

/*
Funktion zum herauffahren der Markisolette
*/
void fahre_hoch(){}