package com.mycompany.payaraapiv1.resources;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @GET
    public Response obtenerUsuarios(@QueryParam("rol") String rol) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            Query query = db.collection("usuarios");

            if (rol != null && !rol.trim().isEmpty()) {
                query = query.whereEqualTo("rol", rol);
            }

            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            List<Map<String, Object>> lista = new ArrayList<>();
            for (DocumentSnapshot doc : documents) {
                Map<String, Object> data = doc.getData();
                if (data != null) {
                    data.put("id", doc.getId());
                    lista.add(data);
                }
            }

            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                   .entity("{\"error\": \"" + e.getMessage() + "\"}")
                   .build();
        }
    }
}