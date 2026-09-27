package com.mycompany.payaraapiv1;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Singleton
@Startup
public class FirebaseInitializer {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                String firebaseCredentialsEnv = System.getenv("FIREBASE_CREDENTIALS");
                InputStream serviceAccount;

                if (firebaseCredentialsEnv != null && !firebaseCredentialsEnv.isBlank()) {
                    // Lee las credenciales inyectadas por variable de entorno (para Cloud Run / Producción)
                    serviceAccount = new ByteArrayInputStream(firebaseCredentialsEnv.getBytes(StandardCharsets.UTF_8));
                } else {
                    // Carga el archivo físico si estás probando localmente
                    serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");
                }

                if (serviceAccount == null) {
                    System.err.println(">>> ERROR: No se encontraron credenciales en FIREBASE_CREDENTIALS ni el archivo serviceAccountKey.json <<<");
                    return;
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println(">>> Firebase Admin SDK Inicializado correctamente en Payara <<<");
            }
        } catch (Exception e) {
            System.err.println(">>> ERROR al inicializar Firebase Admin SDK <<<");
            e.printStackTrace();
        }
    }
}