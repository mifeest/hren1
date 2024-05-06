package utils;

import java.io.*;
import java.nio.ByteBuffer;

public class Serializer {
    public static byte[] serializeObject(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return baos.toByteArray();
    }
    public static Object deserializeObject(ByteBuffer buffer) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = null;
        try {
            object = objectInputStream.readObject();
        }catch (ClassNotFoundException e){
            System.out.println("Не удалось десериализовать обьект");
        }
        return object;
    }
}
