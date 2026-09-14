package BuddyConvo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

@WebServlet("/voice")
public class VoiceServlet extends HttpServlet {

    private static final String API_URL =
            "https://api.elevenlabs.io/v1/text-to-speech/";

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {

        String apiKey = System.getenv("ELEVENLABS_API_KEY");

        String voiceId = System.getenv("ELEVENLABS_VOICE_ID");

        if (voiceId == null || voiceId.isEmpty()) {
            voiceId = "JBFqnCBsd6RMkjVDRZzb";
        }

        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "ElevenLabs API key is not configured."
            );
            return;
        }

        String text = request.getParameter("text");

        if (text == null || text.trim().isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Text is required."
            );
            return;
        }

        if (text.length() > 1000) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Message is too long."
            );
            return;
        }

        String safeText = text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String body =
                "{"
                + "\"text\":\"" + safeText + "\","
                + "\"model_id\":\"eleven_multilingual_v2\""
                + "}";

        URI uri = URI.create(
                API_URL + voiceId + "?output_format=mp3_44100_128"
        );

        HttpURLConnection connection =
                (HttpURLConnection) uri.toURL().openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty(
                "xi-api-key",
                apiKey
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        try (OutputStream output =
                     connection.getOutputStream()) {

            output.write(
                    body.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
        }

        int status = connection.getResponseCode();

        if (status >= 200 && status < 300) {

            response.setContentType("audio/mpeg");
            try (InputStream input =
                         connection.getInputStream();
                 OutputStream output =
                         response.getOutputStream()) {

                byte[] buffer = new byte[8192];

                int bytes;

                while ((bytes = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytes);
                }
            }

        } else {
            response.sendError(
                    status,
                    "ElevenLabs voice generation failed."
            );
        }
        connection.disconnect();
    }
}
