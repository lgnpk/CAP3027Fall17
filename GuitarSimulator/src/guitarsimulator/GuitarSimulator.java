/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guitarsimulator;
import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;
import java.util.Scanner;
import javax.sound.midi.MidiUnavailableException;
import org.jfugue.pattern.Pattern;
import org.jfugue.realtime.RealtimePlayer;
import org.jfugue.theory.Note;
import java.util.Random;
import org.jfugue.midi.MidiDictionary;
import  sun.audio.*;    //import the sun.audio package
import  java.io.*;


/**
 *
 * @author loganpeck
 */
public class GuitarSimulator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //Defines the sound for guitar 1 chosen by the user and plays a sample scale to give idea of sound before selection takes place
        Player guitar1 = new Player();
//        guitar1.play(“V0 I[Guitar] E5s A5s C6s B5s E5s B5s”);
        guitar1.play("V0 I[Guitar] C D E F G A B C");
        
        //Defines the sound for guitar 2 chosen by the user and plays a sample patter to give an idea of what it will sound like after making selection
        Player guitar2 = new Player();
        guitar2.play("V1 I[Guitar] Eq Ch. | Eq Ch. | Dq Eq Dq Cq   V1 I[Flute] Rw | Rw | GmajQQQ CmajQ");
        
        //Defines the sound for the 3rd type of guitar that can be selected by the user
        Player guitar3 = new Player();
        
        ChordProgression firstFourthFifth = new ChordProgression("I IV V");
        
//        Player player = new Player();
//        player.play(firstFourthFifth.eachChordAs("$0q $1q $2q Rq"));
//
//        player.play(firstFourthFifth.allChordsAs("$0q $0q $0q $0q $1q $1q $2q $0q"));
//
//        player.play(firstFourthFifth.allChordsAs("$0 $0 $0 $0 $1 $1 $2 $0").eachChordAs("V0 $0s $1s $2s Rs V1 $!q"));
        
    }
//    public static void main(String[] args) throws MidiUnavailableException {
//        RealtimePlayer player = new RealtimePlayer();
//        Random randoNum = new Random();
//        Scanner scanner = new Scanner(System.in);
//        boolean quit = false;
//        while (quit == false) {
//            System.out.print("Play: ");
//            String userInput = scanner.next();
//            if (userInput.startsWith("+")) {
//               player.startNote(new Note(userInput.substring(1)));
//            }
//            else if (userInput.startsWith("-")) {
//                player.stopNote(new Note(userInput.substring(1)));
//            }
//            else if (userInput.equalsIgnoreCase("R")) {
//                player.changeInstrument(randoNum.nextInt(100));
//            }
//            else if (userInput.equalsIgnoreCase("P")) {
//                player.play(PATTERNS[randoNum.nextInt(PATTERNS.length)]);
//            }
//            else if (userInput.equalsIgnoreCase("Q")) {
//                quit = true;
//            }
//        }
//        scanner.close();
//        player.close();
//    }
//
//    private static Pattern[] PATTERNS = new Pattern[] {
//            new Pattern("Gmajq Cmajq Dmajq"),
//            new Pattern("V0 Ei Gi Di Ci  V1 Gi Ci Fi Ei"),
//            new Pattern("V0 Cmajq V1 Gmajq")
//            
//    };
    
    public void chordProgression1() {
        
    }
    
   
    
}
