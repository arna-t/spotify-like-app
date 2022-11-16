package com.example;

import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/*
    To compile: javac SpotifyLikeApp.java
    To run: java SpotifyLikeApp
 */

// declares a class for the app
public class SpotifyLikeAppExampleCode {

  // global variables for the app
  String status;
  Long position;
  static Clip audioClip;
  Boolean isFavourite;

  private static String basePath =
    "C:/Users/sjccuser/Documents/GitHub/test-office-hours/demo/src/main/java/com/example/";

  // "main" makes this class a java app that can be executed
  public static void main(final String[] args) {
    // reading audio library from json file
    JSONArray library = readAudioLibrary();

    // create a scanner for user input
    Scanner input = new Scanner(System.in);

    String userInput = "";
    while (!userInput.equals("q")) {
      menu();

      // get input
      userInput = input.nextLine();

      // accept upper or lower case commands
      userInput = userInput.toLowerCase();

      // do something
      handleMenu(userInput, library);
    }

    // close the scanner
    input.close();
  }

  /*
   * displays the menu for the app
   */
  public static void menu() {
    System.out.println("---- SpotifyLikeApp ----");
    System.out.println("[H]ome");
    System.out.println("[S]earch by title");
    System.out.println("[L]ibrary");
    System.out.println("[F]avorites");
    System.out.println("S[t]op playing");
    System.out.println("[Q]uit");

    System.out.println("");
    System.out.print("Enter q to Quit:");
  }

  /*
   * handles the user input for the app
   */
  /**
   * @param userInput
   * @param library
   */
  public static void handleMenu(String userInput, JSONArray library) {
    switch (userInput) {
      case "h":
        System.out.println("-->Home<--");
        break;
      case "s":
        System.out.println("-->Search by title<--");
        System.out.println("Enter audio title: ");
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        String name;
        String path;
      final String jsonFileName = "audio-library.json";
       final String filePath = basePath + jsonFileName;
       JSONArray jsonData = readJSONArrayFile(filePath);
        for (Integer i = 0; i < jsonData.size(); i++) {
        JSONObject obj = (JSONObject) jsonData.get(i);
        name = (String) obj.get("name");

          if(title.equals(name)) {
            System.out.println("\nNow playing: " + name);
            final Integer songIndex = i;
            play(library, songIndex);
          }

        }

        break;
      case "l":
      System.out.println("-->Library<--");
       printLibrary(library);

        //System.out.println("1.")
        //readAudioLibrary();
        break;
      case "f":
        System.out.println("-->Favorites<--");
        break;
      case "t":
        System.out.println("-->Stop playing<--");
        // if statement? if song is playing

      case "q":
        System.out.println("-->Quit<--");
        break;
      default:
        break;
    }
  }

  /*
   * plays an audio file
   */
  /**
   * @param library
   */
  public static void printLibrary(JSONArray library) {
    for (Integer i=0; i < library.size(); i++) {
      JSONObject obj = (JSONObject) library.get(i);
      String name = (String) obj.get("name");
      Integer num = i+1;
      String line = "[" + num + "]" + " " + name;
      System.out.println(line);
    }
  }

  public static void play(JSONArray library, Integer songIndex) {
    // open the audio file

    // get the filePath and open an audio file
    final JSONObject obj = (JSONObject) library.get(songIndex);  
    final String filename = (String) obj.get("filename"); 
    final String filePath = basePath + "/wav/" + filename;
    final File file = new File(filePath);

    // stop the current song from playing, before playing the next one
    if (audioClip != null) {
      audioClip.close();
    }

    try {
      // create clip
      audioClip = AudioSystem.getClip();

      // get input stream
      final AudioInputStream in = AudioSystem.getAudioInputStream(file);

      audioClip.open(in);
      audioClip.setMicrosecondPosition(0);
      audioClip.loop(Clip.LOOP_CONTINUOUSLY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //
  // Func: readJSONFile
  // Desc: Reads a json file storing an array and returns an object
  // that can be iterated over
  //
  public static JSONArray readJSONArrayFile(String fileName) {
    // JSON parser object to parse read file
    JSONParser jsonParser = new JSONParser();

    JSONArray dataArray = null;

    try (FileReader reader = new FileReader(fileName)) {
      // Read JSON file
      Object obj = jsonParser.parse(reader);

      dataArray = (JSONArray) obj;
      //System.out.println(dataArray);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return dataArray;
  }

  // read the audio library of music
  public static JSONArray readAudioLibrary() {
    final String jsonFileName = "audio-library.json";
    final String filePath = basePath + jsonFileName;

    JSONArray jsonData = readJSONArrayFile(filePath);
    

    System.out.println("Reading the file " + filePath);

    return jsonData;
  }
}
