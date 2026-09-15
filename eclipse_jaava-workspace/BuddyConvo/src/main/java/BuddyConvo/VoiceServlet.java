package BuddyConvo;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
@WebServlet("/voice")
public class VoiceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String key = System.getenv("ELEVENLABS_API_KEY");
        String voice = System.getenv("ELEVENLABS_VOICE_ID");

        if (key == null || key.isBlank()) {
            res.setStatus(500);
            res.setContentType("text/plain");
            res.getWriter().write("ERROR: ELEVENLABS_API_KEY is missing in Render.");
            return;
        }
        if (voice == null || voice.isBlank()) {
            res.setStatus(500);
            res.setContentType("text/plain");
            res.getWriter().write("ERROR: ELEVENLABS_VOICE_ID is missing in Render.");
            return;
        }

        String text = req.getParameter("text");

        if (text == null || text.isBlank()) {
            res.setStatus(400);
            res.setContentType("text/plain");
            res.getWriter().write("ERROR: No text received.");
            return;
        }

        if (text.length() > 5000) {
            res.setStatus(400);
            res.setContentType("text/plain");
            res.getWriter().write("ERROR: Message is too long.");
            return;
        }

        String safe = text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");

        String json =
                "{\"text\":\"" + safe +
                "\",\"model_id\":\"eleven_multilingual_v2\"}";

        URL url = new URL(
                "https://api.elevenlabs.io/v1/text-to-speech/"
                + voice
                + "?output_format=mp3_44100_128"
        );

        HttpURLConnection c =
                (HttpURLConnection) url.openConnection();

        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setConnectTimeout(15000);
        c.setReadTimeout(60000);

        c.setRequestProperty("xi-api-key", key);
        c.setRequestProperty("Content-Type", "application/json");
        c.setRequestProperty("Accept", "audio/mpeg");

        try (OutputStream out = c.getOutputStream()) {
            out.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int code = c.getResponseCode();

        if (code >= 200 && code < 300) {

            res.setContentType("audio/mpeg");

            try (InputStream in = c.getInputStream();
                 OutputStream out = res.getOutputStream()) {

                byte[] buffer = new byte[8192];
                int n;

                while ((n = in.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                }
            }

        } else {

            InputStream error = c.getErrorStream();

            String message = error == null
                    ? "No response from ElevenLabs."
                    : new String(
                        error.readAllBytes(),
                        StandardCharsets.UTF_8
                      );

            System.out.println(
                    "ELEVENLABS ERROR " + code + ": " + message
            );

            res.setStatus(code);
            res.setContentType("text/plain");
            res.getWriter().write(
                    "ElevenLabs Error " + code + ": " + message
            );
        }

        c.disconnect();
    }
}
