/**
 * Jodan Marx
 * 
 * The program will promt the user to enter either (1) or (2).
 * (1) will use the technique called "steganography" to encode a text message into a sound file.
 * (2) will extract a message from a loaded sound file.
 */

public class EncodeOrExtractMsg
{
  
  public static void main (String[] args) 
  {
    
    // Prompt the user to enter a 1 or 2
    int promtUser = SimpleInput.getIntNumber("Enter 1 to encode a message into a sound file\n"
                                           + "Enter 2 to extract a message from a sound file", 1, 2);
    
    // If the user promted 1
    if (promtUser == 1)
    {
      // Open a sound file  
      String filename1 = FileChooser.pickAFile();
      Sound sound1 = new Sound (filename1);
      
      // Smooth the sound file
      smoothSamples (sound1);
      
      String message;
      message = SimpleInput.getString ("Enter a message to Encode");
      System.out.println("The message entered was:\n" + message);
      
      // Encode the message into the sound file
      int messageDigits[];
      messageDigits = transformMessage(message);
      encodeSound(sound1, messageDigits);
      
      // Play the sound file
      sound1.play();
      
      // Save the new sound file
      String filename3 = FileChooser.pickAFile();
      sound1.write(filename3);
    }
    
    
    // If the user prompted 2
    if (promtUser == 2)
    {
      // Open a sound file
      String filename2 = FileChooser.pickAFile();
      Sound sound2 = new Sound (filename2);
      
      // Decode the message from the sound file
      decodeMessage(sound2);
    }
    
  }
  
  
  // Method for replacing the last digit of every sound sample with a 0
  public static void smoothSamples (Sound s)
  {
    SoundSample[] ssarray;
    ssarray = s.getSamples();
    
    int length = s.getLength();
    System.out.println ("The sound has " + length + " samples");
    
    int i;
    for (i = 0 ; i < length ; ++i)
    {
      int amplitude = ssarray[i].getValue();
      
      // Get the last digit of the sample
      int digit = amplitude % 10;
      
      // Minus the last digit to the original amplitude to make the last digit 0
      amplitude = amplitude - digit;
      
      ssarray[i].setValue (amplitude);
    }
    
  }
  
  
   // Method to transform the message entered
    public static int[] transformMessage (String msg)
  {
    int[] mDigits = new int[ 3 * msg.length()];

    for ( int i = 0 ; i < mDigits.length; i++)
      mDigits[i] = -1;
    
    int pos = 0;
    for (int i = 0 ; i < msg.length() ; i++)
    {
      System.out.println ("The character at index " + i + " is  " + msg.charAt(i));
      
        int num, d1, d2, d3;
        char ch;

        ch = msg.charAt(i);

        num = (int) ch;

        d1 = num % 10;    
        num = num / 10;
        d2 = num % 10;   
        num = num / 10;
        d3 = num % 10;   
        
      mDigits[pos] = d1;
      pos++;
      mDigits[pos] = d2;
      pos++;
      mDigits[pos] = d3;
      pos++;
    }
    
    return mDigits;
  }
  
    
    // Method to encode the digits into the sound samples
    public static void encodeSound (Sound s, int[] msgDig)
    {
      SoundSample[] ssarray;
      ssarray = s.getSamples();
      int length = msgDig.length;
      
      for (int i = 0; i < length; ++i)
      {
        int amp = ssarray[i].getValue();
        int digit = msgDig[i];
          
      if ( amp >= 0) 
      { 
        // Encode a decimal digit into a positive sound sample by adding it
        amp = amp + digit;
      } 
      else 
      { 
        // Encode a decimal digit into a negative sound sample by subtracting it
        amp = amp - digit;
      }
      
      // If statements for the Special Cases
      if (amp > 32767) 
      {
        amp = amp - 10;
      }
      if (amp < -32768) 
      {
        amp = amp + 10;
      }
    
      ssarray[i].setValue(amp);
      }
    }
         
    
    // Method for decoding a message from a sound file
     public static void decodeMessage(Sound s)
    {
      SoundSample[] ssarray;
      ssarray = s.getSamples();
      String hiddenMessage = "";
      
      int number = 1;
      int i, d1, d2, d3;
      
      while (number != 0)
       {  
        for (i = 0 ; i < ssarray.length; ++i)
        {
          int amplitude = ssarray[i].getValue();
          d1 = Math.abs(amplitude % 10);
          ++i;
          amplitude = ssarray[i].getValue();
          d2 = Math.abs(amplitude % 10);
          ++i;
          amplitude = ssarray[i].getValue();
          d3 = Math.abs(amplitude % 10);
      
          // Combine the digits to be a ascii code
          int ascii = d1 + (d2 * 10) + (d3 * 100);
          
          // If ascii is 0 then set number to 0
          // Stop the extraction process, display the hidden message, and exit the program.
          if (ascii == 0)
          {
            number = 0;
            SimpleOutput.showInformation("The Hidden Message is:\n" + hiddenMessage);
            System.exit(0);
          }
          else
          {
            hiddenMessage = hiddenMessage + (char)ascii;
          }
        }
      }
      // Display the hidden message
      SimpleOutput.showInformation("The Hidden Message is:\n" + hiddenMessage);
     }
      
    
}