package com.realcomp.dan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.realcomp.mvr.MVRTransaction;

import java.io.*;
import java.util.Iterator;

/**
 *
 */
public class Example1{

    public static void main(String[] args){

        //Instantiate a Jackson ObjectMapper that can read JSON and map values to a Java object.
        ObjectMapper jackson = new ObjectMapper();

        //Create a Jackson ObjectReader to read multiple objects from a source.
        ObjectReader reader = jackson.readerFor(MVRTransaction.class);

        //Create a BufferedWriter to System.out (STDOUT) in this try-with-resources block
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))){

            //use our Jackson ObjectReader to create a Java Iterator over all the MVRTransactions read from STDIN.
            Iterator<MVRTransaction> itr = reader.readValues(System.in);

            //for every transaction with a vehicle, emit the VIN.
            while (itr.hasNext()){
                MVRTransaction tx = itr.next();
                if (tx.getVehicle() != null){
                    writer.write(tx.getVehicle().getVin());
                }
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
