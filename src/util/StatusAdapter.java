package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Status;

import java.io.IOException;

public class StatusAdapter extends TypeAdapter<Status> {
    @Override
    public void write(JsonWriter jsonWriter, Status status) throws IOException {
        if (status != null){
            jsonWriter.value(status.toString());
        } else {
            jsonWriter.value("null");
        }
    }
    @Override
    public Status read(JsonReader jsonReader) throws IOException {
        if (jsonReader.nextString().equals("null")){
            return Status.NEW;
        } else {
            return Status.valueOf(jsonReader.nextString());
        }
    }
}
