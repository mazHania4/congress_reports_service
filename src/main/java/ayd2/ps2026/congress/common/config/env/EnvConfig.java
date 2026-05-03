package ayd2.ps2026.congress.common.config.env;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    static {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            dotenv.entries().forEach(e ->
                    System.setProperty(e.getKey(), e.getValue())
            );

        } catch (Exception e) {
            System.out.println("No .env file found. Using system environment variables.");
        }
    }
}