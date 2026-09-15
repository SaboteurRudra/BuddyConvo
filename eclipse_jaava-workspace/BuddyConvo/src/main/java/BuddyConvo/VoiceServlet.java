package BuddyConvo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/voice")
public class VoiceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String API_URL =
            "https://api.elevenlabs.io/v1/text-to-speech/";

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {

        String apiKey = System.getenv("ELEVENLABS_API_KEY");
        String voiceId = System.getenv("ELEVENLABS_VOICE_ID");

        if (apiKey == null || apiKey.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "ELEVENLABS_API_KEY is missing in Render."
            );

            return;
        }

        if (voiceId == null || voiceId.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "ELEVENLABS_VOICE_ID is missing in Render."
            );

            return;
        }

        String text = request.getParameter("text");

        if (text == null || text.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please enter a message."
            );

            return;
        }

        text = text.trim();

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
                .replace("\r", "")
                .replace("\n", "\\n");

        String json =
                "{"
                + "\"text\":\"" + safeText + "\","
                + "\"model_id\":\"eleven_multilingual_v2\""
                + "}";

        URL url = new URL(
                API_URL + voiceId
                + "?output_format=mp3_44100_128"
        );

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(60000);

        connection.setRequestProperty(
                "xi-api-key",
                apiKey
        );

        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        connection.setRequestProperty(
                "Accept",
                "audio/mpeg"
        );

        try (OutputStream output =
                     connection.getOutputStream()) {

            output.write(
                    json.getBytes(StandardCharsets.UTF_8)
            );
        }

        int status = connection.getResponseCode();

        if (status >= 200 && status < 300) {

            response.setContentType("audio/mpeg");
            response.setHeader(
                    "Cache-Control",
                    "no-cache"
            );

            try (InputStream input =
                         connection.getInputStream();
                 OutputStream output =
                         response.getOutputStream()) {

                byte[] buffer = new byte[8192];

                int bytes;

                while ((bytes = input.read(buffer)) != -1) {

                    output.write(buffer, 0, bytes);
                }

                output.flush();
            }

        } else {

            InputStream errorStream =
                    connection.getErrorStream();

            String errorMessage = "";

            if (errorStream != null) {

                errorMessage = new String(
                        errorStream.readAllBytes(),
                        StandardCharsets.UTF_8
                );
            }
            System.out.println(
                    "ELEVENLABS ERROR "
                    + status
                    + ": "
                    + errorMessage
            );

            response.sendError(
                    status,
                    "ElevenLabs error: " + errorMessage
            );
        }
        connection.disconnect();
    }
}
