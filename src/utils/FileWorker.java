package utils;

import commands.CommandManager;
import commands.CommandNames;
import ticket.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;
/**
 * The FileWorker class is responsible for loading and saving the collection of tickets from/to a file.
 * It provides methods to load the collection from a file, save the collection to a file, and read ticket fields from the console.
 * The class uses the Gson library to serialize and deserialize the collection.
 * The class also provides methods to parse ticket price and ticket type from strings.
 *
 * This class provides the following methods:
 * - loadCollection(String filePath): Loads the collection of tickets from a file.
 * - saveCollection(File file): Saves the collection of tickets to a file.
 * - setScannerForRead(Scanner scanner): Sets the scanner for reading ticket fields from the console.
 * - readTicketFields(): Reads ticket fields from the console and returns them as an ArrayList<String>.
 * - parseTicketPrice(String s): Parses a string to a Long representing the ticket price.
 * - parseTicketType(String s): Parses a string to a TicketType enum value.
 */

public class FileWorker {
    private static Scanner scannerForRead;

    public static void loadCollection(String filePath) {
        Path path = Paths.get(filePath);
        Scanner scan = null;
        try {
            LinkedList<Ticket> ticketsForInputs;
            scan = new Scanner(path);
            scan.useDelimiter("\\Z");
            String s = scan.next();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(java.time.ZonedDateTime.class, new ZonedDateTimeAdapter());
            Gson gson = builder.create();
            ticketsForInputs = gson.fromJson(s, new TypeToken<LinkedList<Ticket>>() {
            }.getType());
            int k = 0;
            for (Ticket ticketForInput : ticketsForInputs) {
                if (Corrector.checkTicketForCorrect(ticketForInput)) {
                    Ticket ticket = new Ticket(ticketForInput.getId(), ticketForInput.getName(), ticketForInput.getCoordinates(), ticketForInput.getCreationDate(), ticketForInput.getPrice(), ticketForInput.getType(), ticketForInput.getVenue());
                    CollectionHandler.getCollection().add(ticket);
                    CollectionHandler.getTicketIdList().add(ticket.getId());
                    CollectionHandler.getVenueIdList().add(ticket.getVenue().getId());
                } else {
                    System.out.println("Билет номер " + k + " не будет добавлен так как данные не проходят по ограничениям");
                }
                k++;
            }
            System.out.println("Колекция загружена.");
        } catch (IOException e) {
            System.out.println("Указан неверный путь");
        } catch (IllegalArgumentException | JsonSyntaxException e) {
            System.out.println("Проверьте файл на правильность");
        }

    }

    public static String saveCollection(File file) {
        try {
            FileOutputStream writer = new FileOutputStream(file);
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(java.time.ZonedDateTime.class, new ZonedDateTimeAdapter());
            Gson gson = builder.create();
            String s = gson.toJson(CollectionHandler.getCollection());
            writer.write(s.getBytes());
            return "Коллекция сохранена в файл с названием - " + file + ".";
        } catch (IOException e) {
            return "Неверное имя файла или файл не может быть открыт или не может быть создан.";
        }

    }

    public static void setScannerForRead(Scanner scanner) {
        scannerForRead = scanner;
    }

    public static ArrayList<String> readTicketFields() {
        ArrayList<String> ticketFields = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\b(" + String.join("|", getStringValues(CommandNames.values())) + ")\\b");
        while (!scannerForRead.hasNext(pattern) & scannerForRead.hasNextLine()) {
            String s = scannerForRead.nextLine();
            if (Corrector.checkCommandName(s)) {
                break;
            }
            ticketFields.add(s);
        }
        return ticketFields;
    }

    public static Long parseTicketPrice(String s) throws NumberFormatException {
        if (s.equals("null")) {
            return null;
        } else {
            return Long.parseLong(s);
        }
    }

    public static TicketType parseTicketType(String s) throws IllegalArgumentException {
        if (s.equals("null")) {
            return null;
        } else {
            return TicketType.valueOf(s);
        }
    }

    private static String[] getStringValues(Enum<?>[] enums) {
        String[] stringValues = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            stringValues[i] = enums[i].toString();
        }
        return stringValues;
    }
}



