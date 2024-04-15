package de.javamark.ai.api;

import de.javamark.ai.MyAiService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api")
public class MyAiResource {

    final MyAiService myAiService;

    public MyAiResource(MyAiService myAiService) {
        this.myAiService = myAiService;
    }

    @GET
    @Path("/ai")
    public String helloAi(){
        return myAiService.writePoem("Wochenende", 11);
    }
    @GET
    @Path("/itGrundschutz")
    public String itGrundschutz(){
        return myAiService.itGrundschutz("Wochenende", 11);
    }
}
