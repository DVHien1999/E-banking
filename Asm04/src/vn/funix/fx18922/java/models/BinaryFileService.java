package vn.funix.fx18922.java.models;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileService<T>{
    public BinaryFileService() {
    }
    public static <T> List<T> readFile(String fileName) throws IOException {
        File outputFile= FileSystems.getDefault().getPath("store",fileName).toFile();
        List<T> objects= new ArrayList<>();
        try(ObjectInputStream file= new ObjectInputStream(new BufferedInputStream(new FileInputStream(outputFile)))){
            Boolean eof=false;
            while(!eof){
                try{
                    T object= (T) file.readObject();
                    objects.add(object);
                }catch (EOFException e){
                    eof=true;
                }
            }
        }catch (IOException e){
            throw new IOException(e);
        }catch (ClassNotFoundException e){
            System.out.println("Class not found Exception in FileService read: "+e.getMessage());
        }
        return objects;
    }
    public static <T> void writeFile(String fileName,List<T> objects){
        File inPutFile= FileSystems.getDefault().getPath("store",fileName).toFile();
        try(ObjectOutputStream file= new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(inPutFile)))){
            for(T object:objects){
                file.writeObject(object);
            }
        }catch(FileNotFoundException e){
            System.out.println("File not found Exception: in FileService write"+ e.getMessage());
        }catch (IOException e){
            System.out.println("IOException: in FileService write" + e.getMessage());
            e.printStackTrace();
        }
    }

}

