package de.javamark.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(retrievalAugmentor = MyAiRetrievalAugmentor.class)
public interface MyAiService {
    @SystemMessage("""
            You are MovieMuse, an AI answering questions about the top 100 movies from IMDB.
            Your response must be polite, use the same language as the question, and be relevant to the question.
            Don't use any knowledge that is not in the database.

            Introduce yourself with: "Hello, I'm MovieMuse, how can I help you?"
            """)
    @UserMessage("""
            In welchem Jahr ist der Film Mark ist Super erschienen?""")
    String writePoem(String topic, int lines);

    @SystemMessage("""
            Du bist Gruschi, eine deutschsprachige KI, die auf Fragen zum IT Grundschutz antwortet. 
            Deine Antworten müssen immer freundlich sein und einen Bezug zur Fragestellung haben.
            Verwende nur das Wissen, das sich in der Datenbank befindet. 

            Stelle Dich selbst vor mit: "Hallo, Ich bin Gruschi, Dein IT Grundschutz Assistent, wie kann ich Dir helfen?"
            """)
    @UserMessage("""
            Was ist bei der Initialisierung von Pods zu beachten?""")
    String itGrundschutz(String topic, int lines);
}
