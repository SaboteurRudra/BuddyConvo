package BuddyConvo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@WebServlet("/voice")
public class VoiceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req,HttpServletResponse res)
            throws IOException {

        String key=System.getenv("ELEVENLABS_API_KEY");
        String voice=System.getenv("ELEVENLABS_VOICE_ID");

        if(key==null||key.isBlank()){
            res.sendError(500,"ELEVENLABS_API_KEY missing");
            return;
        }

        if(voice==null||voice.isBlank()){
            res.sendError(500,"ELEVENLABS_VOICE_ID missing");
            return;
        }

        String text=req.getParameter("text");

        if(text==null||text.isBlank()){
            res.sendError(400,"Text is empty");
            return;
        }

        String json="{\"text\":\""+
                text.replace("\\","\\\\")
                    .replace("\"","\\\"")
                    .replace("\n","\\n")
                    .replace("\r","")+
                "\",\"model_id\":\"eleven_multilingual_v2\"}";

        URL url=new URL(
            "https://api.elevenlabs.io/v1/text-to-speech/"
            +voice+"?output_format=mp3_44100_128"
        );

        HttpURLConnection c=(HttpURLConnection)url.openConnection();

        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setConnectTimeout(15000);
        c.setReadTimeout(60000);

        c.setRequestProperty("xi-api-key",key);
        c.setRequestProperty("Content-Type","application/json");
        c.setRequestProperty("Accept","audio/mpeg");

        try(OutputStream o=c.getOutputStream()){
            o.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int code=c.getResponseCode();

        if(code>=200&&code<300){

            res.setContentType("audio/mpeg");

            try(InputStream in=c.getInputStream();
                OutputStream out=res.getOutputStream()){

                byte[] b=new byte[8192];
                int n;

                while((n=in.read(b))!=-1)
                    out.write(b,0,n);
            }

        }else{

            InputStream e=c.getErrorStream();

            String msg=e==null?"No error returned":
                new String(e.readAllBytes(),StandardCharsets.UTF_8);

            System.out.println(
                "ELEVENLABS ERROR "+code+": "+msg
            );

            res.setStatus(code);
            res.setContentType("text/plain");
            res.getWriter().write(
                "ElevenLabs error "+code+": "+msg
            );
        }

        c.disconnect();
    }
}
