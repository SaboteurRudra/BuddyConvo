package BuddyConvo;

public class VoiceFeature {

    public static void speak(String text) {
        try {
            Runtime.getRuntime().exec(
                "powershell -Command \"Add-Type -AssemblyName System.Speech; " +
                "(New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('" +
                text + "')\""
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
