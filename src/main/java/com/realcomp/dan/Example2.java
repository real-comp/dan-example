package com.realcomp.dan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.realcomp.address.Address;
import com.realcomp.mvr.MVRTransaction;
import com.realcomp.mvr.Owner;
import com.realcomp.mvr.Vehicle;
import com.realcomp.names.Name;
import com.realcomp.prime.record.Record;
import com.realcomp.prime.record.io.IOContext;
import com.realcomp.prime.record.io.IOContextBuilder;
import com.realcomp.prime.record.io.RecordWriter;
import com.realcomp.prime.record.io.RecordWriterFactory;
import com.realcomp.prime.schema.Schema;
import com.realcomp.prime.schema.SchemaFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Uses Prime to emit some information on every Vehicle Owner.
 *
 * Disclaimer: This is not production quality code and is only meant to provide an example of how Prime features
 * might be used to create an output file.
 */
public class Example2{

    /**
     * Create zero or more Records for each Owner in the provided MVRTransaction
     *
     * Prime does not know how to map fields from an MVRTransaction object to fields in a Record,
     * so we have to build up the Record manually.
     *
     * @param tx not null
     * @return zero or more Records; never null
     */
    private static List<Record> toRecords(MVRTransaction tx){
        Objects.requireNonNull(tx);
        List<Record> result = new ArrayList<>();

        //The MVRTransaction object can have null values, so we must do a lot of checking...

        Vehicle vehicle = tx.getVehicle();
        if (vehicle != null && tx.getOwners() != null){
            for (Owner owner : tx.getOwners()){
                Record record = new Record();
                Name name = getName(owner);

                record.put("name", name.toString());
                record.put("prefix", name.getPrefix());
                record.put("first", name.getFirst());
                record.put("middle", name.getMiddle());
                record.put("last", name.getLast());
                record.put("suffix", name.getSuffix());

                //note that I don't have to create the fields in the same order as defined in the schema.
                record.put("VIN", vehicle.getVin());
                record.put("plate", tx.getPlate());

                //some standardized address data
                Address address = owner.getAddress();
                if (address != null){
                    record.put("address", address.getAddress());
                    record.put("city", address.getCity());
                    record.put("state", address.getState());
                    record.put("zip", address.getZip5());
                }

                result.add(record);
            }
        }

        return result;
    }

    /**
     * @param owner
     * @return the Name of the Owner, or an empty Name if not available.
     */
    private static Name getName(Owner owner){
        Name name = owner.getName();
        return name == null ? new Name() : name;
    }




    public static void main(String[] args){

        //Instantiate a Jackson ObjectMapper that can read JSON and map values to a Java object.
        ObjectMapper jackson = new ObjectMapper();

        //Create a Jackson ObjectReader to read multiple objects from a source.
        ObjectReader reader = jackson.readerFor(MVRTransaction.class);

        //Use the Prime SchemaFactory to read the 'example2.schema' file on the classpath and baked into this jar file.
        // @see src/main/resources/
        Schema schema = SchemaFactory.buildSchema(Example2.class.getResourceAsStream("example2.schema"));

        //this try-with-resources block creates 2 Objects:
        // An Prime IOContext, and a Prime RecordWriter.
        //
        // Prime's input and output configuration is specified using an IOContext.  The IOContextBuilder helps
        // us build this up from what we know.
        //
        // The Prime RecordWriter takes Prime Records and formats them accoring to the Schema and emits them via
        // the IOContext.
        try(IOContext ioContext = new IOContextBuilder().schema(schema).out(System.out).build();
            RecordWriter recordWriter = RecordWriterFactory.build(schema)){

            //open the RecordWriter
            recordWriter.open(ioContext);

            //use our Jackson ObjectReader to create a Java Iterator over all the MVRTransactions read from STDIN.
            Iterator<MVRTransaction> itr = reader.readValues(System.in);

            while (itr.hasNext()){
                MVRTransaction tx = itr.next();

                //for every MVRTransaction create some Prime Records
                List<Record> records = toRecords(tx);
                for (Record record: records){
                    //the RecordWriter will emit the CSV records to STDOUT.
                    recordWriter.write(record);
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
