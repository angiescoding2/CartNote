# CartNote

En inköpslista för Android, byggd i Kotlin med Jetpack Compose.
I appen kan du lägga till eller ta bort varor för att 
enkelt se vad du har lagt i din varukorg.

## Funktioner
I appen kan du lägga till, bocka av, ta bort, redigera vara och
avdelning. Med hjälp av en statistikrad kan du enkelt se
hur många varor du köpt.

## Felsökning
Jag råkade trycka på Create functions för statistikraden som sedan blev Conflicting Overloads när jag 
lade till funktionerna manuellt längst ner i filen.
Jag trodde att orsaken var att jag missat någon måsvinge. Men när jag kollade på listan över
highlightade errors såg jag problemet och vilka rader det gällde. Det här problemet löste
jag med hjälp av AI (Claude) som förklarade att funktionerna fanns två gånger
i filen och vad jag skulle ta bort utan att riskera att något förstördes.

## Hjälpmedel
Jag använde mig av materialet i studieguiden som vi fått hittills, gjorde lite research 
på Coolors.com (ett färgbibliotek) och kollade Dribbble för lite inspiration. 
Jag har använt AI under processen som hjälpt mig förklara kod och felsöka. 
Jag bad AI att förklara kod genom musik eftersom jag är musiklärare och förstår
kod bättre på detta sätt. Det har hjälpt mig mycket att koppla logiken i koden att använda AI på det här sättet.
Det har också hjälpt mycket att ha en checklista i MainActivity och 
skrivit kommentarer för olika kodblock vad de gör
i appen. Detta var något jag siktade på att ha ordning på från början. 

