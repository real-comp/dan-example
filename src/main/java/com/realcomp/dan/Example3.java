package com.realcomp.dan;

import com.realcomp.prime.record.Record;
import com.realcomp.prime.record.io.IOContext;
import com.realcomp.prime.record.io.IOContextBuilder;
import com.realcomp.prime.record.io.RecordWriter;
import com.realcomp.prime.record.io.RecordWriterFactory;
import com.realcomp.prime.record.io.json.JsonReader;
import com.realcomp.prime.schema.Schema;
import com.realcomp.prime.schema.SchemaFactory;

/**
 * Uses Prime to emit some information on the first Vehicle Owner.
 *
 * Note that the MvrTransaction object is NOT used in this example.  This is JSON to CSV using Prime.
 *
 * Note that the output of Example3 will NOT match that of Example2.
 *
 * Disclaimer: This is not production quality code and is only meant to provide an example of how Prime features
 * might be used to create an output file.
 */
public class Example3{

    /**
     * Silly helper method to close an AutoCloseable and make the code a bit more readable.
     * @param closeable
     */
    public static void closeQuietly(AutoCloseable closeable){
        if (closeable != null){
            try{
                closeable.close();
            }
            catch (Exception ignored){
            }
        }
    }

    public static void main(String[] args){

        //In this example, we will use Prime to read the JSON and use example3.schema to emit fields
        // with very little code.
        // You probably won't do it this way, but just wanted to show Prime a bit more.


        //I will use a different Java style and not use a try-with-resources block.
        // I'll need to close() these in the finally block.
        JsonReader reader = null;
        RecordWriter writer = null;
        IOContext inputContext = null;
        IOContext outputContext = null;


        try{

            //There is a RecordReaderFactory in Prime that can inspect a Schema to create an approprate reader of
            //Records from an input source.  I know here that the input is JSON, and a schema is not needed.  The JSON 
            //describes the field names, types, and values without the need for an external schema.
            reader = new JsonReader();

            //Use the Prime SchemaFactory to read the 'example3.schema' file that on the classpath. 
            //see /src/main/resources/...
            Schema schema = SchemaFactory.buildSchema(Example3.class.getResourceAsStream("example3.schema"));

            //create an appropriate RecordWriter
            writer = RecordWriterFactory.build(schema);

            //create the input and output IOContext for STDIN/STDOUT
            inputContext = new IOContextBuilder().in(System.in).build();
            outputContext = new IOContextBuilder().schema(schema).out(System.out).build();

            reader.open(inputContext);
            writer.open(outputContext);

            //read all the Records and pass them to the writer.  The JSON is parsed to a Prime Record.
            //This Record is then transformed and written with information in the output Schema (example3.schema).
            Record record = reader.read();
            while (record != null){
                writer.write(record);
                record = reader.read();
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
        finally{
            closeQuietly(reader);
            closeQuietly(writer);
            closeQuietly(inputContext);
            closeQuietly(outputContext);
        }
    }
}
