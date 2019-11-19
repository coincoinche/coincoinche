package com.coincoinche.engine.serialization.json;

import com.coincoinche.engine.MovePlaying;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/** Custom Serializer for MovePlaying objects. */
public class MovePlayingSerializer extends StdSerializer<MovePlaying> {

  private static final long serialVersionUID = -1186062612511420241L;

  public MovePlayingSerializer() {
    this(null);
  }

  public MovePlayingSerializer(Class<MovePlaying> t) {
    super(t);
  }

  @Override
  public void serialize(MovePlaying value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeStartObject();
    gen.writeStringField("card", value.getShortName());
    gen.writeEndObject();
  }
}
