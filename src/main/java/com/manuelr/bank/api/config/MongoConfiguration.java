package com.manuelr.bank.api.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.util.StreamUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

import static com.manuelr.bank.api.utils.Constants.JKS;
import static com.manuelr.bank.api.utils.Constants.LOCAL_DEV;
import static com.manuelr.bank.api.utils.Constants.TLS;

@Slf4j
@Configuration
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${application.env}")
    private String env;

    @Value("${spring.data.mongodb.uri}")
    private String mongoDbUri;

    @Value("${spring.data.mongodb.database}")
    private String mongoDbName;

    @Value("${spring.data.mongodb.keystore.path}")
    private String keystoreFilePath;

    @Value("${spring.data.mongodb.keystore.fileName}")
    private String keystoreFileName;

    @Value("${spring.data.mongodb.keystore.password}")
    private String keystorePassword;

    @Value("${spring.data.mongodb.keystore.base64}")
    private String keystoreBase64String;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    public MongoConfiguration() {
    }

    @NonNull
    @Override
    protected String getDatabaseName() {
        return mongoDbName;
    }

    @NonNull
    @Override
    public MongoClient reactiveMongoClient() {
        final var connectionString = new ConnectionString(mongoDbUri);
        final var settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    builder.context(sslContext());
                    builder.invalidHostNameAllowed(true);
                });
        if (!env.equals(LOCAL_DEV)) {
            settings.credential(MongoCredential
                    .createCredential(username, getDatabaseName(), password.toCharArray()));
        }
        return MongoClients.create(settings.build());
    }

    private SSLContext sslContext() {
        try {
            final var jksFile = getJksFile();

            final var keystorePassword = this.keystorePassword.toCharArray();
            final var keyStore = KeyStore.getInstance(JKS);

            try (final var inputStream = new FileInputStream(jksFile)) {
                keyStore.load(inputStream, keystorePassword);
            }

            final var keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword);

            final var sslContext = SSLContext.getInstance(TLS);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            return sslContext;
        } catch (Exception e) {
            throw new IllegalStateException("Error configuring SSL context", e);
        }
    }

    private File getJksFile() throws IOException {

        final var path = Paths.get(keystoreFilePath.concat(keystoreFileName));

        if (Files.exists(path)) {
            log.info("Certificate exist at path {}", path);
            return path.toFile();
        }

        return decodeAndCreateFile();

    }

    private File decodeAndCreateFile() throws IOException {

        final var path = Paths.get(keystoreFilePath);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        final var file = new File(keystoreFilePath, keystoreFileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] decodedBytes = Base64.decodeBase64(
                    keystoreBase64String.getBytes(StandardCharsets.UTF_8));
            StreamUtils.copy(decodedBytes, fos);
        }

        log.info("File created in external folder and Base64 content saved to {}", file.getAbsolutePath());
        return file;
    }
}